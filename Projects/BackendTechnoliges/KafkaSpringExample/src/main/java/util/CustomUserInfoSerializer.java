package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.UserInfo;
import org.apache.kafka.common.serialization.Serializer;

public class CustomUserInfoSerializer implements Serializer<UserInfo> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, UserInfo userInfo) {
        try {
            return mapper.writeValueAsBytes(userInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
