package com.example.casi.uppg5_3.service.model;

import android.graphics.Color;

public class ConnectionStatus {

    private boolean _connected;
    private String sender;
    private String _message;

    public ConnectionStatus(String message, boolean connected, String sender) {
        _message = message;
        _connected = connected;
        this.sender = sender;
    }

    public boolean isConnected(){
        return _connected;
    }
    public int getTextColor(){
        if (_connected)
            return Color.parseColor("#006400");
        else return Color.parseColor("#8b0000");
    }
    public String getText(){
        return _message;
    }

}
