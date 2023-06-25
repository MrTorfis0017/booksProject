package com.sytoss.edu.library.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Builder
@Table(name = "AUTHOR")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<BookDTO> book;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "AUTHOR_GENRES",
            joinColumns = @JoinColumn(name = "AUTHOR_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
    private Set<GenreDTO> genres;
}
