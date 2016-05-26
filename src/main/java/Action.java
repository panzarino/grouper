package hello;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Class that carries out actions
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Action {
    private static final Logger log = Logger.getLogger(Action.class.getName());
    private final String number;
    private final String content;
    /**
     * Creates a new Action for message
     * @param num The phone number of the sender
     * @param text The content of the message
     */
    public Action(String num, String text){
        number = num;
        content = text;
    }
    /**
     * Creates a new chat with matching id
     */
    public void create(){
        try{
            String[] split = content.split(" ");
            String key = split[0].substring(0, Math.min(20, split[0].length()));
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            Inserter inserter = new Inserter("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("Name", "Chats", "Name='"+key+"'");
            while (selected.next()){
                if (selected.getString("Name").equals(key)){
                    selector.close();
                    inserter.close();
                    (new SendSms(number, "Someone already has a chat with id: "+key+"\nIf you want to join this chat, run '/join "+key+"'")).sendSms();
                    return;
                }
            }
            inserter.insert("Chats (Name)", "('"+key+"')");
            selector.close();
            inserter.close();
            (new SendSms(number, "You have created a chat with id: "+key)).sendSms();
            join();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
    }
    /**
     * Adds a user to a group of an existing id
     */
    public void join(){
        try{
            String[] split = content.split(" ", 3);
            if (split.length < 2){
                (new SendSms(number, "Cannot join you to the chat. The correct syntax is '/join <chat-id> <username>'")).sendSms();
                return;
            }
            String key = split[0].substring(0, Math.min(20, split[0].length()));
            String name = split[1].substring(0, Math.min(18, split[1].length()));
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            Inserter inserter = new Inserter("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet isin = selector.select("Number", "Users", "Number='"+number+"'");
            while (isin.next()){
                if (isin.getString("Number").equals(number)){
                    (new SendSms(number, "You are already in a chat. Use '/leave' to leave that chat. Use '/join "+key+" <username>' to join the chat after leaving.")).sendSms();
                    return;
                }
            }
            ResultSet selected = selector.select("*", "Chats", "Name='"+key+"'");
            while (selected.next()){
                if (selected.getString("Name").equals(key)){
                    inserter.insert("Users (Name, Number, Chat)", "('"+name+"', '"+number+"', '"+selected.getInt("ID")+"')");
                    selector.close();
                    inserter.close();
                    Action alert = new Action(number, name+" has just joined this chat.");
                    alert.message();
                    (new SendSms(number, "Hi, "+name+", You have joined a chat with id: "+key)).sendSms();
                    return;
                }
            }
            selector.close();
            inserter.close();
            (new SendSms(number, "We couldn't find a chat with id: "+key+"\nTo create a chat, type '/create"+key+"'")).sendSms();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
    }
    /**
     * Sends a message to the group the user is in
     */
    public void message(){
        try{
            String key = content.substring(0, Math.min(140, content.length()));
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Users", "Number='"+number+"'");
            while (selected.next()){
                if (selected.getString("Number").equals(number)){
                    ResultSet users = selector.select("*", "Users", "Chat="+selected.getInt("Chat"));
                    while (users.next()){
                        if (selected.getInt("Chat") == users.getInt("Chat") && !selected.getString("Number").equals(users.getString("Number"))){
                            (new SendSms(users.getString("Number"), selected.getString("Name")+": "+content)).sendSms();
                        }
                    }
                }
            }
            selector.close();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
    }
    /**
     * Lists users in the chat
     */
    public void list(){
        try{
            List<String> members = new ArrayList<String>();
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Users", "Number='"+number+"'");
            while (selected.next()){
                if (selected.getString("Number").equals(number)){
                    ResultSet users = selector.select("*", "Users", "Chat="+selected.getInt("Chat"));
                    while (users.next()){
                        if (selected.getInt("Chat") == users.getInt("Chat")){
                            members.add(users.getString("Name")+": "+users.getString("Number"));
                        }
                    }
                }
            }
            String output = "Members of your chat:\n";
            for (int x=0; x<members.size(); x++){
                output += members.get(x);
                if (x != members.size()-1)
                    output += "\n";
            }
            selector.close();
            (new SendSms(number, output)).sendSms();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
    }
    /**
     * Lists chats
     */
    public void chats(){
        try{
            List<String> chats = new ArrayList<String>();
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Chats");
            while (selected.next()){
                chats.add(selected.getString("Name"));
            }
            String output = "Available chats:\n";
            for (int x=0; x<chats.size(); x++){
                output += chats.get(x);
                if (x != chats.size()-1)
                    output += "\n";
            }
            selector.close();
            (new SendSms(number, output)).sendSms();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
    }
    /**
     * Removes a user from his/her existing group
     */
    public void leave(){
        try{
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            Worker worker = new Worker("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Users", "Number='"+number+"'");
            while (selected.next()){
                if (selected.getString("Number").equals(number)){
                    Action alert = new Action(number, selected.getString("Name")+" has left this chat.");
                    alert.message();
                    ResultSet chatInfo = selector.select("Name", "Chats", "ID="+selected.getInt("Chat"));
                    worker.executeUpdate("DELETE FROM Users WHERE ID="+selected.getInt("ID"));
                    chatInfo.next();
                    String chatName = chatInfo.getString("Name");
                    selector.close();
                    worker.close();
                    (new SendSms(number, "You have left a chat with id: "+chatName)).sendSms();
                    return;
                }
            }
            selector.close();
            worker.close();
            (new SendSms(number, "You are not in any chats.")).sendSms();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
    }
}