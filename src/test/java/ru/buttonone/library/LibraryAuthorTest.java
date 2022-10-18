package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.dao.AuthorDao;


import static org.hamcrest.core.Is.is;
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryAuthorTest {

    public static final String API_BOOKS_1 = "/api/books/1";
    public static final String AUTHORS = "authors";
    public static final String A_1 = "a1";

    @Autowired
    private AuthorDao authorDao;

    @DisplayName("Проверяем содержится автор")
    @Test
    public void shouldHaveCorrectEntityInAuthor() throws JsonProcessingException {

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .when()
                .get(API_BOOKS_1)
                .then()
                .body(AUTHORS, is(A_1))
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);
    }
}
