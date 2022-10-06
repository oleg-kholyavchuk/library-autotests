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
import ru.buttonone.domain.Author;
import ru.buttonone.repository.AuthorRepository;

import static org.hamcrest.core.Is.is;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryAuthorTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingAuthor() throws JsonProcessingException {

        Author expectedAuthor = new Author("1", "a1");

        String jsonExpectedAuthor = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedAuthor);

        RestAssured
                .given()
                .baseUri("http://localhost:8080")
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedAuthor)
                .when()
                .get("/api/books/1")
                .then()
                .body("authors", is("a1"))
                .contentType(ContentType.JSON)
                .statusCode(200);

        System.out.println(authorRepository.getAuthorsByFio("a1"));

        Author firstAuthor = authorRepository.getAuthorsByFio("a1").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("a1", firstAuthor.getFio()));
    }
}
