package com.ws.mesh.incores2.constant;

public enum TimingType {
    DEVICE(0), ZONE(1), SCENE(2);

    private final int value;

    TimingType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
