package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.view.base.IBaseView;

public interface ITimingView extends IBaseView {
    void deleteAlarm(boolean success);

    void switchCircadian();

    void openAlarm(boolean success);

    void closeAlarm(boolean success);

    //刷新昼夜节律
    void refreshCircadian();
}
