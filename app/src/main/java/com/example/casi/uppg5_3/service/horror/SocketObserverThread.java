package com.example.casi.uppg5_3.service.horror;

import android.os.Looper;

import com.example.casi.uppg5_3.service.model.ConnectionStatus;

import java.net.Socket;

public class SocketObserverThread extends Thread {
    SocketListener _socketListener;
    Socket _socket;
    private boolean quit = false;


    public SocketObserverThread(SocketListener socketListener, Socket socket) {
        _socket = socket;

        this._socketListener = socketListener;
    }

    @Override
    public void run() {
        //Check if valid thread.
        try {
            if (Looper.getMainLooper().getThread() == Thread.currentThread())
                throw new Exception("ERROR: SocketObserverThread is on UI/Main");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (! quit) {
            if (_socket.isClosed() && _socket.isConnected()) {
                _socketListener.onConnectionStatus(new ConnectionStatus(null, false, "SocketObserverThread->run()"));
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void please_quit(){
        quit = true;
    }
}


