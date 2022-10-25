package com.ukraine.dc.httpserver.api.service.writer;

import com.ukraine.dc.httpserver.api.exception.ResourceNotFoundException;
import com.ukraine.dc.httpserver.api.model.Response;
import com.ukraine.dc.httpserver.api.model.StatusCode;
import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SocketResponseWriterTest {
    private final ResponseWriter itemWriter = new SocketResponseWriter();

    private static final String OK_RESPONSE = "HTTP/1.1 200 OK\r\n" +
            "key: val\r\n" +
            "\r\n" +
            "ok content";
    private static final String NOT_FOUND_RESPONSE = "HTTP/1.1 404 Not Found\r\n" +
            "\r\n" +
            "Not Found";

    @Test
    void shouldWriteOkStatus_whenPassValidRequestObject() {
        ByteArrayOutputStream stringWriter = new ByteArrayOutputStream();
        BufferedOutputStream writer = new BufferedOutputStream(stringWriter);

        var headers = new HashMap<String, String>() {{
            put("key", "val");
        }};

        Response response = new Response("ok content".getBytes(), StatusCode.OK, headers);
        itemWriter.writeResponse(writer, response);
        assertEquals(OK_RESPONSE, stringWriter.toString());
    }

    @Test
    void shouldWriteNotFoundStatus_whenTryToRequestNotValidResource() {
        ByteArrayOutputStream stringWriter = new ByteArrayOutputStream();
        BufferedOutputStream writer = new BufferedOutputStream(stringWriter);

        var exception = new ResourceNotFoundException("nfe");
        itemWriter.writeErrorResponse(writer, exception);
        assertEquals(NOT_FOUND_RESPONSE, stringWriter.toString());
    }

}