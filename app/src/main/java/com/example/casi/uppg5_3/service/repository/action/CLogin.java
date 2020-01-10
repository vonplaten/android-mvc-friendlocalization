package com.example.casi.uppg5_3.service.repository.action;



public class CLogin extends ClientAction {
    private String command;

    public CLogin(String name, String password) {
        super(true, false);
        int command_number = 0;
        this.command = ++command_number + " " + "LOGIN " + name + " " + password;
    }

    @Override
    public void run() {
        repository.outMessage(command);

    }

    @Override
    public void requires() {
        //Action.uConnect("192.168.1.11", 2002);
    }
}
