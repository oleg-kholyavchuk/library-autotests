package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.dao.BookDao;
import ru.buttonone.domain.Bcomment;


import static org.hamcrest.Matchers.contains;
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBcommentTest {
    public static final String API_BOOKS_1_COMMENTS = "/api/books/1/comments";
    public static final String NICKNAME = "nickname";
    public static final String NICK_1 = "nick1";
    public static final String COMMENTS = "/comments";

    @Autowired
    private BookDao bookDao;

    @DisplayName("Проверяем содержится ли никнейм")
    @Test
    public void shouldHaveCorrectEntityInBComment() throws JsonProcessingException, ClassNotFoundException {

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

    @DisplayName("Проверяем добавился ли никнейма")
    @Test
    public void shouldHaveCorrectAddNicknameById() throws JsonProcessingException {
        Bcomment expectedBcomment = new Bcomment("1", "2", "nick1", "c1");
        String jsonExpectedBcomment = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBcomment);

        long id = bookDao.getId(INT);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBcomment)
                .when()
                .post(API_BOOKS + id + COMMENTS)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);
    }
}
