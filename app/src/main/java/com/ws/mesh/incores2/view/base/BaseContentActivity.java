package com.ws.mesh.incores2.view.base;

import android.os.Bundle;

public abstract class BaseContentActivity<V extends IBaseView, P extends IBasePresenter<V>> extends BaseActivity{
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = createPresenter();
        presenter.attachView((V) this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.detach();
        }
        super.onDestroy();
    }

    protected abstract P createPresenter();
}
