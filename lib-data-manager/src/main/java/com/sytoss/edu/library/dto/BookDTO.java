package com.sytoss.edu.library.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@Table(name = "BOOK")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "YEAR_OF_PUBLISHING")
    private int yearOfPublishing;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID")
    private AuthorDTO author;

    @ManyToMany
    @JoinTable(
            name = "BOOKS_GENRES",
            joinColumns = @JoinColumn(name = "BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
    private Set<GenreDTO> genres;
}
