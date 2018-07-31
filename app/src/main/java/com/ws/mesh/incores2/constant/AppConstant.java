package com.ws.mesh.incores2.constant;

public class AppConstant {

    public static final String SQL_NAME = "IncoreS2";
    public static final String ID = "_id";

    public static final String MESH_DEFAULT_NAME = "Fulife";
    public static final String MESH_DEFAULT_PASSWORD = "2846";
    public static final String DEFAULT_EDIT_PASSWORD = "888888";
    //默认网络名称
    public static final String DEFAULT_MESH_NAME = "MyHome";
    public static final int DEFAULT_TYPE = 0xA0FF;
    public static final String DEVICE_DEFAULT_NAME = "FLEXLINK";

    //设备类型 强波器
    //强波器
    public static final int FORM_LIGHT_BOOSTER = 0x13;

    public static final int ZONE_MAX_NUM = 255;
    public static final int ZONE_START_ID = 0x8001;
    public static final long DAY_TIME = 1000L * 24 * 60 * 60;

    //闹钟所属设备类型
    public static final int ALARM_TYPE_GROUP = 2;
    public static final int ALARM_TYPE_DEVICE = 1;
    public static final int ALARM_TYPE_SCENE = 0;

    public static final byte SCENE_ALARM = (byte) 0x92;
    //关闭闹钟
    public static final byte OFF_ALARM = (byte) 0x90;
    //开启闹钟
    public static final byte ON_ALARM = (byte) 0x91;
    //日模式的場景鬧鐘
    public static final byte DAY_SCENE_ALARM = (byte) 0x82;
    //日模式的关闭鬧鐘
    public static final byte DAY_OFF_ALARM = (byte) 0x80;
    //日模式的打开鬧鐘
    public static final byte DAY_ON_ALARM = (byte) 0x81;
}
