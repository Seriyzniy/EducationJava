package example1.model;

import java.io.Serializable;
import java.time.Instant;

public class RabbitMessage implements Serializable {
    private int messageId;
    private String messageBody;
    private Instant instant;

    public RabbitMessage() {}
    public RabbitMessage(String messageBody, int messageId, Instant instant) {
        this.messageBody = messageBody;
        this.messageId = messageId;
        this.instant = instant;
    }

    public int getMessageId() {return messageId;}
    public void setMessageId(int messageId) {this.messageId = messageId;}
    public String getMessageBody() {return messageBody;}
    public void setMessageBody(String messageBody) {this.messageBody = messageBody;}
    public Instant getInstant() {return instant;}
    public void setInstant(Instant instant) {this.instant = instant;}

    @Override
    public String toString() {
        return "RabbitMessage{" +
                "messageId=" + messageId +
                ", messageBody='" + messageBody + '\'' +
                ", instant=" + instant +
                '}';
    }
}
