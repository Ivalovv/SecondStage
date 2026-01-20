package spring.config;

import org.springframework.context.annotation.*;
import org.springframework.kafka.core.*;
import spring.dto.UserEventDto;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, UserEventDto> kafkaTemplate(ProducerFactory<String, UserEventDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
