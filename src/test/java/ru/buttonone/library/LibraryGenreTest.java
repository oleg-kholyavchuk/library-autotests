package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.dao.BookDao;
import ru.buttonone.domain.Book;

import static org.hamcrest.Matchers.is;
import static ru.buttonone.library.Endpoints.*;
import static ru.buttonone.library.HttpCodes.HEADER_CONTENT_TYPE_JSON;
import static ru.buttonone.library.HttpCodes.STATUS_CODE;

@SuppressWarnings("All")
@SpringBootTest
public class LibraryGenreTest {
    public static final String GENRE = "genre";
    public static final String TEST_ID3 = "1";
    public static final String TEST_T3 = "test_t3";
    public static final String TEST_G3 = "test_g3";
    public static final String TEST_A3 = "test_a3";
    public static final Book EXPECTED_BOOK_GENRE = new Book(TEST_ID3, TEST_T3, TEST_A3, TEST_G3);

    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void insertBook() throws JsonProcessingException {
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(EXPECTED_BOOK_GENRE);

        RestAssured
                .given()
                .header(HEADER_CONTENT_TYPE_JSON)
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_CODE);
    }

    @AfterEach
    public void deleteTestBook() {
        String deleteBookId = bookDao.getBookIdByBookTitle(TEST_T3);

        RestAssured
                .given()
                .when()
                .delete(API_BOOKS + deleteBookId)
                .then()
                .statusCode(STATUS_CODE);
    }

    @DisplayName("Проверяем на содержания никнейма")
    @RepeatedTest(3)
    @Test
    public void shouldHaveCorrectGetGenre() {
        String id = bookDao.getBookIdByBookTitle(TEST_T3);

        RestAssured
                .given()
                .baseUri(HTTP_LOCALHOST_8080)
                .header(HEADER_CONTENT_TYPE_JSON)
                .when()
                .get(API_BOOKS + id)
                .then()
                .body(GENRE, is(TEST_G3))
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(STATUS_CODE);
    }
}
