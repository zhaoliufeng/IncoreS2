package com.ws.mesh.incores2.constant;

public enum TimingType {
    SCENE(0), DEVICE(1), ZONE(2);

    private final int value;

    TimingType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
