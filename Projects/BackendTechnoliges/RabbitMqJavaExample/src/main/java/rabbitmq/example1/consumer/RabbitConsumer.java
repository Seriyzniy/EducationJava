package rabbitmq.example1.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import rabbitmq.example1.model.RabbitMessage;
import rabbitmq.example1.util.CustomObjectMapper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {
    private final static String QUEUE_NAME = "example1-hello-queue";
    private final Connection connection;

    private final ObjectMapper objectMapper = CustomObjectMapper.getInstance();

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
        try{
            Channel channel = connection.createChannel();
            channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel){
                public void handleDelivery(
                        String consumerTag,
                        Envelope envelope,
                        AMQP.BasicProperties properties,
                        byte[] body) throws IOException
                {
                    System.out.println("Consumer Tag: " + consumerTag +
                            "\nExchange: " + envelope.getExchange() +
                            "\nRouting Ket: " + envelope.getRoutingKey()+
                            "\nDelivery Tag" + envelope.getDeliveryTag());

                    RabbitMessage message = objectMapper.readValue(
                            body,
                            RabbitMessage.class);

                    System.out.println("Receive Message: " + message);

                    channel.basicAck(envelope.getDeliveryTag(), false);
                    System.out.println("Sent ACK\n");
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
