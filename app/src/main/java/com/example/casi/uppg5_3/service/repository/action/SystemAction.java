package com.example.casi.uppg5_3.service.repository.action;

public abstract class SystemAction extends Action {
    protected SystemAction() {
        super(false, false, true);
    }

    @Override
    public boolean okToRun() {
        return true;
    }
}
