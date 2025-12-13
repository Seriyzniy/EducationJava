package rabbitmq.example1.util;

import rabbitmq.example1.model.RabbitMessage;

import java.time.Instant;
import java.util.List;
import java.util.Random;

public class RandomizerMessage {
    private static Random rand = new Random();
    private RandomizerMessage() {};
    private static final List<String> buffer = List.of(
            "Banana",
            "Apple",
            "Orange",
            "Potato",
            "Kivi",
            "Watermallon"
    );

    public static RabbitMessage getRandomMessage(){
        return new RabbitMessage(
            buffer.get(rand.nextInt(buffer.size())),
            rand.nextInt(),
            Instant.now()
        );
    }
}
