package com.ukraine.dc.httpserver.api.service.writer;

import com.ukraine.dc.httpserver.api.exception.HttpException;
import com.ukraine.dc.httpserver.api.model.Response;

import java.io.OutputStream;

public interface ResponseWriter {
    void writeResponse(OutputStream stream, Response response);

    void writeErrorResponse(OutputStream stream, HttpException httpException);
}
