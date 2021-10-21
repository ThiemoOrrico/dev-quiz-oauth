package de.neuefische.devquiz.controller;

import de.neuefische.devquiz.security.model.AppUser;
import de.neuefische.devquiz.security.repo.AppUserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Test
    void testGetLoggedInUserDataWithValidToken() {
        //GIVEN
        HttpHeaders token = getJWT();

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange("/user/me", HttpMethod.GET, new HttpEntity<>(getJWT()), String.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }



    private HttpHeaders getJWT() {
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