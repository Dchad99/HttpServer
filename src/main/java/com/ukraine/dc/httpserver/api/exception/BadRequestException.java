package com.ukraine.dc.httpserver.api.exception;

import com.ukraine.dc.httpserver.api.model.StatusCode;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(StatusCode.BAD_REQUEST, message);
    }
}
