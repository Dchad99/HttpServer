package com.ukraine.dc.httpserver.api.service.parser;

import com.ukraine.dc.httpserver.api.model.Request;

import java.io.InputStream;

public interface RequestParser {
    Request parse(InputStream reader);
}
