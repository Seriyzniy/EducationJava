package util.kafkastreams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.EnrichedData;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import util.CustomObjectMapper;

import java.io.IOException;

public class EnrichedDataSerde implements Serde<EnrichedData> {
    private final ObjectMapper objectMapper = CustomObjectMapper.instance();

    @Override
    public Serializer<EnrichedData> serializer() {
        return new Serializer<EnrichedData>() {
            @Override
            public byte[] serialize(String s, EnrichedData enrichedData) {
                try {
                    return objectMapper.writeValueAsBytes(enrichedData);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public Deserializer<EnrichedData> deserializer() {
        return new Deserializer<EnrichedData>() {
            @Override
            public EnrichedData deserialize(String s,byte[] bytes) {
                try {
                    return objectMapper.readValue(bytes, EnrichedData.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
