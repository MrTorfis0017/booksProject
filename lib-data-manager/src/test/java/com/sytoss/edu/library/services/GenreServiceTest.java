package com.sytoss.edu.library.services;

import com.sytoss.edu.library.repositories.GenreRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GenreServiceTest {

    private final GenreRepository genreRepository = mock(GenreRepository.class);

    private final GenreService genreService = new GenreService(genreRepository);

    @Test
    public void getGenreByNameTest() {
        String genre = "Genre";

        genreService.getOrCreateByName(genre);

        verify(genreRepository).getByGenre(genre);
        verify(genreRepository).save(any());
    }
}
