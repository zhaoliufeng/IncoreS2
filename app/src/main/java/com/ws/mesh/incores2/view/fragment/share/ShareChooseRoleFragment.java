package com.ws.mesh.incores2.view.fragment.share;

import android.view.View;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.view.base.BaseFragment;

import butterknife.OnClick;

public class ShareChooseRoleFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_share_choose_role;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.rl_history, R.id.tv_become_sharer, R.id.tv_become_sharees})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_history:
                //分享历史
                pushStageActivity(PageId.SHARE_HISTORY);
                break;
            case R.id.tv_become_sharer:
                //分享
                pushStageActivity(PageId.SHARE_DEVICE_ONLINE);
                break;
            case R.id.tv_become_sharees:
                //接收
                pushStageActivity(PageId.SHARE_RECEIVE);
                break;
        }
    }
}
