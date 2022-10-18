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
import ru.buttonone.domain.Bcomment;
import ru.buttonone.domain.Book;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBookTest {

    public static final String API_BOOKS_ADD = "/api/books/add";
    public static final String TEST_ID1 = "1";
    public static final String TEST_T1 = "test_t1";
    public static final String TEST_G1 = "test_g1";
    public static final String TEST_A1 = "test_a1";

    public static final int INT = 1;
    public static final String COMMENTS = "/comments";
    public static final String CONNECTION = "Connection";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String NICKNAME = "nickname";
    public static final String NICK_1 = "nick1";

    @Autowired
    private BookDao bookDao;

    @BeforeTestMethod
    public void insertBookById() throws JsonProcessingException {
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

        String deleteBookId = bookDao.getBookIdByBookTitle("t100");

        given()
                .when()
                .delete(API_BOOKS + deleteBookId)
                .then()
                .statusCode(STATUS_CODE);
    }

    @DisplayName("Добовляем книгу и проверяем добавилась ли она")
    @Test
    public void shouldHaveCorrectEntityAddingBook() throws JsonProcessingException {

        Book expectedBook = new Book("100", "t100", "a100", "100");
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
                .statusCode(STATUS_CODE);

        deleteTestBook();
    }

    @DisplayName("Меняем данные книгу и проверяем поменялось ли содержимое")
    @Test
    public void shouldHaveCorrectEntityAddingBookId() throws JsonProcessingException {

        Book expectedBook = new Book("100", "t100", "a100", "100");
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        long id = bookDao.getId(INT);


        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBook)
                .when()
                .put(API_BOOKS + id)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);

        deleteTestBook();
    }

    @DisplayName("удалить книгу по id и проверяем удалилась ли она")
    @Test
    public void shouldHaveCorrectDeleteBookById() throws JsonProcessingException {
        insertBookById();

        String deleteBookId = bookDao.getBookIdByBookTitle(TEST_T1);


        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .when()
                .delete(API_BOOKS + deleteBookId)
                .then()
                .contentType(ContentType.TEXT)
                .log().all()
                .statusCode(STATUS_CODE);
    }

    @DisplayName("получить все книги из БД")
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

        long id = bookDao.getId(INT);

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
    }

    @DisplayName("Проверяем на содержания никнейма")
    @Test
    public void shouldHaveCorrectNicknameById() throws JsonProcessingException {

        long id = bookDao.getId(1);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .when()
                .get(API_BOOKS + id + COMMENTS)
                .then()
                .contentType(ContentType.JSON)
                .body(NICKNAME, contains(NICK_1))
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
