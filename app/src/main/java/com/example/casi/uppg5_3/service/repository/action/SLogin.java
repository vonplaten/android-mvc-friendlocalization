package com.example.casi.uppg5_3.service.repository.action;

import com.example.casi.uppg5_3.service.model.LoginStatus;
import com.example.casi.uppg5_3.service.model.ServerMessage;

class SLogin extends ServerAction{
    private boolean login_success;
    private String message;

    public SLogin(boolean login_success, String message) {
        super();
        this.login_success = login_success;
        this.message = message;
    }

    @Override
    public void run() {
        LoginStatus loginStatus = new LoginStatus(true);
        repository.getLoginStatusObservable().postValue(loginStatus);
        repository.loginStatus = loginStatus;
        repository.getMessageObervable().postValue(new ServerMessage(message));

    }

}
