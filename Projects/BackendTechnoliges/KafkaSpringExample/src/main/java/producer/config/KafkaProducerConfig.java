package producer.config;

import model.UserInfo;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.TransactionManager;
import util.CustomUserInfoSerializer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, UserInfo> producerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29092,localhost:39092,localhost:49092"
        );
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                CustomUserInfoSerializer.class
        );

        // Добавление транзакций
        props.put(
                ProducerConfig.TRANSACTIONAL_ID_CONFIG,
                "transact-id-1");
        props.put(
                ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
                true);

        DefaultKafkaProducerFactory<String, UserInfo> factory =
                new DefaultKafkaProducerFactory<>(props);
        factory.setTransactionIdPrefix("transaction-id-prefix-");
        return factory;
    }

    @Bean
    public KafkaTemplate<String, UserInfo> kafkaTemplate(
            ProducerFactory<String, UserInfo> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource driverManager = new DriverManagerDataSource();

        driverManager.setDriverClassName("org.postgresql.Driver");
        driverManager.setUrl("jdbc:postgresql://localhost:5532/testdb");
        driverManager.setUsername("postgres");
        driverManager.setPassword("postgres");

        return driverManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public TransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}