package rabbitmq.example2.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class RabbitProducer {
    private final static String QUEUE_NAME = "example2-task-queue";
    private final static String EXCHANGE_NAME = "example2-task-exchange";
    private final Connection connection;

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        RabbitProducer rabbitProducer = new RabbitProducer(factory);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Input message with few dot. \n>> ");
            String input = scanner.nextLine();
            rabbitProducer.produceMessage(input);
        }
    }
    public RabbitProducer(ConnectionFactory connectionFactory) {
        try {
            this.connection = connectionFactory.newConnection("localhost");

            try (Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, "direct");
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "task");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("[Producer] Connection CONNECTION timed out");
            throw new RuntimeException(e);
        }
    }

    public void produceMessage(String message) {
        try(Channel channel = connection.createChannel()) {
            channel.basicPublish(
                EXCHANGE_NAME,
                "task",
                null,
                message.getBytes());

            System.out.println("[x] Sent '" + message + "'\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("[Producer] Connection CHANNEL timed out");
            throw new RuntimeException(e);
        }
    }
}