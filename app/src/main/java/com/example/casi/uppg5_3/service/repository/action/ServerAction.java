package com.example.casi.uppg5_3.service.repository.action;

public abstract class ServerAction extends Action {


    protected ServerAction() {
        super(false, false, true);
    }

    @Override
    public boolean okToRun() {
        return true;
    }
}
