package com.ukraine.dc.httpserver.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private String requestUri;
    private Map<String, String> headers;
    private HttpMethod httpMethod;
}
