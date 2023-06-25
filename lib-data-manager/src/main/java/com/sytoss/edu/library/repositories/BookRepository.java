package com.sytoss.edu.library.repositories;

import com.sytoss.edu.library.dto.AuthorDTO;
import com.sytoss.edu.library.dto.BookDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface BookRepository extends JpaRepository<BookDTO, Long> {

    BookDTO getBookDTOByNameAndLanguageAndYearOfPublishingAndAuthor(String bookName, String language, Integer yearOfPublishing, AuthorDTO author);
}
