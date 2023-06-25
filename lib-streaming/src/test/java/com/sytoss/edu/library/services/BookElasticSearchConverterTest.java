package com.sytoss.edu.library.services;

import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.converters.BookElasticSearchConverter;
import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.model.BookElasticSearchMessage;
import com.sytoss.edu.library.model.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookElasticSearchConverterTest {

    @Test
    public void elasticSearchConverterTest() {
        DMEventMessage dmEventMessage = createDMEventMessage();
        BookElasticSearchMessage bookElasticSearchMessage = BookElasticSearchConverter.elasticSearchMessageConverter(dmEventMessage);

        Assertions.assertEquals(dmEventMessage.getBook().getId(), bookElasticSearchMessage.getBookId());
        Assertions.assertEquals(dmEventMessage.getBook().getName(), bookElasticSearchMessage.getBookName());
        Assertions.assertEquals(dmEventMessage.getBook().getLanguage(), bookElasticSearchMessage.getLanguage());
        Assertions.assertEquals(dmEventMessage.getBook().getYearOfPublishing(), bookElasticSearchMessage.getYearOfPublishing());

        for (int i = 0; i < dmEventMessage.getBook().getGenres().size(); i++) {
            Assertions.assertEquals(dmEventMessage.getBook().getGenres().get(i), bookElasticSearchMessage.getGenres().get(i));
        }

        Assertions.assertEquals(dmEventMessage.getBook().getAuthor().getId(), bookElasticSearchMessage.getAuthorId());
        Assertions.assertEquals(dmEventMessage.getBook().getAuthor().getFirstName(), bookElasticSearchMessage.getAuthorFirstName());
        Assertions.assertEquals(dmEventMessage.getBook().getAuthor().getLastName(), bookElasticSearchMessage.getAuthorLastName());
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
