package com.ukraine.dc.httpserver.api.service;

import com.ukraine.dc.httpserver.api.exception.ResourceNotFoundException;
import com.ukraine.dc.httpserver.api.service.reader.ResourceReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ResourceReaderTest {
    private static final String webAppResourcePath = "src/test/resources/webapp";
    private static final String requestUri = "/index.html";
    private ResourceReader readerMock = mock(ResourceReader.class);
    private ResourceReader resourceReader;

    @BeforeEach
    void setUp() {
        resourceReader = new ResourceReader(webAppResourcePath);
    }

    @Test
    void shouldReturnFileContentWhenPathIsValid() {
        String content = resourceReader.readContent(requestUri);
        assertNotNull(content);
        assertAll(
                () -> assertTrue(content.contains("<html ")),
                () -> assertTrue(content.contains("<head>")),
                () -> assertTrue(content.contains("<title>Document</title>")),
                () -> assertTrue(content.contains("<body></body>"))
        );
    }

    @Test
    void shouldThrowExceptionWhenResourcePathIsNotValid() {
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> resourceReader.readContent("/test"));
        assertEquals("Resource wasn't found by path: src\\test\\resources\\webapp\\test", exception.getMessage());
    }


}