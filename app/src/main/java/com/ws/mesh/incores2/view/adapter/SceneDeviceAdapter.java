package com.ws.mesh.incores2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telink.bluetooth.light.ConnectionStatus;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.SceneColor;
import com.ws.mesh.incores2.constant.SceneMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SceneDeviceAdapter extends RecyclerView.Adapter {

    private SparseArray<Device> data;
    private SparseArray<SceneColor> sceneDeviceData;
    private Context context;
    private OnSceneDeviceActionListener onSceneDeviceActionListener;

    public SceneDeviceAdapter(SparseArray<Device> deviceSparseArray, SparseArray<SceneColor> sceneColorSparseArray) {
        data = new SparseArray<>();
        sceneDeviceData = sceneColorSparseArray;
        for (int i = 0; i < deviceSparseArray.size(); i++) {
            if (deviceSparseArray.valueAt(i).mConnectionStatus != null &&
                    deviceSparseArray.valueAt(i).mConnectionStatus != ConnectionStatus.OFFLINE) {
                data.append(deviceSparseArray.valueAt(i).mDevMeshId, deviceSparseArray.valueAt(i));
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scene_device, parent, false);
        context = parent.getContext();
        return new SceneDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SceneDeviceViewHolder viewHolder = (SceneDeviceViewHolder) holder;
        final Device device = data.valueAt(viewHolder.getAdapterPosition());
        viewHolder.tvDeviceName.setText(device.mDevName);
        int chooseEventsStrId = 0;
        int chooseEventsBgResId;
        int chooseEventsTextColor;
        if (sceneDeviceData.get(device.mDevMeshId) != null) {
            SceneColor sceneColor = sceneDeviceData.get(device.mDevMeshId);
            chooseEventsBgResId = R.drawable.bg_round_primary;
            chooseEventsTextColor = context.getResources().getColor(R.color.white);
            if (sceneColor.mSceneMode == SceneMode.ON.getValue()) {
                chooseEventsStrId = R.string.on;
            }

            if (sceneColor.mSceneMode == SceneMode.OFF.getValue()) {
                chooseEventsStrId = R.string.off;
            }

            if (sceneColor.mSceneMode == SceneMode.PALETTE.getValue()) {
                chooseEventsStrId = R.string.palette;
            }
        } else {
            chooseEventsStrId = R.string.platte_action;
            chooseEventsBgResId = R.drawable.bg_round_white_with_border;
            chooseEventsTextColor = context.getResources().getColor(R.color.colorPrimary);
        }
        viewHolder.tvChooseEvents.setText(chooseEventsStrId);
        viewHolder.tvChooseEvents.setBackgroundResource(chooseEventsBgResId);
        viewHolder.tvChooseEvents.setTextColor(chooseEventsTextColor);

        viewHolder.tvDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSceneDeviceActionListener != null)
                    onSceneDeviceActionListener.locateDevice(device.mDevMeshId);
            }
        });

        viewHolder.tvChooseEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSceneDeviceActionListener != null)
                    onSceneDeviceActionListener.setDeviceEvent(device.mDevMeshId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SceneDeviceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.tv_choose_events)
        TextView tvChooseEvents;

        SceneDeviceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnSceneDeviceActionListener {
        void locateDevice(int deviceId);

        void setDeviceEvent(int deviceId);
    }

    public void setOnSceneDeivceActionListener(OnSceneDeviceActionListener listener) {
        this.onSceneDeviceActionListener = listener;
    }
}
