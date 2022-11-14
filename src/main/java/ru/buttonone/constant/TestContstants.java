package ru.buttonone.constant;

import io.restassured.http.Header;
import ru.buttonone.domain.Book;

import static org.apache.http.protocol.HTTP.CONTENT_TYPE;

public class TestContstants {
    public static final String TEST_ID1 = "1";
    public static final String TEST_T1 = "test_t1";
    public static final String TEST_G1 = "test_g1";
    public static final String TEST_A1 = "test_a1";
    public static final Book EXPECTED_BOOK = new Book(TEST_ID1, TEST_T1, TEST_A1, TEST_G1);;
    public static final String APPLICATION_JSON = "application/json";
    public static final Header HEADER_CONTENT_TYPE_JSON = new Header(CONTENT_TYPE, APPLICATION_JSON);
}
