package com.ws.mesh.incores2.view.impl;

import com.we_smart.lansharelib.bean.Client;
import com.ws.mesh.incores2.view.base.IBaseView;

/**
 * Created by we_smart on 2018/4/28.
 */

public interface IChooseShareMeshView extends IBaseView {
    
    void  onShareSuccess(Client client);
    
    void onShareFail(String errMsg);
    
    void onShareTimeOut();
    
}
