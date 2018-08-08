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

public class NetworkListPresenter extends IBasePresenter<INetworkListView>{

    public void addNetwork(String netName){
        Mesh mesh = new Mesh();
        //自动生成账户与密码
        String account = AccountUtil.generateAccount();
        String password = AccountUtil.generatePassword(account);
        mesh.mMeshName = account;
        mesh.mMeshPassword = password;
        mesh.mMeshFactoryName = AppConstant.MESH_DEFAULT_NAME;
        mesh.mMeshFactoryPassword = AppConstant.MESH_DEFAULT_PASSWORD;
        mesh.mMeshShowName = netName;
        mesh.mIsShare = false;
        mesh.mMeshEditPassword = AppConstant.DEFAULT_EDIT_PASSWORD;

        if (MeshDAO.getInstance().insertMesh(mesh)) {
            CoreData.core().setCurrMesh(mesh);
            //添加网络后自动切换到新增网络
            SPUtils.setLatelyMesh(mesh.mMeshName);
            CoreData.core().switchMesh(mesh);
            CoreData.core().initMesh();
            getView().addNet(true);
        }else {
            getView().addNet(false);
        }
    }

    public void delNetwork(Mesh mesh) {
        if (MeshDAO.getInstance().deleteMesh(mesh)){
            CoreData.core().mMeshMap.remove(mesh.mMeshName);
            getView().delNet(true);
        }else {
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
