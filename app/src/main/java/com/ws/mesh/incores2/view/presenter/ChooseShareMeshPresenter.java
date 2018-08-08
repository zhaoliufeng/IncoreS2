package com.ws.mesh.incores2.view.presenter;

import android.util.Log;

import com.we_smart.lansharelib.bean.Client;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.bean.ShareManageBean;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.TaskPool;
import com.ws.mesh.incores2.utils.shareuitls.ShareDataMangeUtils;
import com.ws.mesh.incores2.utils.shareuitls.ShareDataUtils;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.fragment.share.ShareUtils;
import com.ws.mesh.incores2.view.impl.IChooseShareMeshView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by we_smart on 2018/4/28.
 */

public class ChooseShareMeshPresenter extends IBasePresenter<IChooseShareMeshView> implements ShareUtils.ShareInfoStatus {

    private static final String TAG = "ChooseShareMesh";
    private InetAddress mInetAddress;

    private boolean isSuccess = false;

    public ChooseShareMeshPresenter() {
        ShareUtils.getInstance().setShareInfoStatus(this);
    }


    public List<Mesh> getMeshList() {
        List<Mesh> meshList = new ArrayList<>();
        for (Map.Entry<String, Mesh> meshEntry : CoreData.core().mMeshMap.entrySet()) {
            Mesh mesh = meshEntry.getValue();
            if (!mesh.mIsShare) {
                meshList.add(mesh);
            }
        }
        return meshList;
    }

    public void startShare(Mesh mesh) {
        if (mInetAddress == null) {
            getView().onShareFail("InetAddress is Null");
        } else {
            if (mesh == null) {
                getView().onShareFail("Share Mesh is Null");
            } else {
                String meshData = ShareDataUtils.getShareData(mesh);
                ShareManageBean shareManageBean = new ShareManageBean();
                Log.i(TAG, "startShare: " + meshData);
                shareManageBean.utc = System.currentTimeMillis();
                shareManageBean.mNetworkName = mesh.mMeshShowName;
                shareManageBean.mUserName = ShareUtils.getInstance().mClientHashMap.get(mInetAddress.getHostAddress()).mClientName;
                ShareDataMangeUtils.addNewSendShareData(shareManageBean);
                ShareUtils.getInstance().sendData(mInetAddress, meshData);
                TaskPool.DefRandTaskPool().PushTask(new Runnable() {
                    @Override
                    public void run() {
                        if (!isSuccess) {
                            if (getView() != null) {
                                getView().onShareTimeOut();
                            }
                        }
                    }
                }, 10 * 1000);
            }
        }
    }


    public void setAddress(InetAddress inetAddress) {
        mInetAddress = inetAddress;
    }

    @Override
    public void onSuccess(InetAddress inetAddress) {
        isSuccess = true;
        if (getView() != null) {
            Client client = ShareUtils.getInstance().mClientHashMap.get(inetAddress.getHostAddress());
            if (client != null) {
                getView().onShareSuccess(client);
            }
        }
    }
}
