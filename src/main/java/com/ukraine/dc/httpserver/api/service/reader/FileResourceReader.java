package com.ukraine.dc.httpserver.api.service.reader;

import com.ukraine.dc.httpserver.api.exception.InternalServerErrorException;
import com.ukraine.dc.httpserver.api.exception.ResourceNotFoundException;
import com.ukraine.dc.httpserver.api.model.Request;
import com.ukraine.dc.httpserver.api.model.StatusCode;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileResourceReader implements ResourceReader {
    private static final Logger log = LoggerFactory.getLogger(FileResourceReader.class);
    private final String webAppResource;

    public FileResourceReader(String webAppResource) {
        this.webAppResource = webAppResource;
    }

    public byte[] readContent(Request request) {
        File resource = new File(webAppResource, request.getRequestUri());
        verifyStaticResourcePath(resource);
        return readResourceContent(resource);
    }

    @SneakyThrows
    public byte[] readResourceContent(File resource) {
        log.info("Start reading the resource: {}", resource.getPath());
        return readResourceContent(new FileInputStream(resource));
    }

    private void verifyStaticResourcePath(File resource) {
        if (!resource.exists()) {
            throw new ResourceNotFoundException("Resource wasn't found by path: " + resource.getPath());
        }
    }

    private byte[] readResourceContent(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try (var reader = new BufferedInputStream(inputStream)) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, count));
            }
            log.info("The content of the resource was read");
        } catch (IOException e) {
            log.info("The error was encountered while reading resource");
            throw new InternalServerErrorException(StatusCode.INTERNAL_SERVER_ERROR.getMessage());
        }
        return builder.toString().getBytes();
    }
}
