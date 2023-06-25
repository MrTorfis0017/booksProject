package com.sytoss.edu.library.dto;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "BOOK_AUDIT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class BookAuditDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "BOOK_ID")
    private long bookId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "YEAR_OF_PUBLISHING")
    private int yearOfPublishing;

    @Column(name = "AUTHOR_ID")
    private long authorId;

    @Column(name = "AUTHOR_FIRST_NAME")
    private String authorFirstName;

    @Column(name = "AUTHOR_SECOND_NAME")
    private String authorSecondName;

    @Column(name = "AUTHOR_NATIONALITY")
    private String authorNationality;

    @Column(name = "VERSION")
    private long version;

    @Column(name = "CHANGE_DATE")
    private Timestamp changeDate;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bookAuditDTO", cascade = CascadeType.ALL)
    private List<BooksAuditGenresDTO> booksAuditGenresDTOs;
}
