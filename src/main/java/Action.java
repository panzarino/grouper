package hello;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Class that carries out actions
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Action {
    private static final Logger log = Logger.getLogger(Action.class.getName());
    private String number;
    private String content;
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
     * Sends error message
     * @return Error message
     */
    public static String error(){
        return "Something was wrong with your message. Check for typos and try again.";
    }
    /**
     * Creates a new chat with matching id
     * @return Message to sender
     */
    public String create(){
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
                    return "Someone already has a chat with id: "+key+"\nIf you want to join this chat, run '/join "+key+"'";
                }
            }
            inserter.insert("Chats (Name, Admin)", "("+key+", "+number+")");
            selector.close();
            inserter.close();
            return "You have created a chat with id: "+key+"\n"+join();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
        return error();
    }
    /**
     * Adds a user to a group of an existing id
     * @return Message to sender
     */
    public String join(){
        try{
            String[] split = content.split(" ", 2);
            if (split.length != 2)
                return "Cannot join you to the chat. The correct syntax is '/join <chat-id> <username>'";
            String key = split[0].substring(0, Math.min(20, split[0].length()));
            String name = split[1].substring(0, Math.min(18, split[1].length()));
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            Inserter inserter = new Inserter("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Chats", "Name='"+key+"'");
            while (selected.next()){
                if (selected.getString("Name").equals(key)){
                    inserter.insert("Users (Name, Number, Chat)", "("+name+", "+number+", "+selected.getInt("ID")+")");
                    selector.close();
                    inserter.close();
                    return "Hi, "+name+", You have joined a chat with id: "+key;
                }
            }
            selector.close();
            inserter.close();
            return "We couldn't find a chat with id: "+key+"\nTo create a chat, type '/create"+key+"'";
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
        return error();
    }
    /**
     * Sends a message to the group the user is in
     * @return Message to sender
     */
    public String message(){
        try{
            String key = content.substring(0, Math.min(140, content.length()));
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Users", "Number='"+number+"'");
            while (selected.next()){
                if (selected.getString("Number").equals(number)){
                    ResultSet users = selector.select("*", "Users", "Chat="+selected.getInt("Chat"));
                    while (selected.next()){
                        if (selected.getInt("Chat") == users.getInt("Chat")){
                            (new SendSms(users.getString("Number"), selected.getString("Name")+": "+content)).sendSms();
                        }
                    }
                }
            }
            selector.close();
            return null;
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
        return error();
    }
    /**
     * Removes a user from his/her existing group
     * @return Message to sender
     */
    public String leave(){
        try{
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            Worker worker = new Worker("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Users", "Number='"+number+"'");
            while (selected.next()){
                if (selected.getString("Number").equals(number)){
                    ResultSet chatInfo = selector.select("Name", "Chats", "ID="+selected.getInt("Chat"));
                    worker.executeUpdate("DELETE FROM Users WHERE ID="+selected.getInt("ID"));
                    selector.close();
                    worker.close();
                    return "You have left a chat with id: "+chatInfo.getString("Name");
                }
            }
            selector.close();
            worker.close();
            return "You are not in any chats.";
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
        return error();
    }
}