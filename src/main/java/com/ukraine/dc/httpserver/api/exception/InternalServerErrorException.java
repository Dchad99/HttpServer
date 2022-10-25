package com.ukraine.dc.httpserver.api.exception;

import com.ukraine.dc.httpserver.api.model.StatusCode;

public class InternalServerErrorException extends HttpException {

    public InternalServerErrorException(String message) {
        super(StatusCode.INTERNAL_SERVER_ERROR, message);
    }
}
