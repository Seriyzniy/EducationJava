package consumer.config;

import model.UserInfo;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import util.CustomUserInfoDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, UserInfo> consumerFactory() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29092,localhost:39092,localhost:49092"
        );
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                CustomUserInfoDeserializer.class
        );
        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );
        props.put(
                ConsumerConfig.ISOLATION_LEVEL_CONFIG,
                "read_committed"
        );
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "custom_consumer"
        );
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserInfo> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserInfo>
            factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        DefaultKafkaHeaderMapper headerMapper = new DefaultKafkaHeaderMapper();
        headerMapper.addTrustedPackages("*");

        MessagingMessageConverter converter = new MessagingMessageConverter();
        converter.setHeaderMapper(headerMapper);

        factory.setRecordMessageConverter(converter);

        return factory;
    }
}
