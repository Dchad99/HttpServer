package com.ukraine.dc.httpserver.api;

import com.ukraine.dc.httpserver.api.service.RequestHandler;
import com.ukraine.dc.httpserver.api.service.parser.RequestParser;
import com.ukraine.dc.httpserver.api.service.reader.ResourceReader;
import com.ukraine.dc.httpserver.api.service.writer.ResponseWriter;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private static final int DEFAULT_PORT = 3000;
    private String webResourcesPath;
    private int port;

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        var parser = new RequestParser();
        var reader = new ResourceReader(webResourcesPath);
        var writer = new ResponseWriter();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                var socket = serverSocket.accept();
                new Thread(() -> new RequestHandler(reader, writer, parser).handle(socket)).start();
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getWebResourcesPath() {
        return webResourcesPath;
    }

    public void setWebResourcesPath(String webResourcesPath) {
        this.webResourcesPath = webResourcesPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
