package com.ws.mesh.incores2.view.activity;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.utils.FragmentFactory;
import com.ws.mesh.incores2.view.base.BaseActivity;

public class StageTwoActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_stage;
    }

    @Override
    protected void initData() {
        setPage(FragmentFactory.create(getPageId()));
    }
}
