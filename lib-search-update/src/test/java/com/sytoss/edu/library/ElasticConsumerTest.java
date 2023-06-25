package com.sytoss.edu.library;

import com.sytoss.edu.library.consumer.ElasticConsumer;
import com.sytoss.edu.library.model.BookElasticSearchMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:19092", "port=19092" },topics = {"${kafka.topics.elastic}"})
public class ElasticConsumerTest {

   private final ElasticConsumer elasticConsumer = mock(ElasticConsumer.class);

    @Test
    public void consumeTest(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        BookElasticSearchMessage bookElasticSearchMessage = new BookElasticSearchMessage();
        DefaultKafkaConsumerFactory defaultKafkaConsumerFactory =
               mock(DefaultKafkaConsumerFactory.class);
        ConcurrentKafkaListenerContainerFactory<String,BookElasticSearchMessage> concurrentKafkaListenerContainerFactory =
                mock(ConcurrentKafkaListenerContainerFactory.class);
        concurrentKafkaListenerContainerFactory.setConsumerFactory(defaultKafkaConsumerFactory);
        when(defaultKafkaConsumerFactory.getConfigurationProperties()).thenReturn(props);
        when(concurrentKafkaListenerContainerFactory.getConsumerFactory()).thenReturn(defaultKafkaConsumerFactory);
        elasticConsumer.consume(bookElasticSearchMessage);
        verify(concurrentKafkaListenerContainerFactory).setConsumerFactory(defaultKafkaConsumerFactory);
        verify(elasticConsumer).consume(bookElasticSearchMessage);
    }
}
