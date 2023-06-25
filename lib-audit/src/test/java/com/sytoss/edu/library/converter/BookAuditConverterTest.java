package com.sytoss.edu.library.converter;

import com.sytoss.edu.library.dto.BookAuditDTO;
import com.sytoss.edu.library.model.BookAuditMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class BookAuditConverterTest {

    @Test
    public void auditConvertor() {
        BookAuditMessage bookAuditMessage = createBookAuditMessage();
        BookAuditDTO bookAuditDTO = BookAuditConverter.toDto(bookAuditMessage);

        Assertions.assertEquals(bookAuditMessage.getBookId(), bookAuditDTO.getBookId());
        Assertions.assertEquals(bookAuditMessage.getName(), bookAuditDTO.getName());
        Assertions.assertEquals(bookAuditMessage.getLanguage(), bookAuditDTO.getLanguage());
        Assertions.assertEquals(bookAuditMessage.getYearOfPublishing(), bookAuditDTO.getYearOfPublishing());

        for (int i = 0; i < bookAuditMessage.getGenres().size(); i++) {
            Long genreId = bookAuditDTO.getBooksAuditGenresDTOs().get(i).getGenreId();
            Assertions.assertTrue(bookAuditMessage.getGenres().get(genreId).equals(bookAuditDTO.getBooksAuditGenresDTOs().get(i).getGenreName()));
        }

        Assertions.assertEquals(bookAuditMessage.getAuthorId(), bookAuditDTO.getAuthorId());
        Assertions.assertEquals(bookAuditMessage.getAuthorFirstName(), bookAuditDTO.getAuthorFirstName());
        Assertions.assertEquals(bookAuditMessage.getAuthorSecondName(), bookAuditDTO.getAuthorSecondName());
        Assertions.assertEquals(bookAuditMessage.getAuthorNationality(), bookAuditDTO.getAuthorNationality());

        Assertions.assertEquals(bookAuditMessage.getChangeDate(), bookAuditDTO.getChangeDate());
        Assertions.assertEquals(bookAuditMessage.getChangedBy(), bookAuditDTO.getChangedBy());
    }

    private BookAuditMessage createBookAuditMessage() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String dateString = "2023-06-19 12:41:23";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        Timestamp timestamp = Timestamp.valueOf(dateTime);

        BookAuditMessage bookAuditMessage = new BookAuditMessage();
        bookAuditMessage.setBookId(1L);
        bookAuditMessage.setName("nameTest");
        bookAuditMessage.setLanguage("languageTest");
        bookAuditMessage.setYearOfPublishing(31);
        bookAuditMessage.setAuthorId(32);
        bookAuditMessage.setAuthorFirstName("authorFirstNameTest");
        bookAuditMessage.setAuthorSecondName("authorSecondNameTest");
        bookAuditMessage.setAuthorNationality("authorNationalityTest");
        bookAuditMessage.setChangeDate(timestamp);
        bookAuditMessage.setChangedBy("changedByTest");
        bookAuditMessage.setGenres(new HashMap<>());
        bookAuditMessage.getGenres().put(22L, "genre1");
        bookAuditMessage.getGenres().put(24L, "genre2");
        return bookAuditMessage;
    }
}
