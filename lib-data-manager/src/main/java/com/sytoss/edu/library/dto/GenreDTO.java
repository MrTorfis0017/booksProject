package com.sytoss.edu.library.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "GENRE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenreDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "GENRE")
    private String genre;

}
