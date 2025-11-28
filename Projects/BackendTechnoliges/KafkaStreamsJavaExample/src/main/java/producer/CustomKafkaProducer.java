package producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Data;
import model.DataProduct;
import model.HeaderDataType;
import model.DataUser;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import util.CustomObjectMapper;
import util.DataSerializer;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CustomKafkaProducer {
    private final Properties props = new Properties();
    private final ObjectMapper objectMapper = CustomObjectMapper.instance();

    public static void main(String[] args) {
        try(FileReader fileReader = new FileReader("src/main/resources/kafka-config.properties")){
            Properties props = new Properties();
            props.load(fileReader);

            CustomKafkaProducer kafkaProducer = new CustomKafkaProducer(props.getProperty("kafka.bootstrap-servers"));

            DataUser dataUser = new DataUser(1, "User-1", "RU");
            DataProduct dataProduct = new DataProduct(101, 1, "Iphone", 1);

            kafkaProducer.sendUserRecord(props.getProperty("kafka.topic-users"), dataUser);
            kafkaProducer.sendProductRecord(props.getProperty("kafka.topic-products"), dataProduct);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public CustomKafkaProducer(String bootstrapServers) {
        props.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapServers
        );
        props.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class
        );
        props.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            DataSerializer.class
        );
    }

    public void sendUserRecord (String topic, DataUser user) {
        HeaderDataType header = new HeaderDataType(user.getClass().getName(), Instant.now());

        try (KafkaProducer<String, Data> producer = new KafkaProducer<>(props)){
            List<Header> producerHeaders = new ArrayList<>();
            producerHeaders.add(
                    new RecordHeader(
                            "DataType",
                            objectMapper.writeValueAsBytes(header)
                    )
            );

            ProducerRecord<String, Data> record =
                    new ProducerRecord<>(
                            topic,
                            null,
                            "users",
                            user,
                            producerHeaders);

            producer.send(record).get(10, TimeUnit.SECONDS);

        } catch (JsonProcessingException e) {
            System.out.println("Exception on serializer header on send record");
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Exception on send record");
            throw new RuntimeException(e);
        }
    }

    public void sendProductRecord (String topic, DataProduct product) {
        HeaderDataType header = new HeaderDataType(product.getClass().getName(), Instant.now());

        try (KafkaProducer<String, Data> producer = new KafkaProducer<>(props)){
            List<Header> producerHeaders = new ArrayList<>();
            producerHeaders.add(
                    new RecordHeader(
                            "DataType",
                            objectMapper.writeValueAsBytes(header)
                    )
            );

            ProducerRecord<String, Data> record =
                    new ProducerRecord<>(
                            topic,
                            null,
                            "products",
                            product,
                            producerHeaders);

            producer.send(record).get(10, TimeUnit.SECONDS);

        } catch (JsonProcessingException e) {
            System.out.println("Exception on serializer header on send record");
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Exception on send record");
            throw new RuntimeException(e);
        }
    }
}
