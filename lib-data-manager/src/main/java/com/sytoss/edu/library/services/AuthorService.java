package com.sytoss.edu.library.services;

import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorDTO getOrCreateByName(String firstName, String lastName) {
        AuthorDTO authorDTO = authorRepository.getByFirstNameAndLastName(firstName, lastName);
        if (authorDTO != null) {
            return authorDTO;
        } else {
            authorDTO = new AuthorDTO();
            authorDTO.setFirstName(firstName);
            authorDTO.setLastName(lastName);
            return authorRepository.save(authorDTO);
        }
    }
}
