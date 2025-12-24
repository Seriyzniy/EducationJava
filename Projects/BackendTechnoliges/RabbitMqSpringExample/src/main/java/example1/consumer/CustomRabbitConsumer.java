package example1.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import example1.model.RabbitMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomRabbitConsumer {

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomRabbitConsumer(ObjectMapper jsonObjectMapper) {
        this.objectMapper = jsonObjectMapper;
    }

    @RabbitListener(queues = "#{'${rabbit.queue}'}")
    public void consume(
            Message message,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            Channel channel)
    {
        try {
            RabbitMessage rabbitMessage =
                objectMapper.readValue(
                    message.getBody(),
                    RabbitMessage.class);

            String header = message.getMessageProperties().getHeader("MyCustomHeader");

            System.out.println("Received \nMessage: " + rabbitMessage + "\nHeader: " + header);

            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
