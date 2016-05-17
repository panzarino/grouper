package hello;

/**
 * Class that carries out actions
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Action {
    /**
     * Creates a new chat with matching id
     * @param content The content of the message
     * @return Status of creation
     */
    public static String create(String content){
        String code = content.replaceAll("\\s+","");
        String key = code.substring(0, Math.min(20, code.length()));
        
        return "";
    }
}