package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.domain.Bcomment;
import ru.buttonone.repository.BcommentRepository;

import static org.hamcrest.core.Is.is;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBcommentTest {

    @Autowired
    private BcommentRepository bcommentRepository;

    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingBComment() throws JsonProcessingException {

        Bcomment expectedBcomment = new Bcomment("1", "1", "nick1", "m1");

        String jsonExpectedBcomment = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBcomment);

        RestAssured
                .given()
                .baseUri("http://localhost:8080")
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBcomment)
                .when()
                .get("/api/books/1/comments")
                .then()
                .body("nickname", is("nick1"))
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(200);

        System.out.println(bcommentRepository.getBcommentsByNickname("nick1"));

        Bcomment firstBcomment = bcommentRepository.getBcommentsByNickname("nick1").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("nick1", firstBcomment.getNickname()));

    }
}
