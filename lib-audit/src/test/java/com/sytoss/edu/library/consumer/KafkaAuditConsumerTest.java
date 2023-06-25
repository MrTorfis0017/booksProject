package com.sytoss.edu.library.consumer;

import com.sytoss.edu.library.model.BookAuditMessage;
import com.sytoss.edu.library.services.BookAuditService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class KafkaAuditConsumerTest {

    private final BookAuditService bookAuditService = mock(BookAuditService.class);

    private final KafkaAuditConsumer kafkaAuditConsumer = new KafkaAuditConsumer(bookAuditService);

    @Test
    public void consumeTest() {
        BookAuditMessage bookAuditMessage = new BookAuditMessage();
        bookAuditMessage.setBookId(117L);
        kafkaAuditConsumer.consume(bookAuditMessage);

        verify(bookAuditService).saveChanges(argThat(value -> value.getBookId() == bookAuditMessage.getBookId()));
    }
}
