package com.ukraine.dc.httpserver.api.exception;

import com.ukraine.dc.httpserver.api.model.StatusCode;

public class ResourceNotFoundException extends HttpException {
    public ResourceNotFoundException(String message) {
        super(StatusCode.NOT_FOUND, message);
    }

    public ResourceNotFoundException() {
        super(StatusCode.NOT_FOUND, StatusCode.NOT_FOUND.getMessage());
    }
}
