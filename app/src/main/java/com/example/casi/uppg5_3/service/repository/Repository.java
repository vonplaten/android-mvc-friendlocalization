package com.example.casi.uppg5_3.service.repository;

import android.util.Log;

import com.example.casi.uppg5_3.service.horror.Connection;
import com.example.casi.uppg5_3.service.model.ConnectionStatus;
import com.example.casi.uppg5_3.service.model.FragmentPicker;
import com.example.casi.uppg5_3.service.model.LoginStatus;
import com.example.casi.uppg5_3.service.model.Message;
import com.example.casi.uppg5_3.service.model.Player;
import com.example.casi.uppg5_3.service.model.ServerMessage;
import com.example.casi.uppg5_3.service.model.SignupStatus;
import com.example.casi.uppg5_3.service.repository.message.MessageIn;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@Singleton
public class Repository implements PropertyChangeListener {

    private static final String TAG = "Repository";
    public Connection connection;
    public ConnectionStatus connectionStatus;
    public LoginStatus loginStatus;
    public List<Player> players;

    //LIVE data
    private MutableLiveData<List<Player>> playerListObservable;
    private MutableLiveData<ConnectionStatus> connectionStatusObservable;
    private MutableLiveData<SignupStatus> signupStatusObservable;
    private MutableLiveData<Message> messageObservable;
    private MutableLiveData<LoginStatus> loginStatusObervable;
    private MutableLiveData<FragmentPicker> fragmentPickerObservable;

    @Inject
    public Repository(@NonNull Connection con) {
        connection = con;
        connectionStatus = new ConnectionStatus(null, false, null);
        loginStatus = new LoginStatus(false);
        connection.addPropertyChangeListener(this);
        players = new ArrayList<>();

        connectionStatusObservable = new MutableLiveData<>();
        messageObservable = new MutableLiveData<>();
        signupStatusObservable = new MutableLiveData<>();
        loginStatusObervable = new MutableLiveData<>();
        playerListObservable = new MutableLiveData<>();
        fragmentPickerObservable = new MutableLiveData<>();
    }


    public MutableLiveData<List<Player>> getPlayerListObservable() {
        return playerListObservable;
    }

    public MutableLiveData<ConnectionStatus> getConnectionStatusObservable() {
        return connectionStatusObservable;
    }

    public MutableLiveData<Message> getMessageObervable() {
        return messageObservable;
    }

    public MutableLiveData<SignupStatus> getSignupStatusObservable() {
        return signupStatusObservable;
    }

    public MutableLiveData<LoginStatus> getLoginStatusObservable() {
        return loginStatusObervable;
    }

    public MutableLiveData<FragmentPicker> getFragmentPickerObservable() {
        return fragmentPickerObservable;
    }


    public void outMessage(String m) {
        connection.sendMessage(m);
        Log.d(TAG, "->outMessage(): " + m);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("ConnectionStatus")) {
            ConnectionStatus cs = (ConnectionStatus) evt.getNewValue();
            connectionStatus = cs;
            //Log.d(TAG, "->propertyChange(): ConnectionStatus: " + connectionStatus.isConnected());
            connectionStatusObservable.postValue(cs);
        }
        if (evt.getPropertyName().equals("MessageIn")) {
            MessageIn m = (MessageIn) evt.getNewValue();
            //Log.d(TAG, "->PropertyChange(): MessageIn: LineNumber: " + m.getLineNr() + " Message: " + m.getLine_from_server());
            m.process();
        }
    }


//    public void setZombie(boolean val) {
//
//    }
//
//    public void forget_one_player(String name) {
//
//    }
//
//    public void new_player_status(String name, boolean is_zombie, double latitude, double longitude) {
//
//    }
//
//    public String get_actual_login_name() {
//        return null;
//    }
//
//    public void setLoggedIn(String logged_in_user_name) {
//
//    }
//
//    public void forget_all_players() {
//
//    }
//

}
