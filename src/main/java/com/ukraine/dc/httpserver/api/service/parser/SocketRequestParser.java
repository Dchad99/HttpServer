package com.ukraine.dc.httpserver.api.service.parser;

import com.ukraine.dc.httpserver.api.exception.InternalServerErrorException;
import com.ukraine.dc.httpserver.api.exception.MethodNotAllowedException;
import com.ukraine.dc.httpserver.api.model.HttpMethod;
import com.ukraine.dc.httpserver.api.model.Request;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class SocketRequestParser implements RequestParser {
    private static final Logger log = LoggerFactory.getLogger(SocketRequestParser.class);

    @Override
    public Request parse(InputStream inputStream) {
        try {
            var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Request request = new Request();
            injectUriAndMethod(bufferedReader.readLine(), request);
            List<String> requestHeaders = new ArrayList<>();
            String line;
            while (Objects.nonNull(line = bufferedReader.readLine()) && !line.isEmpty()) {
                requestHeaders.add(line);
            }
            injectHeaders(requestHeaders, request);
            log.info("The RequestParser finished parse() and prepare request: {}", request);
            return request;
        } catch (IOException e) {
            log.info("Error during parse request");
            throw new InternalServerErrorException("Internal server error");
        }
    }

    public void injectUriAndMethod(String line, Request request) {
        if (!line.contains(HttpMethod.GET.getHttpMethod())) {
            log.info("The API supports only GET request");
            throw new MethodNotAllowedException("The API doesn't support such type of request");
        }
        String[] inputRequest = line.split(" ");
        request.setRequestUri(inputRequest[1]);
        request.setHttpMethod(HttpMethod.getHttpMethodByRequestName(inputRequest[0]));
    }

    public void injectHeaders(List<String> requestHeaders, Request request) {
        Map<String, String> headers = new HashMap<>();
        for (String line : requestHeaders) {
            String[] header = line.split(":\\s");
            headers.put(header[0].trim(), header[1]);
        }
        request.setHeaders(headers);
    }

}
