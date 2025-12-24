package example1.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example1.model.RabbitMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomRabbitProducer {

    @Value("${rabbit.exchange}")
    private String exchangeName;

    @Value("${rabbit.routing-key}")
    private String routingKey;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CustomRabbitProducer(RabbitTemplate rabbitTemplate, ObjectMapper jsonObjectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = jsonObjectMapper;
    }
    public void sendMessage(RabbitMessage payload) {
        try {
            Message message = MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(payload))
                    .setHeader("MyCustomHeader", "womp womp")
                    .build();

            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

            System.out.println("Message sent");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}