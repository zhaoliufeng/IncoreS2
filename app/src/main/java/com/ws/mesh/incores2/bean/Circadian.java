package com.ws.mesh.incores2.bean;

/**
 * 昼夜节律
 */
public class Circadian {
    //Day开始时
    public int dayStartHours = -1;
    //Day开始分
    public int dayStartMinutes = -1;
    //Day持续时间
    public int dayDurTime = 0;
    //Day模式是否开始
    public boolean isDayOpen = false;
    //Night开始时
    public int nightStartHours = -1;
    //Night开始分
    public int nightStartMinutes = -1;
    //Night持续时间
    public int nightDurTime = 0;
    //Night模式是否开始
    public boolean isNightOpen = false;
}
