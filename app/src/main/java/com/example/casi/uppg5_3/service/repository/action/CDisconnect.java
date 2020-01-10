package com.example.casi.uppg5_3.service.repository.action;

public class CDisconnect extends ClientAction {

    public CDisconnect() {
        super(true, false);
    }

    @Override
    public void run() {
        repository.connection.disconnect();

    }

}
