package Routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            String severity = getSeverity(args);
            String message = getMessage(args);

            channel.basicPublish(EXCHANGE_NAME,severity,null,message.getBytes("UTF-8"));
            System.out.println("Sent "+severity + ":"+message);
        }
        catch (Exception e) {
            System.out.println("EmitLogDirect error " + e.getMessage());
        }
    }
    private static String getSeverity(String[] args) {
        if(args.length<1) {
            return "info";
        }
        return args[0];
    }
    private static String getMessage(String[] args) {
        if(args.length<2) {
            return "Hello World!";
        }
        return joinStrings(args," ",1);
    }
    private static String joinStrings(String[] args, String delimiter,int startIndex) {
        int length = args.length;
        if (length == 0) return "";
        if (length <= startIndex) return "";
        StringBuilder words = new StringBuilder(args[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(args[i]);
        }
        return words.toString();
    }
}
