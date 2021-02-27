package Work_Queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
* Worker Queue
* 각각의 task가 정확히 하나의 worker들에게 전달됨
* */
public class Worker {
    private static final String TASK_QUEUE = "task_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE,true,false,false,null);

        channel.basicQos(1); // message를 한번에 보내는 갯수를 조절함
        DeliverCallback deliverCallback = (consumerTag,delivery)-> {
            String message = new String(delivery.getBody(),"UTF-8");
            try {
                doWork(message);
            }
            finally {
                System.out.println("DONE");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        channel.basicConsume(TASK_QUEUE,false,deliverCallback,consumerTag->{});
    }
    private static void doWork(String Task) {
        for(char ch : Task.toCharArray()) {
            if(ch == '.') {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
