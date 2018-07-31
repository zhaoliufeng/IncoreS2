package com.ws.mesh.incores2.view.fragment.main;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.activity.ControlActivity;
import com.ws.mesh.incores2.view.adapter.DeviceAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;

import butterknife.BindView;

public class DeviceFragment extends BaseFragment {

    @BindView(R.id.rl_device_list)
    RecyclerView rlDeviceList;

    private DeviceAdapter deviceAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    protected void initData() {
        deviceAdapter = new DeviceAdapter(CoreData.core().mDeviceSparseArray);
        rlDeviceList.setAdapter(deviceAdapter);
        rlDeviceList.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        deviceAdapter.setOnDeviceSelectedListener(new DeviceAdapter.OnDeviceSelectedListener() {
            @Override
            public void onSelected(int meshId) {
                pushActivity(ControlActivity.class, meshId);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceAdapter.refreshDeviceList();
    }

    public void refreshDevice(Device device) {
        deviceAdapter.refreshDevice(device);
    }
}
