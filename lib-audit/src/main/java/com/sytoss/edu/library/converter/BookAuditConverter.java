package com.sytoss.edu.library.converter;

import com.sytoss.edu.library.dto.BookAuditDTO;
import com.sytoss.edu.library.dto.BooksAuditGenresDTO;
import com.sytoss.edu.library.model.BookAuditMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BookAuditConverter {

    private BookAuditConverter() {
    }

    public static BookAuditDTO toDto(BookAuditMessage bookAuditMessage) {
        BookAuditDTO bookAuditDTO = BookAuditDTO.builder()
                .bookId(bookAuditMessage.getBookId())
                .name(bookAuditMessage.getName())
                .language(bookAuditMessage.getLanguage())
                .yearOfPublishing(bookAuditMessage.getYearOfPublishing())
                .authorId(bookAuditMessage.getAuthorId())
                .authorFirstName(bookAuditMessage.getAuthorFirstName())
                .authorSecondName(bookAuditMessage.getAuthorSecondName())
                .authorNationality(bookAuditMessage.getAuthorNationality())
                .changeDate(bookAuditMessage.getChangeDate())
                .version(1)
                .changedBy(bookAuditMessage.getChangedBy())
                .build();
        List<BooksAuditGenresDTO> booksAuditGenresDTOs = null;
        if (bookAuditMessage.getGenres() != null) {
            booksAuditGenresDTOs = bookAuditMessage.getGenres().entrySet()
                    .stream()
                    .map(entry -> BooksAuditGenresDTO.builder()
                            .genreId(entry.getKey())
                            .genreName(entry.getValue())
                            .bookAuditDTO(bookAuditDTO)
                            .build())
                    .collect(Collectors.toList());
        }

        bookAuditDTO.setBooksAuditGenresDTOs(booksAuditGenresDTOs);
        log.info("toDto bookAuditDTO {}", bookAuditDTO);
        return bookAuditDTO;
    }
}
