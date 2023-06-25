package com.sytoss.edu.library.cucumber;

import com.sytoss.edu.library.dto.BookAuditDTO;
import com.sytoss.edu.library.dto.BooksAuditGenresDTO;
import io.cucumber.java.DataTableType;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trim;

public class DefinitionDataTableType {

    @DataTableType
    public BookAuditDTODataTable convert(Map<String, String> entry) {
        int version = (entry.containsKey("version")) ? Integer.parseInt(entry.get("version")) : 1;
        BookAuditDTO bookAuditDTO = BookAuditDTO.builder()
                .bookId(Long.parseLong(entry.get("bookId")))
                .name(entry.get("name"))
                .language(entry.get("language"))
                .yearOfPublishing(Integer.parseInt(entry.get("yearOfPublishing")))
                .changeDate(toTimestamp(entry.get("changeDate")))
                .changedBy(entry.get("changedBy"))
                .authorId(Long.parseLong(entry.get("authorId")))
                .authorFirstName(entry.get("authorFirstName"))
                .authorSecondName(entry.get("authorSecondName"))
                .authorNationality(entry.get("authorNationality"))
                .version(version)
                .build();

        List<BooksAuditGenresDTO> booksAuditGenresDTOs = new ArrayList<>();
        List<String> genres = List.of(entry.get("genres").split(","));
        for (String value : genres) {
            List<String> genre = List.of(trim(value).split("\\s+"));
            BooksAuditGenresDTO booksAuditGenresDTO = BooksAuditGenresDTO.builder()
                    .genreId(Integer.parseInt(genre.get(0)))
                    .genreName(genre.get(1))
                    .bookAuditDTO(bookAuditDTO)
                    .build();
            booksAuditGenresDTOs.add(booksAuditGenresDTO);
        }
        bookAuditDTO.setBooksAuditGenresDTOs(booksAuditGenresDTOs);
        String id = entry.get("id");
        BookAuditDTODataTable bookAuditDTODataTable = new BookAuditDTODataTable(id, bookAuditDTO);
        return bookAuditDTODataTable;
    }
    
    @Getter
    public static class BookAuditDTODataTable{

        private final String id;

        private final BookAuditDTO bookAuditDTO;

        public BookAuditDTODataTable(String id, BookAuditDTO bookAuditDTO) {
            this.id = id;
            this.bookAuditDTO = bookAuditDTO;
        }
    }

    private Timestamp toTimestamp(String dateString) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        return Timestamp.valueOf(dateTime);
    }
}
