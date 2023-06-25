package com.sytoss.edu.library.repositories;

import com.sytoss.edu.library.dto.AuthorDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorDTO, Long> {

    AuthorDTO getByFirstNameAndLastName(String firstName, String lastName);
}
