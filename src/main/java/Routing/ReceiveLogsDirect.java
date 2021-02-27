package Routing;

import com.rabbitmq.client.*;

/*
* not Working
* args is empty
* */
public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        for(String severity : args) {
            channel.queueBind(queueName,EXCHANGE_NAME,severity);
        }

        DeliverCallback deliverCallback = (consumerTag,delivery) -> {
            String message = new String(delivery.getBody(),"UTF-8");
            System.out.println("Received : "+ message);
        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});
    }
}
