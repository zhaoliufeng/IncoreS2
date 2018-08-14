package com.ws.mesh.incores2.view.fragment.action;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.adapter.DeviceAdapter;
import com.ws.mesh.incores2.view.adapter.ZoneDeviceAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.IZoneDeviceManageView;
import com.ws.mesh.incores2.view.presenter.ZoneDeviceManagePresenter;

import org.w3c.dom.Text;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

//房间设备预览 开关
public class ZoneDeviceManageFragment extends BaseContentFragment<IZoneDeviceManageView, ZoneDeviceManagePresenter> implements IZoneDeviceManageView {

    private static final String TAG = "ZoneDeviceManage";
    private static final int LOCATE_DEVICE = 0;
    @BindView(R.id.tv_zone_name)
    TextView tvZoneName;
    @BindView(R.id.rl_device_list)
    RecyclerView rlDeviceList;
    @BindView(R.id.rl_zone_device_list)
    RecyclerView rlZoneDeviceList;

    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.iv_edit_zone_devices)
    ImageView ivEdit;
    @BindView(R.id.tv_finish)
    TextView tvFinish;

    private DeviceAdapter deviceAdapter;
    private ZoneDeviceAdapter zoneDeviceAdapter;
    private int meshAddress;

    @Override
    protected ZoneDeviceManagePresenter createPresent() {
        return new ZoneDeviceManagePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zone_device_manage;
    }

    @Override
    protected void initData() {
        if (getActivity() != null) {
            meshAddress = getActivity().getIntent().getIntExtra(
                    IntentConstant.MESH_ADDRESS, -1);
        }
        presenter.init(meshAddress);
        tvZoneName.setText(presenter.getZoneName());
        rlDeviceList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceAdapter = new DeviceAdapter(presenter.getZoneDevices());
        rlDeviceList.setAdapter(deviceAdapter);
        rlDeviceList.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        zoneDeviceAdapter = new ZoneDeviceAdapter(presenter.getDevices(), presenter.getZoneDevices());
        rlZoneDeviceList.setAdapter(zoneDeviceAdapter);
        rlZoneDeviceList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        zoneDeviceAdapter.setOnDeviceSelectedListener(new ZoneDeviceAdapter.OnDeviceSelectedListener() {
            @Override
            public void onSelected(boolean isAdd, int meshId) {
                //弹窗 定位设备
                popLocateDialog(isAdd, meshId);
            }
        });
    }

    public void popLocateDialog(final boolean isAdd, final int meshId) {
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

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLocating = false;
                    dialog.dismiss();
                }
            });

            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLocating = false;
                    dialog.dismiss();
                    if (isAdd){
                        presenter.addDevice(meshId);
                    }else {
                        presenter.removeDevice(meshId);
                    }
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

    @OnClick(R.id.iv_edit_zone_devices)
    public void onEdit() {
        llBg.setBackgroundColor(getResources().getColor(R.color.black_333));
        ivEdit.setVisibility(View.GONE);
        tvFinish.setVisibility(View.VISIBLE);
        rlDeviceList.setVisibility(View.GONE);
        rlZoneDeviceList.setVisibility(View.VISIBLE);
        tvZoneName.setTextColor(getResources().getColor(R.color.white));
    }

    @OnClick(R.id.tv_finish)
    public void onFinish() {
        llBg.setBackgroundColor(getResources().getColor(R.color.app_bg));
        ivEdit.setVisibility(View.VISIBLE);
        tvFinish.setVisibility(View.GONE);
        rlDeviceList.setVisibility(View.VISIBLE);
        rlZoneDeviceList.setVisibility(View.GONE);
        tvZoneName.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void statusUpdate(final Device device) {
        //刷新设备状态
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceAdapter.refreshDevice(device);
            }
        });
    }

    @Override
    public void updateDeviceList(boolean success) {
        if (success){
            onResume();
        }else {
            toast(R.string.save_failed);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destory();
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
