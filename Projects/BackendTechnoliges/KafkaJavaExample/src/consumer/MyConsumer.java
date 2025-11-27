package consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MyConsumer {
    private final Properties props;

    public MyConsumer() {
        props = new Properties();
        props.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "localhost:9092"
        );
        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class
        );
        props.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            "group-2");
        props.put(
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
            "earliest");

        //Настройка на чтение только ЗАКОМИЧЕННЫХ СООБЩЕНИЙ
        props.put(
            ConsumerConfig.ISOLATION_LEVEL_CONFIG,
            "read_committed");
    }

    public void consumeMessages() {
        try(KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of("topic-1", "topic-2"));
            System.out.println("[INFO] Subscribed to topic: topic-1 and topic-2");

            System.out.println("[INFO] Polling messages...");
            while(true){
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                for(TopicPartition partition : records.partitions()){
                    System.out.println("[INFO] Message is consumed!\nPartition: " + partition);
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

                    for(ConsumerRecord<String, String> record : partitionRecords){
                        System.out.println("Message: " + record.value());
                    }

                    System.out.println();
                }
            }

        } catch (Exception e){
            System.out.println("Exception on consume message");
        }
    }

    public static void main(String[] args) {
        MyConsumer myConsumer = new MyConsumer();
        myConsumer.consumeMessages();
    }
}
