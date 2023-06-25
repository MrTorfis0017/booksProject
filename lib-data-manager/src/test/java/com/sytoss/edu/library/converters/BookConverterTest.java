package com.sytoss.edu.library.converters;

import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.dto.GenreDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookConverterTest {

    private final GenreConverter genreConverter = new GenreConverter();

    private final AuthorConverter authorConverter = new AuthorConverter(genreConverter);

    private final BookConverter bookConverter = new BookConverter(authorConverter, genreConverter);

    @Test
    public void toDtoTest() {
        BookDTO destination = new BookDTO();
        Book source = new Book();
        source.setName("Tom Sawyer");
        source.setLanguage("UA");
        source.setYearOfPublishing(2004);
        Author author = new Author();
        author.setFirstName("Mark");
        source.setAuthor(author);
        source.setGenres(List.of("NOVEL", "ADVENTURE"));

        bookConverter.toDto(source, destination);

        Assertions.assertEquals(source.getName(), destination.getName());
        Assertions.assertEquals(source.getLanguage(), destination.getLanguage());
        Assertions.assertEquals(source.getYearOfPublishing(), destination.getYearOfPublishing());
        Assertions.assertEquals(source.getAuthor().getFirstName(), destination.getAuthor().getFirstName());
        Assertions.assertEquals(source.getAuthor().getLastName(), destination.getAuthor().getLastName());
        for (GenreDTO genreDTO : destination.getGenres()) {
            Assertions.assertTrue(source.getGenres().contains(genreDTO.getGenre()));
        }
    }

    @Test
    public void fromDTO() {
        Set<GenreDTO> genresSet = new HashSet<>();
        GenreDTO genreDTO = new GenreDTO(1L, "Fantasy");
        genresSet.add(genreDTO);
        AuthorDTO authorDTO = new AuthorDTO(1L, "Joanne", "Rowling", null, new HashSet<>());
        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .name("Harry Potter")
                .yearOfPublishing(1997)
                .language("English")
                .genres(genresSet)
                .author(authorDTO)
                .build();
        Book book = new Book();
        bookConverter.fromDTO(bookDTO, book);
        Assertions.assertEquals(bookDTO.getId(), book.getId());
        Assertions.assertEquals(bookDTO.getName(), book.getName());
        Assertions.assertEquals(bookDTO.getLanguage(), book.getLanguage());
        Assertions.assertEquals(bookDTO.getYearOfPublishing(), book.getYearOfPublishing());

    }
}
