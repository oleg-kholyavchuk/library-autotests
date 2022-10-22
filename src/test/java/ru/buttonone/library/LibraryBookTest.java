package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import ru.buttonone.dao.BookDao;
import ru.buttonone.domain.Book;


import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryBookTest {

    public static final String API_BOOKS_ADD = "/api/books/add";
    public static final String TEST_ID1 = "1";
    public static final String TEST_T1 = "test_t1";
    public static final String TEST_G1 = "test_g1";
    public static final String TEST_A1 = "test_a1";
    public static final String CONNECTION = "Connection";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String NICKNAME = "nickname";
    public static final String NICK_1 = "nick1";

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
                .baseUri(HTTP_LOCALHOST_8081)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .contentType(ContentType.JSON)
                .body("title", is("t100"))
                .statusCode(STATUS_CODE);

        deleteTestBook();
    }

    @DisplayName("Меняем данные книгу и проверяем поменялось ли содержимое")
    @Test
    public void shouldHaveCorrectEntityAddingBookId() throws JsonProcessingException {

//        insertBook();

//        ValidatableResponse validatableResponse = given()
//                .when()
//                .get("/api/books")
//                .then()
//                .statusCode(403);
//
//        List<Book> bookList = validatableResponse
//                .extract()
//                .body()
//                .jsonPath().getList("", Book.class);
//
//        System.out.println("bookList = " + bookList);

        Book expectedBook = new Book("100", "t100", "a100", "100");
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        long id = bookDao.getId(INTID);



        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8081)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .body(jsonExpectedBook)
                .when()
                .put(API_BOOKS + id)
                .then()
                .contentType(ContentType.JSON)
                .body("title", is("t100"))
                .log().all()
                .statusCode(STATUS_CODE);

        deleteTestBook();
    }

    @DisplayName("удалить книгу по id и проверяем удалилась ли она")
    @Test
    public void shouldHaveCorrectDeleteBookById() throws JsonProcessingException {
        insertBook();

        String deleteBookId = bookDao.getBookIdByBookTitle(TEST_T1);


        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8081)
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
                .baseUri(HTTP_LOCALHOST_8081)
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

        long id = bookDao.getId(INTID);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8081)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .when()
                .get(API_BOOKS + id)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);
    }
}
