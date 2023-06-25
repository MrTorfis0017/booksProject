package com.sytoss.edu.library.converters;

import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.model.BookElasticSearchMessage;

public class BookElasticSearchConverter {

    public static BookElasticSearchMessage elasticSearchMessageConverter(DMEventMessage dmEventMessage) {
        BookElasticSearchMessage bookElasticSearchMessage = new BookElasticSearchMessage();
        bookElasticSearchMessage.setBookId(dmEventMessage.getBook().getId());
        bookElasticSearchMessage.setBookName(dmEventMessage.getBook().getName());
        bookElasticSearchMessage.setLanguage(dmEventMessage.getBook().getLanguage());
        bookElasticSearchMessage.setYearOfPublishing(dmEventMessage.getBook().getYearOfPublishing());
        bookElasticSearchMessage.setGenres(dmEventMessage.getBook().getGenres());

        bookElasticSearchMessage.setAuthorId(dmEventMessage.getBook().getAuthor().getId());
        bookElasticSearchMessage.setAuthorFirstName(dmEventMessage.getBook().getAuthor().getFirstName());
        bookElasticSearchMessage.setAuthorLastName(dmEventMessage.getBook().getAuthor().getLastName());

        return bookElasticSearchMessage;
    }
}
