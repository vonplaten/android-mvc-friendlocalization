package com.example.casi.uppg5_3.service.horror;

import android.os.AsyncTask;
import android.util.Log;

import com.example.casi.uppg5_3.service.model.ConnectionStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketCreatorAsync extends AsyncTask<Void, Void, Socket> {

    private static final String TAG = "HorrorSocketCreatorAsyn";
    private SocketListener _listener;
    private String _server;
    private int _port;

    public SocketCreatorAsync(SocketListener listener, String server, int port) {
        _listener = listener;
        _server = server;
        _port = port;
    }

    @Override
    protected Socket doInBackground(Void... voids) {
        Socket socket = null;
        InetSocketAddress addr = new java.net.InetSocketAddress(_server, _port);
        try {
            socket = new Socket();
            socket.connect(addr, 5000);
        } catch (UnknownHostException e) {
            Log.d(TAG, "Unknown server name: " + addr);
            _listener.onConnectionStatus(new ConnectionStatus(
                    "Unknown server name: " + addr,
                    false,
                    "SocketCreatorAsync->doInBackground()"));
        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    // Error when closing the socket. Not much we can or should do here, so do nothing.
                }
            }
            Log.d(TAG, "Couldn't connect to " + addr);
            _listener.onConnectionStatus(new ConnectionStatus(
                    "Couldn't connect to " + addr,
                    false,
                    "SocketCreatorAsync->doInBackground()"));
        }
        return socket;
    }

    @Override
    protected void onPostExecute(Socket socket) {
        if (socket != null && socket.isConnected()) {
            _listener.onSocketReady(socket);
        }
    }



}

