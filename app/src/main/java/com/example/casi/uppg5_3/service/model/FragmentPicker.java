package com.example.casi.uppg5_3.service.model;

public class FragmentPicker{
    private Enum pick;

    public FragmentPicker(Enum pick) {
        this.pick = pick;
    }

    public Enum getPick() {
        return pick;
    }

    public enum PickCode {
        LOGIN_FRAGMENT,
        MAP_FRAGMENT

    }
}

