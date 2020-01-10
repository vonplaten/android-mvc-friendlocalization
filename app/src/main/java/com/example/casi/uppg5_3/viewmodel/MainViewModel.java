package com.example.casi.uppg5_3.viewmodel;

import com.example.casi.uppg5_3.service.model.FragmentPicker;
import com.example.casi.uppg5_3.service.model.Player;
import com.example.casi.uppg5_3.service.repository.Repository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

@Singleton
public class MainViewModel extends ViewModel {
    private Repository repository;
    private LiveData<FragmentPicker> fragmentPickerObservable;


    @Inject
    public MainViewModel(@NonNull Repository repo) {
        this.repository = repo;
        fragmentPickerObservable = repository.getFragmentPickerObservable();
    }

    public LiveData<FragmentPicker> getFragmentPickerObservable(){
        return fragmentPickerObservable;
    }
}
