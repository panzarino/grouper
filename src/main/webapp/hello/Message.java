package hello;

/**
 * Class that determines what to do with a message
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Message {
    private String text;
    /**
     * Creates a new Message and stores the text
     * @param content The content of the message
     */
    public Message(String content){
        text = content;
    }
    /**
     * Determines what to do with a message
     * @return Whether the message was successfully executed
     */
    public boolean execute(){
        if (text.startsWith("/")){
            int space = text.indexOf(" ");
            if (space == -1)
                return false;
            String command = text.substring(1, space);
            String content = text.substring(space+1);
            return true;
        }
        return true;
    }
}