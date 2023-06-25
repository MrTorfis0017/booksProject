package com.sytoss.edu.library.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Setter
@ToString
@Document(indexName = "books")
public class BookElasticSearchMessage {

    @Id
    @Field(type = FieldType.Keyword)
    private Long bookId;

    @Field(type = FieldType.Text)
    private String bookName;

    @Field(type = FieldType.Text)
    private String language;

    @Field(type = FieldType.Integer)
    private int yearOfPublishing;

    @Field(type = FieldType.Text)
    private List<String> genres;

    @Field(type = FieldType.Long)
    private Long authorId;

    @Field(type = FieldType.Text)
    private String authorFirstName;

    @Field(type = FieldType.Text)
    private String authorLastName;
}
