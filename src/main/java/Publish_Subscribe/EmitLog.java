package Publish_Subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;


/*
* deliver multiple consumers
*
* */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

            String message = args.length < 1? "info : Hello World!"  : String.join(" ",args);
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[Sent] "+message);
        }
        catch (Exception e) {
            System.out.println("EmitLog error : "+e.getMessage());
        }
    }
}
