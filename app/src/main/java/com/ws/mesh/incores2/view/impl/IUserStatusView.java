package com.ws.mesh.incores2.view.impl;

import com.we_smart.lansharelib.bean.Client;
import com.ws.mesh.incores2.view.base.IBaseView;

import java.util.List;

/**
 * Created by we_smart on 2018/4/30.
 */

public interface IUserStatusView extends IBaseView {

    void onOnline(List<Client> clients);

    void onOffline(List<Client> clients);

}
