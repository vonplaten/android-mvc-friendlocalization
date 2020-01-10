package com.example.casi.uppg5_3.service.repository.action;

import com.example.casi.uppg5_3.service.model.ServerMessage;
import com.example.casi.uppg5_3.service.model.SystemMessage;

class SysMessage extends SystemAction {

    private SystemMessage message;

    public SysMessage(SystemMessage message) {
        super();
        this.message = message;
    }

    @Override
    public void run() {
        repository.getMessageObervable().postValue(message);

    }

}
