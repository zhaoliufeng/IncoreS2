package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.view.base.IBaseView;

public interface IZoneView extends IBaseView{

    //房间数量达到最大
    void maxRoomNum();
    //添加房间成功
    void addRoom(boolean success);
}
