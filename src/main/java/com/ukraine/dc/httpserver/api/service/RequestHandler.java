package com.ukraine.dc.httpserver.api.service;

import com.ukraine.dc.httpserver.api.exception.HttpException;
import com.ukraine.dc.httpserver.api.model.HttpMethod;
import com.ukraine.dc.httpserver.api.model.Response;
import com.ukraine.dc.httpserver.api.model.StatusCode;
import com.ukraine.dc.httpserver.api.service.parser.RequestParser;
import com.ukraine.dc.httpserver.api.service.reader.ResourceReader;
import com.ukraine.dc.httpserver.api.service.writer.ResponseWriter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final ResourceReader resourceReader;
    private final ResponseWriter responseWriter;
    private final RequestParser requestParser;

    public RequestHandler(ResourceReader reader, ResponseWriter writer, RequestParser parser) {
        this.resourceReader = reader;
        this.responseWriter = writer;
        this.requestParser = parser;
    }

    @SneakyThrows
    public void handle(InputStream inputStream, OutputStream outputStream) {
        try {
            var request = requestParser.parse(inputStream);
            var content = resourceReader.readContent(request);
            responseWriter.writeResponse(outputStream, Response.builder()
                    .content(content)
                    .statusCode(StatusCode.OK)
                    .headers(request.getHeaders())
                    .build());
            log.info("Method handle() finished successfully.");
        } catch (HttpException e) {
            log.info("Method handle() failed");
            responseWriter.writeErrorResponse(outputStream, e);
        }
    }

}
