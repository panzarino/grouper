package hello;

/**
 * Class that carries out actions
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Action {
    /**
     * Creates a new chat with matching id
     * @param number The phone number of the sender
     * @param content The content of the message
     */
    public static void create(String number, String content){
        String code = content.replaceAll("\\s+","");
        String key = code.substring(0, Math.min(20, code.length()));
        Selector selector = new Selector("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
        Inserter inserter = new Inserter("jdbc:mysql://localhost:3306/Grouper", SQL.username, SQL.password);
        ResultSet selected = selector.select("Name", "Chats", "Name='"+key+"'");
        while (selected.next()){
            if (selected.getString("Name") == key){
                (new SendSms(number, "Someone already has a chat with id: "+key+"\nIf you want to join this chat, run '/join "+key+"'")).sendSms();
                return;
            }
        }
        inserter.insert("Chats (Name, Admin)", "("+key+", "+number+")");
        (new SendSms(number, "You have created and joined a chat with id: "+key)).sendSms();
    }
    /**
     * Adds a user to a group of an existing id
     * @param number The phone number of the sender
     * @param content The content of the message
     */
    public static void join(String number, String content){
        String code = content.replaceAll("\\s+","");
        String key = code.substring(0, Math.min(20, code.length()));
    }
    /**
     * Sends a message to the group the user is in
     * @param number The phone number of the sender
     * @param content The content of the message
     */
    public static void message(String number, String content){
        String key = content.substring(0, Math.min(140, content.length()));
    }
    /**
     * Removes a user from his/her existing group
     * @param number The phone number of the sender
     */
    public static void leave(String number){
        
    }
}