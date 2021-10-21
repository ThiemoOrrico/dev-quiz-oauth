package de.neuefische.devquiz.controller;

import de.neuefische.devquiz.model.Answer;
import de.neuefische.devquiz.model.AnswerValidation;
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
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayControllerTest {

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
    @DisplayName("Should return a random question from db (here only one question in db)")
    void testGetQuestion() {

        //GIVEN
        Question expected = Question.builder()
                .id("1")
                .questionText("Question with ID '1'")
                .answers(List.of(
                        Answer.builder()
                                .id("ABC")
                                .answerText("Antworttext")
                                .correct(true)
                                .build(),
                        Answer.builder()
                                .id("XYZ")
                                .answerText("Meine Antwort")
                                .correct(false)
                                .build()))
                .build();

        questionRepo.save(expected);

        //WHEN
        ResponseEntity<Question> response = testRestTemplate.exchange("/api/question/quiz", HttpMethod.GET, new HttpEntity<>(getJWT()), Question.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expected));
    }



    @Test
    @DisplayName("Should return true for a correctly chosen answer")
    void testCheckAnswerTrue() {

        //GIVEN
        Answer answer1 = new Answer();
        answer1.setAnswerText("True answer");
        answer1.setCorrect(true);
        answer1.setId(UUID.randomUUID().toString());

        Answer answer2 = new Answer();
        answer2.setAnswerText("False answer");
        answer2.setCorrect(false);
        answer2.setId(UUID.randomUUID().toString());

        Question question = new Question("1", "Frage?", List.of(answer1, answer2));
        String chosenId = answer1.getId();

        questionRepo.save(question);
        AnswerValidation answerValidation = new AnswerValidation(question, chosenId);

        //WHEN
        ResponseEntity<Boolean> response = testRestTemplate.exchange("/api/question/quiz", HttpMethod.POST,new HttpEntity<>(answerValidation, getJWT()), Boolean.class );


        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(Boolean.TRUE));


    }

    @Test
    @DisplayName("Should return false for a incorrectly chosen answer")
    void testCheckAnswerFalse() {

        //GIVEN
        Answer answer1 = new Answer();
        answer1.setAnswerText("True answer");
        answer1.setCorrect(true);
        answer1.setId(UUID.randomUUID().toString());

        Answer answer2 = new Answer();
        answer2.setAnswerText("False answer");
        answer2.setCorrect(false);
        answer2.setId(UUID.randomUUID().toString());

        Question question = new Question("1", "Frage?", List.of(answer1, answer2));
        String chosenId = answer2.getId();

        questionRepo.save(question);
        AnswerValidation answerValidation = new AnswerValidation(question, chosenId);

        //WHEN
        ResponseEntity<Boolean> response = testRestTemplate.exchange("/api/question/quiz", HttpMethod.POST,new HttpEntity<>(answerValidation, getJWT()), Boolean.class );
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(Boolean.FALSE));
    }

    private HttpHeaders getJWT() {
        appUserRepo.save(AppUser.builder()
                .username("test-username")
                .password(passwordEncoder.encode("some-password"))
                .build());
        AppUser loginData = new AppUser("test-username","some-password");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/auth/login", loginData, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody());
        return headers;
    }
}