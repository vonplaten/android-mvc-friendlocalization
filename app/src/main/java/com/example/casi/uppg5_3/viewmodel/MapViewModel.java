package com.example.casi.uppg5_3.viewmodel;


import com.example.casi.uppg5_3.service.model.LoginStatus;
import com.example.casi.uppg5_3.service.model.Message;
import com.example.casi.uppg5_3.service.model.Player;
import com.example.casi.uppg5_3.service.repository.Repository;

import java.util.List;


import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

@Singleton
public class MapViewModel extends ViewModel {

    private Repository repository;
    private LiveData<List<Player>> playerListObservable;
    private LiveData<Message> messageObservable;
    private LiveData<LoginStatus> loginStatusObservable;


    @Inject
    public MapViewModel(@NonNull Repository repo) {
        this.repository = repo;
        playerListObservable = repository.getPlayerListObservable();
        messageObservable = repository.getMessageObervable();
        loginStatusObservable = repository.getLoginStatusObservable();
    }

    public LiveData<List<Player>> getPlayerList(){
        return playerListObservable;
    }
    public LiveData<Message> getMessageObservable(){
        return messageObservable;
    }
    public LiveData<LoginStatus> getLoginStatusObservable(){
        return loginStatusObservable;
    }
}
