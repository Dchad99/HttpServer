package com.ukraine.dc.httpserver.api.service.writer;

import com.ukraine.dc.httpserver.api.model.Response;
import java.io.Writer;

public interface ItemWriter {
    void writeResponse(Writer stream, Response response);
}
