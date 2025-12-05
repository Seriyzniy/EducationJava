package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class CustomKeyDeserializer implements Deserializer<Integer> {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public Integer deserialize(String s, byte[] bytes) {
        try {
            JsonNode node = mapper.readTree(bytes);
            int id = node.get("id").asInt();
            return id;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
