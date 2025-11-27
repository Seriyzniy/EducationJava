package producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class MyProducer {
    private final Properties props;

    public MyProducer() {
        props = new Properties();
        props.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "localhost:9092");
        props.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());
        props.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());

        props.put(
                ProducerConfig.RETRIES_CONFIG,
                5);
        props.put(
            ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
            10);
    }

    public void produceMessaging(){
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, String> record1 =
                new ProducerRecord<>(
                    "topic-1",
                    "Message to Topic-1");

            ProducerRecord<String, String> record2 =
                new ProducerRecord<>(
                    "topic-2",
                    "Message to Topic-2");

            producer.send(record1, (metadata, exception) -> {
                if(exception == null) {
                    System.out.println("[INFO] Message successfully sent to topic-1");
                } else {
                    System.out.println("[ERROR] Error while sending message to topic-1");
                }
            });
            producer.send(record2, (metadata, exception) -> {
                if(exception == null) {
                    System.out.println("[INFO] Message successfully sent to topic-2");
                } else {
                    System.out.println("[ERROR] Error while sending message to topic-2");
                }
            });
            System.out.println("[INFO] Message sent");

        } catch (Exception e){
            System.out.println("Exception on produce message");
        }
    }

    public static void main(String[] args) {
        MyProducer myProducer = new MyProducer();
        myProducer.produceMessaging();
    }
}
