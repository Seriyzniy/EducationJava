package util.kafkastreams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.DataProduct;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import util.CustomObjectMapper;

import java.io.IOException;

public class DataProductSerde implements Serde<DataProduct> {
    private final ObjectMapper objectMapper = CustomObjectMapper.instance();

    @Override
    public Serializer<DataProduct> serializer() {
        return new Serializer<DataProduct>() {
            @Override
            public byte[] serialize(String s, DataProduct dataProduct) {
                try {
                    return objectMapper.writeValueAsBytes(dataProduct);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public Deserializer<DataProduct> deserializer() {
        return new Deserializer<>() {
            @Override
            public DataProduct deserialize(String s, byte[] bytes) {
                try {
                    return objectMapper.readValue(bytes, DataProduct.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
