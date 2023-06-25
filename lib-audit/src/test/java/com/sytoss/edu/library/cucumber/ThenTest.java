package com.sytoss.edu.library.cucumber;

import com.sytoss.edu.library.IntegrationTest;
import com.sytoss.edu.library.TestContext;
import com.sytoss.edu.library.dto.BookAuditDTO;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
public class ThenTest extends IntegrationTest {

    @Then("book audit record should be created in DB with parameters")
    @Transactional
    public void bookAuditRecordShouldBeCreatedInDBWithParameters(List<DefinitionDataTableType.BookAuditDTODataTable> bookAuditDTODataTable) {
        for (DefinitionDataTableType.BookAuditDTODataTable bookAudit : bookAuditDTODataTable) {
            long bookAuditId = TestContext.getInstance().getBookAuditIds().get(bookAudit.getId());
            BookAuditDTO bookAuditDTOFromDB = getBookAuditRepository().getReferenceById(bookAuditId);
            BookAuditDTO bookAuditDTOExcepted = bookAudit.getBookAuditDTO();

            Assertions.assertEquals(bookAuditDTOExcepted.getBookId(), bookAuditDTOFromDB.getBookId());
            Assertions.assertEquals(bookAuditDTOExcepted.getName(), bookAuditDTOFromDB.getName());
            Assertions.assertEquals(bookAuditDTOExcepted.getLanguage(), bookAuditDTOFromDB.getLanguage());
            Assertions.assertEquals(bookAuditDTOExcepted.getYearOfPublishing(), bookAuditDTOFromDB.getYearOfPublishing());
            Assertions.assertEquals(bookAuditDTOExcepted.getAuthorId(), bookAuditDTOFromDB.getAuthorId());
            Assertions.assertEquals(bookAuditDTOExcepted.getVersion(), bookAuditDTOFromDB.getVersion());
            Assertions.assertEquals(bookAuditDTOExcepted.getChangeDate(), bookAuditDTOFromDB.getChangeDate());
            Assertions.assertEquals(bookAuditDTOExcepted.getChangedBy(), bookAuditDTOFromDB.getChangedBy());
            Assertions.assertEquals(bookAuditDTOExcepted.getAuthorId(), bookAuditDTOFromDB.getAuthorId());
            Assertions.assertEquals(bookAuditDTOExcepted.getAuthorFirstName(), bookAuditDTOFromDB.getAuthorFirstName());
            Assertions.assertEquals(bookAuditDTOExcepted.getAuthorSecondName(), bookAuditDTOFromDB.getAuthorSecondName());
            Assertions.assertEquals(bookAuditDTOExcepted.getAuthorNationality(), bookAuditDTOFromDB.getAuthorNationality());
            Assertions.assertEquals(bookAuditDTOExcepted.getBooksAuditGenresDTOs().size(), bookAuditDTOFromDB.getBooksAuditGenresDTOs().size());
            for (int i = 0; i < bookAuditDTOExcepted.getBooksAuditGenresDTOs().size(); i++) {
                Assertions.assertEquals(bookAuditDTOExcepted.getBooksAuditGenresDTOs().get(i).getGenreName(), bookAuditDTOFromDB.getBooksAuditGenresDTOs().get(i).getGenreName());
                Assertions.assertEquals(bookAuditDTOExcepted.getBooksAuditGenresDTOs().get(i).getGenreId(), bookAuditDTOFromDB.getBooksAuditGenresDTOs().get(i).getGenreId());
            }
        }
    }
}
