package com.sytoss.edu.library.producer;

import com.sytoss.edu.library.model.DMEventMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class DMChangeProducerTest {

    private final KafkaTemplate kafkaTemplate = mock(KafkaTemplate.class);

    @Value("${spring.kafka.topics.input-library}")
    private String DMChangesTopic;

    private final DMChangeProducer dmChangeProducer = new DMChangeProducer(kafkaTemplate);

    @Test
    public void sendTest() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        DMEventMessage dmEventMessage = new DMEventMessage();
        DefaultKafkaProducerFactory producerFactory = mock(DefaultKafkaProducerFactory.class);

        when(kafkaTemplate.getProducerFactory()).thenReturn(producerFactory);
        when(producerFactory.getConfigurationProperties()).thenReturn(configProps);

        dmChangeProducer.send(dmEventMessage);

        verify(kafkaTemplate).send(DMChangesTopic, dmEventMessage);
    }
}
