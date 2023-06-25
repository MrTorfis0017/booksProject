package com.sytoss.edu.library.converters;

import com.sytoss.edu.library.dto.GenreDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenreConverterTest {

    private final GenreConverter genreConverter = new GenreConverter();

    @Test
    public void toDtoTest() {
        List<String> source = new ArrayList<>();
        Set<GenreDTO> destination = new HashSet<>();
        source.add("FANTASY");
        source.add("HORROR");
        genreConverter.toDto(source, destination);

        for (GenreDTO genre : destination) {
            Assertions.assertTrue(source.contains(genre.getGenre()));
        }
    }

    @Test
    public void fromDTO() {
        GenreDTO genreDTO = new GenreDTO(1L, "Fantasy");
        String genre = genreConverter.fromDTO(genreDTO);
        Assertions.assertEquals(genreDTO.getGenre(), genre);
    }
}
