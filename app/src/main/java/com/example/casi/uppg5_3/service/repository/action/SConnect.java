package com.example.casi.uppg5_3.service.repository.action;

import com.example.casi.uppg5_3.service.model.ServerMessage;

class SConnect extends ServerAction {
    private String message;

    public SConnect(String message) {
        super();
        this.message = message;
    }

    @Override
    public void run() {
        repository.getMessageObervable().postValue(new ServerMessage(message));
    }

}
