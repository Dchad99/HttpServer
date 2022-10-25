package com.ukraine.dc.httpserver.api;

import com.ukraine.dc.httpserver.api.service.RequestHandler;
import com.ukraine.dc.httpserver.api.service.parser.SocketRequestParser;
import com.ukraine.dc.httpserver.api.service.reader.FileResourceReader;
import com.ukraine.dc.httpserver.api.service.writer.SocketResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
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
        var parser = new SocketRequestParser();
        var reader = new FileResourceReader(webResourcesPath);
        var writer = new SocketResponseWriter();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                var socket = serverSocket.accept();
                log.info("Server started");
                new Thread(() -> {
                    try (var inputStream = socket.getInputStream();
                         var outputStream = socket.getOutputStream()) {
                        log.info("The method handle() was invoked on RequestHandler");
                        new RequestHandler(reader, writer, parser).handle(inputStream, outputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException exception) {
            log.info("Socket was closed");
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
