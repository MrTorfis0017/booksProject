package com.sytoss.edu.library.consumer;

import com.sytoss.edu.library.converter.BookAuditConverter;
import com.sytoss.edu.library.model.BookAuditMessage;
import com.sytoss.edu.library.services.BookAuditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaAuditConsumer {

    private final BookAuditService bookAuditService;

    @KafkaListener(topics = "${spring.kafka.topics.audit}", groupId = "lib-audit-consumer")
    public void consume(BookAuditMessage bookAuditMessage) {
        log.info("KafkaAuditConsumer message = " + bookAuditMessage.toString());
        bookAuditService.saveChanges(BookAuditConverter.toDto(bookAuditMessage));
    }
}
