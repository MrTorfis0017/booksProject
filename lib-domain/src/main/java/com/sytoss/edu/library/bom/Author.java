package com.sytoss.edu.library.bom;

import com.sytoss.edu.library.view.BookView;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;


@Getter
@Setter
public class Author {

    private long id;

    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    private String firstName;

    @JsonView(BookView.FullBookInfoWithAuthorNameAndSurname.class)
    private String lastName;

    private String nationality;

    private List<String> genres;

}
