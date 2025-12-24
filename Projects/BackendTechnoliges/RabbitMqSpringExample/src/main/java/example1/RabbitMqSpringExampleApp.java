package example1;

import example1.model.RabbitMessage;
import example1.producer.CustomRabbitProducer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
@ComponentScan({"example1.config", "example1.consumer", "example1.producer"})
public class RabbitMqSpringExampleApp {

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(RabbitMqSpringExampleApp.class);

        RabbitMessage message = new RabbitMessage(
                "Second Message Hello",
                7,
                Instant.now()
        );

        CustomRabbitProducer producer =
            context.getBean(
                "customRabbitProducer",
                CustomRabbitProducer.class);

        producer.sendMessage(message);
    }
}
