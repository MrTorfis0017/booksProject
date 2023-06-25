package com.sytoss.edu.library.converters;

import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.dto.AuthorDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class AuthorConverterTest {

    private final GenreConverter genreConverter = new GenreConverter();

    private final AuthorConverter authorConverter = new AuthorConverter(genreConverter);

    @Test
    public void toDtoTest() {
        Author source = new Author();
        source.setFirstName("FirstName");
        source.setLastName("LastName");
        source.setNationality("UKRAINIAN");

        AuthorDTO destination = new AuthorDTO();
        authorConverter.toDto(source, destination);

        Assertions.assertEquals(source.getFirstName(), destination.getFirstName());
        Assertions.assertEquals(source.getLastName(), destination.getLastName());
    }

    @Test
    public void fromDTO() {
        AuthorDTO authorDTO = new AuthorDTO(1L, "Joanne", "Rowling", null, new HashSet<>());
        Author author = new Author();
        authorConverter.fromDTO(authorDTO, author);
        Assertions.assertEquals(authorDTO.getId(), author.getId());
        Assertions.assertEquals(authorDTO.getFirstName(), author.getFirstName());
        Assertions.assertEquals(authorDTO.getLastName(), author.getLastName());
    }
}
