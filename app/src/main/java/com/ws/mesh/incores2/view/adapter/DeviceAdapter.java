package com.ws.mesh.incores2.view.adapter;

import android.content.Context;
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
import com.ws.mesh.incores2.utils.SendMsg;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceAdapter extends RecyclerView.Adapter {

    private SparseArray<Device> mDatas;
    private Context context;

    public DeviceAdapter(SparseArray<Device> deviceSparseArray) {
        mDatas = new SparseArray<>();
        for (int i = 0; i < deviceSparseArray.size(); i++) {
            if (deviceSparseArray.valueAt(i).mConnectionStatus != null &&
                    deviceSparseArray.valueAt(i).mConnectionStatus != ConnectionStatus.OFFLINE) {
                mDatas.append(deviceSparseArray.valueAt(i).mDevMeshId, deviceSparseArray.valueAt(i));
            }
        }
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
                    onDeviceSelectedListener.onSelected(device.mDevMeshId);
                }
            }
        });
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

        DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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

    public void refreshDeviceList(){
        notifyDataSetChanged();
    }

    private OnDeviceSelectedListener onDeviceSelectedListener;

    public void setOnDeviceSelectedListener(OnDeviceSelectedListener listener) {
        this.onDeviceSelectedListener = listener;
    }

    public interface OnDeviceSelectedListener {
        void onSelected(int meshId);
    }
}
