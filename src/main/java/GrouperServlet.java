package hello;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Enumeration;

 
import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.Message;

import org.apache.log4j.Logger;

 
public class GrouperServlet extends HttpServlet {
 
    private static final Logger log = Logger.getLogger(Worker.class.getName());

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        org.apache.log4j.BasicConfigurator.configure();
    
        String phoneNumber = request.getParameter("From");
        String body = request.getParameter("Body");
        
        //for testing!
        /* 
        Enumeration<String> parameterNames = request.getParameterNames();
    
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            log.info(paramName);
        }
        */
        
        log.info("body: " + body + " from phone number " + phoneNumber);
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