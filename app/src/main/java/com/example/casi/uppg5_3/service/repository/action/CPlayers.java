package com.example.casi.uppg5_3.service.repository.action;

import com.example.casi.uppg5_3.service.model.FragmentPicker;

class CPlayers extends ClientAction{
    String command;

    protected CPlayers() {
        super(true, true);
        int command_number = 0;
        this.command = ++command_number + " " + "LIST-VISIBLE-PLAYERS";
    }

    @Override
    public void run() {
        repository.outMessage(command);

    }

    @Override
    public void requires() {
        //Action.sysFragment(new FragmentPicker(FragmentPicker.PickCode.LOGIN_FRAGMENT));
    }
}
