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
        
        return "";
    }
    
    /**
     * Adds a user to a group of an existing id
     * @param number The phone number of the sender
     * @param content The content of the message
     */
    public static void join(String number, String content){
        String code = content.replaceAll("\\s+","");
        String key = code.substring(0, Math.min(20, code.length()));
        
        return "";
    } 
    
    /**
     * Sends a message to the group the user is in
     * @param number The phone number of the sender
     * @param content The content of the message
     */
    public static void message(String number, String content){
        String key = code.substring(0, Math.min(140, code.length()));
        
        return "";
    } 
}