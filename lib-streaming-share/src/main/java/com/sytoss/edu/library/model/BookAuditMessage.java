package com.sytoss.edu.library.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.Map;

@Getter
@Setter
@ToString
public class BookAuditMessage {

    private long bookId;

    private String name;

    private String language;

    private int yearOfPublishing;

    private long authorId;

    private String authorFirstName;

    private String authorSecondName;

    private String authorNationality;

    private Timestamp changeDate;

    private String changedBy;

    private Map<Long, String> genres;

}
