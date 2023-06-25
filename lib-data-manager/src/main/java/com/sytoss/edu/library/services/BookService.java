package com.sytoss.edu.library.services;

import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.converters.BookConverter;
import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.dto.GenreDTO;
import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.model.EventType;
import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.model.EventType;
import com.sytoss.edu.library.params.UpdateBookParams;
import com.sytoss.edu.library.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookConverter bookConverter;

    private final DMChangeService dmChangeService;

    public Book registerBook(Book book, String username) {
        AuthorDTO authorDTO = authorService.getOrCreateByName(book.getAuthor().getFirstName(), book.getAuthor().getLastName());
        Set<GenreDTO> genreDTOSet = new HashSet<>();
        for (String str : book.getGenres()) {
            genreDTOSet.add(genreService.getOrCreateByName(str));
        }
        BookDTO bookDTO = new BookDTO();
        bookConverter.toDto(book, bookDTO);
        bookDTO.setAuthor(authorDTO);
        bookDTO.setGenres(genreDTOSet);
        if (bookRepository.getBookDTOByNameAndLanguageAndYearOfPublishingAndAuthor(book.getName(), book.getLanguage(), book.getYearOfPublishing(), bookDTO.getAuthor()) == null) {
            BookDTO savedBookDTO = bookRepository.save(bookDTO);
            log.info("Book [{}] was created with id [{}]", bookDTO.getName(), savedBookDTO.getId());
            DMEventMessage dmEventMessage = generateDMEventMessage(savedBookDTO, EventType.INSERT, username);
            dmChangeService.sendMessage(dmEventMessage);
            book.setId(savedBookDTO.getId());
            book.getAuthor().setId(savedBookDTO.getAuthor().getId());
            return book;
        } else {
            log.warn("Book [{}] is already exists", bookDTO.getName());
            return null;
        }
    }

    @Transactional
    public Book getBookById(Long bookId) {
        BookDTO bookDTO = bookRepository.getReferenceById(bookId);
        Book book = new Book();
        bookConverter.fromDTO(bookDTO, book);
        return book;
    }

    public ResponseEntity<HttpStatus> updateBook(UpdateBookParams updateBookParams, String username) {
        try {
            BookDTO bookDTO = bookRepository.getReferenceById(updateBookParams.getId());
            updateBookDTO(bookDTO, updateBookParams);
            BookDTO savedBookDTO = bookRepository.save(bookDTO);
            log.info("Book with id [{}] was updated", updateBookParams.getId());

            DMEventMessage dmEventMessage = generateDMEventMessage(savedBookDTO, EventType.UPDATE, username);
            dmChangeService.sendMessage(dmEventMessage);
        } catch (EntityNotFoundException e) {
            log.warn("There is no such book in DataBase with id [{}]", updateBookParams.getId());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateBookDTO(BookDTO bookDTO, UpdateBookParams updateBookParams) {
        if (updateBookParams.getName() != null) {
            bookDTO.setName(updateBookParams.getName());
        }
        if (updateBookParams.getLanguage() != null) {
            bookDTO.setLanguage(updateBookParams.getLanguage());
        }
        if (updateBookParams.getYearOfPublishing() != null) {
            bookDTO.setYearOfPublishing(updateBookParams.getYearOfPublishing());
        }

        if (updateBookParams.getGenres() != null) {
            Set<GenreDTO> genreDTOSet = new HashSet<>();
            for (String genre : updateBookParams.getGenres()) {
                genreDTOSet.add(genreService.getOrCreateByName(genre));
            }
            bookDTO.setGenres(genreDTOSet);
        }

        if (updateBookParams.getAuthor() != null) {
            AuthorDTO authorDTO = authorService.getOrCreateByName(updateBookParams.getAuthor().getFirstName(), updateBookParams.getAuthor().getLastName());
            bookDTO.setAuthor(authorDTO);
        }
    }

    private DMEventMessage generateDMEventMessage(BookDTO savedBookDTO, EventType eventType, String username) {
        Book book = new Book();
        bookConverter.fromDTO(savedBookDTO, book);

        List<Long> genresIds = new ArrayList<>(savedBookDTO.getGenres().stream().map(GenreDTO::getId).toList());
        Timestamp changeDate = new Timestamp(new Date().getTime());

        return DMEventMessage.builder().
                eventType(eventType)
                .book(book)
                .genresIds(genresIds)
                .userName(username)
                .changeDate(changeDate)
                .build();
    }
}
