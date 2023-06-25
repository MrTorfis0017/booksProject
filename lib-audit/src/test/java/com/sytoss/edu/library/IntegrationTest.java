package com.sytoss.edu.library;

import com.sytoss.edu.library.repositories.BookAuditRepository;
import com.sytoss.edu.library.repositories.BooksAuditGenresRepository;
import com.sytoss.edu.library.services.BookAuditService;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@CucumberContextConfiguration
@Getter
public class IntegrationTest extends AbstractControllerTest {

    @Autowired
    private BookAuditService bookAuditService;

    @Autowired
    private BookAuditRepository bookAuditRepository;

    @Autowired
    private BooksAuditGenresRepository booksAuditGenresRepository;

}
