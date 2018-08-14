package com.ws.mesh.incores2.view.activity;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.AppLifeStatusConstant;
import com.ws.mesh.incores2.utils.CoreData;
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
    protected void onStart() {
        super.onStart();
        presenter.initMesh();
    }

    @Override
    protected LauncherPresenter createPresenter() {
        return new LauncherPresenter();
    }

    @Override
    public void enterMainView() {
        pushActivity(MainActivity.class);
        finish();
    }
}
