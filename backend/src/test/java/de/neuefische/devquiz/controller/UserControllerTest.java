package de.neuefische.devquiz.controller;

import de.neuefische.devquiz.security.model.AppUser;
import de.neuefische.devquiz.security.repo.AppUserRepo;
import de.neuefische.devquiz.security.service.JWTUtilService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "neuefische.devquiz.jwt.secret=super-fancy-secret-from-jan-oke")
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JWTUtilService jwtUtilService = mock(JWTUtilService.class);

    @Test
    void testGetLoggedInUserDataWithValidToken() {
        //GIVEN
        HttpHeaders token = getHeaderWithJWT();

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange("/user/me", HttpMethod.GET, new HttpEntity<>(getHeaderWithJWT()), String.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void testGetLoggedInUserDataWithExpiredToken() {
        //GIVEN

        when(jwtUtilService.createToken(new HashMap<>(), "some-username")).thenReturn(
                Jwts.builder()
                        .setSubject("some-username")
                        .setIssuedAt(Date.from(Instant.now().minus(Duration.ofHours(5))))
                        .setExpiration(Date.from(Instant.now().minus(Duration.ofHours(4))))
                        .signWith(SignatureAlgorithm.HS256, "neuefische.devquiz.jwt.secret=super-fancy-secret-from-jan-oke")
                        .compact()
        );

        HttpHeaders token = getHeaderWithJWT();

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange("/user/me", HttpMethod.GET, new HttpEntity<>(getHeaderWithJWT()), String.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void testGetLoggedInUserDataWithInvalidToken() {
        //GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("ey.asden.sanfosih");

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange("/user/me", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }


    private HttpHeaders getHeaderWithJWT() {
        appUserRepo.save(AppUser.builder()
                .username("some-username")
                .password(passwordEncoder.encode("some-password"))
                .build());

        AppUser loginData = new AppUser("some-username", "some-password");
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", loginData, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody());
        return headers;

    }

}