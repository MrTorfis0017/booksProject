package com.sytoss.edu.library.converters;

import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.dto.AuthorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorConverter {

    private final GenreConverter genreConverter;

    public void toDto(Author source, AuthorDTO destination) {
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }

    public void fromDTO(AuthorDTO source, Author destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        if (source.getGenres() != null && source.getGenres().size() > 0) {
            destination.setGenres(source.getGenres().stream().map(genreConverter::fromDTO).collect(Collectors.toList()));
        }
    }
}
