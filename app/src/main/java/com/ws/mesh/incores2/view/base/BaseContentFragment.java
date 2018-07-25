package com.ws.mesh.incores2.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseContentFragment<V extends IBaseView, P extends IBasePresenter<V>> extends BaseFragment {

    protected P presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = createPresent();
        presenter.attachView((V) this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract P createPresent();

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.detach();
        }
        super.onDestroy();
    }
}
