package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.dao.BookDao;
import ru.buttonone.domain.Bcomment;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static ru.buttonone.constant.TestContstants.*;
import static ru.buttonone.library.Endpoints.*;
import static ru.buttonone.library.HttpCodes.STATUS_CODE;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBcommentTest {
    public static final String NICKNAME = "nickname";
    public static final String NICK_1 = "nick1";
    public static final Header HOST = new Header("Host", "localhost:8080");
    public static final Header HEADER_CONTENT_LENGTH = new Header("Content-Length", "86");
    public static final Header HEADER_HOST = new Header("Host", "localhost:8080");

    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void insertBook() throws JsonProcessingException {
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(EXPECTED_BOOK);

        given()
                .header(HEADER_CONTENT_TYPE_JSON)
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_CODE);
    }

    @AfterEach
    public void deleteTestBook() {
        String deleteBookId = bookDao.getBookIdByBookTitle(TEST_T1);

        given()
                .when()
                .delete(API_BOOKS + deleteBookId)
                .then()
                .statusCode(STATUS_CODE);
    }

    @DisplayName("Проверяем содержится ли никнейм")
    @Test
    public void shouldHaveCorrectEntityInBComment() throws JsonProcessingException {
        String id = bookDao.getBookIdByBookTitle(TEST_T1);

        Bcomment expectedBcomment = new Bcomment("1", id, "nick1", "m1");
        String jsonExpectedBcomment = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBcomment);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_LENGTH)
                .header(HOST)
                .header(HEADER_CONTENT_TYPE_JSON)
                .sessionId(id)
                .body(jsonExpectedBcomment)
                .log().all()
                .when()
                .post(API_BOOKS + id + COMMENTS)
                .then()
                .log().all()
                .statusCode(STATUS_CODE);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .when()
                .get(API_BOOKS + id + COMMENTS)
                .then()
                .contentType(ContentType.JSON)
                .body(NICKNAME,  contains(NICK_1))
                .log().all()
                .statusCode(STATUS_CODE);
    }

    @DisplayName("Проверяем добавился ли никнейма")
    @Test
    public void shouldHaveCorrectAddNicknameById() throws JsonProcessingException {
        String id = bookDao.getBookIdByBookTitle(TEST_T1);

        Bcomment expectedBcomment = new Bcomment("1", id, "string1", "string1");
        String jsonExpectedBcomment = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBcomment);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .header(HEADER_HOST)
                .header(HEADER_CONTENT_LENGTH)
                .body(jsonExpectedBcomment)
                .log().all()
                .when()
                .post(API_BOOKS + id + COMMENTS)
                .then()
                .log().all()
                .statusCode(STATUS_CODE);
    }
}
