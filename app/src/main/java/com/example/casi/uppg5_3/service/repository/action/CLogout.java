package com.example.casi.uppg5_3.service.repository.action;

public class CLogout extends ClientAction {
    String command;

    public CLogout() {
        super(true, true);
        int command_number = 0;
        this.command = ++command_number + " " + "LOGOUT";
    }

    @Override
    public void run() {
        repository.outMessage(command);

    }

}
