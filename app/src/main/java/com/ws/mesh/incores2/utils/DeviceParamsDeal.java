package com.ws.mesh.incores2.utils;

import android.content.Context;

import com.telink.bluetooth.light.ConnectionStatus;
import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.constant.AppConstant;

/**
 * Created by zxd on 2017/5/26.
 */

public class DeviceParamsDeal {

    //根据不同的设备的类型选择不同的灯
    public static int getDeviceIcon(Device device) {
        int id;
        switch (device.mDevType & 0xFF) {
            case AppConstant.FORM_LIGHT_BOOSTER:
                //强波器只有在线和离线两种状态
                if (device.mConnectionStatus == ConnectionStatus.OFFLINE){
                    id = R.drawable.icon_delete;
                }else {
                    id = R.drawable.icon_booster_on;
                }

                break;
            default:
                if (device.mConnectionStatus == ConnectionStatus.ON) {
                    id = R.drawable.icon_device_on;
                } else if (device.mConnectionStatus == ConnectionStatus.OFF){
                    id = R.drawable.icon_device_off;
                }else {
                    id = R.drawable.icon_delete;
                }
        }
        return id;

    }

    //获取在线状态的图标
    public static int getOnlineDeviceIcon(Device device) {
        int id;
        switch (device.mDevType & 0xFF) {
            case AppConstant.FORM_LIGHT_BOOSTER:
                id = R.drawable.icon_booster_on;
                break;
            default:
                id = R.drawable.icon_device_on;
        }
        return id;
    }

    public static synchronized String getDeviceName(int deviceType) {
        String name;
        Context context = MeshApplication.getInstance();
        switch (deviceType & 0xFF) {
            case AppConstant.FORM_LIGHT_BOOSTER:
                name = context.getString(R.string.device_extender);
                break;
            default:
                name = context.getString(R.string.device_flexlink);
        }
        return name;
    }
}
