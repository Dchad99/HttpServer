package com.ukraine.dc.httpserver.api.exception;

import com.ukraine.dc.httpserver.api.model.StatusCode;

public class MethodNotAllowedException extends HttpException {

    public MethodNotAllowedException(String message) {
        super(StatusCode.METHOD_NOT_ALLOWED, message);
    }
}
