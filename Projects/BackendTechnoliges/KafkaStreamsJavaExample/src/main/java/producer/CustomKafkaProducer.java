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

            DataUser dataUser1 = new DataUser(1,"Tom","US");
            DataUser dataUser2 = new DataUser(2, "Jerry", "US");
            DataUser dataUser3 = new DataUser(3, "Alice", "RU");
            DataUser dataUser4 = new DataUser(2, "Jerry", "US");
            DataUser dataUser5 = new DataUser(1,"Tom","US");
            DataUser dataUser6 = new DataUser(4, "Elizabeth", "RU");

            List<DataUser> dataUsers = List.of(dataUser1, dataUser2, dataUser3, dataUser4, dataUser5, dataUser6);

            DataProduct dataProduct1 =
                    new DataProduct(101,1,"Iphone",1);
            DataProduct dataProduct2 =
                    new DataProduct(102, 2, "MacBook Pro", 1);
            DataProduct dataProduct3 =
                    new DataProduct(103, 3, "Samsung Galaxy", 2);
            DataProduct dataProduct4 =
                    new DataProduct(104, 2, "AirPods", 3);
            DataProduct dataProduct5 =
                    new DataProduct(105, 1, "iPad", 1);
            DataProduct dataProduct6 =
                    new DataProduct(106, 4, "PlayStation 5", 1);

            List<DataProduct> dataProducts = List.of(dataProduct1, dataProduct2, dataProduct3, dataProduct4, dataProduct5,dataProduct6);

            kafkaProducer.sendUserRecord(props.getProperty("kafka.topic-users"), dataUsers);
            kafkaProducer.sendProductRecord(props.getProperty("kafka.topic-products"), dataProducts);

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

    public void sendUserRecord (String topic, List<DataUser> users) {
        try (KafkaProducer<String, Data> producer = new KafkaProducer<>(props)){
            for(DataUser user : users){
                HeaderDataType header = new HeaderDataType(user.getClass().getName(), Instant.now());
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
                                String.valueOf(user.getUserId()),
                                user,
                                producerHeaders);

                producer.send(record).get(10, TimeUnit.SECONDS);
            }
        } catch (JsonProcessingException e) {
            System.out.println("Exception on serializer header on send record");
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Exception on send record");
            throw new RuntimeException(e);
        }
    }

    public void sendProductRecord (String topic, List<DataProduct> products) {
        try (KafkaProducer<String, Data> producer = new KafkaProducer<>(props)){
            for(DataProduct product : products) {
                HeaderDataType header = new HeaderDataType(product.getClass().getName(), Instant.now());

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
                                String.valueOf(product.getPurchaseId()),
                                product,
                                producerHeaders);

                producer.send(record).get(10, TimeUnit.SECONDS);
            }

        } catch (JsonProcessingException e) {
            System.out.println("Exception on serializer header on send record");
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Exception on send record");
            throw new RuntimeException(e);
        }
    }
}
