package com.ukraine.dc.httpserver.api.exception;

import com.ukraine.dc.httpserver.api.model.StatusCode;

public class HttpException extends RuntimeException {
    private StatusCode statusCode;

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }


}
