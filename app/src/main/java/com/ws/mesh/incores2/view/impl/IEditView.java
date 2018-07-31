package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.view.base.IBaseView;

public interface IEditView extends IBaseView{
    //完成编辑名称
    public void editName(boolean success);
    //移除设备
    public void remove(boolean success);
}
