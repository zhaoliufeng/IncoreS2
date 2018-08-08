package com.ws.mesh.incores2.utils.shareuitls;


import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.we_smart.lansharelib.shareconstant.ShareConstant;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.db.DeviceDAO;
import com.ws.mesh.incores2.db.TimingDAO;

/**
 * Created by we_smart on 2018/4/26.
 */

public class ShareDevUtils {

    //获取设备信息
    public static JSONArray getDevJsonArray(SparseArray<Device> deviceSparseArray, String meshName) {
        JSONArray jsonArray = new JSONArray();
        if (deviceSparseArray == null || deviceSparseArray.size() == 0) return jsonArray;
        for (int x = 0; x < deviceSparseArray.size(); x++) {
            JSONObject jsonObject = new JSONObject();
            Device device = deviceSparseArray.valueAt(x);
            jsonObject.put(ShareConstant.DeviceConstant.NAME, device.mDevName);
            jsonObject.put(ShareConstant.DeviceConstant.TYPE, device.mDevType);
            jsonObject.put(ShareConstant.DeviceConstant.MESH_ID, device.mDevMeshId);
            jsonObject.put(ShareConstant.DeviceConstant.MACADDRESS, device.mDevMacAddress);
            jsonObject.put(ShareConstant.DeviceConstant.FIRMVERSION, device.mFirmwareVersion);
            jsonObject.put(ShareConstant.DeviceConstant.CHANNEL_ONE, device.mChannelOne);
            jsonObject.put(ShareConstant.DeviceConstant.CHANNEL_TWO, device.mChannelTwo);
            jsonObject.put(ShareConstant.DeviceConstant.CHANNEL_THREE, device.mChannelThree);
            jsonObject.put(ShareConstant.DeviceConstant.CHANNEL_FOUR, device.mChannelFour);
            jsonObject.put(ShareConstant.DeviceConstant.CHANNEL_FIVE, device.mChannelFive);
            jsonObject.put(ShareConstant.DeviceConstant.CIRADIAN, device.circadianString);
            jsonObject.put(ShareConstant.DeviceConstant.SYMBOLSTR, device.symbolStr);
            jsonObject.put(ShareConstant.DeviceConstant.ALARM, ShareAlarmUtils.getDevAlarmJsonArray(TimingDAO.
                    getInstance().queryDeviceAlarmByMeshId(device.mDevMeshId, meshName)));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    //解析设备数据
    public static void parseDevJsonArray(JSONArray jsonArray, String meshName) {
        if (jsonArray == null || jsonArray.size() == 0) return;
        for (int x = 0; x < jsonArray.size(); x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            Device device = new Device();
            device.mDevName = jsonObject.getString(ShareConstant.DeviceConstant.NAME);
            device.mDevMacAddress = jsonObject.getString(ShareConstant.DeviceConstant.MACADDRESS);
            device.mDeviceMeshName = meshName;
            device.mDevMeshId = jsonObject.getIntValue(ShareConstant.DeviceConstant.MESH_ID);
            device.mDevType = jsonObject.getIntValue(ShareConstant.DeviceConstant.TYPE);
            device.mFirmwareVersion = jsonObject.getString(ShareConstant.DeviceConstant.FIRMVERSION);
            device.mChannelOne = jsonObject.getString(ShareConstant.DeviceConstant.CHANNEL_ONE);
            device.mChannelTwo = jsonObject.getString(ShareConstant.DeviceConstant.CHANNEL_TWO);
            device.mChannelThree = jsonObject.getString(ShareConstant.DeviceConstant.CHANNEL_THREE);
            device.mChannelFour = jsonObject.getString(ShareConstant.DeviceConstant.CHANNEL_FOUR);
            device.mChannelFive = jsonObject.getString(ShareConstant.DeviceConstant.CHANNEL_FIVE);
            device.circadianString = jsonObject.getString(ShareConstant.DeviceConstant.CIRADIAN);
            device.circadian = Device.stringToCircadian(device.circadianString);
            device.symbolStr = jsonObject.getIntValue(ShareConstant.DeviceConstant.SYMBOLSTR);
            ShareAlarmUtils.parseDevAlarmJson(jsonObject.getJSONArray(ShareConstant.DeviceConstant.ALARM), meshName, device.mDevMeshId);
            DeviceDAO.getInstance().insertShareDevice(device);
        }
    }
}
