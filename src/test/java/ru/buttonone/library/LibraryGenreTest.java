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
import ru.buttonone.dao.BookDao;
import ru.buttonone.domain.Book;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static ru.buttonone.library.Constant.*;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryGenreTest {
    public static final String GENRE = "genre";

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

    @DisplayName("Проверяем на содержания никнейма")
    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingGenre() throws JsonProcessingException {
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
                .statusCode(STATUS_CODE);

        String id = bookDao.getBookIdByBookTitle(TEST_T1);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(new Header(CONTENT_TYPE, APPLICATION_JSON))
                .when()
                .get(API_BOOKS + id)
                .then()
                .body(GENRE, is(TEST_G1))
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);
        deleteTestBook();
    }
}
