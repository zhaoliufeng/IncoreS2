package com.ws.mesh.incores2.view.fragment;

import android.view.View;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.view.base.BaseFragment;

import butterknife.OnClick;

public class SettingFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.rl_network_manager, R.id.rl_share_network, R.id.rl_about_us})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_network_manager:
                pushStageActivity(PageId.NET_MANAGE);
                break;
            case R.id.rl_share_network:
                pushStageActivity(PageId.CHOOSE_ROLE);
                break;
            case R.id.rl_about_us:

                break;
        }
    }
}
