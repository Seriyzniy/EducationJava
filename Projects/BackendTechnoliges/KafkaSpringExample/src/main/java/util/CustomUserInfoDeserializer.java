package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.UserInfo;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class CustomUserInfoDeserializer implements Deserializer<UserInfo> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public UserInfo deserialize(String s, byte[] bytes) {
        try {
            return mapper.readValue(bytes, UserInfo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
