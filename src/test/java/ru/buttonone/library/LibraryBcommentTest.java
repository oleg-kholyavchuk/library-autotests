package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.hamcrest.Matchers.contains;
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBcommentTest {
    public static final String API_BOOKS_1_COMMENTS = "/api/books/1/comments";
    public static final String NICKNAME = "nickname";
    public static final String NICK_1 = "nick1";

    @DisplayName("Проверяем содержится никнейм")
    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingBComment() throws JsonProcessingException, ClassNotFoundException {

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .when()
                .get(API_BOOKS_1_COMMENTS)
                .then()
                .contentType(ContentType.JSON)
                .body(NICKNAME,  contains(NICK_1))
                .log().all()
                .statusCode(STATUS_CODE);
    }
}
