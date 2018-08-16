package com.ws.mesh.incores2.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.we_smart.permissions.PermissionsListener;
import com.we_smart.permissions.PermissionsManager;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.AppLifeStatusConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.base.BaseActivity;
import com.ws.mesh.incores2.view.base.BaseContentActivity;
import com.ws.mesh.incores2.view.impl.ILauncherView;
import com.ws.mesh.incores2.view.presenter.LauncherPresenter;

public class LauncherActivity extends BaseContentActivity<ILauncherView, LauncherPresenter> implements ILauncherView {

    private PermissionsManager permissionsManager;
    private String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private PermissionsListener permissionsListener = new PermissionsListener() {
        @Override
        public void getAllPermissions() {
            pushActivity(MainActivity.class);
            finish();
        }

        @Override
        public void PermissionsDenied(String... deniedPermissions) {
            permissionsManager.startRequestPermission(deniedPermissions);
        }

        @Override
        public void cancelPermissionRequest() {
            pushActivity(MainActivity.class);
            finish();
        }
    };

    @Override
    protected int getLayoutId() {
        CoreData.core().setCurrAppStatus(AppLifeStatusConstant.NORMAL_START);
        return R.layout.activity_launcher;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        permissionsManager = new PermissionsManager(this, permissions);
        presenter.initMesh();
    }

    @Override
    protected LauncherPresenter createPresenter() {
        return new LauncherPresenter();
    }

    @Override
    public void enterMainView() {
       permissionsManager.checkPermissions(permissionsListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int deniedCount = 0;
        if (requestCode == PermissionsManager.REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedCount++;
                    }
                }
                if (deniedCount != 0){
                    permissionsManager.showDialogTipUserGoToAppSettting(getString(R.string.no_locate_permission), getString(R.string.tip_no_locate_permission));
                }else {
                    pushActivity(MainActivity.class);
                    finish();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsManager.REQUEST_SETTING_CODE) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    permissionsManager.showDialogTipUserGoToAppSettting(getString(R.string.no_locate_permission), getString(R.string.tip_no_locate_permission));
                } else {
                    pushActivity(MainActivity.class);
                    finish();
                }
            }
        }
    }
}
