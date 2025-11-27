package producer;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.Scanner;

public class ActiveMqProducer {
    private static final ConnectionFactory connectionFactory =
            new ActiveMQConnectionFactory("tcp://localhost:61616");

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String input = " ";
        try {
            System.out.println("[INFO] Create connection");
            Connection connection = connectionFactory.createConnection();
            connection.start();

            System.out.println("[INFO] Create session");
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            System.out.println("[INFO] Create producer");
            MessageProducer producer = session.createProducer(session.createQueue("MessageQueue"));

            System.out.println("[INFO] Create text message");
            TextMessage textMessage = session.createTextMessage();

            System.out.println("[INFO] Input text (enter q to exit)");

            while(!input.equals("q")){
                System.out.print(">");
                input = scanner.nextLine();
                textMessage.setText(input);

                System.out.println("[INFO] Message sent");
                producer.send(textMessage);
            }

            System.out.println("[INFO] All complete");

            session.close();
            connection.stop();
        } catch (JMSException e) {
            System.out.println("[Exception] Error to create connection");
        }
    }
}


