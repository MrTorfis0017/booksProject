package com.sytoss.edu.library.cucumber;

import com.sytoss.edu.library.IntegrationTest;
import com.sytoss.edu.library.TestContext;
import com.sytoss.edu.library.bom.Book;
import com.sytoss.edu.library.dto.BookDTO;
import com.sytoss.edu.library.params.UpdateBookParams;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
public class WhenTest extends IntegrationTest {

    @When("user register book")
    public void userSendRegisterRequestWithRequestBody(BookDTO bookDTO) {
        Book book = new Book();
        getBookConverter().fromDTO(bookDTO, book);

        String url = "/book/register";

        ResponseEntity<Book> response = doPost(url, book, Book.class, List.of("ADMIN"));
        TestContext.getInstance().setRegisterResponse(response);
        if (response.getBody() != null) {
            TestContext.getInstance().getBookIds().put(String.valueOf(bookDTO.getId()), response.getBody().getId());
        }
    }

    @When("user update book")
    public void userUpdateBook(UpdateBookParams updateBookParams) {
        String url = "/book/update";

        ResponseEntity<String> response = doPatch(url, updateBookParams, null, List.of("ADMIN"));
        TestContext.getInstance().setResponse(response);
    }

    @When("user getting book from the storage by ids:")
    public void userGettingBookFromTheStorageByIds(DataTable dataTable) {
        List<List<String>> rows = dataTable.rows(1).asLists(String.class);
        long id = Long.parseLong(rows.get(0).get(0).replaceAll("\\*", ""));
        String url = "/book/" + id;
        doGet(url, null, Book.class, List.of("USER"));
    }
}
