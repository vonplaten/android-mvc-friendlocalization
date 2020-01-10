package com.example.casi.uppg5_3.service.repository.action;

import com.example.casi.uppg5_3.service.model.Player;
import com.google.android.gms.maps.model.LatLng;

class SPlayer extends ServerAction {
    Player.Type type;
    private String name;
    private LatLng latLng;

    public SPlayer(Player.Type type, String name, LatLng latLng) {
        this.type = type;
        this.name = name;
        this.latLng = latLng;
    }

    @Override
    public void run() {
        Player player = new Player(type, name, latLng);
        if (repository.players.contains(player)) {
            repository.players.remove(player);
        }
        if (!type.equals(Player.Type.GONE))
            repository.players.add(player);
        repository.getPlayerListObservable().postValue(repository.players);
    }
}
