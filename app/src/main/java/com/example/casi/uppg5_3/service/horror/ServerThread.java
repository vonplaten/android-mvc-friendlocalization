package com.example.casi.uppg5_3.service.horror;

import android.os.Looper;

import com.example.casi.uppg5_3.service.model.ConnectionStatus;
import com.example.casi.uppg5_3.service.repository.message.MessageIn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private final ServerListener _listener;
    private final Socket socket;
    private final BufferedReader from_server;
    private final PrintWriter to_server;
    private boolean quit = false;
    public int received_lines = 0;
    private int command_number = 0;

    public ServerThread(ServerListener listener, Socket socket, BufferedReader from_server, PrintWriter to_server) {
        this._listener = listener;
        this.socket = socket;
        this.from_server = from_server;
        this.to_server = to_server;
    }

    // public ClientListenerThread(MainServerThread _listener,

    public void run() {
        //Check if valid Thread
        try {
            if (Looper.getMainLooper().getThread() == Thread.currentThread())
                throw new Exception("ERROR: ServerThread is on UI/Main");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (! quit) {
            String line_from_server;
            try {
                line_from_server = from_server.readLine();
                if (line_from_server == null) {
                    _listener.onServerMessage(new MessageIn(
                            received_lines,
                            "ServerThread->run(): No data from server",
                            MessageIn.Type.ERROR_MESSAGE));
                    please_quit();
                }
                else {
                    received_lines++;
                    _listener.onServerMessage(new MessageIn(
                            received_lines,
                            line_from_server,
                            MessageIn.Type.SERVER_MESSAGE));
                }
            }
            catch (IOException e) {
                _listener.onServerMessage(new MessageIn(
                        received_lines,
                        "ServerThread->run()->IOException: " + e.getMessage(),
                        MessageIn.Type.ERROR_MESSAGE));
                _listener.onServerMessage(new MessageIn(
                        received_lines,
                        "Disconnected",
                        MessageIn.Type.INFO_MESSAGE));
            }
        }
        close_everything();
    }

    public void please_quit() {
        quit = true;
        close_everything();
        _listener.onConnectionStatus(
                new ConnectionStatus(
                        "Connection is shut down.",
                        false,
                        "ServerThread->please_quit()"
                )
        );
    }

    public void send_line(String line) {
        to_server.println(line);
    }

    private void close_everything() {
        to_server.close();
        try {
            from_server.close();
        }
        catch (IOException e) {
            // Just ignore that we couldn't close the BufferedReader
        }
        try {
            socket.close();
        }
        catch (IOException e) {
            // Just ignore that we couldn't close the socket
        }
        received_lines = 0;
    }

}
