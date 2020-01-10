package com.example.casi.uppg5_3.service.horror;

import com.example.casi.uppg5_3.service.model.ConnectionStatus;

import java.net.Socket;

public interface SocketListener {
    void onSocketReady(Socket status);
    void onConnectionStatus(ConnectionStatus s);
}
