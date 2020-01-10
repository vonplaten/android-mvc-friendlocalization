package com.example.casi.uppg5_3.service.repository.action;

import com.example.casi.uppg5_3.service.model.LoginStatus;
import com.example.casi.uppg5_3.service.model.ServerMessage;

class SLogout extends ServerAction {
    private boolean logout_success;
    private String message;

    public SLogout(boolean logout_success, String message) {
        super();
        this.logout_success = logout_success;
        this.message = message;
    }

    @Override
    public void run() {
        repository.getLoginStatusObservable().postValue(new LoginStatus(!logout_success));
        repository.getMessageObervable().postValue(new ServerMessage(message));

    }

}
