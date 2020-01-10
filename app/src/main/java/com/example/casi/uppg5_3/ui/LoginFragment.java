package com.example.casi.uppg5_3.ui;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.casi.uppg5_3.HorrorApplication;
import com.example.casi.uppg5_3.R;
import com.example.casi.uppg5_3.di.AppComponent;
import com.example.casi.uppg5_3.di.DaggerAppComponent;
import com.example.casi.uppg5_3.ui.util.ProgressbarAsync;
import com.example.casi.uppg5_3.ui.util.ProgressbarListener;
import com.example.casi.uppg5_3.viewmodel.LoginViewModel;

import javax.inject.Inject;

import com.example.casi.uppg5_3.service.repository.action.Action;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements ProgressbarListener {

    private static final String TAG = "LoginFragment";
    private TextView connectionStatus, serverMessage;
    private EditText editUsername, editPassword, editTextAddress, editTextPort;
    private Button buttonConnect, buttonDisconnect, buttonSignup, buttonLogin, buttonLogout;
    private ProgressBar progressBar;


    LoginViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //ELEMENTS
        editUsername = view.findViewById(R.id.userEditText);
        editPassword = view.findViewById(R.id.userPasswordText);
        editTextAddress = (EditText) view.findViewById(R.id.addressEditText);
        editTextPort = (EditText) view.findViewById(R.id.portEditText);
        buttonConnect = (Button) view.findViewById(R.id.connectButton);
        buttonDisconnect = (Button) view.findViewById(R.id.disconnectButton);
        buttonLogin = (Button) view.findViewById(R.id.loginButton);
        buttonLogout = (Button) view.findViewById(R.id.logoutButton);
        buttonSignup = (Button) view.findViewById(R.id.signupButton);
        //buttonDisconnect.setEnabled(false);
        TextView _error_msg = (TextView) view.findViewById(R.id.responseTextView);
        progressBar = view.findViewById(R.id.progressbar);
        connectionStatus = view.findViewById(R.id.connectionStatus);
        serverMessage = view.findViewById(R.id.serverMessage);
        setButtonStatus(false);
        viewModel = HorrorApplication.getAppComponent().getLoginViewModel();
//        //ViewModel
//        AppComponent component = DaggerAppComponent.create();
//        viewModel = component.getLoginViewModel();

        //Observe connection
        viewModel.getConnectionStatus().observe(this, status -> {
            if (status.getText() != null)
                this.connectionStatus.setText(status.getText());
            this.connectionStatus.setTextColor(status.getTextColor());
            setButtonStatus(status.isConnected());
        });

        //Observe messages
        viewModel.getMessageObservable().observe(this, message -> {
            if (message.getMessage() != null)
                serverMessage.setText(message.getMessage());
        });

        //Observe signup
        viewModel.getSignupStatusObervable().observe(this, signup -> {
            buttonSignup.setEnabled(!signup.isSignedup());
        });

        //Observe login
        viewModel.getLoginStatusObservable().observe(this, login -> {
            buttonLogin.setEnabled(!login.isLoggedin());
            buttonLogout.setEnabled(login.isLoggedin());
            buttonSignup.setEnabled(!login.isLoggedin());
        });

        buttonConnect.setOnClickListener((v) -> {
            ProgressbarAsync progressbarAsync = new ProgressbarAsync(this);
            progressbarAsync.execute(2);
            Action.uConnect(editTextAddress.getText().toString(),
                    Integer.parseInt(editTextPort.getText().toString()
                    ));
        });
        buttonDisconnect.setOnClickListener((v) -> {
            ProgressbarAsync progressbarAsync = new ProgressbarAsync(this);
            progressbarAsync.execute(2);
            Action.uDisconnect();
        });
        buttonSignup.setOnClickListener((v) -> {
            ProgressbarAsync progressbarAsync = new ProgressbarAsync(this);
            progressbarAsync.execute(2);
            Action.uSignup(editUsername.getText().toString(), editPassword.getText().toString());
        });
        buttonLogin.setOnClickListener((v) -> {
            ProgressbarAsync progressbarAsync = new ProgressbarAsync(this);
            progressbarAsync.execute(2);
            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("username", editUsername.getText().toString());
            editor.apply();
            Action.uLogin(editUsername.getText().toString(), editPassword.getText().toString());
        });
        buttonLogout.setOnClickListener((v) -> {
            ProgressbarAsync progressbarAsync = new ProgressbarAsync(this);
            progressbarAsync.execute(2);
            Action.uLogout();
        });


        return view;
    }

    private void setButtonStatus(boolean connected){
        if(connected) {
            buttonConnect.setEnabled(false);
            buttonDisconnect.setEnabled(true);
            buttonSignup.setEnabled(true);
            buttonLogin.setEnabled(true);
            buttonLogout.setEnabled(true);
        }else{
            buttonConnect.setEnabled(true);
            buttonDisconnect.setEnabled(false);
            buttonSignup.setEnabled(false);
            buttonLogin.setEnabled(false);
            buttonLogout.setEnabled(false);
        }
    }

    @Override
    public void onProgressbarFinished() throws InterruptedException {
        //Toast.makeText(getActivity(), "Finished", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean isFinishing() {
        return isRemoving();
    }

    @Override
    public void setProgressbarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void setProgressbarProgress(Integer progress) {
        progressBar.setProgress(progress);
    }
}
