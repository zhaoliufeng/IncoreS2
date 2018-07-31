package com.ws.mesh.incores2.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.view.adapter.DeviceAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.IZoneManageDeviceView;
import com.ws.mesh.incores2.view.presenter.ZoneManageDevicePresenter;

import butterknife.BindView;

public class ZoneDeviceManageFragment extends BaseContentFragment<IZoneManageDeviceView, ZoneManageDevicePresenter> implements IZoneManageDeviceView {

    @BindView(R.id.tv_zone_name)
    TextView tvZoneName;
    @BindView(R.id.rl_device_list)
    RecyclerView rlDeviceList;

    private DeviceAdapter deviceAdapter;

    @Override
    protected ZoneManageDevicePresenter createPresent() {
        return new ZoneManageDevicePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zone_device_manage;
    }

    @Override
    protected void initData() {
        tvZoneName.setText(presenter.getZoneName());
        rlDeviceList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceAdapter = new DeviceAdapter(presenter.getZoneDevices());
        rlDeviceList.setAdapter(deviceAdapter);
    }

    @Override
    public void statusUpdate(Device device) {
        deviceAdapter.refreshDevice(device);
    }
}
