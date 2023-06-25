package com.sytoss.edu.library.cucumber;

import com.sun.net.httpserver.HttpServer;
import com.sytoss.edu.library.IntegrationTest;
import com.sytoss.edu.library.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.ParameterType;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CucumberHooks extends IntegrationTest {

    private static HttpServer httpServer;

    private static HttpServer createHttpServer() {
        try {
            HttpServer result = HttpServer.create(new InetSocketAddress(9030), 0);

            String publicKey = "{\n" + "  \"keys\": [" + jwk.toPublicJWK().toJSONString() + "]\n" + "}";

            result.createContext("/realms/library-realm/protocol/openid-connect/certs", exchange -> {
                log.info("Request comming");
                byte[] response = publicKey.getBytes();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
                exchange.getResponseBody().write(response);
                exchange.close();
            });
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize Http Server", e);
        }
    }

    @BeforeAll
    public static void beforeAllHook() {
        httpServer = createHttpServer();
        httpServer.start();
        log.info("HTTP Server started");
    }

    @AfterAll
    public static void afterAllHook() {
        httpServer.stop(0);
        log.info("HTTP Server stopped");
    }

    @After
    public void tearDown() {
        TestContext.dropInstance();
        log.info("tearDown completed...");
    }

    @ParameterType(".*")
    public List<Integer> intList(String value) {
        return Arrays.stream(value.split(",")).map(s -> Integer.valueOf(s.trim())).collect(Collectors.toList());
    }

    @ParameterType(".*")
    public List<String> stringList(String value) {
        return Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
