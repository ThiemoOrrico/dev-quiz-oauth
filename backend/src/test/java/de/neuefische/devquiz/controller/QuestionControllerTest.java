package de.neuefische.devquiz.controller;

import de.neuefische.devquiz.model.Question;
import de.neuefische.devquiz.repo.QuestionRepo;
import de.neuefische.devquiz.security.model.AppUser;
import de.neuefische.devquiz.security.repo.AppUserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void clearDb() {
        questionRepo.deleteAll();
    }

    @Test
    @DisplayName("Should return a list with all questions from db")
    void testListQuestion() {
        // GIVEN

        questionRepo.save(new Question("1", "Question with ID '1'", List.of()));
        questionRepo.save(new Question("2", "Question with ID '2'", List.of()));
        questionRepo.save(new Question("3", "Question with ID '3'", List.of()));
        // WHEN
        ResponseEntity<Question[]> responseEntity = testRestTemplate.exchange("/api/question", HttpMethod.GET, new HttpEntity<>(getJWT()), Question[].class);
        // THEN
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), arrayContainingInAnyOrder(
                new Question("1", "Question with ID '1'", List.of()),
                new Question("2", "Question with ID '2'", List.of()),
                new Question("3", "Question with ID '3'", List.of())
        ));

    }

    @Test
    @DisplayName("Should return a question object with the given id")
    void testGet() {
        // GIVEN
        Question question = new Question("302", "Question with ID '302'", List.of());

        questionRepo.save(question);
        // WHEN
        ResponseEntity<Question> responseEntity = testRestTemplate.exchange("/api/question/" + question.getId(), HttpMethod.GET, new HttpEntity<>(getJWT()), Question.class);
        // THEN
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(new Question("302", "Question with ID '302'", List.of())));

    }

    @Test
    @DisplayName("Should add a new question item to the db")
    void testAddQuestion() {
        // GIVEN
        Question questionToAdd = new Question("22", "This is a question", List.of());


        // WHEN
        ResponseEntity<Question> postResponseEntity = testRestTemplate.exchange("/api/question/", HttpMethod.POST, new HttpEntity<>(questionToAdd, getJWT()), Question.class);
        Question actual = postResponseEntity.getBody();

        // THEN
        assertThat(postResponseEntity.getStatusCode(), is(HttpStatus.OK));
        assertNotNull(actual);
        assertThat(actual, is(new Question("22", "This is a question", List.of())));

        // THEN - check via GET
        ResponseEntity<Question> getResponse = testRestTemplate.exchange("/api/question/" + questionToAdd.getId(),HttpMethod.GET,new HttpEntity<>(getJWT()), Question.class);
        Question persistedQuestion = getResponse.getBody();

        assertNotNull(persistedQuestion);
        assertThat(persistedQuestion.getId(), is(questionToAdd.getId()));
        assertThat(persistedQuestion.getQuestionText(), is(questionToAdd.getQuestionText()));

    }

    private HttpHeaders getJWT(){
        appUserRepo.save(AppUser.builder()
                        .username("test-username")
                        .password(passwordEncoder.encode("some-password"))
                .build());
        AppUser loginData = new AppUser("test-username", "some-password");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/auth/login", loginData, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody());
        return headers;

    }
}