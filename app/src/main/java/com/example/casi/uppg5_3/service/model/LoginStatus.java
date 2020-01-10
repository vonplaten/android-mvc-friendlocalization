package com.example.casi.uppg5_3.service.model;

public class LoginStatus {

    private boolean loggedin;

    public LoginStatus(boolean loggedin) {
        this.loggedin = loggedin;
    }
    public boolean isLoggedin(){
        return loggedin;
    }

}
