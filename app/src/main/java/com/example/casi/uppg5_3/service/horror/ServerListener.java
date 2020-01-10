package com.example.casi.uppg5_3.service.horror;

import com.example.casi.uppg5_3.service.model.ConnectionStatus;
import com.example.casi.uppg5_3.service.repository.message.MessageIn;

public interface ServerListener {
    void onServerMessage(MessageIn m);
    void onConnectionStatus(ConnectionStatus s);
}
