package com.example.casi.uppg5_3;

import android.app.Application;

import com.example.casi.uppg5_3.di.AppComponent;
import com.example.casi.uppg5_3.di.DaggerAppComponent;


public class HorrorApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().build();

    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }
}
