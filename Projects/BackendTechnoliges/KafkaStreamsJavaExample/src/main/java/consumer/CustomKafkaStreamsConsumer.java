package consumer;

import model.DataProduct;
import model.DataUser;
import model.EnrichedData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import util.kafkastreams.DataProductSerde;
import util.kafkastreams.DataUserSerde;
import util.kafkastreams.EnrichedDataSerde;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CustomKafkaStreamsConsumer {
    private final Properties props = new Properties();

    public static void main(String[] args) {
        try(FileReader fileReader = new FileReader("src/main/resources/kafka-config.properties")){
            Properties props = new Properties();
            props.load(fileReader);

            CustomKafkaStreamsConsumer kafkaStreamsConsumer = new CustomKafkaStreamsConsumer(props.getProperty("kafka.bootstrap-servers"));

            kafkaStreamsConsumer.consumeInStream(
                    props.getProperty("kafka.topic-users"),
                    props.getProperty("kafka.topic-products"),
                    props.getProperty("kafka.topic-result"));

        } catch (FileNotFoundException e) {
            System.out.println("File not found in consumer");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomKafkaStreamsConsumer(String bootstrapServers) {
        props.put(
            StreamsConfig.APPLICATION_ID_CONFIG,
            "application.id"
        );
        props.put(
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapServers
        );
        props.put(
            StreamsConfig.STATE_DIR_CONFIG,
            "local-streams-directory"
        );
        props.put(
            StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG),
            "earliest"
        );
    }

    public void consumeInStream(String userTopic, String productTopic, String resultTopic){
        StreamsBuilder builder = new StreamsBuilder();

        KTable<String, DataUser> userKTable = builder.table(
                userTopic,
                Consumed.with(Serdes.String(), new DataUserSerde()));

        KStream<String, DataProduct> productKStream = builder.stream(
                productTopic,
                Consumed.with(Serdes.String(), new DataProductSerde()));

        KStream<String, DataProduct> productKStreamByUserId = productKStream
                .selectKey((key, value) -> String.valueOf((value.getUserId())));

        // Выводим для отладки
        userKTable.toStream().peek((key, value) ->
                System.out.println("KTable User - Key: " + key + ", Value: " + value)
        );

        // Выводим для отладки
        productKStreamByUserId.peek((key, value) ->
                System.out.println("Product stream - Key: " + key + ", Value: " + value)
        );

        KStream<String, EnrichedData> enrichedStream = productKStreamByUserId
                .leftJoin(
                    userKTable,
                    (product, user) -> {
                        // Создаем обогащенный объект
                        EnrichedData enriched = new EnrichedData();

                        if(user != null){
                            enriched.setUserName(user.getUserName());
                            enriched.setCountry(user.getCountry());
                        } else {
                            enriched.setUserName("NOT READY");
                            enriched.setCountry("NOT READY");
                        }
                        enriched.setId(product.getPurchaseId());
                        enriched.setProductName(product.getProductName());
                        enriched.setAmount(product.getAmount());

                        return enriched;
                    },
                    Joined.with(Serdes.String(), new DataProductSerde(), new DataUserSerde())
            );

        // Выводим результат для отладки
        enrichedStream.peek((key, value) ->
                System.out.println("Enriched data - Key: " + key + ", Value: " + value)
        );

        enrichedStream.to(resultTopic, Produced.with(Serdes.String(), new EnrichedDataSerde()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}