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
public class Response {
    private byte[] content;
    private StatusCode statusCode;
    private Map<String, String> headers;
}
