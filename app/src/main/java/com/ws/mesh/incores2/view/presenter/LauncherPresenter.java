package com.ws.mesh.incores2.view.presenter;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.MeshDAO;
import com.ws.mesh.incores2.service.WeSmartService;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SPUtils;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.ILauncherView;

public class LauncherPresenter extends IBasePresenter<ILauncherView> {

    public LauncherPresenter() {
        if (WeSmartService.Instance() == null) {
            MeshApplication.getInstance().doInit();
        }
    }

    public void initMesh() {
        CoreData.core().initMesh();
        if (CoreData.core().getCurrMesh() != null) {
            if (!TextUtils.isEmpty(SPUtils.getLatelyMesh())) {
                getView().enterMainView();
            } else {
                initData();
            }
        } else {
            initData();
        }
    }

    private void initData() {
        if (CoreData.core().mMeshMap == null
                || CoreData.core().mMeshMap.size() == 0) {
            Mesh mesh = new Mesh();

            //生成默认Fulife网络 打开即用
            mesh.mMeshName = AppConstant.MESH_DEFAULT_NAME;
            mesh.mMeshPassword = AppConstant.MESH_DEFAULT_PASSWORD;
            mesh.mMeshFactoryName = AppConstant.MESH_DEFAULT_NAME;
            mesh.mMeshFactoryPassword = AppConstant.MESH_DEFAULT_PASSWORD;
            mesh.mMeshShowName = AppConstant.DEFAULT_MESH_NAME;
            mesh.mIsShare = false;
            mesh.mMeshEditPassword = AppConstant.DEFAULT_EDIT_PASSWORD;
            SPUtils.saveDefaultMesh(mesh.mMeshName);

            if (MeshDAO.getInstance().insertMesh(mesh)) {
                CoreData.core().setCurrMesh(mesh);

                SPUtils.setLatelyMesh(mesh.mMeshName);
                CoreData.core().switchMesh(mesh);
                CoreData.core().initMesh();
            }
        } else {
            String meshName = SPUtils.getLatelyMesh();
            Mesh mesh = CoreData.core().mMeshMap.get(meshName);
            CoreData.core().switchMesh(mesh);
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().enterMainView();
            }
        }, 1500);
    }
}
