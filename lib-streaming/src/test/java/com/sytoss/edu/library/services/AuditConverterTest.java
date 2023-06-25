package com.sytoss.edu.library.services;

import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.converters.AuditConverter;
import com.sytoss.edu.library.model.BookAuditMessage;
import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.model.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AuditConverterTest {

    @Test
    public void auditConvertor() {

        DMEventMessage dmEventMessage = createDMEventMessage();
        BookAuditMessage bookAuditMessage = AuditConverter.auditConvertor(dmEventMessage);

        Assertions.assertEquals(dmEventMessage.getBook().getId(), bookAuditMessage.getBookId());
        Assertions.assertEquals(dmEventMessage.getBook().getName(), bookAuditMessage.getName());
        Assertions.assertEquals(dmEventMessage.getBook().getLanguage(), bookAuditMessage.getLanguage());
        Assertions.assertEquals(dmEventMessage.getBook().getYearOfPublishing(), bookAuditMessage.getYearOfPublishing());

        for (int i = 0; i < dmEventMessage.getBook().getGenres().size(); i++) {
            Long genreId = dmEventMessage.getGenresIds().get(i);
            Assertions.assertTrue(dmEventMessage.getBook().getGenres().get(i).equals(bookAuditMessage.getGenres().get(genreId)));
        }

        Assertions.assertEquals(dmEventMessage.getBook().getAuthor().getId(), bookAuditMessage.getAuthorId());
        Assertions.assertEquals(dmEventMessage.getBook().getAuthor().getFirstName(), bookAuditMessage.getAuthorFirstName());
        Assertions.assertEquals(dmEventMessage.getBook().getAuthor().getLastName(), bookAuditMessage.getAuthorSecondName());
        Assertions.assertEquals(dmEventMessage.getBook().getAuthor().getNationality(), bookAuditMessage.getAuthorNationality());

        Assertions.assertEquals(dmEventMessage.getChangeDate(), bookAuditMessage.getChangeDate());
        Assertions.assertEquals(dmEventMessage.getUserName(), bookAuditMessage.getChangedBy());
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
