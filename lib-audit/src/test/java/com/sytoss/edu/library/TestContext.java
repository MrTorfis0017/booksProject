package com.sytoss.edu.library;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

@Getter
@Setter
public class TestContext {

    private static final ThreadLocal<TestContext> testContext = new ThreadLocal<>();

    private ResponseEntity<String> response;

    private HashMap<String, Long> bookAuditIds = new HashMap<>();

    public static TestContext getInstance() {
        if (testContext.get() == null) {
            testContext.set(new TestContext());
        }
        return testContext.get();
    }

    public static void dropInstance() {
        testContext.set(null);
    }
}