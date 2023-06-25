package com.sytoss.edu.library;

import com.sytoss.edu.library.bom.Book;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

@Getter
@Setter
public class TestContext {

    private static final ThreadLocal<TestContext> testContext = new ThreadLocal<>();

    private ResponseEntity<String> response;

    private ResponseEntity<Book> registerResponse;

    public static TestContext getInstance() {
        if (testContext.get() == null) {
            testContext.set(new TestContext());
        }
        return testContext.get();
    }

    private HashMap<String, Long> bookIds = new HashMap<>();

    public static void dropInstance() {
        testContext.set(null);
    }
}