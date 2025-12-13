package rabbitmq.example1.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import rabbitmq.example1.model.RabbitMessage;
import rabbitmq.example1.util.CustomObjectMapper;
import rabbitmq.example1.util.RandomizerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class RabbitProducer {
    private final static String QUEUE_NAME = "example1-hello-queue";
    private final static String EXCHANGE_NAME = "example1-hello-exchange";
    private final Connection connection;
    private static ObjectMapper objectMapper = CustomObjectMapper.getInstance();

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        RabbitProducer rabbitProducer = new RabbitProducer(factory);

        Scanner scanner = new Scanner(System.in);
        int action = 1;
        while (action > 0) {
            RabbitMessage message = RandomizerMessage.getRandomMessage();
            rabbitProducer.produceMessage(message);

            action = scanner.nextInt();
        }
    }
    public RabbitProducer(ConnectionFactory connectionFactory) {
        try {
            this.connection = connectionFactory.newConnection("localhost");

            try (Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, "direct");
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "hello");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("[Producer] Connection CONNECTION timed out");
            throw new RuntimeException(e);
        }
    }

    public void produceMessage(RabbitMessage message) {
        try(Channel channel = connection.createChannel()) {

            String jsonMessage = objectMapper.writeValueAsString(message);

            AMQP.BasicProperties messageProperties = new AMQP.BasicProperties.Builder()
                .headers(Map.of("CustomHeader", "ValueHeader"))
                .build();

            channel.basicPublish(
                EXCHANGE_NAME,
                "hello",
                messageProperties,
                jsonMessage.getBytes());

            System.out.println("[PRODUCER] Message " + message.toString() + " sent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("[Producer] Connection CHANNEL timed out");
            throw new RuntimeException(e);
        }
    }
}