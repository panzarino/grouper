import hello.SendSms;

public class smsTester{
    public static void main(String[] argv){
        SendSms sender = new SendSms("+19087527625", "Hello, World!");
        sender.sendSms();
    }
}