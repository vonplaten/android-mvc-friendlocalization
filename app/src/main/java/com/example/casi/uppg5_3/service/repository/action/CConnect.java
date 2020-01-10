package com.example.casi.uppg5_3.service.repository.action;

public class CConnect extends ClientAction {

    private String adress;
    private int port;

    protected CConnect(String adress, int port) {
        super(false, false);
        this.adress = adress;
        this.port = port;
    }

    @Override
    public void run() {
        repository.connection.connect(adress, port);

    }

}
