package com.ukraine.dc.httpserver.api.service.writer;

import com.ukraine.dc.httpserver.api.exception.*;
import com.ukraine.dc.httpserver.api.model.Response;
import com.ukraine.dc.httpserver.api.model.StatusCode;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;
import java.util.Map;

public class SocketResponseWriter implements ResponseWriter {
    private static final Logger log = LoggerFactory.getLogger(SocketResponseWriter.class);
    private static final String STATUS_LINE = "HTTP/1.1 %s %s";
    private static final String HEADER_TEMPLATE = "%s: %s";
    private static final String HTTP_SPECIFICATION_DELIMITER = "\r\n";
    private static final Map<Class<? extends HttpException>, StatusCode> ERROR_CODES = Map.of(
            BadRequestException.class, StatusCode.BAD_REQUEST,
            ResourceNotFoundException.class, StatusCode.NOT_FOUND,
            MethodNotAllowedException.class, StatusCode.METHOD_NOT_ALLOWED,
            InternalServerErrorException.class, StatusCode.INTERNAL_SERVER_ERROR);

    @Override
    public void writeResponse(OutputStream outputStream, Response response) {
        write(outputStream, response);
    }

    @Override
    public void writeErrorResponse(OutputStream outputStream, HttpException exception) {
        var error = ERROR_CODES.get(exception.getClass());
        write(outputStream, Response.builder()
                .statusCode(error)
                .content(error.getMessage().getBytes())
                .headers(Collections.emptyMap())
                .build());
    }

    @SneakyThrows
    private void write(OutputStream outputStream, Response response) {
        try (var bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            bufferedWriter.write(buildStatusLine(response.getStatusCode()));
            bufferedWriter.write(HTTP_SPECIFICATION_DELIMITER);
            if (response.getHeaders() != null) {
                for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                    bufferedWriter.write(String.format(HEADER_TEMPLATE, header.getKey(), header.getValue()));
                    bufferedWriter.write(HTTP_SPECIFICATION_DELIMITER);
                }
            }
            bufferedWriter.write(HTTP_SPECIFICATION_DELIMITER);
            bufferedWriter.write(new String(response.getContent()));
            log.info("The response was written successfully");
        } catch (IOException e) {
            log.info("The response was failed");
            throw new InternalServerErrorException("Server error");
        }
    }

    private String buildStatusLine(StatusCode statusCode) {
        return String.format(STATUS_LINE, statusCode.getCode(), statusCode.getMessage());
    }

}
