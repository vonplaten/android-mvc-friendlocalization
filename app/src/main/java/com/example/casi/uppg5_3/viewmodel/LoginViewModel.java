package com.example.casi.uppg5_3.viewmodel;

import com.example.casi.uppg5_3.service.model.ConnectionStatus;
import com.example.casi.uppg5_3.service.model.LoginStatus;
import com.example.casi.uppg5_3.service.model.Message;
import com.example.casi.uppg5_3.service.model.ServerMessage;
import com.example.casi.uppg5_3.service.model.SignupStatus;
import com.example.casi.uppg5_3.service.repository.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

@Singleton
public class LoginViewModel extends ViewModel {

    private Repository repository;

    private LiveData<ConnectionStatus> connectionStatusObservable;
    private LiveData<Message> messageObservable;
    private LiveData<SignupStatus> signupStatusObservable;
    private LiveData<LoginStatus> loginStatusObservable;

    @Inject
    public LoginViewModel(@NonNull Repository repository) {
        this.repository = repository;
        connectionStatusObservable = repository.getConnectionStatusObservable();
        messageObservable = repository.getMessageObervable();
        signupStatusObservable = repository.getSignupStatusObservable();
        loginStatusObservable = repository.getLoginStatusObservable();
    }
    public LiveData<ConnectionStatus> getConnectionStatus(){
        return connectionStatusObservable;
    }
    public LiveData<Message> getMessageObservable(){
        return messageObservable;
    }
    public LiveData<SignupStatus> getSignupStatusObervable(){
        return signupStatusObservable;
    }
    public LiveData<LoginStatus> getLoginStatusObservable(){
        return loginStatusObservable;
    }

}
