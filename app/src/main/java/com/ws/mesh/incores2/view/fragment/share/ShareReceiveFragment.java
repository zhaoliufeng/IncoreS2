package com.ws.mesh.incores2.view.fragment.share;


import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.we_smart.lansharelib.LanShareReceiver;
import com.we_smart.lansharelib.listener.OnReceiverDataListener;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.ShareManageBean;
import com.ws.mesh.incores2.utils.shareuitls.ShareDataUtils;
import com.ws.mesh.incores2.view.base.BaseFragment;

import java.net.InetAddress;
import java.net.SocketException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareReceiveFragment extends BaseFragment {


    private LanShareReceiver mLanShareReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_receive_share;
    }

    @Override
    protected void initData() {
        try {
            //可以自己修改接受区域的大小
            mLanShareReceiver = new LanShareReceiver();
            mLanShareReceiver.setOnDataReceiveListener(onReceiverDataListener);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private OnReceiverDataListener onReceiverDataListener = new OnReceiverDataListener() {
        @Override
        public void receiveData(String jsonString, InetAddress senderIp) {
            if (!TextUtils.isEmpty(jsonString)) {
                //拿到数据
                ShareManageBean shareManageBean = ShareDataUtils.parseShareData(jsonString);
                if (shareManageBean == null){
                    toast("数据格式错误");
                }else {
                    final AlertDialog mAlertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog).create();
                    mAlertDialog.show();
                    Window window = mAlertDialog.getWindow();
                    if (window != null) {
                        window.setContentView(R.layout.dialog_receive_share_tip);
                        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        mAlertDialog.setCanceledOnTouchOutside(false);
                        Button btnConfirm = window.findViewById(R.id.btn_confirm);
                        TextView tvReceiveNetworkName = window.findViewById(R.id.share_network_name);
                        TextView tvSenderUserName = window.findViewById(R.id.share_send_user_name);
                        tvReceiveNetworkName.setText(shareManageBean.mNetworkName);
                        tvSenderUserName.setText(shareManageBean.mUserName);
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
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLanShareReceiver != null){
            mLanShareReceiver.clientOffline();
        }
        ShareUtils.getInstance().onDestroy();
    }
}
