package com.ws.mesh.incores2.view.presenter;

import com.we_smart.lansharelib.bean.Client;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.fragment.share.ShareUtils;
import com.ws.mesh.incores2.view.impl.IUserStatusView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by we_smart on 2018/4/28.
 */

public class ShareUserPresenter extends IBasePresenter<IUserStatusView> implements ShareUtils.StatusChangeListener {

    private List<Client> mClientList = new ArrayList<>();


    public ShareUserPresenter() {
        ShareUtils.getInstance().init();
        ShareUtils.getInstance().setStatusChangeListener(this);
    }


    @Override
    public void Online(Client client) {
        if (mClientList != null) {
            for (Client c : mClientList) {
                if (c.mIpAddress.getHostAddress().
                        equals(client.mIpAddress.getHostAddress())) {
                    return;
                }
            }
            mClientList.add(client);
        }
        if (getView() != null) {
            getView().onOnline(mClientList);
        }
    }

    @Override
    public void Offline(InetAddress client) {
        if (mClientList != null) {
            for (int x = mClientList.size() - 1; x >= 0; x--) {
                Client c = mClientList.get(x);
                if (c.mIpAddress.getHostAddress().
                        equals(client.getHostAddress())) {
                    mClientList.remove(c);
                }
            }
        }
        if (getView() != null) {
            getView().onOnline(mClientList);
        }
    }

    public void onDestroy() {
        ShareUtils.getInstance().onDestroy();
    }
}
