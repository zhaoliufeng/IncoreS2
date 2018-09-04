package com.ws.mesh.incores2.view.presenter;

import com.telink.bluetooth.light.LeAutoConnectParameters;
import com.telink.bluetooth.light.Parameters;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.MeshDAO;
import com.ws.mesh.incores2.service.WeSmartService;
import com.ws.mesh.incores2.utils.AccountUtil;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SPUtils;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.INetworkListView;

public class NetworkListPresenter extends IBasePresenter<INetworkListView> {

    public void addNetwork(String netName) {
        addNetwork(netName, AppConstant.MESH_DEFAULT_PASSWORD, netName);
    }

    private void addDefaultNetwork() {
        addNetwork(AppConstant.MESH_DEFAULT_NAME,
                AppConstant.MESH_DEFAULT_PASSWORD,
                AppConstant.DEFAULT_MESH_NAME);
    }

    private void addNetwork(String account, String password, String showName) {
        Mesh mesh = new Mesh();
        mesh.mMeshName = account;
        mesh.mMeshPassword = password;
        mesh.mMeshFactoryName = AppConstant.MESH_DEFAULT_NAME;
        mesh.mMeshFactoryPassword = AppConstant.MESH_DEFAULT_PASSWORD;
        mesh.mMeshShowName = showName;
        mesh.mIsShare = false;
        mesh.mMeshEditPassword = AppConstant.DEFAULT_EDIT_PASSWORD;

        if (MeshDAO.getInstance().insertMesh(mesh)) {
            Mesh currMesh = CoreData.core().getCurrMesh();
            boolean currIsDefaultMesh = currMesh.mMeshName.equals(AppConstant.MESH_DEFAULT_NAME);
            //如果添加的是非默认网络 或者当前选中的不是默认网络 添加网络后自动切换到新增网络
            if (!mesh.mMeshName.equals(AppConstant.MESH_DEFAULT_NAME) || currIsDefaultMesh) {
                CoreData.core().setCurrMesh(mesh);
                SPUtils.setLatelyMesh(mesh.mMeshName);
                CoreData.core().switchMesh(mesh);
            }
            CoreData.core().initMesh();
            getView().addNet(true);
        } else {
            getView().addNet(false);
        }
    }

    public void delNetwork(Mesh mesh) {
        if (MeshDAO.getInstance().deleteMesh(mesh)) {
            CoreData.core().mMeshMap.remove(mesh.mMeshName);
            //如果当前删除的时分享获得的默认网络 删除之后需要重新新建网络
            if (mesh.mMeshName.equals(SPUtils.getDefaultMesh())) {
                addDefaultNetwork();
            }
            getView().delNet(true);
        } else {
            getView().delNet(false);
        }
    }

    public void switchNetwork(final Mesh mesh) {
        //判断是否与当前网络相同 相同不做处理return
        if (mesh.mMeshName.equals(CoreData.core().getCurrMesh().mMeshName)) {
            return;
        }

        CoreData.core().setCurrMesh(mesh);
        CoreData.core().initMeshData();

        if (WeSmartService.Instance() != null) {
            WeSmartService.Instance().idleMode(true);
        }
        CoreData.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginNetworkParams(mesh);
                getView().switchNet();
            }
        }, 1000);
    }

    private void loginNetworkParams(Mesh mesh) {
        SPUtils.setLatelyMesh(mesh.mMeshName);

        LeAutoConnectParameters connectParams = Parameters.createAutoConnectParameters();
        connectParams.setMeshName(mesh.mMeshName);
        connectParams.setPassword(mesh.mMeshPassword);
        connectParams.setTimeoutSeconds(7);
        connectParams.autoEnableNotification(true);
        WeSmartService.Instance().autoConnect(connectParams);
    }
}
