package com.ws.mesh.incores2.view.fragment.share;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.utils.NetStatus;
import com.ws.mesh.incores2.view.activity.StageThreeActivity;
import com.ws.mesh.incores2.view.activity.StageTwoActivity;
import com.ws.mesh.incores2.view.base.BaseFragment;

import butterknife.OnClick;

/**
 * 选择分享的角色
 * 是分享方 还是 接收方
 */
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
                showEventsDialog(PageId.SHARE_DEVICE_ONLINE);
                break;
            case R.id.tv_become_sharees:
                //接收
                showEventsDialog(PageId.SHARE_RECEIVE);
                break;
        }
    }

    private void showEventsDialog(final int pageId) {
        final AlertDialog mAlertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_share_tip);
            mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            Button confirm = window.findViewById(R.id.btn_confirm);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetStatus.isWifiConnect(getActivity())){
                        pushStageActivity(pageId);
                    }else {
                        toast(R.string.connect_wifi);
                    }
                    mAlertDialog.dismiss();
                }
            });
        }
    }
}
