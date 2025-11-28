package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CustomObjectMapper {
    private static final ObjectMapper INSTANCE = new ObjectMapper();

    private CustomObjectMapper() {}
    static {
        INSTANCE.registerModule(new JavaTimeModule());
    }

    public static ObjectMapper instance() {
        return INSTANCE;
    }
}
