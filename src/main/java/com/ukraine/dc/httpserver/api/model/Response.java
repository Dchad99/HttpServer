package com.ukraine.dc.httpserver.api.model;

import java.util.Map;

public class Response {
    private String content;
    private StatusCode statusCode;
    private Map<String, String> headers;

    public Response() {
    }

    public Response(StatusCode statusCode, String content, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.content = content;
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
