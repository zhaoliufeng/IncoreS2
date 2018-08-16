package com.ws.mesh.incores2.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Room;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ws.mesh.incores2.view.adapter.RoomAdapter.ACTION_TYPE.BREATH;
import static com.ws.mesh.incores2.view.adapter.RoomAdapter.ACTION_TYPE.CONTROL;
import static com.ws.mesh.incores2.view.adapter.RoomAdapter.ACTION_TYPE.DEVICE_MANAGE;
import static com.ws.mesh.incores2.view.adapter.RoomAdapter.ACTION_TYPE.EDIT;
import static com.ws.mesh.incores2.view.adapter.RoomAdapter.ACTION_TYPE.MUSIC;
import static com.ws.mesh.incores2.view.adapter.RoomAdapter.ACTION_TYPE.TIMING;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private SparseArray<Room> data;

    public RoomAdapter(SparseArray<Room> data) {
        this.data = data;
    }

    private int expandPosition = -1;

    public enum ACTION_TYPE {
        DEVICE_MANAGE, CONTROL, BREATH, MUSIC, TIMING, EDIT
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_zone, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        final int index = holder.getAdapterPosition();
        Room room = data.valueAt(position);
        holder.tvZoneName.setText(room.mRoomName);
        if (position == expandPosition) {
            holder.rlZoneMenu.setVisibility(View.VISIBLE);
        } else {
            holder.rlZoneMenu.setVisibility(View.GONE);
        }
        holder.rlZoneFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果当前点击的zone item已经展开 则收起菜单
                if (index == expandPosition) {
                    expandPosition = -1;
                } else {
                    expandPosition = index;
                }
                notifyDataSetChanged();
            }
        });

        holder.tvRoomOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onZoneClickListener != null) {
                    onZoneClickListener.onSwitch(true, index);
                }
            }
        });

        holder.tvRoomOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onZoneClickListener != null) {
                    onZoneClickListener.onSwitch(false, index);
                }
            }
        });

        holder.ivZoneDevice.setOnClickListener(onZoneMenuListener);
        holder.ivZonePanel.setOnClickListener(onZoneMenuListener);
        holder.ivZoneBreath.setOnClickListener(onZoneMenuListener);
        holder.ivZoneMusic.setOnClickListener(onZoneMenuListener);
        holder.ivZoneAlarm.setOnClickListener(onZoneMenuListener);
        holder.ivZoneEdit.setOnClickListener(onZoneMenuListener);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private View.OnClickListener onZoneMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ACTION_TYPE actionType;
            switch (v.getId()) {
                case R.id.iv_zone_device:
                    actionType = DEVICE_MANAGE;
                    break;
                case R.id.iv_zone_panel:
                    actionType = CONTROL;
                    break;
                case R.id.iv_zone_breath:
                    actionType = BREATH;
                    break;
                case R.id.iv_zone_music:
                    actionType = MUSIC;
                    break;
                case R.id.iv_zone_alarm:
                    actionType = TIMING;
                    break;
                case R.id.iv_zone_edit:
                    actionType = EDIT;
                    break;
                default:
                    actionType = DEVICE_MANAGE;
            }
            if (onZoneClickListener != null) {
                onZoneClickListener.onMenu(actionType, expandPosition);
            }
        }
    };

    private OnZoneClickListener onZoneClickListener;

    //群组点击事件接口
    public interface OnZoneClickListener {
        void onMenu(ACTION_TYPE actionType, int position);

        void onSwitch(boolean isOpen, int position);
    }

    public void setOnZoneMenuListener(OnZoneClickListener onZoneMenuListener) {
        this.onZoneClickListener = onZoneMenuListener;
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_zone_name)
        TextView tvZoneName;
        @BindView(R.id.rl_zone_frame)
        RelativeLayout rlZoneFrame;
        @BindView(R.id.rl_zone_menu)
        RelativeLayout rlZoneMenu;
        @BindView(R.id.tv_room_on)
        TextView tvRoomOn;
        @BindView(R.id.tv_room_off)
        TextView tvRoomOff;
        @BindView(R.id.iv_zone_device)
        ImageView ivZoneDevice;
        @BindView(R.id.iv_zone_panel)
        ImageView ivZonePanel;
        @BindView(R.id.iv_zone_breath)
        ImageView ivZoneBreath;
        @BindView(R.id.iv_zone_music)
        ImageView ivZoneMusic;
        @BindView(R.id.iv_zone_alarm)
        ImageView ivZoneAlarm;
        @BindView(R.id.iv_zone_edit)
        ImageView ivZoneEdit;

        RoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
