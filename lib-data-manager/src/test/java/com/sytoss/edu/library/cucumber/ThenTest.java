package com.sytoss.edu.library.cucumber;

import com.sytoss.edu.library.IntegrationTest;
import com.sytoss.edu.library.TestContext;
import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.dto.BookDTO;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ThenTest extends IntegrationTest {

    @Transactional
    @Then("book should be registered")
    public void bookShouldBeCreatedInDbWithParameters(List<BookDTO> list) {
        for (BookDTO expectedBookDTO : list) {
            BookDTO bookFromDB = getBookRepository().getReferenceById(TestContext.getInstance().getBookIds().get(String.valueOf(expectedBookDTO.getId())));
            Assertions.assertEquals(expectedBookDTO.getName(), bookFromDB.getName());
            Assertions.assertEquals(expectedBookDTO.getLanguage(), bookFromDB.getLanguage());
            Assertions.assertEquals(expectedBookDTO.getYearOfPublishing(), bookFromDB.getYearOfPublishing());
            Assertions.assertEquals(expectedBookDTO.getAuthor().getFirstName(), bookFromDB.getAuthor().getFirstName());
            Assertions.assertEquals(expectedBookDTO.getAuthor().getLastName(), bookFromDB.getAuthor().getLastName());
            Assertions.assertTrue(expectedBookDTO.getGenres().stream().allMatch(expectedGenre -> bookFromDB.getGenres().stream().anyMatch(actualGenre -> actualGenre.getGenre().equals(expectedGenre.getGenre()))));
        }
    }

    @Then("the book should be returned in responce with this parametres")
    public void theBookWithIdNameLanguageYearOfPublishingGenresAndAuthorShouldBeReturnedInResponce(BookDTO bookDTO) {
        Book needBook = new Book();
        long id = bookDTO.getId();
        needBook.setId(id);
        needBook.setName(bookDTO.getName());
        needBook.setLanguage(bookDTO.getLanguage());
        needBook.setYearOfPublishing(bookDTO.getYearOfPublishing());
        needBook.setGenres(bookDTO.getGenres()
                .stream()
                .map(i -> getGenreConverter().fromDTO(i))
                .collect(Collectors.toList()));
        Author author = new Author();
        getAuthorConverter().fromDTO(bookDTO.getAuthor(), author);
        needBook.setAuthor(author);
        Book actualBook = getBookService().getBookById(id);
        Assertions.assertEquals(needBook.getId(), actualBook.getId());
        Assertions.assertEquals(needBook.getYearOfPublishing(), actualBook.getYearOfPublishing());
        Assertions.assertEquals(needBook.getLanguage(), actualBook.getLanguage());
        Assertions.assertEquals(needBook.getName(), actualBook.getName());
        Assertions.assertEquals(needBook.getAuthor().getFirstName(), actualBook.getAuthor().getFirstName());
        Assertions.assertEquals(needBook.getAuthor().getLastName(), actualBook.getAuthor().getLastName());
        Assertions.assertArrayEquals(needBook.getGenres().stream().sorted().toArray(), actualBook.getGenres().stream().sorted().toArray());
    }

    @Transactional
    @Then("this book should be updated")
    public void thisBookShouldBeUpdated(BookDTO bookDTOExpected) {
        BookDTO bookDTO = getBookRepository().getReferenceById(TestContext.getInstance().getBookIds().get(String.valueOf(bookDTOExpected.getId())));

        assertEquals(bookDTOExpected.getName(), bookDTO.getName());
        assertEquals(bookDTOExpected.getLanguage(), bookDTO.getLanguage());
        assertEquals(bookDTOExpected.getYearOfPublishing(), bookDTO.getYearOfPublishing());
        assertTrue(bookDTOExpected.getGenres().stream().allMatch(expectedGenre -> bookDTO.getGenres().stream().anyMatch(actualGenre -> actualGenre.getGenre().equals(expectedGenre.getGenre()))));
        assertEquals(bookDTOExpected.getAuthor().getFirstName(), bookDTO.getAuthor().getFirstName());
        assertEquals(bookDTOExpected.getAuthor().getLastName(), bookDTO.getAuthor().getLastName());
    }

    @Then("the book should not be registered")
    public void theBookShouldNotBeRegistered() {
        assertNull(TestContext.getInstance().getRegisterResponse().getBody());
    }
}
