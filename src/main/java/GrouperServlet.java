package hello;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
public class GrouperServlet extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String phoneNumber = request.getParameter("From");
        String body = request.getParameter("Body");
        Text text = new Text(phoneNumber, body);
        text.execute();
        response.setContentType("application/xml");
        response.getWriter().print("<Response></Response>");
    }
}