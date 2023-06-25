package com.sytoss.edu.library.services;

import com.sytoss.edu.library.converters.AuditConverter;
import com.sytoss.edu.library.converters.BookElasticSearchConverter;
import com.sytoss.edu.library.model.BookAuditMessage;
import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.model.BookElasticSearchMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class EventStreamLibraryProcessor {

    private static final Serde<String> STRING_SERDE = Serdes.String();

    private static final Serde<DMEventMessage> dmEventMessageSerde = new JsonSerde<>(DMEventMessage.class);

    private static final Serde<BookAuditMessage> bookAuditMessage = new JsonSerde<>(BookAuditMessage.class);

    private static final Serde<BookElasticSearchMessage> elasticSearchMessage = new JsonSerde<>(BookElasticSearchMessage.class);

    @Value(value = "${spring.kafka.streams.topics.input-library}")
    private String inputTopic;

    @Value(value = "${spring.kafka.streams.topics.output-audit}")
    private String outputAuditTopic;

    @Value(value = "${spring.kafka.streams.topics.output-elasticsearch}")
    private String outputElasticSearchTopic;

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {
        log.info("buildPipeline: inputTopic {} outputAuditTopic {} outputElasticSearch{}", inputTopic, outputAuditTopic, outputElasticSearchTopic);
        KStream<String, DMEventMessage> inputTopicStream = streamsBuilder
                .stream(inputTopic, Consumed.with(STRING_SERDE, dmEventMessageSerde));

        inputTopicStream.foreach((key, value) -> log.info("inputTopicStream: Key: {} Value {}", key, value.toString()));

        KStream<String, BookAuditMessage> outputAuditStream = inputTopicStream.mapValues(AuditConverter::auditConvertor);
        outputAuditStream.filter((key, value) -> value != null).to(outputAuditTopic, Produced.with(STRING_SERDE, bookAuditMessage));

        outputAuditStream.foreach((key, value) -> log.info("outputTopicStream: Key: {} Value {}", key, value.toString()));

        KStream<String, BookElasticSearchMessage> outputElasticSearchStream = inputTopicStream.mapValues(BookElasticSearchConverter::elasticSearchMessageConverter);
        outputElasticSearchStream.filter((key, value) -> value != null).to(outputElasticSearchTopic, Produced.with(STRING_SERDE, elasticSearchMessage));

        outputElasticSearchStream.foreach((key, value) -> log.info("outputElasticSearchStream: Key: {} Value {}", key, value.toString()));
    }
}
