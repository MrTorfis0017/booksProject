package com.sytoss.edu.library.bom;

import com.sytoss.edu.library.view.BookView;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;

@Getter
@Setter
public class Book {

    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    private long id;

    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    private String name;

    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    private String language;

    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    private int yearOfPublishing;

    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    private List<String> genres;

    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    private Author author;
}
