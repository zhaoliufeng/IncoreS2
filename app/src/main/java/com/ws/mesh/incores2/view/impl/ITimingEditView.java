package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.view.base.IBaseView;

/**
 * Created by we_smart on 2017/11/23.
 */

public interface ITimingEditView extends IBaseView {

    //删除定时
    void deleteAlarm(boolean success);

    //添加定时
    void addAlarm(boolean success);

    //修改定时
    void updateAlarm(boolean success);

    //达到最大定时数量
    void maximumNumber();

}
