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

    /**
     * 设备通道留作备用
     */
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

    /**判断该设备是否设置过昼节律,夜节律
     * 默认值为0
     * 10 2 设置了夜节律没有设置昼节律**/
    @DBFiled
    public int symbolStr;

    public void setSunrise(boolean isOpen){
        //将之前的symbolStr的sunrise值置0
        symbolStr = (symbolStr & 0x02) + (isOpen ? 1 : 0);
    }

    public void setSunset(boolean isOpen){
        symbolStr = (symbolStr & 0x01) + (isOpen ? 2 : 0);
    }

    public boolean isSetSunrise(){
        return (symbolStr & 0x01) == 0x01;
    }

    public boolean isSetSunset(){
        return (symbolStr & 0x02) == 0x02;
    }

    //Circadian
    public static String circadianToString(Circadian circadian) {
        if (circadian == null) return "";
        return JSON.toJSONString(circadian);
    }

    public static Circadian stringToCircadian(String circadian){
        return JSON.parseObject(circadian, Circadian.class);
    }
}
