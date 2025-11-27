package admin;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.List;
import java.util.Properties;

public class MyAdminClient {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient admin = AdminClient.create(props)){
            NewTopic topic1 = new NewTopic("topic-1", 1, (short) 1);
            NewTopic topic2 = new NewTopic("topic-2", 1, (short) 1);
            admin.createTopics(List.of(topic1, topic2));

        } catch (Exception e){
            System.out.println("Exception on Admin API" + e.getMessage());
        }
    }
}