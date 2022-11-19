package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import ru.buttonone.dao.BookDao;
import ru.buttonone.domain.Book;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static ru.buttonone.library.Endpoints.*;
import static ru.buttonone.library.HttpCodes.HEADER_CONTENT_TYPE_JSON;
import static ru.buttonone.library.HttpCodes.STATUS_CODE;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBookTest {
    public static final String CONNECTION = "Connection";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String TITLE = "title";
    public static final String TEST_ID2 = "1";
    public static final String TEST_T2 = "test_t2";
    public static final String TEST_G2 = "test_g2";
    public static final String TEST_A2 = "test_a2";
    public static final Book EXPECTED_BOOK = new Book(TEST_ID2, TEST_T2, TEST_A2, TEST_G2);

    @Autowired
    private BookDao bookDao;

    @BeforeTestMethod
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

    @AfterTestMethod
    public void deleteTestBook() {
        String deleteBookId = bookDao.getBookIdByBookTitle(TEST_T2);

        given()
                .when()
                .delete(API_BOOKS + deleteBookId)
                .then()
                .statusCode(STATUS_CODE);
    }

    @DisplayName("Добовляем книгу")
    @Test
    public void shouldHaveCorrectPostBook() throws JsonProcessingException {
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(EXPECTED_BOOK);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .contentType(ContentType.JSON)
                .body(TITLE, is(TEST_T2))
                .statusCode(STATUS_CODE);

        String testId = bookDao.getBookIdByBookTitle(TEST_T2);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .when()
                .get(API_BOOKS + testId)
                .then()
                .contentType(ContentType.JSON)
                .body(TITLE, is(TEST_T2))
                .log().all()
                .statusCode(STATUS_CODE);
        deleteTestBook();
    }

    @DisplayName("Меняем данные книгу и проверяем поменялось ли содержимое")
    @Test
    public void shouldHaveCorrectPutAddingBookId() throws JsonProcessingException {
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(EXPECTED_BOOK);

        String id = EXPECTED_BOOK.getId();

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .body(jsonExpectedBook)
                .when()
                .put(API_BOOKS + id)
                .then()
                .contentType(ContentType.JSON)
                .body(TITLE, is(TEST_T2))
                .log().all()
                .statusCode(STATUS_CODE);
        deleteTestBook();
    }


    @DisplayName("Удалить книгу по id")
    @Test
    public void shouldHaveCorrectDeleteBookById() throws JsonProcessingException {
        insertBook();

        String deleteBookId = bookDao.getBookIdByBookTitle(TEST_T2);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .when()
                .delete(API_BOOKS + deleteBookId)
                .then()
                .statusCode(STATUS_CODE);
    }

    @DisplayName("Получить все книги из БД")
    @Test
    public void shouldHaveCorrectGetAllBooks() {
        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .when()
                .get(API_BOOKS)
                .then()
                .log().all()
                .header(CONNECTION, KEEP_ALIVE)
                .statusCode(STATUS_CODE);
    }

    @DisplayName("Проверяемм содержится ли книга по id")
    @Test
    public void shouldHaveCorrectGetBookById() throws JsonProcessingException {
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(EXPECTED_BOOK);

        String id = EXPECTED_BOOK.getId();

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_CODE);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .when()
                .get(API_BOOKS + id)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);
        deleteTestBook();
    }
}
