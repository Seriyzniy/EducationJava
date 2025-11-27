package producer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import producer.service.ProducerService;

@Configuration
@ComponentScan({"producer.config", "producer.service"})
@EnableTransactionManagement
public class KafkaProducerApp {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(KafkaProducerApp.class);

        ProducerService producerService = (ProducerService)context.getBean("producerService");
        producerService.produceMessage();
    }
}
