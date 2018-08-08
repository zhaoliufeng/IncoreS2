package com.ws.mesh.incores2.view.impl;

import com.telink.bluetooth.light.ConnectionStatus;
import com.ws.mesh.incores2.view.base.IBaseView;

public interface IControlView extends IBaseView {
    void addFavoriteColor(boolean success);

    void setColor(int color, int warm, int cold);

    void setBrightness(int brightness);

    void setStatus(ConnectionStatus connectStatus);
}
