package example1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProducerConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(
            ObjectMapper jsonObjectMapper,
            CachingConnectionFactory baseCachingConnectionFactory)
    {
        baseCachingConnectionFactory.setPublisherConfirmType(
                CachingConnectionFactory.ConfirmType.CORRELATED
        );

        RabbitTemplate rabbitTemplate = new RabbitTemplate(baseCachingConnectionFactory);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                if(b){
                    System.out.println("Message acked");
                } else {
                    System.out.println("Message not acked\nCause: " + s);
                }
            }
        });
        rabbitTemplate.setMessageConverter(
                new Jackson2JsonMessageConverter(
                        jsonObjectMapper,
                        "*"));
        return rabbitTemplate;
    }
}
