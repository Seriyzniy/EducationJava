package rabbitmq.example1.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CustomObjectMapper {
    private static final ObjectMapper INSTANCE;

    private CustomObjectMapper() {}
    static {
        INSTANCE = new ObjectMapper();
        INSTANCE.registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }
}