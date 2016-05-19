package hello;

/**
 * Class that determines what to do with a message
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Text {
    private String number, text;
    /**
     * Creates a new text and stores the text
     * @param num Sending phone number
     * @param content The content of the message
     */
    public Text(String num, String content){
        number = num;
        text = content;
    }
    /**
     * Determines what to do with a message
     * @return Whether the message was successfully executed
     */
    public void execute(){
        if (text!=null && number!= null){
            if (text.startsWith("/")){
                int space = text.indexOf(" ");
                if (space != 1){
                    String command = text.substring(1, space);
                    String content = text.substring(space+1);
                    if (command.equals("create")){
                        Action.create(number, content);
                        return;
                    }
                    if (command.equals("join")){
                        Action.join(number, content);
                        return;
                    }
                    if (command.equals("leave")){
                        Action.leave(number);
                        return;
                    }
                }
            }
            Action.message(number, text);
            return;
        }
    }
}