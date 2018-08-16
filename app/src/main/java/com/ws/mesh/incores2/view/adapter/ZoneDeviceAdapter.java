package com.ws.mesh.incores2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.telink.bluetooth.light.ConnectionStatus;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZoneDeviceAdapter extends RecyclerView.Adapter<ZoneDeviceAdapter.DeviceViewHolder> {

    //显示的所有设备
    private SparseArray<Device> mDatas;
    //当前设备管理的设备
    private SparseArray<Device> mZoneDevices;
    private Context context;

    public ZoneDeviceAdapter(SparseArray<Device> deviceSparseArray, SparseArray<Device> zoneDevices) {
        mDatas = new SparseArray<>();
        mZoneDevices = new SparseArray<>();
        //剔除全部设备中不在线的设备
        for (int i = 0; i < deviceSparseArray.size(); i++) {
            Device device = deviceSparseArray.valueAt(i);
            if (device.mConnectionStatus != null &&
                    device.mConnectionStatus != ConnectionStatus.OFFLINE) {
                mDatas.append(device.mDevMeshId, device);
            }
        }

        //剔除房间下不在线设备
        for (int i = 0; i < zoneDevices.size(); i++) {
            Device device = zoneDevices.valueAt(i);
            if (device.mConnectionStatus != null &&
                    device.mConnectionStatus != ConnectionStatus.OFFLINE) {
                mZoneDevices.append(device.mDevMeshId, device);
            }
        }
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_zone_deivce, parent, false);
        context = parent.getContext();
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        final Device device = mDatas.valueAt(position);
        device.updateIcon();
        holder.ivDeviceIcon.setImageResource(device.mIconRes);
        holder.tvDeviceName.setText(device.mDevName);

        if (mZoneDevices.get(device.mDevMeshId) != null) {
            holder.rlDeviceFrame.setBackgroundResource(R.drawable.bg_zone_device_icon_selected);
            holder.tvSelect.setBackgroundResource(R.drawable.bg_zone_device_selected);
            holder.tvSelect.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvSelect.setText(R.string.selected);
        } else {
            holder.rlDeviceFrame.setBackgroundResource(R.drawable.bg_zone_device_icon_unselected);
            holder.tvSelect.setBackgroundResource(R.drawable.bg_zone_device_unselected);
            holder.tvSelect.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvSelect.setText(R.string.select);
        }

        holder.tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdd = mZoneDevices.get(device.mDevMeshId) == null;
                if (onDeviceSelectedListener != null)
                    onDeviceSelectedListener.onSelected(isAdd, device.mDevMeshId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void refreshDevice(Device device) {
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
    }

    public void refreshDeviceList() {
        notifyDataSetChanged();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_device_icon)
        ImageView ivDeviceIcon;
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.tv_select)
        TextView tvSelect;
        @BindView(R.id.rl_device_frame)
        RelativeLayout rlDeviceFrame;

        DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnDeviceSelectedListener onDeviceSelectedListener;

    public void setOnDeviceSelectedListener(OnDeviceSelectedListener listener) {
        this.onDeviceSelectedListener = listener;
    }

    public interface OnDeviceSelectedListener {
        void onSelected(boolean isAdd, int meshId);
    }
}
