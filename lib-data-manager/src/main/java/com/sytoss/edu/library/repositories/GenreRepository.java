package com.sytoss.edu.library.repositories;

import com.sytoss.edu.library.dto.GenreDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreDTO, Long> {

    GenreDTO getByGenre(String genre);
}
