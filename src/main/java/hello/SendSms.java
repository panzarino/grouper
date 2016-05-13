package hello;

import java.util.Map;
import java.util.HashMap;
 
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
 
public class SendSms {
 
    /* Find your sid and token at twilio.com/user/account */
    private static final String ACCOUNT_SID = "AC3d620ec78e36b7b18f4f215e4ef83d61";
    private static final String AUTH_TOKEN = "55fb105d330607ca85435a7b8891d4d3";
    private static final String FROM_NUMBER = "+19086529317"; //TODO import constants from another file
    private String toNumber;
    private String body;
    private TwilioRestClient client;
    private Account account;
    private  MessageFactory messageFactory;
    
    /**
     * @author Jake Sylvestre
     * @param phonenumber - number with country code before it, example: +1-908-752-7625
     * @param body - body of the sms
     */
    public SendSms(String phoneNumber, String messageBody){
        toNumber = phoneNumber;
        body = messageBody;
        client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
        account = client.getAccount();
        messageFactory = account.getMessageFactory();
    }
 
    /**
     * @author Jake Sylvestre
     * Send SMS given phoneNumber and body given in original code
     */
    public void sendSms(){
        try{
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("To", toNumber)); // Replace with a valid phone number for your account.
            params.add(new BasicNameValuePair("From", "+19086529317 ")); // Replace with a valid phone number for your account.
            params.add(new BasicNameValuePair("Body", body));
            Message sms = messageFactory.create(params);
        }
        catch(TwilioRestException error){
                System.err.println("Caught TwilioRestClient: " + error.getMessage());
        }
    }
}
