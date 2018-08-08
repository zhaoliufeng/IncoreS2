package com.ws.mesh.incores2.constant;

public enum SceneMode {

    ON(0), OFF(1), PALETTE(2);

    private final int value;
    SceneMode(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
