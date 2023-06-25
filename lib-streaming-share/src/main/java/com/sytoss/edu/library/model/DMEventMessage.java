package com.sytoss.edu.library.model;

import com.sytoss.edu.library.bom.Book;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DMEventMessage {

    private EventType eventType;

    private Book book;

    private List<Long> genresIds;

    private String userName;

    private Timestamp changeDate;
}
