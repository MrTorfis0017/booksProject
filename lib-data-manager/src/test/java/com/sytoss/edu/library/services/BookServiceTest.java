package com.sytoss.edu.library.services;

import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.converters.AuthorConverter;
import com.sytoss.edu.library.converters.BookConverter;
import com.sytoss.edu.library.converters.GenreConverter;
import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.dto.GenreDTO;
import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.params.UpdateBookParams;
import com.sytoss.edu.library.repositories.AuthorRepository;
import com.sytoss.edu.library.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private final BookRepository bookRepository = mock(BookRepository.class);

    private final AuthorRepository authorRepository = mock(AuthorRepository.class);

    private final AuthorService authorService = mock(AuthorService.class);

    private final GenreService genreService = mock(GenreService.class);

    private final GenreConverter genreConverter = mock(GenreConverter.class);

    private final AuthorConverter authorConverter = mock(AuthorConverter.class);

    private final BookConverter bookConverter = new BookConverter(authorConverter, genreConverter);

    private final DMChangeService dmChangeService = mock(DMChangeService.class);

    private final BookService bookService = new BookService(bookRepository, authorService, genreService, bookConverter, dmChangeService);

    @Test
    public void registerBookTest() {
        Book book = mock(Book.class);
        List<String> genreDTOList = new ArrayList<>();
        genreDTOList.add("Genre1");

        Set<GenreDTO> genreDTOSet = new HashSet<>();
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(0L);
        genreDTO.setGenre("Genre1");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(0L);
        bookDTO.setGenres(genreDTOSet);

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        bookDTO.setAuthor(authorDTO);

        when(book.getAuthor()).thenReturn(mock(Author.class));
        when(book.getGenres()).thenReturn(genreDTOList);
        when(genreService.getOrCreateByName(any())).thenReturn(mock(GenreDTO.class));
        when(bookRepository.save(any(BookDTO.class))).thenReturn(bookDTO);

        bookService.registerBook(book, "username");

        verify(authorService).getOrCreateByName(book.getAuthor().getFirstName(), book.getAuthor().getLastName());
        verify(genreService, times(genreDTOList.size())).getOrCreateByName(any());
        verify(bookRepository).save(any());
        verify(dmChangeService).sendMessage(any(DMEventMessage.class));
    }

    @Test
    public void getBookById() {
        BookDTO bookDTO = mock(BookDTO.class);
        when(bookRepository.getReferenceById(1L)).thenReturn(bookDTO);
        bookService.getBookById(1L);
        verify(bookRepository).getReferenceById(1L);
    }

    @Test
    public void updateBookTest() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(0L);
        UpdateBookParams bookParams = mock(UpdateBookParams.class);

        when(bookRepository.getReferenceById(bookParams.getId())).thenReturn(bookDTO);
        when(bookRepository.save(any(BookDTO.class))).thenReturn(bookDTO);

        ResponseEntity<HttpStatus> response = bookService.updateBook(bookParams, "username");

        verify(bookRepository).getReferenceById(bookParams.getId());
        verify(bookRepository).save(bookDTO);
        verify(dmChangeService).sendMessage(any(DMEventMessage.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateNonExistentBookTest() {
        BookDTO bookDTO = new BookDTO();
        UpdateBookParams bookParams = mock(UpdateBookParams.class);

        when(bookRepository.getReferenceById(bookParams.getId())).thenReturn(bookDTO);
        when(bookRepository.getReferenceById(bookParams.getId())).thenThrow(new EntityNotFoundException());

        ResponseEntity<HttpStatus> response = bookService.updateBook(bookParams, "username");

        verify(bookRepository).getReferenceById(bookParams.getId());
        verify(bookRepository, never()).save(bookDTO);
        verify(dmChangeService, never()).sendMessage(any(DMEventMessage.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
