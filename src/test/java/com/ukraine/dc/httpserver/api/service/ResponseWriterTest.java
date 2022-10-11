package com.ukraine.dc.httpserver.api.service;

import com.ukraine.dc.httpserver.api.exception.InternalServerErrorException;
import com.ukraine.dc.httpserver.api.model.Response;
import com.ukraine.dc.httpserver.api.model.StatusCode;
import com.ukraine.dc.httpserver.api.service.writer.ItemWriter;
import com.ukraine.dc.httpserver.api.service.writer.ResponseWriter;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResponseWriterTest {
    private ItemWriter itemWriter = new ResponseWriter();

    private static final String EXPECTED_RESPONSE = "HTTP/1.1 200 OK\r\n" +
            "key: val\r\n" +
            "\r\n" +
            "ok content";

    @Test
    void shouldWriteOkStatus_whenPassValidRequestObject() {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);

        var headers = new HashMap<String, String>() {{
            put("key", "val");
        }};

        Response response = new Response(StatusCode.OK, "ok content", headers);
        itemWriter.writeResponse(writer, response);
        assertEquals(EXPECTED_RESPONSE, stringWriter.toString());
    }

    @Test
    void shouldThrowInternalServerError_WhenWriterIsNull() {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);

        Response response = new Response(StatusCode.OK, "ok content", null);
        Exception exception = assertThrows(InternalServerErrorException.class,
                () -> {
                    writer.close();
                    itemWriter.writeResponse(writer, response);
                });
        assertEquals("Server error", exception.getMessage());
    }

}