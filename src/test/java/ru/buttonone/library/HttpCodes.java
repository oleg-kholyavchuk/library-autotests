package ru.buttonone.library;

import io.restassured.http.Header;

import static org.apache.http.protocol.HTTP.CONTENT_TYPE;

public class HttpCodes {
    public static final int STATUS_CODE = 200;
    public static final String APPLICATION_JSON = "application/json";
    public static final Header HEADER_CONTENT_TYPE_JSON = new Header(CONTENT_TYPE, APPLICATION_JSON);
}
