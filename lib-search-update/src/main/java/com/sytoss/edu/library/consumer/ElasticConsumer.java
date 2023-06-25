package com.sytoss.edu.library.consumer;

import com.sytoss.edu.library.model.BookElasticSearchMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ElasticConsumer {

    @KafkaListener(topics = "${kafka.topics.elastic}", groupId = "lib-streaming-elastic")
    public void consume(BookElasticSearchMessage bookElasticSearchMessage) {
        log.info("message = {} " ,bookElasticSearchMessage.toString());
    }
}
