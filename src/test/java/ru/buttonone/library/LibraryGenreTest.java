package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.domain.Genre;

import static org.hamcrest.core.Is.is;
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryGenreTest {
    public static final String API_BOOKS_1 = "/api/books/1";
    public static final String GENRE = "genre";
    public static final String G_1 = "g1";

    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingGenre() throws JsonProcessingException {

        Genre expectedGenre = new Genre("1", "g1");

        String jsonExpectedGenre = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedGenre);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedGenre)
                .when()
                .get(API_BOOKS_1)
                .then()
                .body(GENRE, is(G_1))
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);

    }
}
