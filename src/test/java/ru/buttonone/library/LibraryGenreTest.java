package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.domain.Genre;
import ru.buttonone.repository.GenreRepository;

import static org.hamcrest.core.Is.is;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryGenreTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingGenre() throws JsonProcessingException {

        Genre expectedGenre = new Genre("1", "g1");

        String jsonExpectedGenre = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedGenre);

        RestAssured
                .given()
                .baseUri("http://localhost:8080")
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedGenre)
                .when()
                .get("/api/books/1")
                .then()
                .body("genre", is("g1"))
                .contentType(ContentType.JSON)
                .statusCode(200);

        System.out.println(genreRepository.getGenresByName("g1"));

        Genre firstGenre = genreRepository.getGenresByName("g1").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("g1", firstGenre.getName()));

    }
}
