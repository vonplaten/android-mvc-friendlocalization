package com.example.casi.uppg5_3.service.horror;


import android.util.Log;

import com.example.casi.uppg5_3.service.model.ConnectionStatus;
import com.example.casi.uppg5_3.service.repository.message.MessageIn;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Connection implements SocketListener, ServerListener {
    private static final String TAG = "Connection";
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    //THREADS
    private  ServerThread serverThread;
    private  SocketObserverThread socketObserverThread;

    @Inject
    public Connection() {
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void connect(String adress, int port) {


        //first shut down any existing threads
        try {
            this.disconnect();

        } catch (NullPointerException e) {
        }

        //then make new connection
        SocketCreatorAsync socketCreator = new SocketCreatorAsync(this, adress, port);
        socketCreator.execute();

    }

    public void disconnect() {
        socketObserverThread.please_quit();
        serverThread.please_quit();
    }


    @Override
    public void onSocketReady(Socket socket) {
        //Create thread 1
        socketObserverThread = new SocketObserverThread(this, socket);
        socketObserverThread.start();
        //Create thread 2
        try {
            BufferedReader from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter to_server = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            serverThread = new ServerThread(this, socket, from_server, to_server);
            serverThread.start();
        } catch (IOException e) {
            Log.d("sunk", "Error starting horror server.");
            e.printStackTrace();
        }
        onConnectionStatus(new ConnectionStatus("Connected to " + socket.getInetAddress(), true, "Connection->onSocketReady()"));
    }

    public void sendMessage(String m) {
        serverThread.send_line(m);
    }

    @Override
    public void onConnectionStatus(ConnectionStatus s) {
        //Log.d(TAG, "->onConnectionStatus()");
        propertyChangeSupport.firePropertyChange("ConnectionStatus", null, s);
    }


    @Override
    public void onServerMessage(MessageIn m) {
        propertyChangeSupport.firePropertyChange("MessageIn", null, m);
    }


}


