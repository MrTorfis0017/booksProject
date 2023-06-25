package com.sytoss.edu.library.converters;

import com.sytoss.edu.library.model.BookAuditMessage;
import com.sytoss.edu.library.model.DMEventMessage;

import java.util.HashMap;
import java.util.Map;

public final class AuditConverter {

    private AuditConverter() {
    }

    public static BookAuditMessage auditConvertor(DMEventMessage dmEventMessage) {
        BookAuditMessage bookAuditMessage = new BookAuditMessage();
        bookAuditMessage.setBookId(dmEventMessage.getBook().getId());
        bookAuditMessage.setName(dmEventMessage.getBook().getName());
        bookAuditMessage.setLanguage(dmEventMessage.getBook().getLanguage());
        bookAuditMessage.setYearOfPublishing(dmEventMessage.getBook().getYearOfPublishing());

        bookAuditMessage.setAuthorId(dmEventMessage.getBook().getAuthor().getId());
        bookAuditMessage.setAuthorFirstName(dmEventMessage.getBook().getAuthor().getFirstName());
        bookAuditMessage.setAuthorSecondName(dmEventMessage.getBook().getAuthor().getLastName());
        bookAuditMessage.setAuthorNationality(dmEventMessage.getBook().getAuthor().getNationality());

        bookAuditMessage.setChangeDate(dmEventMessage.getChangeDate());
        bookAuditMessage.setChangedBy(dmEventMessage.getUserName());

        Map<Long, String> genres = new HashMap<>();
        for (int i = 0; i < dmEventMessage.getGenresIds().size(); i++) {
            genres.put(dmEventMessage.getGenresIds().get(i), dmEventMessage.getBook().getGenres().get(i));
        }
        bookAuditMessage.setGenres(genres);

        return bookAuditMessage;
    }
}
