package rabbitmq.example2.consumer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {
    private final static String QUEUE_NAME = "example2-task-queue";
    private final Connection connection;

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        RabbitConsumer consumer = new RabbitConsumer(factory);
        consumer.consumeMessageWithChannel();
    }

    public RabbitConsumer(ConnectionFactory connectionFactory) {
        try {
            this.connection = connectionFactory.newConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("Connection timed out");
            throw new RuntimeException(e);
        }
    }

    public void consumeMessageWithChannel() {
        System.out.println("Consuming message ... ");

        try {
            Channel channel = connection.createChannel();
            channel.basicQos(1);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Envelope envelope = delivery.getEnvelope();
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                System.out.println("Consumer Tag: " + consumerTag +
                        "\nExchange: " + envelope.getExchange() +
                        "\nRouting Ket: " + envelope.getRoutingKey()+
                        "\nDelivery Tag" + envelope.getDeliveryTag() +
                        "\nMessage: " + message + "\n");

                try{
                    doWork(message);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };

            boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doWork(String message) throws InterruptedException {
        for(char ch : message.toCharArray()){
            if(ch == '.') Thread.sleep(1000);
        }
    }
}