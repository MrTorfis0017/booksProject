package com.sytoss.edu.library.params;

import com.sytoss.edu.library.bom.Author;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UpdateBookParams {

    private Long id;

    private String name;

    private String language;

    private Integer yearOfPublishing;

    private List<String> genres;

    private Author author;
}
