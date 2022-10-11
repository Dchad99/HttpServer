package com.ukraine.dc.httpserver.api.service.reader;

import com.ukraine.dc.httpserver.api.exception.BadRequestException;
import com.ukraine.dc.httpserver.api.exception.ResourceNotFoundException;

import java.io.*;

public class ResourceReader implements ItemReader {
    private final String webAppResource;

    public ResourceReader(String webAppResource) {
        this.webAppResource = webAppResource;
    }

    public String readContent(String requestUri) {
        File resource = new File(webAppResource + requestUri);
        verifyStaticResourcePath(resource);
        return readResourceContent(resource);
    }

    private void verifyStaticResourcePath(File resource) {
        if (!resource.exists()) {
            throw new ResourceNotFoundException("Resource wasn't found by path: " + resource.getPath());
        }
    }

    private String readResourceContent(File resource) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(resource)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line.trim());
            }
        } catch (IOException e) {
            throw new BadRequestException(e);
        }
        return builder.toString();
    }
}
