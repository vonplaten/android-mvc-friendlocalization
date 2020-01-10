package com.example.casi.uppg5_3.service.repository.action;


import com.example.casi.uppg5_3.service.model.FragmentPicker;
import com.google.android.gms.maps.model.LatLng;

class CLocation extends ClientAction {
    private LatLng latLng;
    private String command;

    public CLocation(LatLng latLng) {
        super(true, true);
        this.latLng = latLng;
        int command_number = 0;
        String latitude = String.valueOf(latLng.latitude);
        String longitude = String.valueOf(latLng.longitude);
        this.command = ++command_number + " " + "I-AM-AT " + latitude + " " + longitude;
    }

    @Override
    public void run() {
        repository.outMessage(command);

    }

    @Override
    public void requires() {
        //Action.sysFragment(new FragmentPicker(FragmentPicker.PickCode.LOGIN_FRAGMENT));
    }
}
