package com.ukraine.dc.httpserver.api.model;

import java.util.Map;

public class Request {
    private String requestUri;
    private Map<String, String> headers;
    private HttpMethod httpMethod;

    public Request() {
    }

    public Request(String requestUri, Map<String, String> headers, HttpMethod httpMethod) {
        this.requestUri = requestUri;
        this.headers = headers;
        this.httpMethod = httpMethod;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
