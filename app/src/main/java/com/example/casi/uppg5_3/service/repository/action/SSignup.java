package com.example.casi.uppg5_3.service.repository.action;

import com.example.casi.uppg5_3.service.model.ConnectionStatus;
import com.example.casi.uppg5_3.service.model.ServerMessage;
import com.example.casi.uppg5_3.service.model.SignupStatus;

class SSignup extends ServerAction {
    private boolean signup_success;
    private String message;

    public SSignup(boolean signup_success, String message) {
        super();
        this.signup_success = signup_success;
        this.message = message;
    }

    @Override
    public void run() {
        repository.getSignupStatusObservable().postValue(new SignupStatus(signup_success));
        repository.getMessageObervable().postValue(new ServerMessage(message));

    }

}
