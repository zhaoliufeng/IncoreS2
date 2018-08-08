package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.view.base.IBaseView;

public interface INetworkListView extends IBaseView{

    void addNet(boolean success);

    void delNet(boolean success);

    void switchNet();
}
