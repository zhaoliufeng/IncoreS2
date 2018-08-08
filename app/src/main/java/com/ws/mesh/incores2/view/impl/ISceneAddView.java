package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.view.base.IBaseView;

public interface ISceneAddView extends IBaseView{
    //修改场景名称
    void editName(boolean success);

    //获取场景定时
    void getSceneSchedule(Timing timing);
}
