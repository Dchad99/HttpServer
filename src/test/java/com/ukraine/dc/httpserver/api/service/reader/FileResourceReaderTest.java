package com.ukraine.dc.httpserver.api.service.reader;

import com.ukraine.dc.httpserver.api.exception.ResourceNotFoundException;
import com.ukraine.dc.httpserver.api.model.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileResourceReaderTest {
    private static final String webAppResourcePath = "src/test/resources/webapp";
    private static final String requestUri = "/index.html";
    private ResourceReader resourceReader;

    @BeforeEach
    void setUp() {
        resourceReader = new FileResourceReader(webAppResourcePath);
    }

    @Test
    void shouldReturnFileContentWhenPathIsValid() {
        byte[] result = resourceReader.readContent(buildRequest(requestUri));
        var content = new String(result);
        assertNotNull(content);
        assertAll(
                () -> assertTrue(content.contains("<html ")),
                () -> assertTrue(content.contains("<head>")),
                () -> assertTrue(content.contains("<title>Document</title>")),
                () -> assertTrue(content.contains("<h1>Hello world</h1>"))
        );
    }

    private Request buildRequest(String requestUri) {
        return Request.builder()
                .requestUri(requestUri)
                .build();
    }
}