package com.sytoss.edu.library.controllers;

import com.sytoss.edu.library.AbstractControllerTest;
import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.converters.AuthorConverter;
import com.sytoss.edu.library.converters.BookConverter;
import com.sytoss.edu.library.converters.GenreConverter;
import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.dto.GenreDTO;
import com.sytoss.edu.library.params.UpdateBookParams;
import com.sytoss.edu.library.repositories.AuthorRepository;
import com.sytoss.edu.library.repositories.BookRepository;
import com.sytoss.edu.library.repositories.GenreRepository;
import com.sytoss.edu.library.services.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@Getter
public class BookControllerTest extends AbstractControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    @SpyBean
    private BookRepository bookRepository;

    @Autowired
    @SpyBean
    private AuthorRepository authorRepository;

    @Autowired
    @SpyBean
    private GenreRepository genreRepository;

    private final GenreConverter genreConverter = new GenreConverter();

    private final AuthorConverter authorConverter = new AuthorConverter(genreConverter);

    private final BookConverter bookConverter = new BookConverter(authorConverter, genreConverter);

    @Test
    public void registerBookTest200() {
        Author author = new Author();
        author.setFirstName("Mark");
        author.setLastName("Twain");
        Book book = new Book();
        book.setName("Book One");
        book.setLanguage("UA");
        book.setYearOfPublishing(2004);
        book.setGenres(List.of("FANTASY", "HORROR"));
        book.setAuthor(author);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Book> httpEntity = new HttpEntity<>(book, headers);
        ResponseEntity<Book> response = doPost("/book/register", httpEntity, null, List.of("ADMIN"));

        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void registerBookTest403() {
        Author author = new Author();
        author.setFirstName("Mark");
        author.setLastName("Twain");
        Book book = new Book();
        book.setName("Book One");
        book.setLanguage("UA");
        book.setYearOfPublishing(2004);
        book.setGenres(List.of("FANTASY", "HORROR"));
        book.setAuthor(author);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Book> httpEntity = new HttpEntity<>(book, headers);
        ResponseEntity<Book> response = doPost("/book/register", httpEntity, null, List.of("USER"));

        Assertions.assertEquals(403, response.getStatusCode().value());
    }

    @Test
    public void getBookByIdTest200() {

        Set<GenreDTO> genresSet = new HashSet<>();
        GenreDTO genreDTO = new GenreDTO(1L, "Fantasy");
        getGenreRepository().save(genreDTO);
        genresSet.add(genreDTO);
        AuthorDTO authorDTO = new AuthorDTO(1L, "Joanne", "Rowling", new ArrayList<>(), new HashSet<>());
        getAuthorRepository().save(authorDTO);
        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .name("Harry Potter")
                .yearOfPublishing(1997)
                .language("English")
                .genres(genresSet)
                .author(authorDTO)
                .build();
        BookDTO saved = getBookRepository().save(bookDTO);

        Book book = new Book();
        bookConverter.fromDTO(saved, book);
        when(bookService.getBookById(any())).thenReturn(book);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(bookDTO, headers);
        ResponseEntity<Book> result = doGet("/book/1", httpEntity, Book.class, List.of("USER"));

        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
    }

    @Test
    public void getBookByIdTest500() {
        when(bookService.getBookById(700L)).thenThrow(new EntityNotFoundException());

        ResponseEntity<Object> result = doGet("/book/700", null, Object.class, List.of("USER"));
        assertEquals(HttpStatusCode.valueOf(500), result.getStatusCode());
    }

    @Transactional
    @Test
    public void updateBookTest() {
        Set<GenreDTO> genresSet = new HashSet<>();
        GenreDTO genreDTO = new GenreDTO(1L, "Fantasy");
        getGenreRepository().save(genreDTO);
        genresSet.add(genreDTO);
        AuthorDTO authorDTO = new AuthorDTO(1L, "Joanne", "Rowling", new ArrayList<>(), new HashSet<>());
        getAuthorRepository().save(authorDTO);
        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .name("Harry Potter")
                .yearOfPublishing(1997)
                .language("English")
                .genres(genresSet)
                .author(authorDTO)
                .build();

        BookDTO savedBookDTO = getBookRepository().save(bookDTO);

        UpdateBookParams updateBookParams = UpdateBookParams.builder()
                .id(savedBookDTO.getId())
                .name("ChangedBookName")
                .yearOfPublishing(2010)
                .genres(List.of("Horror"))
                .build();

        String url = "/book/update";

        ResponseEntity<String> response = doPatch(url, updateBookParams, null, List.of("ADMIN"));
        log.info(getBookRepository().getReferenceById(1L).getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateNotExistentBookTest() {

        when(bookService.updateBook(any(), any())).thenThrow(new EntityNotFoundException());

        UpdateBookParams updateBookParams = UpdateBookParams.builder()
                .id(2345L)
                .name("ChangedBookName")
                .yearOfPublishing(2010)
                .genres(List.of("FANTASY"))
                .build();

        String url = "/book/update";

        ResponseEntity<String> response = doPatch(url, updateBookParams, null, List.of("ADMIN"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
