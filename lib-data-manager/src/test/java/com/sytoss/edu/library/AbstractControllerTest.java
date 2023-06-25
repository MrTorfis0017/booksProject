package com.sytoss.edu.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class AbstractControllerTest extends AbstractApplicationTest {

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
}