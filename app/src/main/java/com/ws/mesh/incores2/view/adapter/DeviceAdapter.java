package com.ws.mesh.incores2.view.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telink.bluetooth.light.ConnectionStatus;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.utils.SendMsg;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceAdapter extends RecyclerView.Adapter {

    private SparseArray<Device> mDatas;
    private TextView tvDeviceNum;

    public DeviceAdapter(SparseArray<Device> deviceSparseArray) {
        mDatas = new SparseArray<>();
        for (int i = 0; i < deviceSparseArray.size(); i++) {
            if (deviceSparseArray.valueAt(i).mConnectionStatus != null &&
                    deviceSparseArray.valueAt(i).mConnectionStatus != ConnectionStatus.OFFLINE) {
                mDatas.append(deviceSparseArray.valueAt(i).mDevMeshId, deviceSparseArray.valueAt(i));
            }
        }
    }

    public DeviceAdapter(SparseArray<Device> deviceSparseArray, TextView tvDeviceNum) {
        this.tvDeviceNum = tvDeviceNum;
        mDatas = new SparseArray<>();
        for (int i = 0; i < deviceSparseArray.size(); i++) {
            if (deviceSparseArray.valueAt(i).mConnectionStatus != null &&
                    deviceSparseArray.valueAt(i).mConnectionStatus != ConnectionStatus.OFFLINE) {
                mDatas.append(deviceSparseArray.valueAt(i).mDevMeshId, deviceSparseArray.valueAt(i));
            }
        }
        setNumText();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deivce, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeviceViewHolder viewHolder = (DeviceViewHolder) holder;
        final Device device = mDatas.valueAt(position);

        //如果是强波器设备类型则不显示开关INVISIBLE
        if (isBooster(device)) {
            viewHolder.tvDeviceOn.setVisibility(View.INVISIBLE);
            viewHolder.tvDeviceOff.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tvDeviceOn.setVisibility(View.VISIBLE);
            viewHolder.tvDeviceOff.setVisibility(View.VISIBLE);
        }
        device.updateIcon();
        viewHolder.ivDeviceIcon.setImageResource(device.mIconRes);
        viewHolder.tvDeviceName.setText(device.mDevName);
        viewHolder.tvDeviceOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsg.switchDevice(device.mDevMeshId, true);
            }
        });

        viewHolder.tvDeviceOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsg.switchDevice(device.mDevMeshId, false);
            }
        });
        viewHolder.ivDeviceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeviceSelectedListener != null) {
                    if (isBooster(device))
                        onDeviceSelectedListener.onEdit(device.mDevMeshId);
                    else
                        onDeviceSelectedListener.onSelected(device.mDevMeshId);
                }
            }
        });

        viewHolder.ivSunrise.setVisibility(device.isSetSunrise() ? View.VISIBLE : View.GONE);
        viewHolder.ivSunset.setVisibility(device.isSetSunset() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_device_on)
        TextView tvDeviceOn;
        @BindView(R.id.tv_device_off)
        TextView tvDeviceOff;
        @BindView(R.id.iv_device_icon)
        ImageView ivDeviceIcon;
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.iv_sun)
        ImageView ivSunrise;
        @BindView(R.id.iv_moon)
        ImageView ivSunset;

        DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private boolean isBooster(Device device) {
        return (device.mDevType & 0xFF) == AppConstant.FORM_LIGHT_BOOSTER;
    }

    //刷新设备列表
    public void refreshDevice(Device device) {
        //如果设备当前是离线状态则从列表中剔除
        if (device.mConnectionStatus != null && device.mConnectionStatus != ConnectionStatus.OFFLINE) {
            mDatas.append(device.mDevMeshId, device);
            int index = mDatas.indexOfKey(device.mDevMeshId);
            notifyItemChanged(index);
        } else {
            if (mDatas.get(device.mDevMeshId) != null) {
                int index = mDatas.indexOfKey(device.mDevMeshId);
                mDatas.remove(device.mDevMeshId);
                notifyItemRemoved(index);
            }
        }
        setNumText();
    }

    public void refreshDeviceList(SparseArray<Device> deviceSparseArray) {
        mDatas.clear();
        for (int i = 0; i < deviceSparseArray.size(); i++) {
            if (deviceSparseArray.valueAt(i).mConnectionStatus != null &&
                    deviceSparseArray.valueAt(i).mConnectionStatus != ConnectionStatus.OFFLINE) {
                mDatas.append(deviceSparseArray.valueAt(i).mDevMeshId, deviceSparseArray.valueAt(i));
            }
        }
        notifyDataSetChanged();
        setNumText();
    }

    //获取当前显示设备数量
    private int getDataCount() {
        return mDatas.size();
    }

    @SuppressLint("DefaultLocale")
    private void setNumText() {
        if (tvDeviceNum != null)
            tvDeviceNum.setText(String.format("%s-%d", "Devices-", getDataCount()));
    }

    private OnDeviceSelectedListener onDeviceSelectedListener;

    public void setOnDeviceSelectedListener(OnDeviceSelectedListener listener) {
        this.onDeviceSelectedListener = listener;
    }

    public interface OnDeviceSelectedListener {
        void onSelected(int meshId);

        //强波器 直接进入编辑界面
        void onEdit(int meshId);
    }
}
