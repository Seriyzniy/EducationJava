package example1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:rabbit.properties")
public class RabbitConfig {

    @Value("${rabbit.queue}")
    private String queueName;

    @Value("${rabbit.exchange}")
    private String exchangeName;

    @Value("${rabbit.routing-key}")
    private String routingKey;

    @Value("${rabbit.host}")
    private String host;

    @Value("${rabbit.port}")
    private int port;

    @Value("${rabbit.user}")
    private String username;

    @Value("${rabbit.password}")
    private String userPassword;

    @Bean
    public Queue queue(){
        return QueueBuilder
                .nonDurable(queueName)
                .build();
    }

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder
                .directExchange(exchangeName)
                .durable(false)
                .build();
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey)
                .noargs();
    }

    @Bean
    public ObjectMapper jsonObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    @Scope("prototype")
    public CachingConnectionFactory baseCachingConnectionFactory(){
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory();

        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(userPassword);

        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory baseCachingConnectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(baseCachingConnectionFactory);
        rabbitAdmin.setAutoStartup(true);

        return rabbitAdmin;
    }
}