package com.sytoss.edu.library.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOOKS_AUDIT_GENRES")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class BooksAuditGenresDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "GENRE_ID")
    private long genreId;

    @Column(name = "GENRE_NAME")
    private String genreName;

    @ManyToOne
    @JoinColumn(name = "BOOK_AUDIT_ID", referencedColumnName = "ID")
    private BookAuditDTO bookAuditDTO;
}
