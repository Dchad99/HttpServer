package com.ukraine.dc.httpserver.api.service.parser;

import com.ukraine.dc.httpserver.api.exception.MethodNotAllowedException;
import com.ukraine.dc.httpserver.api.model.HttpMethod;
import com.ukraine.dc.httpserver.api.model.Request;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;



class SocketRequestParserTest {
    private static final String GET_REQUEST = "GET /src/test/java/webapp/index.html HTTP/1.1\\s";
    private static final String NOT_GET_REQUEST = "POST /src/test/java/webapp/index.html HTTP/1.1\\s";
    private static final String GET_REQUEST_WITH_HEADERS = "GET /src/test/java/webapp/users HTTP/1.1\\s\n" +
            "            user: Windows NT\n" +
            "            Host: www.drc.com";

    private final RequestParser itemParser = new SocketRequestParser();
    @Test
    void shouldReturnMethodTypeWhenCallGetRequest() {
        Request request = itemParser.parse(new ByteArrayInputStream(GET_REQUEST.getBytes()));
        assertEquals(HttpMethod.GET, request.getHttpMethod());
        assertEquals("/src/test/java/webapp/index.html", request.getRequestUri());
        assertEquals(0, request.getHeaders().size());
    }

    @Test
    void shouldReturnUnsupportedOperationException() {
        Exception exception = assertThrows(MethodNotAllowedException.class, () -> {
            itemParser.parse(new ByteArrayInputStream(NOT_GET_REQUEST.getBytes()));
        });
        assertEquals("The API doesn't support such type of request", exception.getMessage());
    }

    @Test
    void shouldReturnRequestWithHeaders() {
        Request request = itemParser.parse(new ByteArrayInputStream(GET_REQUEST_WITH_HEADERS.getBytes()));
        Map<String, String> headers =  request.getHeaders();

        assertEquals(HttpMethod.GET, request.getHttpMethod());
        assertFalse(request.getHeaders().isEmpty());
        assertEquals("/src/test/java/webapp/users", request.getRequestUri());
        assertEquals("www.drc.com", headers.get("Host"));
        assertEquals("Windows NT", headers.get("user"));
    }





//
//    @Test
//    void shouldReturnMethodTypeWhenCallGetRequest() {
//        var request = buildRequest();
//        when(parserMock.parse(inputStream)).thenReturn(request);
//        var b = new BufferedReader(new StringReader())
//        parserMock.parse(inputStream);
//
//        verify(parserMock).injectUriAndMethod(any(), any());
//        verify(parserMock).injectHeaders(anyList(), request);
//        verifyNoInteractions(parserMock);
//    }
//
//    @Test
//    void shouldThrowMethodNotAllowedException_whenRequestIsNotGet() {
//        var request = new Request();
//        Exception exception = Assertions.assertThrows(MethodNotAllowedException.class,
//                () -> parser.injectUriAndMethod(NOT_GET_REQUEST, request));
//        assertEquals(ERROR_MESSAGE, exception.getMessage());
//    }
//
//    @Test
//    void testInjectUriAndMethod() {
//        var request = new Request();
//        parser.injectUriAndMethod(GET_REQUEST, request);
//        assertNotNull(request.getRequestUri());
//        assertEquals("/src/test/java/webapp/index.html", request.getRequestUri());
//        assertEquals(HttpMethod.GET, request.getHttpMethod());
//    }
//
//    @Test
//    void testInjectHeaders() {
//        var request = new Request();
//        parser.injectHeaders(REQUEST_HEADERS, request);
//        assertNotNull(request.getHeaders());
//        assertEquals("Windows NT", request.getHeaders().get("user"));
//        assertEquals("www.drc.com", request.getHeaders().get("Host"));
//    }
//
//    private Request buildRequest() {
//        return Request.builder()
//                .requestUri("/src/test/java/webapp/users")
//                .headers(Map.of("user", "Windows KT", "host", "localhost"))
//                .httpMethod(HttpMethod.GET)
//                .build();
//    }
}