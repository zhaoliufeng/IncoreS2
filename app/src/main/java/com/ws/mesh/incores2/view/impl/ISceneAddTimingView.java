package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.view.base.IBaseView;

public interface ISceneAddTimingView extends IBaseView{

    //设置定时信息 如果当前是编辑已有定时的时候
    void setTimingInfo(Timing timing);

    //保存定时成功
    void saveSchedule(boolean success);
}
