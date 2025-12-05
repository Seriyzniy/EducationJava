package consumer;

import model.UserInfo;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import util.CustomKeyDeserializer;
import util.CustomUserInfoDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class CustomKafkaConsumer {
    private final Properties props;

    public static void main(String[] args) {
        CustomKafkaConsumer consumer = new CustomKafkaConsumer();

        consumer.consumeUserInfo();
    }

    public CustomKafkaConsumer() {
        props = new Properties();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29092,localhost:39092,localhost:49092"
        );
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                CustomKeyDeserializer.class
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
                ConsumerConfig.GROUP_ID_CONFIG,
                "custom_consumer"
        );
    }

    public void consumeUserInfo(){
        try(KafkaConsumer<Integer, UserInfo> consumer = new KafkaConsumer<>(props)){
            consumer.subscribe(Collections.singleton("pg-dev.public.userinfo"));

            while(true){
                ConsumerRecords<Integer, UserInfo> records = consumer.poll(Duration.ofSeconds(1000));

                for(ConsumerRecord<Integer, UserInfo> record : records){
                    System.out.println(
                            "Key = " + record.key() +
                            "\nValue =" +  record.value().toString());
                }
            }
        }
    }
}
