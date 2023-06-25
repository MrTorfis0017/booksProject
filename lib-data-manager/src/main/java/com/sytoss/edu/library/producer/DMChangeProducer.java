package com.sytoss.edu.library.producer;

import com.sytoss.edu.library.model.DMEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class DMChangeProducer {

    private final KafkaTemplate<String, DMEventMessage> kafkaTemplate;

    @Value("${spring.kafka.topics.input-library}")
    private String DMChangesTopic;

    private final int ADMIN_CLIENT_TIMEOUT_MS = 5000;

    public void send(DMEventMessage eventMessage) {
        Map<String, Object> properties = kafkaTemplate.getProducerFactory().getConfigurationProperties();
        try (AdminClient client = AdminClient.create(properties)) {
            Collection<TopicListing> topicsOptions = client.listTopics(new ListTopicsOptions().timeoutMs(ADMIN_CLIENT_TIMEOUT_MS)).listings().get();
            client.close();
            if (!topicsOptions.isEmpty()) {
                kafkaTemplate.send(DMChangesTopic, eventMessage);
            }
        } catch (ExecutionException | InterruptedException ex) {
            log.error("Kafka is not available, timed out after {} ms", ADMIN_CLIENT_TIMEOUT_MS);
        }
    }
}
