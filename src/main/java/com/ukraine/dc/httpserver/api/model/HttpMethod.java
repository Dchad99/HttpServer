package com.ukraine.dc.httpserver.api.model;

import java.util.Arrays;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    PATCH("PATCH");
    private final String requestName;

    HttpMethod(String requestName) {
        this.requestName = requestName;
    }

    public String getHttpMethod(){
        return requestName;
    }

    public static HttpMethod getHttpMethodByRequestName(String requestName){
        return Arrays.stream(HttpMethod.values())
                .filter(s -> s.requestName.equalsIgnoreCase(requestName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("HttpMethod " + requestName + " doesn't exist"));
    }
}
