package com.sytoss.edu.library.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.converters.AuditConverter;
import com.sytoss.edu.library.converters.BookElasticSearchConverter;
import com.sytoss.edu.library.model.BookAuditMessage;
import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.model.BookElasticSearchMessage;
import com.sytoss.edu.library.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventStreamLibraryProcessorTest {

    @Autowired
    private EventStreamLibraryProcessor eventStreamLibraryProcessor;

    @Autowired
    private KafkaStreamsConfiguration kStreamsConfig;

    private TestInputTopic<String, DMEventMessage> inputTopic;

    private TestOutputTopic<String, BookAuditMessage> outputAuditTopic;

    private TestOutputTopic<String, BookElasticSearchMessage> outputElasticTopic;

    private TopologyTestDriver topologyTestDriver;

    @BeforeAll
    public void setup() {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        eventStreamLibraryProcessor.buildPipeline(streamsBuilder);
        Topology topology = streamsBuilder.build();
        try {
            topologyTestDriver = new TopologyTestDriver(topology, kStreamsConfig.asProperties());
            inputTopic = topologyTestDriver
                    .createInputTopic(eventStreamLibraryProcessor.getInputTopic(), new StringSerializer(), new JsonSerializer<>());
            outputAuditTopic = topologyTestDriver
                    .createOutputTopic(eventStreamLibraryProcessor.getOutputAuditTopic(), new StringDeserializer(), new JsonDeserializer<>(BookAuditMessage.class));
            outputElasticTopic = topologyTestDriver
                    .createOutputTopic(eventStreamLibraryProcessor.getOutputElasticSearchTopic(), new StringDeserializer(), new JsonDeserializer<>(BookElasticSearchMessage.class));
        } catch (Exception e) {
            log.error("Error with pipe stream: {}", e);
        }
    }

    @Test
    void buildPipelineTest() {
        try {
            DMEventMessage dmEventMessage = createDMEventMessage();
            String jsonDMEventMessage = new ObjectMapper().writeValueAsString(dmEventMessage);
            log.info("input jsonDMEventMessage {}", jsonDMEventMessage);

            inputTopic.pipeInput("", dmEventMessage);
            List<KeyValue<String, BookAuditMessage>> listAuditValue = outputAuditTopic.readKeyValuesToList().stream().toList();
            log.info("buildPipelineTest: number of messages: {}", listAuditValue.size());

            List<KeyValue<String, BookElasticSearchMessage>> listElasticValue = outputElasticTopic.readKeyValuesToList().stream().toList();
            log.info("buildPipelineTest: number of messages: {}", listElasticValue.size());

            BookAuditMessage bookAuditMessageExpected = AuditConverter.auditConvertor(dmEventMessage);
            String expectedAuditJson = new ObjectMapper().writeValueAsString(bookAuditMessageExpected);

            BookElasticSearchMessage bookElasticSearchMessageExpected = BookElasticSearchConverter.elasticSearchMessageConverter(dmEventMessage);
            String expectedElasticJson = new ObjectMapper().writeValueAsString(bookElasticSearchMessageExpected);

            Assertions.assertEquals(1, listAuditValue.size());
            Assertions.assertEquals(expectedAuditJson, new ObjectMapper().writeValueAsString(listAuditValue.get(0).value));
            Assertions.assertEquals(1, listElasticValue.size());
            Assertions.assertEquals(expectedElasticJson, new ObjectMapper().writeValueAsString(listElasticValue.get(0).value));
        } catch (Exception e) {
            log.error("Error with pipe stream: {}", e);
            Assertions.fail("buildPipelineTest failed by exception");
        }
    }

    @Test
    void buildPipeline2messagesTest() {
        try {
            DMEventMessage dmEventMessage = createDMEventMessage();
            inputTopic.pipeInput("", dmEventMessage);
            inputTopic.pipeInput("", dmEventMessage);

            List<KeyValue<String, BookAuditMessage>> listAuditValue = outputAuditTopic.readKeyValuesToList().stream().toList();
            List<KeyValue<String, BookElasticSearchMessage>> listElasticValue = outputElasticTopic.readKeyValuesToList().stream().toList();

            Assertions.assertEquals(2, listAuditValue.size());
            Assertions.assertEquals(2, listElasticValue.size());
        } catch (Exception e) {
            log.error("Error with pipe stream: {}", e);
            Assertions.fail("buildPipeline2messagesTest failed by exception");
        }
    }

    private DMEventMessage createDMEventMessage() {
        Book book = new Book();
        book.setId(13L);
        book.setName("Java");
        book.setLanguage("English");
        book.setYearOfPublishing(2013);
        book.setGenres(List.of("ADVENTURE", "TECHNIQUES"));

        Author author = new Author();
        author.setId(14L);
        author.setFirstName("Tom");
        author.setLastName("Sojer");
        author.setNationality("UA");

        book.setAuthor(author);

        String pattern = "yyyy-MM-dd HH:mm:ss";
        String dateString = "2023-06-19 12:41:23";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        Timestamp timestamp = Timestamp.valueOf(dateTime);

        return DMEventMessage.builder()
                .eventType(EventType.INSERT)
                .genresIds(List.of(1L, 2L))
                .book(book)
                .userName("Volodymyr")
                .changeDate(timestamp)
                .build();
    }
}
