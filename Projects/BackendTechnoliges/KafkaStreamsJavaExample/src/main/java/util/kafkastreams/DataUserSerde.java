package util.kafkastreams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.DataUser;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import util.CustomObjectMapper;

import java.io.IOException;

public class DataUserSerde implements Serde<DataUser> {
    private final ObjectMapper objectMapper = CustomObjectMapper.instance();

    @Override
    public Serializer<DataUser> serializer() {
        return new Serializer<>() {
            @Override
            public byte[] serialize(String s, DataUser dataUser) {
                try {
                    return objectMapper.writeValueAsBytes(dataUser);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public Deserializer<DataUser> deserializer() {
        return new Deserializer<>() {
            @Override
            public DataUser deserialize(String s, byte[] bytes) {
                try {
                    return objectMapper.readValue(bytes, DataUser.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
