package com.example.casi.uppg5_3.service.repository.action;


import android.util.Log;

import com.example.casi.uppg5_3.HorrorApplication;
import com.example.casi.uppg5_3.service.model.FragmentPicker;
import com.example.casi.uppg5_3.service.model.Player;
import com.example.casi.uppg5_3.service.model.SystemMessage;
import com.example.casi.uppg5_3.service.repository.Repository;
import com.google.android.gms.maps.model.LatLng;

public abstract class Action implements Comparable<Action>, Runnable {
    private static final String TAG = "Action";
    protected Repository repository;
    protected ActionQueue actionQueue;

    protected boolean requires_network;
    protected boolean requires_login;

    protected boolean allow_multiple;


    protected Action(boolean requiresnetwork, boolean requireslogin, boolean allowmultiple) {
        if (repository == null) {
            repository = HorrorApplication.getAppComponent().getRepository();
        }
        if (actionQueue == null) {
            actionQueue = HorrorApplication.getAppComponent().getActionQueue();
        }
        requires_network = requiresnetwork;
        requires_login = requireslogin;
        allow_multiple = allowmultiple;
    }



    public abstract boolean okToRun();


    public void requires() {
        Log.d(TAG, "->requires() Not implemented. " +this.toString());
    }

    @Override
    public int compareTo(Action o) {
        int a = Boolean.compare(requires_network, o.requires_network);
        int b = Boolean.compare(requires_login, o.requires_login);
        return a+b;
    }

    protected void execute() {
        actionQueue.add(this);
    }

    public static void uConnect(String adress, int port) {
        new CConnect(adress, port).execute();
    }

    public static void uLogin(String name, String password) {
        new CLogin(name, password).execute();
    }

    public static void uDisconnect() {
        new CDisconnect().execute();
    }

    public static void uSignup(String name, String password) {
        new CSignup(name, password).execute();
    }

    public static void uLogout() {
        new CLogout().execute();
    }

    public static void uLocation(LatLng latLng) {
        new CLocation(latLng).execute();
    }
    public static void uTurn(Player.Type type){
        new CTurn(type).execute();
    }
    public static void uPlayers() {
        new CPlayers().execute();
    }

    public static void sConnect(String message) {
        new SConnect(message).execute();
    }

    public static void sError(String message) {
        new SError(message).execute();
    }

    public static void sLogin(boolean login_success, String message) {
        new SLogin(login_success, message).execute();
    }

    public static void sSignup(boolean signup_success, String message) {
        new SSignup(signup_success, message).execute();
    }

    public static void sLogout(boolean logout_success, String message) {
        new SLogout(logout_success, message).execute();
    }

    public static void sMessage(String message) {
        new SMessage(message).execute();
    }
    public static void sPlayer(Player.Type type, String name, LatLng latLng) {
        new SPlayer(type, name, latLng).execute();
    }
    public static void sysFragment(FragmentPicker fragmentPicker) {
        new SysFragment(fragmentPicker).execute();
    }

    public static void sysMessage(SystemMessage message) {
        new SysMessage(message).execute();
    }
}
