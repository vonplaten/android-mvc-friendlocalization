package com.example.casi.uppg5_3.service.repository.action;

import com.example.casi.uppg5_3.service.model.Player;

public class CTurn extends ClientAction {
    String command;
    private Player.Type type;

    public CTurn(Player.Type type) {
        super(true, true);
        this.type = type;
        int command_number = 0;
        if (type.equals(Player.Type.ZOMBIE))
            this.command = ++command_number + " " + "TURN ZOMBIE";
        if (type.equals(Player.Type.HUMAN))
            this.command = ++command_number + " " + "TURN HUMAN";

    }

    @Override
    public void run() {
        repository.outMessage(command);
    }
}
