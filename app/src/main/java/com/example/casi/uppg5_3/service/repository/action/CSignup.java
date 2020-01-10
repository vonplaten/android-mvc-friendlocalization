package com.example.casi.uppg5_3.service.repository.action;

class CSignup extends ClientAction {
    private String command;
    public CSignup(String name, String password) {
        super(true, false);
        int command_number = 0;
        this.command = ++command_number + " " + "REGISTER " + name + " " + password;
    }

    @Override
    public void run() {
        repository.outMessage(command);

    }

}
