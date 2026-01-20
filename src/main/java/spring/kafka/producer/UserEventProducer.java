package spring.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.*;
import spring.dto.UserEventDto;

@Component
public class UserEventProducer {

    private static final String TOPIC = "user-events";

    private final KafkaTemplate<String, UserEventDto> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UserEventDto event) {
        kafkaTemplate.send(TOPIC, event);
    }

}

