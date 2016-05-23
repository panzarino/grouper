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
    /**
     * Sends error message
     * @param number The phone number to send to
     */
    public static void error(String number){
        (new SendSms(number, "Something was wrong with your message. Check for typos and try again.")).sendSms();
    }
    /**
     * Creates a new chat with matching id
     * @param number The phone number of the sender
     * @param content The content of the message
     */
    public static void create(String number, String content){
        try{
            String[] split = content.split(" ");
            String key = split[0].substring(0, Math.min(20, split[0].length()));
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            Inserter inserter = new Inserter("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("Name", "Chats", "Name='"+key+"'");
            while (selected.next()){
                if (selected.getString("Name").equals(key)){
                    (new SendSms(number, "Someone already has a chat with id: "+key+"\nIf you want to join this chat, run '/join "+key+"'")).sendSms();
                    return;
                }
            }
            inserter.insert("Chats (Name, Admin)", "("+key+", "+number+")");
            (new SendSms(number, "You have created a chat with id: "+key)).sendSms();
            join(number, content);
            selector.close();
            inserter.close();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
            error(number);
        }
    }
    /**
     * Adds a user to a group of an existing id
     * @param number The phone number of the sender
     * @param content The content of the message
     */
    public static void join(String number, String content){
        try{
            String[] split = content.split(" ");
            String key = split[0].substring(0, Math.min(20, split[0].length()));
            String name = split[1].substring(0, Math.min(18, split[1].length()));
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            Inserter inserter = new Inserter("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Chats", "Name='"+key+"'");
            while (selected.next()){
                if (selected.getString("Name").equals(key)){
                    inserter.insert("Users (Name, Number, Chat)", "("+name+", "+number+", "+selected.getInt("ID")+")");
                    (new SendSms(number, "Hi, "+name+", You have a chat with id: "+key)).sendSms();
                    return;
                }
            }
            (new SendSms(number, "We couldn't find a chat with id: "+key+"\nTo create a chat, type '/create"+key+"'")).sendSms();
            selector.close();
            inserter.close();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
            error(number);
        }
    }
    /**
     * Sends a message to the group the user is in
     * @param number The phone number of the sender
     * @param content The content of the message
     */
    public static void message(String number, String content){
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
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
            error(number);
        }
    }
    /**
     * Removes a user from his/her existing group
     * @param number The phone number of the sender
     */
    public static void leave(String number){
        try{
            Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            Worker worker = new Worker("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
            ResultSet selected = selector.select("*", "Users", "Number='"+number+"'");
            while (selected.next()){
                if (selected.getString("Number").equals(number)){
                    ResultSet chatInfo = selector.select("Name", "Chats", "ID="+selected.getInt("Chat"));
                    worker.execute("DELETE FROM Users WHERE ID="+selected.getInt("ID"));
                    (new SendSms(number, "You have left a chat with id: "+chatInfo.getString("Name"))).sendSms();
                    return;
                }
            }
            (new SendSms(number, "You are not in any chats.")).sendSms();
            selector.close();
            worker.close();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
            error(number);
        }
    }
}