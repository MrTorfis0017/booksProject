package com.sytoss.edu.library.converters;

import com.sytoss.edu.library.bom.Author;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.dto.GenreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookConverter {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public void toDto(Book source, BookDTO destination) {
        destination.setName(source.getName());
        destination.setLanguage(source.getLanguage());
        destination.setYearOfPublishing(source.getYearOfPublishing());
        AuthorDTO authorDTO = new AuthorDTO();
        authorConverter.toDto(source.getAuthor(), authorDTO);
        destination.setAuthor(authorDTO);
        Set<GenreDTO> genres = new HashSet<>();
        genreConverter.toDto(source.getGenres(), genres);
        destination.setGenres(genres);
    }

    public void fromDTO(BookDTO source, Book book) {
        book.setId(source.getId());
        book.setName(source.getName());
        book.setLanguage(source.getLanguage());
        book.setYearOfPublishing(source.getYearOfPublishing());
        book.setGenres(source.getGenres()
                .stream()
                .map(genreConverter::fromDTO)
                .collect(Collectors.toList()));
        Author author = new Author();
        authorConverter.fromDTO(source.getAuthor(), author);
        book.setAuthor(author);
    }
}
