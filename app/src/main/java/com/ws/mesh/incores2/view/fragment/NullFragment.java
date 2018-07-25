package com.ws.mesh.incores2.view.fragment;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.view.base.BaseFragment;

/**
 * 空fragment 如果factory中没有对应id的fragment 则显示这个fragment
 */
public class NullFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_null;
    }

    @Override
    protected void initData() {

    }
}
