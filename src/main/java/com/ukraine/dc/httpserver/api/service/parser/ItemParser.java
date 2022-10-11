package com.ukraine.dc.httpserver.api.service.parser;

import com.ukraine.dc.httpserver.api.model.Request;

import java.io.Reader;

public interface ItemParser {
    Request parse(Reader reader);
}
