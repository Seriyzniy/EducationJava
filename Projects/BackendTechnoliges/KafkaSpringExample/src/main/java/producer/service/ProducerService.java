package producer.service;

import model.CustomHeader;
import model.UserInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class ProducerService {
    private final KafkaTemplate<String, UserInfo> kafkaTemplate;
    private final JdbcTemplate jdbcTemplate;

    public ProducerService(KafkaTemplate<String, UserInfo> kafkaTemplate, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional("transactionManager")
    public void produceMessage(){
        System.out.println("[PRODUCER] In produceMessage");
        UserInfo userInfo = new UserInfo("Transaction in Error", 100);
        CustomHeader customHeader = new CustomHeader("transactionHeader", UUID.randomUUID(), 17);

        Message<UserInfo> message = MessageBuilder
                .withPayload(userInfo)
                .setHeader(KafkaHeaders.TOPIC, "topic-spring-1")
                .setHeader("StringHeader", "string")
                .setHeader("CustomHeader", customHeader)
                .build();

        jdbcTemplate.update(
                "INSERT INTO userinfo(uname, age) VALUES(?, ?)",
                userInfo.getUserName(),
                userInfo.getAge());

        try {
            kafkaTemplate.send(message).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Exception on send Message in transaction");
            e.printStackTrace();
        }
    }
}
