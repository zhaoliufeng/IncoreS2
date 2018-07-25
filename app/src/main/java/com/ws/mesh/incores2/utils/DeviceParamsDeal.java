package com.ws.mesh.incores2.utils;

import android.content.Context;

import com.telink.WSMeshApplication;
import com.telink.bluetooth.light.ConnectionStatus;
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
        switch (device.mDevType >> 8) {
            case AppConstant.FORM_LIGHT_BOOSTER:
                if (device.mConnectionStatus == ConnectionStatus.ON) {
                    id = R.drawable.icon_booster_on;
                } else {
                    id = R.drawable.icon_booster_off;
                }
                break;
            default:
                if (device.mConnectionStatus == ConnectionStatus.ON) {
                    id = R.drawable.icon_device_on;
                } else {
                    id = R.drawable.icon_device_off;
                }
        }
        return id;

    }

    public static synchronized String getDeviceName(int deviceType) {
        String name;
        Context context = WSMeshApplication.getInstance();
        switch (deviceType >> 8) {
            case AppConstant.FORM_LIGHT_BOOSTER:
                name = context.getString(R.string.device_extender);
                break;
            default:
                name = context.getString(R.string.device_flexlink);
        }
        return name;
    }
}
