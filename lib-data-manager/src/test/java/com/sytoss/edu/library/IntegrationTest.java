package com.sytoss.edu.library;

import com.sytoss.edu.library.converters.AuthorConverter;
import com.sytoss.edu.library.converters.BookConverter;
import com.sytoss.edu.library.converters.GenreConverter;
import com.sytoss.edu.library.repositories.AuthorRepository;
import com.sytoss.edu.library.repositories.BookRepository;
import com.sytoss.edu.library.repositories.GenreRepository;
import com.sytoss.edu.library.services.BookService;
import com.sytoss.edu.library.services.GenreService;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;

@CucumberContextConfiguration
@Getter
@Slf4j
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:19092", "port=19092"}, topics = {"${spring.kafka.topics.input-library}"})
public class IntegrationTest extends AbstractApplicationTest {

    @Autowired
    @SpyBean
    private BookService bookService;

    @Autowired
    @SpyBean
    private BookRepository bookRepository;

    @Autowired
    @SpyBean
    private AuthorRepository authorRepository;

    @Autowired
    @SpyBean
    private GenreRepository genreRepository;

    @Autowired
    @SpyBean
    private GenreConverter genreConverter;

    @Autowired
    @SpyBean
    private AuthorConverter authorConverter;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    @SpyBean
    private GenreService genreService;
}
