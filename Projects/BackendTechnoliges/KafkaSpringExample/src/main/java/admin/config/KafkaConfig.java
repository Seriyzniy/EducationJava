package admin.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String,Object> configProps = new HashMap<>();
        configProps.put(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29092,localhost:39092,localhost:49092");

        return new KafkaAdmin(configProps);
    }

    @Bean
    public NewTopic topic(){
        return TopicBuilder.name("topic-spring-1")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
