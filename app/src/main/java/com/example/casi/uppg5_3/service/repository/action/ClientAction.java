package com.example.casi.uppg5_3.service.repository.action;

public abstract class ClientAction extends Action {
    protected ClientAction(boolean requiresnetwork, boolean requireslogin) {
        super(requiresnetwork, requireslogin, false);
    }

    @Override
    public boolean okToRun() {
        boolean network = true;
        boolean login = true;

        if (requires_network) {
            network = repository.connectionStatus.isConnected();
        }
        if(requires_login){
            login = repository.loginStatus.isLoggedin();
        }
        return network && login;
    }
}
