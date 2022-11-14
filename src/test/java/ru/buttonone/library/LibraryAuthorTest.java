package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.dao.BookDao;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static ru.buttonone.constant.TestContstants.*;
import static ru.buttonone.library.Endpoints.*;
import static ru.buttonone.library.HttpCodes.STATUS_CODE;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryAuthorTest {
    public static final String AUTHORS = "authors";

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

    @DisplayName("Проверяем содержится ли автор")
    @Test
    public void shouldHaveCorrectGetnAuthor() {
        String id = bookDao.getBookIdByBookTitle(TEST_T1);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .when()
                .get(API_BOOKS + id)
                .then()
                .contentType(ContentType.JSON)
                .body(AUTHORS, is(TEST_A1))
                .log().all()
                .statusCode(STATUS_CODE);
    }
}
