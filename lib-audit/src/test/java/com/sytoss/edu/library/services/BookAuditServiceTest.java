package com.sytoss.edu.library.services;

import com.sytoss.edu.library.dto.BookAuditDTO;
import com.sytoss.edu.library.dto.BooksAuditGenresDTO;
import com.sytoss.edu.library.repositories.BookAuditRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class BookAuditServiceTest {

    private final BookAuditRepository bookAuditRepository = mock(BookAuditRepository.class);

    private final BookAuditService bookAuditService = new BookAuditService(bookAuditRepository);

    @Test
    public void saveChangesTest() {
        LocalDateTime localDateTime = LocalDateTime.parse("2023-06-11 10:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Timestamp changeDate = Timestamp.valueOf(localDateTime);

        BookAuditDTO bookAuditDTO = BookAuditDTO.builder()
                .bookId(1L)
                .name("name")
                .language("language")
                .yearOfPublishing(2013)
                .authorId(2L)
                .changeDate(changeDate)
                .changedBy("changedBy")
                .authorFirstName("authorFirstName")
                .authorSecondName("authorSecondName")
                .authorNationality("authorNationality")
                .build();
        List<BooksAuditGenresDTO> booksAuditGenresDTOs = new ArrayList<>();
        BooksAuditGenresDTO booksAuditGenresDTO = BooksAuditGenresDTO.builder()
                .genreId(8L)
                .bookAuditDTO(bookAuditDTO)
                .build();
        booksAuditGenresDTOs.add(booksAuditGenresDTO);
        bookAuditDTO.setBooksAuditGenresDTOs(booksAuditGenresDTOs);

        long currentVersion = 7;
        when(bookAuditRepository.getLastVersion(bookAuditDTO.getBookId())).thenReturn(currentVersion);
        when(bookAuditRepository.save(bookAuditDTO)).thenReturn(bookAuditDTO);

        bookAuditService.saveChanges(bookAuditDTO);

        verify(bookAuditRepository).save(bookAuditDTO);
        Assertions.assertEquals(currentVersion + 1, bookAuditDTO.getVersion());
    }
}
