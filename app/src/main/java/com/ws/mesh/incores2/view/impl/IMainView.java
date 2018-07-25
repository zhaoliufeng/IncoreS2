package com.ws.mesh.incores2.view.impl;

import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.view.base.IBaseView;

public interface IMainView extends IBaseView {

    //找到设备
    void onFindDevice();

    //登陆成功
    void onLoginSuccess();

    //设备下线
    void offline(Device device);

    //设备上线
    void online(SparseArray<Device> sparseArray);

    //状态更新
    void statusUpdate(Device device);

    //断开连接
    void onLoginOut();

    //更新设备类型
    void updateDeviceType();

    //更新设备
    void updateDevice(SparseArray<Device> deviceSparseArray);
}

