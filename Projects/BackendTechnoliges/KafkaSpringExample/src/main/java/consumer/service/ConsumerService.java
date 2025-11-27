package consumer.service;

import model.CustomHeader;
import model.UserInfo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ConsumerService {

    @KafkaListener(
            topics = "topic-spring-1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listener(Message<UserInfo> message,
                         @Nullable @Header("StringHeader") String simple_header,
                         @Nullable @Header("CustomHeader") CustomHeader custom_header) {
        System.out.println("[CONSUMER] In listener");
        System.out.println(message.getPayload());

        if(simple_header != null && custom_header != null) {
            System.out.println(simple_header);
            System.out.println(custom_header);
        }
    }
}
