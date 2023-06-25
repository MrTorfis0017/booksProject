package com.sytoss.edu.library.cucumber;

import com.sytoss.edu.library.IntegrationTest;
import com.sytoss.edu.library.TestContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WhenTest extends IntegrationTest {

    @When("consumer saving book changes with parameters")
    public void consumerSavingBookChangesWithParameters(List<DefinitionDataTableType.BookAuditDTODataTable> bookAuditDTODataTable) {
        for (DefinitionDataTableType.BookAuditDTODataTable bookAuditDTO : bookAuditDTODataTable) {
            Long id = getBookAuditService().saveChanges(bookAuditDTO.getBookAuditDTO());
            TestContext.getInstance().getBookAuditIds().put(bookAuditDTO.getId(), id);
            log.debug("consumerSavingBookChangesWithParameters: saved BookAudit with id {} ", id);
        }
    }
}
