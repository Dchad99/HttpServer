package com.ukraine.dc.httpserver.api.service.writer;

import com.ukraine.dc.httpserver.api.exception.InternalServerErrorException;
import com.ukraine.dc.httpserver.api.model.Response;
import com.ukraine.dc.httpserver.api.model.StatusCode;

import java.io.*;
import java.util.Map;

public class ResponseWriter implements ItemWriter {
    private static final String STATUS_LINE = "HTTP/1.1 %s %s";
    private static final String HEADER_TEMPLATE = "%s: %s";

    @Override
    public void writeResponse(Writer writer, Response response) {
        write(writer, response);
    }

    private void write(Writer writer, Response response) {
        try (var bufferedWriter = new BufferedWriter(writer)) {
            StatusCode statusCode = response.getStatusCode();
            bufferedWriter.write(String.format(STATUS_LINE, statusCode.getCode(), statusCode.getMessage()));
            bufferedWriter.write("\r\n");
            if (response.getHeaders() != null && !response.getHeaders().isEmpty()) {
                for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                    bufferedWriter.write(String.format(HEADER_TEMPLATE, header.getKey(), header.getValue()));
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.write("\r\n");
            bufferedWriter.write(response.getContent());
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new InternalServerErrorException("Server error", e);
        }
    }


}
