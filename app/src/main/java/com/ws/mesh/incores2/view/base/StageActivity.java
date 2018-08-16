package com.ws.mesh.incores2.view.base;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.ws.mesh.incores2.utils.FragmentFactory;

public abstract class StageActivity extends BaseActivity {

    @Override
    protected void initData() {
        setPage(FragmentFactory.create(getPageId()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mCurrFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCurrFragment.onActivityResult(requestCode, resultCode, data);
    }
}
