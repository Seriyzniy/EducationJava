package consumer;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Data;
import model.HeaderDataType;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;
import util.CustomObjectMapper;
import util.DataDeserializer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class CustomKafkaConsumer {
    private final Properties props = new Properties();
    private final ObjectMapper objectMapper = CustomObjectMapper.instance();

    public static void main(String[] args) {
        try(FileReader fileReader = new FileReader("src/main/resources/kafka-config.properties")){
            Properties props = new Properties();
            props.load(fileReader);

            CustomKafkaConsumer kafkaConsumer = new CustomKafkaConsumer(props.getProperty("kafka.bootstrap-servers"));

            kafkaConsumer.consume(
                    props.getProperty("kafka.topic-users"),
                    props.getProperty("kafka.topic-products"));

        } catch (FileNotFoundException e) {
            System.out.println("File not found in consumer");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomKafkaConsumer(String bootstrapServers) {
        props.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapServers
        );
        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class
        );
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            DataDeserializer.class
        );
        props.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            "group-1"
        );
        props.put(
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
            "earliest"
        );
    }

    public void consume(String userTopic, String productTopic) {
        try (KafkaConsumer<String, Data> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of(userTopic, productTopic));

            while(true){
                ConsumerRecords<String, Data> records = consumer.poll(Duration.ofSeconds(10));

                for(TopicPartition topicPartition : records.partitions()){
                    System.out.println("Consumer from : " + topicPartition.topic());

                    for(ConsumerRecord<String, Data> record : records.records(topicPartition)){
                        System.out.println(
                            record.value().toString()
                        );
                    }
                    System.out.println();
                }
            }
        }
    }
}
