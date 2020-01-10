package com.example.casi.uppg5_3.service.repository.message;

import android.util.Log;

import com.example.casi.uppg5_3.service.model.FragmentPicker;
import com.example.casi.uppg5_3.service.model.Player;
import com.example.casi.uppg5_3.service.model.SystemMessage;
import com.example.casi.uppg5_3.service.repository.action.Action;
import com.google.android.gms.maps.model.LatLng;


public class MessageIn {
    private static final String TAG = "MessageIn";

    private int received_lines;
    private String line_from_server;
    private Type message_type;

    public MessageIn(int received_lines, String line_from_server, Type type) {
        this.received_lines = received_lines;
        this.line_from_server = line_from_server;
        this.message_type = type;
    }


    public enum Type{
        INFO_MESSAGE,
        SERVER_MESSAGE,
        ERROR_MESSAGE
    }
    public void process(){
        switch(message_type){
            case INFO_MESSAGE:
                Action.sysMessage(new SystemMessage(line_from_server, "MessageIn->process()"));
                break;
            case ERROR_MESSAGE:
                Log.d(TAG, "ERROR_MESSAGE: " + line_from_server);
                break;
            case SERVER_MESSAGE:
                Log.d(TAG, "SERVER_MESSAGE: LineNumber: " + received_lines + " Message: " + line_from_server);
                process_line();
                break;
        }
    }
    private void process_line() {
        if (received_lines == 1) {
            if (!line_from_server.startsWith("ZombieServer ")) {
                //Log.d(TAG, "->process_line(): This doesn't seem to be a zombie-game server. It said: " + line_from_server);
                Action.sConnect("This doesn't seem to be a zombie-game server. It said: " + line_from_server);
//                _repository.onConnectionStatus(new ConnectionStatus(
//                        "This doesn't seem to be a zombie-game server. It said: " + line_from_server,
//                        false, "MessageIn->process_line")
//                );
            } else {
                String server_version = line_from_server.substring(13);
                //Log.d(TAG, "->process_line(): Connected to a zombie-game server, server version " + server_version);
                Action.sConnect("Connected to a zombie-game server, server version " + server_version);
//                _repository.onConnectionStatus(new ConnectionStatus(
//                        "Connected to a zombie-game server, server version " + server_version,
//                        true, "MessageIn->process_line")
//                );
            }
        } else if (line_from_server.equals("ERROR MALFORMED-COMMAND")) {
            //Log.d(TAG, "->process_line(): The server sent an error message: " + line_from_server);
            Action.sError("The server sent an error message: " + line_from_server);
        } else if (line_from_server.startsWith("ASYNC ")) {
            // Handle an asynchronous message from the server
            int length = line_from_server.length();
            int position = 6; // After "ASYNC "
            while (position < length && Character.isWhitespace(line_from_server.charAt(position)))
                ++position;
            int command_start = position;
            while (position < length && !Character.isWhitespace(line_from_server.charAt(position)))
                ++position;

            String command = line_from_server.substring(command_start, position);

            while (position < length && Character.isWhitespace(line_from_server.charAt(position)))
                ++position;
            int arguments_start = position;
            String arguments = line_from_server.substring(arguments_start, length);
            handle_async_command(command, arguments, line_from_server);
        } else {
            // Handle a (synchronous) reply from the server
            int length = line_from_server.length();
            int position = 0;
            while (position < length && Character.isWhitespace(line_from_server.charAt(position)))
                ++position;
            int request_number_start = position;
            while (position < length && Character.isDigit(line_from_server.charAt(position)))
                ++position;
            String request_number_string = line_from_server.substring(request_number_start, position);
            if (request_number_string.length() == 0) {
                Log.d(TAG, "->process_line(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
                return;
            }
            int request_number = Integer.parseInt(request_number_string); // We already know it's just digits

            while (position < length && Character.isWhitespace(line_from_server.charAt(position)))
                ++position;
            int command_start = position;
            // while (position < length && Character.isLetter(line_from_server.charAt(position)))
            while (position < length && !Character.isWhitespace(line_from_server.charAt(position)))
                ++position;
            String command = line_from_server.substring(command_start, position);
            if (command.equals("ERROR")) {
                Log.d(TAG, "->process_line(): The server sent an error message: " + line_from_server);
                Action.sError("The server sent an error message: " + line_from_server);
                if (line_from_server.endsWith("NOT-LOGGED-IN")){
                    //Action.sysFragment(new FragmentPicker(FragmentPicker.PickCode.LOGIN_FRAGMENT));
                    Action.sysMessage(new SystemMessage("You must log in to play", "MessageIn->handle_async_command()"));
                }
            } else {
                while (position < length && Character.isWhitespace(line_from_server.charAt(position)))
                    ++position;
                int arguments_start = position;
                String arguments = line_from_server.substring(arguments_start, length);
                handle_command(request_number, command, arguments, line_from_server);
            }
        }

    } // received_line

    private void handle_async_command(String command, String arguments, String line_from_server) {
        final String split_arguments[] = split_arguments(arguments);
        if (command.equals("YOU-ARE")) {
            if (split_arguments.length != 1) {
                Log.d(TAG, "->handle_async_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else if (split_arguments[0].equals("ZOMBIE")) {
                /////////////////////////////////////////////////////////////////
//                _repository.setZombie(true);
                Log.d(TAG, "->handle_async_command(): Oh no! You are a zombie!");
                Action.sMessage("Oh no! You are a zombie!");
            } else if (split_arguments[0].equals("HUMAN")) {
                /////////////////////////////////////////////////////////////////
//                _repository.setZombie(false);
                Log.d(TAG, "->handle_async_command(): Great! You are human!");
                Action.sMessage("Great! You are human!");
            } else {
                Log.d(TAG, "->handle_async_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            }
        } else if (command.equals("PLAYER")) {
            if (split_arguments.length == 2 && split_arguments[1].equals("GONE")) {
                // That player is no longer visible
                String name = split_arguments[0];
                //Forget one player
                Action.sPlayer(Player.Type.GONE, name, new LatLng(0,0));
            } else if (split_arguments.length == 4) {
                String name = split_arguments[0];
                String status = split_arguments[1];
                String latitude_string = split_arguments[2];
                String longitude_string = split_arguments[3];
                boolean is_zombie = status.equals("ZOMBIE"); // Trust the server, assume it is either ZOMBIE or HUMAN
                double latitude = Double.parseDouble(latitude_string); // Trust the server, assume it is a valid number
                double longitude = Double.parseDouble(longitude_string); // Trust the server, assume it is a valid number
                //Update or add Player
                if (is_zombie)
                    Action.sPlayer(Player.Type.ZOMBIE,name, new LatLng(latitude, longitude));
                else
                    Action.sPlayer(Player.Type.HUMAN,name, new LatLng(latitude, longitude));
                Action.sysMessage(new SystemMessage("Updated", "MessageIn->handle_async_command()"));
            } else {
                Log.d(TAG, "->handle_async_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            }
        } else {
            Log.d(TAG, "->handle_async_command(): What?! The server sent an incorrect message: " + line_from_server);
            Action.sError("What?! The server sent an incorrect message: " + line_from_server);
        }
    }

    private String[] split_arguments(String arguments) {
        String[] result = arguments.split("[\t ]+");
        if (result.length == 1 && result[0].equals("")) {
            result = new String[0];
        }
        return result;
    }

    private void handle_command(int request_number, String command, String arguments, String line_from_server) {
        final String split_arguments[] = split_arguments(arguments);
        if (command.equals("REGISTERED")) {
            if (split_arguments.length != 1) {
                Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else {
                //Log.d(TAG, "->handle_command(): You have been registered as player number " + split_arguments[0]);
                Action.sSignup(true, "You have been registered as player number " + split_arguments[0]);
            }
        } else if (command.equals("WELCOME")) {
            if (split_arguments.length != 1) {
                //Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else {
                //Log.d(TAG, "->handle_command(): You have been logged in as player number " + split_arguments[0]);
                Action.sLogin(true, "You have been logged in as player number " + split_arguments[0]);
            }
        } else if (command.equals("GOODBYE")) {
            if (split_arguments.length != 0) {
                //Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else {
                /////////////////////////////////////////////////////////////////
//                _repository.setLoggedIn(null);
                Log.d(TAG, "->handle_command(): You have been logged out.");
                Action.sLogout(true, "You have been logged out.");
            }
        } else if (command.equals("OK")) {
            // Generic reply
            if (split_arguments.length != 0) {
                Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else {
                Log.d(TAG, "->handle_command(): The server says OK.");
                Action.sMessage("The server says OK");
            }
        } else if (command.equals("YOU-ARE")) {
            if (split_arguments.length != 1) {
                Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else if (split_arguments[0].equals("HUMAN")) {
                Action.sMessage("You are a human.");
                Log.d(TAG, "->handle_command(): You are a human.");
            } else if (split_arguments[0].equals("ZOMBIE")) {
                Action.sMessage("You are a zombie.");
                Log.d(TAG, "->handle_command(): You are a zombie.");
            } else {
                Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            }
        } else if (command.equals("YOU-ARE-AT")) {
            if (split_arguments.length != 2) {
                Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else {
                Action.sMessage("Your position: lat " + split_arguments[0] + ", long " + split_arguments[1]);
                Log.d(TAG, "->handle_command(): Your position: lat " + split_arguments[0] + ", long " + split_arguments[1]);
            }
        } else if (command.equals("VISIBLE-PLAYERS")) {
            // This is a reply to a "LIST-VISIBLE-PLAYERS" command. A list of players will follow.
            if (split_arguments.length != 2) {
                Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else {
                // Reset player list
                /////////////////////////////////////////////////////////////////
//                _repository.forget_all_players();

                //Not implemented
            }
        } else if (command.equals("PLAYER")) {
            // This is part of the player list after a "VISIBLE_PLAYERS" reply to a "LIST-VISIBLE-PLAYERS" command
            if (split_arguments.length != 4) {
                Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
                Action.sError("What?! The server sent an incorrect message: " + line_from_server);
            } else {
                // Add to player list
                String name = split_arguments[0];
                String status = split_arguments[1];
                String latitude_string = split_arguments[2];
                String longitude_string = split_arguments[3];
                boolean is_zombie = status.equals("ZOMBIE"); // Trust the server, assume it is either ZOMBIE or HUMAN
                double latitude = Double.parseDouble(latitude_string); // Trust the server, assume it is a validnumber
                double longitude = Double.parseDouble(longitude_string); // Trust the server, assume it is a validnumber
                if (is_zombie)
                    Action.sPlayer(Player.Type.ZOMBIE,name, new LatLng(latitude, longitude));
                else
                    Action.sPlayer(Player.Type.HUMAN,name, new LatLng(latitude, longitude));
            }
        } else {
            Log.d(TAG, "->handle_command(): What?! The server sent an incorrect message: " + line_from_server);
            Action.sError("What?! The server sent an incorrect message: " + line_from_server);
        }
    } // handle_command

    @Override
    public String toString() {
        return "MessageIn->toString(): " + line_from_server + "   TYPE: " + message_type;
    }

}
