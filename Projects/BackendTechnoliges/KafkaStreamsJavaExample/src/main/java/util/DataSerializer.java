package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Data;
import org.apache.kafka.common.serialization.Serializer;

public class DataSerializer implements Serializer<Data> {
    private final ObjectMapper objectMapper = CustomObjectMapper.instance();

    @Override
    public byte[] serialize(String s, Data data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            System.out.println("Exception on serializer User");
            throw new RuntimeException(e);
        }
    }
}
