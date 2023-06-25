package com.sytoss.edu.library.cucumber;

import com.sytoss.edu.library.IntegrationTest;
import com.sytoss.edu.library.TestContext;
import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.dto.GenreDTO;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class GivenTest extends IntegrationTest {

    @Given("Book exist:")
    public void recordInDatabase(BookDTO bookDTO) {
        AuthorDTO authorDTO = getAuthorRepository().getByFirstNameAndLastName(bookDTO.getAuthor().getFirstName(), bookDTO.getAuthor().getLastName());
        bookDTO.setAuthor(authorDTO == null ? getAuthorRepository().save(bookDTO.getAuthor()) : authorDTO);

        Set<GenreDTO> genreDTOSaved = new HashSet<>();
        for (GenreDTO el : bookDTO.getGenres()) {
            GenreDTO genreDTO = getGenreRepository().getByGenre(el.getGenre());
            genreDTOSaved.add(genreDTO == null ? getGenreRepository().save(el) : genreDTO);
        }
        bookDTO.setGenres(genreDTOSaved);

        Long id = getBookRepository().save(bookDTO).getId();
        TestContext.getInstance().getBookIds().put(String.valueOf(bookDTO.getId()), id);
    }

    @Given("In the store the following books")
    public void inTheStoreTheFollowingBooks(List<BookDTO> bookDTO) {
        bookDTO.forEach(i -> {
            AuthorDTO authorDTO = getAuthorRepository().getByFirstNameAndLastName(i.getAuthor().getFirstName(), i.getAuthor().getLastName());
            i.setAuthor(authorDTO == null ? getAuthorRepository().save(i.getAuthor()) : authorDTO);
        });

        bookDTO.forEach(i -> {
            Set<GenreDTO> genreDTOSaved = new HashSet<>();
            for (GenreDTO el : i.getGenres()) {
                GenreDTO genreDTO = getGenreRepository().getByGenre(el.getGenre());
                genreDTOSaved.add(genreDTO == null ? getGenreRepository().save(el) : genreDTO);
            }
            i.setGenres(genreDTOSaved);
        });

        getBookRepository().saveAll(bookDTO);
    }
}
