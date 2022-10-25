package com.ukraine.dc.httpserver.api.service.reader;

import com.ukraine.dc.httpserver.api.model.Request;

public interface ResourceReader {
    byte[] readContent(Request request);
}
