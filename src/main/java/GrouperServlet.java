package hello;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
 
import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.Message;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

 
public class GrouperServlet extends HttpServlet {
 
    private static final Logger log = Logger.getLogger(Worker.class.getName());

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BasicConfigurator.configure();
        log.info("test");
        String phoneNumber = request.getParameter("From");
        String body = request.getParameter("body");
        Text text = new Text(phoneNumber, body);
        text.execute();
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