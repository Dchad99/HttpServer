package com.ukraine.dc.httpserver.api.service;

import com.ukraine.dc.httpserver.api.exception.BadRequestException;
import com.ukraine.dc.httpserver.api.exception.InternalServerErrorException;
import com.ukraine.dc.httpserver.api.exception.ResourceNotFoundException;
import com.ukraine.dc.httpserver.api.model.Response;
import com.ukraine.dc.httpserver.api.model.StatusCode;
import com.ukraine.dc.httpserver.api.service.parser.ItemParser;
import com.ukraine.dc.httpserver.api.service.reader.ItemReader;
import com.ukraine.dc.httpserver.api.service.writer.ItemWriter;

import java.io.*;
import java.net.Socket;

public class RequestHandler {
    private final ItemReader resourceReader;
    private final ItemWriter responseWriter;
    private final ItemParser requestParser;

    public RequestHandler(ItemReader reader, ItemWriter writer, ItemParser parser) {
        this.resourceReader = reader;
        this.responseWriter = writer;
        this.requestParser = parser;
    }

    public void handle(Socket socket) {
        try (var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            Response response = new Response();
            try {
                var request = requestParser.parse(reader);
                var content = resourceReader.readContent(request.getRequestUri());
                response.setStatusCode(StatusCode.OK);
                response.setContent(content);
                response.setHeaders(request.getHeaders());
                responseWriter.writeResponse(writer, response);
            } catch (UnsupportedOperationException e) {
                response.setStatusCode(StatusCode.METHOD_NOT_ALLOWED);
                response.setContent(StatusCode.METHOD_NOT_ALLOWED.getMessage());
                responseWriter.writeResponse(writer, response);
            } catch (ResourceNotFoundException e) {
                response.setStatusCode(StatusCode.NOT_FOUND);
                response.setContent(StatusCode.NOT_FOUND.getMessage());
                responseWriter.writeResponse(writer, response);
            } catch (BadRequestException e) {
                response.setStatusCode(StatusCode.BAD_REQUEST);
                response.setContent(StatusCode.BAD_REQUEST.getMessage());
                responseWriter.writeResponse(writer, response);
            } catch (InternalServerErrorException e) {
                response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR);
                response.setContent(StatusCode.INTERNAL_SERVER_ERROR.getMessage());
                responseWriter.writeResponse(writer, response);
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("Internal Server Error", e);
        }
    }

}
