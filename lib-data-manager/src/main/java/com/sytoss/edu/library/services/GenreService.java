package com.sytoss.edu.library.services;

import com.sytoss.edu.library.dto.GenreDTO;
import com.sytoss.edu.library.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreDTO getOrCreateByName(String genre) {
        GenreDTO genreDTO = genreRepository.getByGenre(genre);
        if (genreDTO != null) {
            return genreDTO;
        } else {
            genreDTO = new GenreDTO();
            genreDTO.setGenre(genre);
            return genreRepository.save(genreDTO);
        }
    }

}
