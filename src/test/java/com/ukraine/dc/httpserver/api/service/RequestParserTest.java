package com.ukraine.dc.httpserver.api.service;

import com.ukraine.dc.httpserver.api.model.HttpMethod;
import com.ukraine.dc.httpserver.api.model.Request;
import com.ukraine.dc.httpserver.api.service.parser.ItemParser;
import com.ukraine.dc.httpserver.api.service.parser.RequestParser;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestParserTest {
    private static final String GET_REQUEST = "GET /src/test/java/webapp/index.html HTTP/1.1\\s";
    private static final String GET_REQUEST_WITH_HEADERS = "GET /src/test/java/webapp/users HTTP/1.1\\s\n" +
            "            user: Windows NT\n" +
            "            Host: www.drc.com";
    private static final String NOT_GET_REQUEST = "POST /src/test/java/webapp/index.html HTTP/1.1\\s";;
    private final ItemParser itemParser = new RequestParser();

    @Test
    void shouldReturnMethodTypeWhenCallGetRequest() {
        BufferedReader reader = new BufferedReader(new StringReader(GET_REQUEST));
        Request request = itemParser.parse(reader);
        assertEquals(HttpMethod.GET, request.getHttpMethod());
        assertEquals("/src/test/java/webapp/index.html", request.getRequestUri());
        assertEquals(0, request.getHeaders().size());
    }

    @Test
    void shouldReturnUnsupportedOperationException() {
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
                    BufferedReader reader = new BufferedReader(new StringReader(NOT_GET_REQUEST));
                    itemParser.parse(reader);
                });
        assertEquals("The API doesn't support such type of request", exception.getMessage());
    }

    @Test
    void shouldReturnRequestWithHeaders() {
        BufferedReader reader = new BufferedReader(new StringReader(GET_REQUEST_WITH_HEADERS));
        Request request = itemParser.parse(reader);
        Map<String, String> headers =  request.getHeaders();

        assertEquals(HttpMethod.GET, request.getHttpMethod());
        assertFalse(request.getHeaders().isEmpty());
        assertEquals("/src/test/java/webapp/users", request.getRequestUri());
        assertEquals("www.drc.com", headers.get("Host"));
        assertEquals("Windows NT", headers.get("user"));
    }

}