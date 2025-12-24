package example1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConsumerConfig implements InitializingBean {
    private final RabbitAdmin rabbitAdmin;
    private final Queue queue;
    private final Exchange exchange;
    private final Binding binding;

    @Autowired
    public RabbitConsumerConfig(
            RabbitAdmin rabbitAdmin,
            Queue queue,
            Exchange exchange,
            Binding binding)
    {
        this.rabbitAdmin = rabbitAdmin;
        this.queue = queue;
        this.exchange = exchange;
        this.binding = binding;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(binding);
    }

    @Bean
    public DirectRabbitListenerContainerFactory rabbitListenerContainerFactory(
            CachingConnectionFactory baseCachingConnectionFactory,
            ObjectMapper jsonObjectMapper) {
        DirectRabbitListenerContainerFactory factory =
            new DirectRabbitListenerContainerFactory();

        factory.setConnectionFactory(baseCachingConnectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(10);

        factory.setMessageConverter(
            new Jackson2JsonMessageConverter(
                jsonObjectMapper,
                "*"));

        factory.setFailedDeclarationRetryInterval(5000L);
        factory.setIdleEventInterval(60000L);

        return factory;
    }
}