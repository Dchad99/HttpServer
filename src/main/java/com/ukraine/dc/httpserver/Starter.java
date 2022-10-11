package com.ukraine.dc.httpserver;

import com.ukraine.dc.httpserver.api.Server;

public class Starter {
    public static void main(String[] args) {
        Server server = new Server();
        server.setPort(3005);
        server.setWebResourcesPath("src/main/resources/webapp");
        server.start();
    }
}