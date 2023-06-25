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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class})
public abstract class AbstractApplicationTest extends AbstractJunitTest {

    public static final int TOKEN_EXPIRATION_TIME = 100 * 60 * 1000;

    @Autowired
    private AbstractApplicationContext applicationContext;

    protected final static RSAKey jwk = createJWK();

    @Autowired
    private TestRestTemplate restTemplate;

    private static RSAKey createJWK() {
        try {
            return new RSAKeyGenerator(2048).keyID("1234").generate();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldLoadApplicationContext() {
        assertNotNull(applicationContext);
    }

    protected long getPort() {
        return ((AnnotationConfigServletWebServerApplicationContext) applicationContext).getWebServer().getPort();
    }

    protected URI getEndpoint(String uriPath) {
        try {
            return new URI("http://localhost:" + getPort() + uriPath);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected String generateJWT(List<String> roles) {
        try {
            LinkedTreeMap<String, ArrayList<String>> realmAccess = new LinkedTreeMap<>();
            realmAccess.put("roles", new ArrayList<>(roles));

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject("test").issuer("test@test").claim("realm_access", realmAccess).expirationTime(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME)).build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).type(new JOSEObjectType("jwt")).keyID("1234").build(), claimsSet);

            signedJWT.sign(new RSASSASigner(jwk));

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Could not generate token", e);
        }
    }

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, Object requestEntity, Class<T> responseType, List<String> roles) {
        return restTemplate.execute(getEndpoint(uri), method, request -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            if (roles != null && !roles.isEmpty()) {
                request.getHeaders().set("Authorization", "Bearer " + generateJWT(roles));
            }
            ObjectMapper objectMapper = new ObjectMapper();
            if (requestEntity != null) {
                request.getBody().write(objectMapper.writeValueAsBytes(requestEntity));
            }
        }, response -> {
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] bytes = response.getBody().readAllBytes();
            T body = responseType == null || bytes.length == 0 ? null : objectMapper.readValue(bytes, responseType);
            return new ResponseEntity<>(body, response.getStatusCode());
        });
    }

    public <T> ResponseEntity<T> doPost(String uri, Object requestEntity, Class<T> responseType, List<String> roles) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType, roles);
    }

    public <T> ResponseEntity<T> doGet(String uri, Object requestEntity, Class<T> responseType, List<String> roles) {
        return perform(uri, HttpMethod.GET, requestEntity, responseType, roles);
    }

    public <T> ResponseEntity<T> doPatch(String uri, Object requestEntity, Class<T> responseType, List<String> roles) {
        return perform(uri, HttpMethod.PATCH, requestEntity, responseType, roles);
    }
}
