package com.ws.mesh.incores2.view.fragment.share;


import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.we_smart.lansharelib.bean.Client;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.view.adapter.ChooseShareNetworkAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.IChooseShareMeshView;
import com.ws.mesh.incores2.view.presenter.ChooseShareMeshPresenter;

import java.net.InetAddress;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择需要分享的网络
 */
public class ChooseShareMeshFragment extends BaseContentFragment<IChooseShareMeshView,
        ChooseShareMeshPresenter> implements IChooseShareMeshView {

    @BindView(R.id.show_share_mesh)
    RecyclerView mMeshRecyclerView;
    private ChooseShareNetworkAdapter mChooseShareNetworkAdapter;

    private Mesh mCurrMesh;

    private InetAddress mInetAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_share_choose_mesh;
    }

    @Override
    protected void initData() {
        if (getActivity() != null) {
            mInetAddress = (InetAddress) getActivity().getIntent().getSerializableExtra(IntentConstant.INTENT_OBJ);
        }

        mChooseShareNetworkAdapter = new ChooseShareNetworkAdapter();
        mMeshRecyclerView.setAdapter(mChooseShareNetworkAdapter);
        mMeshRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChooseShareNetworkAdapter.setShareUserList(presenter.getMeshList());
        mChooseShareNetworkAdapter.setClickListener(new ChooseShareNetworkAdapter.onClickListener() {
            @Override
            public void onChooseItem(Mesh mesh) {
                mCurrMesh = mesh;
            }
        });
        presenter.setAddress(mInetAddress);
    }

    @Override
    protected ChooseShareMeshPresenter createPresent() {
        return new ChooseShareMeshPresenter();
    }

    @Override
    public void onShareSuccess(Client client) {
        //分享成功
        onShareSuccessDialog(client);
        toast(R.string.share_success);
    }

    @Override
    public void onShareFail(String errMsg) {
        //分享失败
        if (errMsg != null)
            toast(errMsg);
        else
            toast(R.string.share_fail);
    }

    @Override
    public void onShareTimeOut() {
        //分享超时
        toast(R.string.share_time_out);
    }

    @OnClick(R.id.tv_next)
    public void onShare() {
        if (mCurrMesh == null) {
            toast(R.string.choose_mesh_reminder);
            return;
        }
        presenter.startShare(mCurrMesh);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareUtils.getInstance().onDestroy();
    }

    public void onShareSuccessDialog(Client client) {
        if (getActivity() == null) return;
        final AlertDialog mAlertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_send_share_tip);
            mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            mAlertDialog.setCanceledOnTouchOutside(false);
            Button btnConfirm = window.findViewById(R.id.btn_confirm);
            TextView tvReceiveNetworkName = window.findViewById(R.id.share_network_name);
            TextView tvSenderUserName = window.findViewById(R.id.share_send_user_name);
            tvReceiveNetworkName.setText(mCurrMesh.mMeshShowName);
            tvSenderUserName.setText(client.mClientName);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    mAlertDialog.dismiss();
                }
            });
        }
    }

}
