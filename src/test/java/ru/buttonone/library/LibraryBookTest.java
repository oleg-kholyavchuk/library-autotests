package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
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
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBookTest {
    public static final String CONNECTION = "Connection";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String NICKNAME = "nickname";
    public static final String TITLE = "title";

    @Autowired
    private BookDao bookDao;

    @BeforeTestMethod
    public void insertBook() throws JsonProcessingException {
        Book expectedBook = new Book(TEST_ID1, TEST_T1, TEST_A1, TEST_G1);
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        given()
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_CODE);
    }

    @AfterTestMethod
    public void deleteTestBook() {

        String deleteBookId = bookDao.getBookIdByBookTitle(TEST_T1);

        given()
                .when()
                .delete(API_BOOKS + deleteBookId)
                .then()
                .statusCode(STATUS_CODE);
    }

    @DisplayName("Добовляем книгу")
    @Test
    public void shouldHaveCorrectEntityAddingBook() throws JsonProcessingException {
        Book expectedBook = new Book(TEST_ID1, TEST_T1, TEST_A1, TEST_G1);
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .contentType(ContentType.JSON)
                .body(TITLE, is(TEST_T1))
                .statusCode(STATUS_CODE);
        deleteTestBook();
    }

    @DisplayName("Меняем данные книгу и проверяем поменялось ли содержимое")
    @Test
    public void shouldHaveCorrectEntityAddingBookId() throws JsonProcessingException {
        Book expectedBook = new Book(TEST_ID1, TEST_T1, TEST_A1, TEST_G1);
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        String id = expectedBook.getId();

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBook)
                .when()
                .put(API_BOOKS + id)
                .then()
                .contentType(ContentType.JSON)
                .body(TITLE, is(TEST_T1))
                .log().all()
                .statusCode(STATUS_CODE);
        deleteTestBook();
    }

    @DisplayName("Удалить книгу по id")
    @Test
    public void shouldHaveCorrectDeleteBookById() throws JsonProcessingException {
        insertBook();

        String deleteBookId = bookDao.getBookIdByBookTitle(TEST_T1);


        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
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
    public void shouldHaveCorrectBookById() throws JsonProcessingException {

        Book expectedBook = new Book("", TEST_T1, TEST_A1, TEST_G1);
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        String id = expectedBook.getId();

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_CODE);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .when()
                .get(API_BOOKS + id)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);
        deleteTestBook();
    }
}
