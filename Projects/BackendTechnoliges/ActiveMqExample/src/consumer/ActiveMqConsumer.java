package consumer;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMqConsumer {
    private static final ConnectionFactory connectionFactory =
            new ActiveMQConnectionFactory("tcp://localhost:61616");

    public static void main(String[] args) {
        try {
            System.out.println("[INFO] Create connection");
            Connection connection = connectionFactory.createConnection();
            connection.start();

            System.out.println("[INFO] Create session");
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            System.out.println("[INFO] Create producer");
            MessageConsumer consumer = session.createConsumer(session.createQueue("MessageQueue"));

            System.out.println("[INFO] Receive message");
            Message receiveMessage = consumer.receive();

            System.out.println("Receive message:" + receiveMessage.getBody(String.class));

            System.out.println("[INFO] All complete");

            session.close();
            connection.stop();
        } catch (JMSException e) {
            System.out.println("[Exception] Error to create connection");
        }
    }
}
