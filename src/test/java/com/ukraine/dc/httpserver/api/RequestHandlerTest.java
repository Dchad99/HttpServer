package com.ukraine.dc.httpserver.api;

import com.ukraine.dc.httpserver.api.exception.InternalServerErrorException;
import com.ukraine.dc.httpserver.api.exception.ResourceNotFoundException;
import com.ukraine.dc.httpserver.api.model.HttpMethod;
import com.ukraine.dc.httpserver.api.model.Request;
import com.ukraine.dc.httpserver.api.model.Response;
import com.ukraine.dc.httpserver.api.model.StatusCode;
import com.ukraine.dc.httpserver.api.service.RequestHandler;
import com.ukraine.dc.httpserver.api.service.parser.ItemParser;
import com.ukraine.dc.httpserver.api.service.parser.RequestParser;
import com.ukraine.dc.httpserver.api.service.reader.ItemReader;
import com.ukraine.dc.httpserver.api.service.reader.ResourceReader;
import com.ukraine.dc.httpserver.api.service.writer.ItemWriter;
import com.ukraine.dc.httpserver.api.service.writer.ResponseWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RequestHandlerTest {
    private static final String TEST_CONTENT = "test content";
    private static final String METHOD_NOT_ALLOWED = "Method Not Allowed";
    private static final String NOT_FOUND_CONTENT = "Not Found";
    private static final String REQUEST_URI = "/index.html";

    private final ItemReader resourceReader = mock(ResourceReader.class);
    private final ItemWriter responseWrite = mock(ResponseWriter.class);
    private final ItemParser requestParser = mock(RequestParser.class);
    private final Socket socket = mock(Socket.class);
    private final Writer writer = mock(Writer.class);

    private RequestHandler handler;
    private final InputStream inputStream = mock(InputStream.class);
    private final OutputStream outputStream = mock(OutputStream.class);
    private InputStream stream;


    @BeforeEach
    void setUp() throws IOException {
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        when(mockServerSocket.accept()).thenReturn(socket);

        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        handler = new RequestHandler(resourceReader, responseWrite, requestParser);
    }

    @Test
    void shouldThrowInternalServerError_whenServerHandleNotGetRequest() throws IOException {
        var s  = new Socket();
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        when(mockServerSocket.accept()).thenReturn(s);

        var exception = assertThrows(InternalServerErrorException.class,
                () -> handler.handle(s));
        assertEquals("Internal Server Error", exception.getMessage());
    }

    @Test
    void testGetRequest() {
        var request = buildRequest(HttpMethod.GET);
        when(requestParser.parse(any())).thenReturn(request);
        when(resourceReader.readContent(request.getRequestUri())).thenReturn(TEST_CONTENT);

        handler.handle(socket);
        verify(requestParser).parse(any());
        verify(resourceReader).readContent(anyString());

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseWrite).writeResponse(any(), responseCaptor.capture());
        var response = responseCaptor.getValue();

        assertEquals(TEST_CONTENT, response.getContent());
        assertEquals(StatusCode.OK.getCode(), response.getStatusCode().getCode());
        assertEquals(StatusCode.OK.getMessage(), response.getStatusCode().getMessage());

        verifyNoMoreInteractions(requestParser);
        verifyNoMoreInteractions(resourceReader);
    }

    @Test
    void testNotGetRequest() {
        when(requestParser.parse(any())).thenThrow(UnsupportedOperationException.class);
        handler.handle(socket);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseWrite).writeResponse(any(), responseCaptor.capture());
        var response = responseCaptor.getValue();

        assertEquals(METHOD_NOT_ALLOWED, response.getContent());
        assertEquals(StatusCode.METHOD_NOT_ALLOWED.getCode(), response.getStatusCode().getCode());
    }


    @Test
    void testGetRequest_whenServerWorkWithInvalidSource() {
        var request = buildRequest(HttpMethod.GET);
        when(requestParser.parse(any())).thenReturn(request);
        when(resourceReader.readContent(any())).thenThrow(ResourceNotFoundException.class);

        handler.handle(socket);
        verify(requestParser).parse(any());

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseWrite).writeResponse(any(), responseCaptor.capture());
        var response = responseCaptor.getValue();

        assertEquals(NOT_FOUND_CONTENT, response.getContent());
        assertEquals(StatusCode.NOT_FOUND.getCode(), response.getStatusCode().getCode());

        verifyNoMoreInteractions(requestParser);
    }


    private Request buildRequest(HttpMethod httpMethod) {
        Request request = new Request();
        request.setRequestUri(REQUEST_URI);
        request.setHttpMethod(httpMethod);
        return request;
    }

    private Response buildResponse(StatusCode statusCode) {
        Response response = new Response();
        response.setContent(TEST_CONTENT);
        response.setStatusCode(statusCode);
        return response;
    }

}