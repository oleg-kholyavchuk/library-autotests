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
import org.springframework.test.context.event.annotation.AfterTestMethod;
import ru.buttonone.dao.BookDao;
import ru.buttonone.domain.Bcomment;
import ru.buttonone.domain.Book;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBcommentTest {
    public static final String NICKNAME = "nickname";
    public static final String NICK_1 = "nick1";
    public static final String COMMENTS = "/comments";

    @Autowired
    private BookDao bookDao;

    @AfterTestMethod
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
    public void shouldHaveCorrectEntityInBComment() throws JsonProcessingException, ClassNotFoundException {
        Book expectedBook = new Book(TEST_ID1, TEST_T1, TEST_A1, TEST_G1);
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBook)
                .log().all()
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_CODE);

        String id = bookDao.getBookIdByBookTitle(TEST_T1);

        Bcomment expectedBcomment = new Bcomment("1", id, "nick1", "m1");
        String jsonExpectedBcomment = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBcomment);

        int length = jsonExpectedBcomment.length();


        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header("Content-Length", "86"))
                .header(new Header("Host", "localhost:8080"))
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
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
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .when()
                .get(API_BOOKS + id + COMMENTS)
                .then()
                .contentType(ContentType.JSON)
                .body(NICKNAME,  contains(NICK_1))
                .log().all()
                .statusCode(STATUS_CODE);
        deleteTestBook();
    }

    @DisplayName("Проверяем добавился ли никнейма")
    @Test
    public void shouldHaveCorrectAddNicknameById() throws JsonProcessingException {
        Bcomment expectedBcomment = new Bcomment("1", "1", "string1", "string1");
        String jsonExpectedBcomment = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBcomment);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                //.header(new Header("Content-Length", "91"))
                .header(new Header("Host", "localhost:8080"))
                .body(jsonExpectedBcomment)
                .log().all()
                .when()
                .post(API_BOOKS + "1" + COMMENTS)
                .then()
                .log().all()
                .statusCode(STATUS_CODE);
    }
}
