package admin;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class CustomKafkaAdmin {
    private final Properties props = new Properties();

    public CustomKafkaAdmin(String bootstrapServers) {
        props.put(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapServers
        );
    }

    public void createTopic(String topicName, int partitions, int replication) {
        try(AdminClient adminClient = AdminClient.create(props)) {
            adminClient.createTopics(
                    Collections.singleton(
                            new NewTopic(
                                    topicName,
                                    partitions,
                                    (short)replication
                            )
                    )
            );
        } catch (Exception e) {
            System.out.println("Exception on Kafka Admin, in create topic: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try(FileReader fileReader = new FileReader("src/main/resources/kafka-config.properties")) {
            Properties props = new Properties();
            props.load(fileReader);

            CustomKafkaAdmin admin = new CustomKafkaAdmin(props.getProperty("kafka.bootstrap-servers"));

            admin.createTopic(props.getProperty("kafka.topic-users"), 2, 2);
            admin.createTopic(props.getProperty("kafka.topic-products"), 2, 2);
            admin.createTopic(props.getProperty("kafka.topic-result"), 2, 2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
