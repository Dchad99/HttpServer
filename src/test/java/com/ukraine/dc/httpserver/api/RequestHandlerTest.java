package com.ukraine.dc.httpserver.api;

import com.ukraine.dc.httpserver.api.exception.HttpException;
import com.ukraine.dc.httpserver.api.exception.MethodNotAllowedException;
import com.ukraine.dc.httpserver.api.model.HttpMethod;
import com.ukraine.dc.httpserver.api.model.Request;
import com.ukraine.dc.httpserver.api.model.Response;
import com.ukraine.dc.httpserver.api.service.RequestHandler;
import com.ukraine.dc.httpserver.api.service.parser.RequestParser;
import com.ukraine.dc.httpserver.api.service.parser.SocketRequestParser;
import com.ukraine.dc.httpserver.api.service.reader.FileResourceReader;
import com.ukraine.dc.httpserver.api.service.reader.ResourceReader;
import com.ukraine.dc.httpserver.api.service.writer.ResponseWriter;
import com.ukraine.dc.httpserver.api.service.writer.SocketResponseWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RequestHandlerTest {
    private static final String REQUEST_URI = "/index.html";

    private final ResourceReader resourceReader = mock(FileResourceReader.class);
    private final ResponseWriter responseWrite = mock(SocketResponseWriter.class);
    private final RequestParser requestParser = mock(SocketRequestParser.class);

    private RequestHandler handler = mock(RequestHandler.class);
    private final InputStream inputStream = mock(InputStream.class);
    private final OutputStream outputStream = mock(OutputStream.class);

    @BeforeEach
    void setUp() {
        handler = new RequestHandler(resourceReader, responseWrite, requestParser);
    }

    @Test
    void shouldWriteMessageWithOkStatus_whenHandleGetRequest() {
        var request = buildRequest(HttpMethod.GET);

        when(requestParser.parse(inputStream)).thenReturn(request);
        when(resourceReader.readContent(request)).thenReturn("".getBytes());
        handler.handle(inputStream, outputStream);
        ArgumentCaptor<Request> readerCaptor = ArgumentCaptor.forClass(Request.class);
        verify(requestParser).parse(inputStream);
        verify(resourceReader).readContent(readerCaptor.capture());
        var requestToRead = readerCaptor.getValue();
        assertEquals(REQUEST_URI, requestToRead.getRequestUri());
        assertEquals(HttpMethod.GET, requestToRead.getHttpMethod());

        verify(responseWrite).writeResponse(any(OutputStream.class), any(Response.class));
    }

    @Test
    void shouldWriteErrorMessageWithOkStatus_whenHandleNotGetRequest() {
        when(requestParser.parse(inputStream)).thenThrow(MethodNotAllowedException.class);
        handler.handle(inputStream, outputStream);
        verify(responseWrite).writeErrorResponse(any(OutputStream.class), any(HttpException.class));
    }


    private Request buildRequest(HttpMethod httpMethod) {
        Request request = new Request();
        request.setRequestUri(REQUEST_URI);
        request.setHttpMethod(httpMethod);
        return request;
    }

}