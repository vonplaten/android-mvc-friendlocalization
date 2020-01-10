package com.example.casi.uppg5_3.di;


import com.example.casi.uppg5_3.service.repository.Repository;
import com.example.casi.uppg5_3.service.repository.action.ActionQueue;
import com.example.casi.uppg5_3.viewmodel.LoginViewModel;
import com.example.casi.uppg5_3.viewmodel.MainViewModel;
import com.example.casi.uppg5_3.viewmodel.MapViewModel;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface AppComponent {
    Repository getRepository();
    ActionQueue getActionQueue();

    LoginViewModel getLoginViewModel();
    MapViewModel getMapViewModel();
    MainViewModel getMainViewModel();
}
