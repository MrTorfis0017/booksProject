package com.sytoss.edu.library.cucumber;

import com.sytoss.edu.library.IntegrationTest;
import com.sytoss.edu.library.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class GivenTest extends IntegrationTest {

    @Given("book record exists in audit DB with parameters")
    public void bookRecordExistsInAuditDBWithParameters(List<DefinitionDataTableType.BookAuditDTODataTable> bookAuditDTODataTable) {
        for (DefinitionDataTableType.BookAuditDTODataTable bookAuditDTO : bookAuditDTODataTable) {
            getBookAuditRepository().deleteByBookId(bookAuditDTO.getBookAuditDTO().getBookId());
            Long id = getBookAuditService().saveChanges(bookAuditDTO.getBookAuditDTO());
            TestContext.getInstance().getBookAuditIds().put(bookAuditDTO.getId(), id);
            log.debug("bookRecordExistsInAuditDBWithParameters: saved BookAudit with id {} ", id);
        }
    }

    @Given("There are no books with the following IDs in the database")
    public void thereAreNoBooksWithTheFollowingIDsInTheDatabase(DataTable table) {
        List<Map<String, String>> entry = table.asMaps(String.class, String.class);
        for (Map<String, String> form : entry) {
            long bookId = Long.parseLong(form.get("bookId"));
            getBookAuditRepository().deleteByBookId(bookId);
        }
    }
}
