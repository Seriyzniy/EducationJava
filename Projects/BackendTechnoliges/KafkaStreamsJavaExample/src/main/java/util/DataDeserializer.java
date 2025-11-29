package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Data;
import model.HeaderDataType;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class DataDeserializer implements Deserializer<Data> {
    private final ObjectMapper objectMapper = CustomObjectMapper.instance();

    @Override
    public Data deserialize(String s, byte[] bytes) {
        System.out.println("=== Simple Deserialization ===");
        System.out.println("Topic: " + s);
        System.out.println("Data: " + new String(bytes));
        return null;
    }

    @Override
    public Data deserialize(String topic, Headers headers, byte[] data) {
        System.out.println("=== Headers Deserialization ===");
        System.out.println("Topic: " + topic);
        System.out.println("Data: " + new String(data));

        try {
            byte[] headerDataTypeByte = headers.lastHeader("DataType").value();
            HeaderDataType headerDataType = objectMapper.readValue(headerDataTypeByte, HeaderDataType.class);

            Class<?> clazz = Class.forName(headerDataType.getClassName());

            return (Data)objectMapper.readValue(data, clazz);
        } catch (IOException e) {
            System.out.println("Exception on deserializing data");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("Exception on found CLASS for name");
            throw new RuntimeException(e);
        }
    }
}
