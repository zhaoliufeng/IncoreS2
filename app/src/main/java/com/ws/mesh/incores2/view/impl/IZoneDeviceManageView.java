package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.view.base.IBaseView;

public interface IZoneDeviceManageView extends IBaseView{
    void statusUpdate(Device device);

    void updateDeviceList(boolean success);
}
