package com.example.casi.uppg5_3.service.model;

public class SystemMessage implements Message {

    private String message;
    private String source;

    public SystemMessage(String message, String source) {
        this.message = message;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
