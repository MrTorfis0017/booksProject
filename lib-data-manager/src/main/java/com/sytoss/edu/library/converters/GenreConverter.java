package com.sytoss.edu.library.converters;

import com.sytoss.edu.library.dto.GenreDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class GenreConverter {

    public void toDto(List<String> source, Set<GenreDTO> destination) {
        for (String genre : source) {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setGenre(genre);
            destination.add(genreDTO);
        }
    }

    public String fromDTO(GenreDTO genreDTO) {
        return genreDTO.getGenre();
    }
}
