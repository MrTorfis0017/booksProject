package com.sytoss.edu.library.services;

import com.sytoss.edu.library.repositories.AuthorRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuthorServiceTest {

    private final AuthorRepository authorRepository = mock(AuthorRepository.class);

    private final AuthorService authorService = new AuthorService(authorRepository);

    @Test
    public void getAuthorByNameTest() {
        String firstName = "FirstName";
        String lastName = "LastName";

        authorService.getOrCreateByName(firstName, lastName);

        verify(authorRepository).getByFirstNameAndLastName(firstName, lastName);
        verify(authorRepository).save(any());
    }
}
