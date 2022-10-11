package com.ukraine.dc.httpserver.api.service.parser;

import com.ukraine.dc.httpserver.api.exception.InternalServerErrorException;
import com.ukraine.dc.httpserver.api.model.HttpMethod;
import com.ukraine.dc.httpserver.api.model.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestParser implements ItemParser {

    @Override
    public Request parse(Reader reader) {
        return parseRequest(reader);
    }

    private Request parseRequest(Reader reader) {
        String line;
        try(var bufferedReader = new BufferedReader(reader)) {
            Request request = new Request();
            line = bufferedReader.readLine();
            injectUriAndMethod(line, request);
            injectHeaders(bufferedReader, request);
            return request;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Internal server error", e);
        }
    }

    private void injectUriAndMethod(String reader, Request request) {
        if (reader.contains(HttpMethod.GET.getHttpMethod())) {
            String[] inputRequest = reader.split(" ");
            request.setRequestUri(inputRequest[1]);
            request.setHttpMethod(HttpMethod.getHttpMethodByRequestName(inputRequest[0]));
            return;
        }
        throw new UnsupportedOperationException("The API doesn't support such type of request");
    }

    private void injectHeaders(BufferedReader reader, Request request) {
        Map<String, String> headers = new HashMap<>();
        try {
            String line;
            while (Objects.nonNull(line = reader.readLine()) && !line.isEmpty()) {
                String[] header = line.split(":\\s");
                headers.put(header[0].trim(), header[1]);
            }
            request.setHeaders(headers);
        } catch (IOException e) {
            throw new InternalServerErrorException("Internal server error", e);
        }
    }

}
