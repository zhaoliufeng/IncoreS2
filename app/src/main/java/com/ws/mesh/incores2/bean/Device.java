package com.ws.mesh.incores2.bean;

import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.telink.bluetooth.light.ConnectionStatus;
import com.we_smart.sqldao.Annotation.DBFiled;
import com.ws.mesh.incores2.utils.DeviceParamsDeal;

import java.util.List;

/**
 * Created by we_smart on 2017/11/15.
 */

public class Device {

    //设备的meshId
    @DBFiled
    public int mDevMeshId;
    //设备名称
    @DBFiled
    public String mDevName;

    //群组所属网络名
    @DBFiled
    public String mDeviceMeshName;

    //设备类型
    @DBFiled
    public int mDevType;

    //固件版本
    @DBFiled
    public String mFirmwareVersion;

    //设备的MAC地址
    @DBFiled
    public String mDevMacAddress;

    //设备通道1
    @DBFiled
    public String mChannelOne;

    //设备通道2
    @DBFiled
    public String mChannelTwo;

    //设备通道3
    @DBFiled
    public String mChannelThree;

    //设备通道4
    @DBFiled
    public String mChannelFour;

    //设备通道5
    @DBFiled
    public String mChannelFive;

    public Circadian circadian;

    @DBFiled
    public String circadianString;

    public int mIconRes;

    //设备状态
    public ConnectionStatus mConnectionStatus = ConnectionStatus.OFFLINE;

    //亮度
    public int mBrightness;

    public void updateIcon() {
        mIconRes = DeviceParamsDeal.getDeviceIcon(this);
    }

    //Circadian
    public static String circadianToString(Circadian circadian) {
        if (circadian == null) return "";
        return JSON.toJSONString(circadian);
    }

    public static Circadian stringToCircadian(String circadian){
        return (Circadian) JSON.parse(circadian);
    }

}
