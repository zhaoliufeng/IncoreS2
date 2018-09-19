package com.ws.mesh.incores2.view.fragment.main;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.telink.bluetooth.light.ConnectionStatus;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.activity.ControlActivity;
import com.ws.mesh.incores2.view.adapter.DeviceAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceFragment extends BaseFragment {

    @BindView(R.id.rl_device_list)
    RecyclerView rlDeviceList;
    @BindView(R.id.tv_mesh_name)
    TextView tvMeshName;
    @BindView(R.id.tv_device_num)
    TextView tvDeviceNum;

    //All Devices
    @BindView(R.id.rl_all_menu)
    RelativeLayout rlAllMenu;

    private DeviceAdapter deviceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initData() {
        deviceAdapter = new DeviceAdapter(CoreData.core().mDeviceSparseArray, tvDeviceNum);
        rlDeviceList.setAdapter(deviceAdapter);
        rlDeviceList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rlDeviceList.setItemAnimator(null);
        deviceAdapter.setOnDeviceSelectedListener(new DeviceAdapter.OnDeviceSelectedListener() {
            @Override
            public void onSelected(int meshId) {
                pushActivity(ControlActivity.class, meshId);
            }

            @Override
            public void onEdit(int meshId) {
                pushStageActivity(PageId.EDIT_DEVICE, meshId);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        deviceAdapter.refreshDeviceList(CoreData.core().mDeviceSparseArray);
        tvMeshName.setText(CoreData.core().getCurrMesh().mMeshShowName);
    }

    public void refreshDevice(Device device) {
        deviceAdapter.refreshDevice(device);
    }

    //All Devices Click
    @OnClick({R.id.rl_all_frame, R.id.iv_all_panel, R.id.iv_all_breath, R.id.iv_all_music,
            R.id.tv_all_on, R.id.tv_all_off})
    void allDeviceListener(View v) {
        switch (v.getId()) {
            case R.id.rl_all_frame:
                if (rlAllMenu.getVisibility() == View.VISIBLE) {
                    rlAllMenu.setVisibility(View.GONE);
                } else {
                    rlAllMenu.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_all_panel:
                pushActivity(ControlActivity.class, AppConstant.ALL_DEVICE_MESH_ID);
                break;
            case R.id.iv_all_breath:
                pushStageActivity(PageId.BREATH, AppConstant.ALL_DEVICE_MESH_ID);
                break;
            case R.id.iv_all_music:
                pushStageActivity(PageId.MUSIC, AppConstant.ALL_DEVICE_MESH_ID);
                break;
            case R.id.tv_all_on:
                SendMsg.switchDevice(AppConstant.ALL_DEVICE_MESH_ID, true);
                break;
            case R.id.tv_all_off:
                SendMsg.switchDevice(AppConstant.ALL_DEVICE_MESH_ID, false);
                break;
        }
    }
}
