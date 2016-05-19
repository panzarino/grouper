import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
 
import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.Message;
 
public class GrouperServlet extends HttpServlet {
 
    // service() responds to both GET and POST requests.
    // You can also use doGet() or doPost()
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TwiMLResponse twiml = new TwiMLResponse();
        Message message = new Message("Hello, Mobile Monkey");
        try {
            twiml.append(message);
        } catch (TwiMLException e) {
            e.printStackTrace();
        }
 
        response.setContentType("application/xml");
        response.getWriter().print(twiml.toXML());
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String phoneNumber = request.getParameter("From");
        String body = request.getParameter("body");
        TwiMLResponse twiml = new TwiMLResponse();
        Message message = new Message("Your message has been sent!");
        try {
            twiml.append(message);
        } catch (TwiMLException e) {
            e.printStackTrace();
        }
 
        response.setContentType("application/xml");
        response.getWriter().print(twiml.toXML());
    }
}