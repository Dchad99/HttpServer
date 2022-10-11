package com.ukraine.dc.httpserver.api.model;

import java.util.Arrays;

public enum StatusCode {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public static StatusCode getHttpStatusCode(String message) {
        return Arrays.stream(StatusCode.values())
                .filter(s -> s.message.equalsIgnoreCase(message))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("HttpMethod with code: " + message + " wasn't found!"));
    }
}
