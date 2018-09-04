package com.ws.mesh.incores2.view.fragment.scene;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.activity.SceneControlActivity;
import com.ws.mesh.incores2.view.adapter.SceneDeviceAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.ISceneAddDeviceView;
import com.ws.mesh.incores2.view.presenter.SceneAddDevicePresenter;

import butterknife.BindView;
import butterknife.OnClick;

//场景添加设备
public class SceneAddDeviceFragment extends BaseContentFragment<ISceneAddDeviceView, SceneAddDevicePresenter> implements ISceneAddDeviceView {

    private static final String TAG = "SceneAddDeviceFragment";
    private static final int LOCATE_DEVICE = 0;

    @BindView(R.id.rl_device_list)
    RecyclerView rlDeviceList;
    @BindView(R.id.tv_scene_title)
    TextView tvTitle;

    private int sceneId;
    private SceneDeviceAdapter sceneDeviceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scene_add_device;
    }

    @Override
    protected void initData() {
        if (getActivity() != null){
            sceneId = getActivity().getIntent().getIntExtra(IntentConstant.MESH_ADDRESS, -1);
        }
        presenter.init(sceneId);
        rlDeviceList.setLayoutManager(new LinearLayoutManager(getActivity()));
        sceneDeviceAdapter = new SceneDeviceAdapter(presenter.getDeviceArray(), presenter.getSceneColorArray());
        tvTitle.setText(String.format(
                getString(R.string.title_device),
                presenter.getDeviceArray().size()
        ));
        rlDeviceList.setAdapter(sceneDeviceAdapter);
        sceneDeviceAdapter.setOnSceneDeviceActionListener(new SceneDeviceAdapter.OnSceneDeviceActionListener() {
            @Override
            public void locateDevice(int deviceId) {
                popLocateDialog(deviceId);
            }

            @Override
            public void setDeviceEvent(int deviceId) {
                //设置场景设备状态 开关 颜色
                pushActivity(SceneControlActivity.class, deviceId, sceneId);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sceneDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    protected SceneAddDevicePresenter createPresent() {
        return new SceneAddDevicePresenter();
    }

    public void popLocateDialog(final int meshId) {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog).create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_find_device);
            TextView tvCancel = window.findViewById(R.id.tv_cancel);
            TextView tvConfirm = window.findViewById(R.id.tv_confirm);
            ImageView ivIcon = window.findViewById(R.id.iv_device_icon);
            Device device = CoreData.core().mDeviceSparseArray.get(meshId);
            ivIcon.setImageResource(device.mIconRes);

            //开始定位设备位置
            isLocating = true;
            Message message = new Message();
            message.what = LOCATE_DEVICE;
            message.arg1 = meshId;
            handler.sendMessage(message);

            tvConfirm.setVisibility(View.GONE);

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLocating = false;
                    dialog.dismiss();
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isLocating = false;
                }
            });
        }
    }

    private boolean isLocating = false;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isLocating) {
                switch (msg.what) {
                    case LOCATE_DEVICE:
                        Log.i(TAG, "handleMessage: 正在定位设备 address --> " + msg.arg1);
                        SendMsg.locationDevice(msg.arg1);
                        Message message = new Message();
                        message.arg1 = msg.arg1;
                        message.what = LOCATE_DEVICE;
                        handler.sendMessageDelayed(message, 2000);
                        break;
                    default:
                }
            }
        }
    };
}
