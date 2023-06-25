package com.sytoss.edu.library.cucumber;

import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.dto.GenreDTO;
import com.sytoss.edu.library.params.UpdateBookParams;
import io.cucumber.java.DataTableType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DataTableCommon {

    @DataTableType
    public BookDTO mapBook(Map<String, String> entry) {
        BookDTO bookDTO = new BookDTO();
        Long id = Long.parseLong(entry.get("id").replaceAll("\\*", ""));
        bookDTO.setId(id);
        bookDTO.setName(entry.get("name"));
        bookDTO.setLanguage(entry.get("language"));
        bookDTO.setYearOfPublishing(Integer.parseInt(entry.get("yearOfPublishing")));
        Set<GenreDTO> genreDTOS = new HashSet<>();
        String[] genres = entry.get("genres").trim().split(",");
        for (String str : genres) {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setGenre(str);
            genreDTOS.add(genreDTO);
        }
        bookDTO.setGenres(genreDTOS);

        String[] author = entry.get("author").trim().split(" ");
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName(author[0]);
        authorDTO.setLastName(author[1]);
        authorDTO.setGenres(new HashSet<>());
        authorDTO.setId(id);
        bookDTO.setAuthor(authorDTO);
        return bookDTO;
    }

    @DataTableType
    public UpdateBookParams updateBookParams(Map<String, String> entry) {
        Long id = Long.parseLong(entry.get("id").replaceAll("\\*", ""));

        List<String> genres;
        if (entry.get("genres") != null) {
            genres = List.of(entry.get("genres").trim().split(","));
        } else {
            genres = null;
        }

        String[] authorSplit;
        Author author;
        if (entry.get("author") != null) {
            authorSplit = entry.get("author").trim().split(" ");
            author = new Author();
            author.setFirstName(authorSplit[0]);
            author.setLastName(authorSplit[1]);
        } else {
            author = null;
        }

        return UpdateBookParams.builder()
                .id(id)
                .name(entry.get("name"))
                .language(entry.get("language"))
                .yearOfPublishing(Integer.parseInt(entry.get("yearOfPublishing")))
                .genres(genres)
                .author(author)
                .build();
    }
}
