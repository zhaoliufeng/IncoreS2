package com.ws.mesh.incores2.view.impl;

import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.view.base.IBaseView;

public interface IScanView extends IBaseView {
    void addDeviceSuccess(SparseArray<Device> sparseArray);

    void addDeviceFinish(int num);

    void addDeviceStatus(int status);

    //结束扫描
    void onStopScan();

    //蓝牙关闭
    void onBleClose();
}
