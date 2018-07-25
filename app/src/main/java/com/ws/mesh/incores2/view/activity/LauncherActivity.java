package com.ws.mesh.incores2.view.activity;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.AppLifeStatusConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.base.BaseActivity;
import com.ws.mesh.incores2.view.base.BaseContentActivity;
import com.ws.mesh.incores2.view.impl.ILauncherView;
import com.ws.mesh.incores2.view.presenter.LauncherPresenter;

public class LauncherActivity extends BaseContentActivity<ILauncherView, LauncherPresenter> implements ILauncherView {
    @Override
    protected int getLayoutId() {
        CoreData.core().setCurrAppStatus(AppLifeStatusConstant.NORMAL_START);
        return R.layout.activity_launcher;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected LauncherPresenter createPresenter() {
        return new LauncherPresenter();
    }

    @Override
    public void enterMainView() {
        pushActivity(MainActivity.class);
    }

    @Override
    public void enterScanView() {
        //Intent附带Extra标识当前是否需要进入扫描设备界面
        pushActivity(MainActivity.class, true);
    }
}
