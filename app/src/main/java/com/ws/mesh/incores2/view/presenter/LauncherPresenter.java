package com.ws.mesh.incores2.view.presenter;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.MeshDAO;
import com.ws.mesh.incores2.service.WeSmartService;
import com.ws.mesh.incores2.utils.AccountUtil;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SPUtils;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.ILauncherView;

public class LauncherPresenter extends IBasePresenter<ILauncherView> {

    public LauncherPresenter() {
        if (WeSmartService.Instance() == null) {
            MeshApplication.getInstance().doInit();
        }
        initMesh();
    }

    private void initMesh() {
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
        boolean isInitMesh = false;
        if (CoreData.core().mMeshMap == null
                || CoreData.core().mMeshMap.size() == 0) {
            isInitMesh = true;
            Mesh mesh = new Mesh();

            //自动生成账户与密码
            String account = AccountUtil.generateAccount();
            String password = AccountUtil.generatePassword(account);
//            mesh.mMeshName = account;
//            mesh.mMeshPassword = password;
            mesh.mMeshName = AppConstant.MESH_DEFAULT_NAME;;
            mesh.mMeshPassword = AppConstant.MESH_DEFAULT_PASSWORD;
            mesh.mMeshFactoryName = AppConstant.MESH_DEFAULT_NAME;
            mesh.mMeshFactoryPassword = AppConstant.MESH_DEFAULT_PASSWORD;
            mesh.mMeshShowName = AppConstant.DEFAULT_MESH_NAME;
            mesh.mIsShare = false;
            mesh.mMeshEditPassword = AppConstant.DEFAULT_EDIT_PASSWORD;

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

        final boolean finalIsInitMesh = isInitMesh;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (finalIsInitMesh) {
                    getView().enterScanView();
                } else {
                    getView().enterScanView();
                }
            }
        }, 1500);
    }
}
