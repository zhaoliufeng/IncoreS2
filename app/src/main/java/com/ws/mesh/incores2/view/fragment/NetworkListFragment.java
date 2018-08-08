package com.ws.mesh.incores2.view.fragment;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.activity.ScanDeviceActivity;
import com.ws.mesh.incores2.view.adapter.NetworkAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.INetworkListView;
import com.ws.mesh.incores2.view.presenter.NetworkListPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class NetworkListFragment extends BaseContentFragment<INetworkListView, NetworkListPresenter> implements INetworkListView {

    @BindView(R.id.ll_main_frame)
    LinearLayout llMainFrame;
    @BindView(R.id.tv_network_title)
    TextView tvNetTitle;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.rl_net_list)
    RecyclerView rlNetList;
    @BindView(R.id.iv_add_net)
    ImageView ivAddNet;
    @BindView(R.id.view_space)
    View viewSpace;
    @BindView(R.id.tv_finish)
    TextView tvFinish;

    private NetworkAdapter networkAdapter;

    @Override
    protected NetworkListPresenter createPresent() {
        return new NetworkListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_net_list;
    }

    @Override
    protected void initData() {
        networkAdapter = new NetworkAdapter();
        rlNetList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlNetList.setAdapter(networkAdapter);
        networkAdapter.setOnNetActionListener(new NetworkAdapter.OnNetActionListener() {
            @Override
            public void switchNet(Mesh mesh) {
                presenter.switchNetwork(mesh);
            }

            @Override
            public void delNet(Mesh mesh) {
                presenter.delNetwork(mesh);
            }

            @Override
            public void addDevice() {
                pushActivity(ScanDeviceActivity.class);
            }
        });
    }

    @OnClick({R.id.iv_add_net, R.id.iv_edit, R.id.tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_net:
                //添加默认网络
                popAddNetWork();
                break;
            case R.id.iv_edit:
                networkAdapter.setEditMode(true);
                llMainFrame.setBackgroundColor(getResources().getColor(R.color.black_333));
                tvNetTitle.setTextColor(getResources().getColor(R.color.white));
                ivAddNet.setVisibility(View.GONE);
                ivEdit.setVisibility(View.GONE);
                viewSpace.setVisibility(View.VISIBLE);
                tvFinish.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_finish:
                networkAdapter.setEditMode(false);
                llMainFrame.setBackgroundColor(getResources().getColor(R.color.app_bg));
                tvNetTitle.setTextColor(getResources().getColor(R.color.black_333));
                ivAddNet.setVisibility(View.VISIBLE);
                ivEdit.setVisibility(View.VISIBLE);
                viewSpace.setVisibility(View.GONE);
                tvFinish.setVisibility(View.GONE);
                break;
        }
    }

    private void popAddNetWork() {
        final AlertDialog mAlertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog).create();
        mAlertDialog.show();
        final Window window = mAlertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_custom_change_network);
            mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            final EditText mEtNewNetWork = window.findViewById(R.id.new_network_name);
            mEtNewNetWork.setHint(R.string.new_home_name);
            mEtNewNetWork.setFocusable(true);
            mEtNewNetWork.setFocusableInTouchMode(true);

            CoreData.mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEtNewNetWork.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                }
            }, 100);

            TextView tvCancel = window.findViewById(R.id.tv_cancel);
            TextView tvConfirm = window.findViewById(R.id.tv_confirm);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                }
            });

            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newNetworkName = mEtNewNetWork.getText().toString().trim();
                    if (AppConstant.DEFAULT_MESH_NAME.equals(newNetworkName)) {
                        toast(getString(R.string.cant_create_network));
                        return;
                    }

                    if (TextUtils.isEmpty(newNetworkName)) {
                        toast(getString(R.string.name_can_not_null));
                        return;
                    }

                    if (newNetworkName.contains("?") || newNetworkName.contains("？")) {
                        toast(getString(R.string.cant_include));
                        return;
                    }
                    if (newNetworkName.getBytes().length > AppConstant.MAX_MESH_LENGTH) {
                        toast(getString(R.string.name_too_long));
                        return;
                    }

                    mAlertDialog.dismiss();
                    //从本地添加网络
                    presenter.addNetwork(newNetworkName);
                }
            });
        }
    }

    @Override
    public void addNet(boolean success) {
        if (success) {
            networkAdapter.refreshMeshList();
        } else {
            toast(R.string.add_failed);
        }
    }

    @Override
    public void delNet(boolean success) {
        if (success) {
            networkAdapter.refreshMeshList();
        } else {
            toast(R.string.delete_fail);
        }
    }

    @Override
    public void switchNet() {
        networkAdapter.refreshMeshList();
    }
}
