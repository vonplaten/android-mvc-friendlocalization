package com.example.casi.uppg5_3.service.model;

public class ServerMessage implements Message {
    private String message;

    public ServerMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
