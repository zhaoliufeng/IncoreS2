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

public class RoomAdapter extends RecyclerView.Adapter {
    private SparseArray<Room> data;

    public RoomAdapter(SparseArray<Room> data) {
        this.data = data;
    }

    private int expandPosition = -1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_zone, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RoomViewHolder viewHolder = (RoomViewHolder) holder;
        Room room = data.valueAt(position);
        viewHolder.tvZoneName.setText(room.mRoomName);
        if (position == expandPosition) {
            viewHolder.rlZoneMenu.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlZoneMenu.setVisibility(View.GONE);
        }
        viewHolder.rlZoneFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果当前点击的zone item已经展开 则收起菜单
                if (position == expandPosition) {
                    expandPosition = -1;
                } else {
                    expandPosition = position;
                }
                notifyDataSetChanged();
            }
        });

        viewHolder.tvRoomOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onZoneClickListener != null){
                    onZoneClickListener.onSwitch(true, position);
                }
            }
        });

        viewHolder.tvRoomOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onZoneClickListener != null){
                    onZoneClickListener.onSwitch(false, position);
                }
            }
        });

        viewHolder.ivZoneDevice.setOnClickListener(onZoneMenuListener);
        viewHolder.ivZonePanel.setOnClickListener(onZoneMenuListener);
        viewHolder.ivZoneBreath.setOnClickListener(onZoneMenuListener);
        viewHolder.ivZoneMusic.setOnClickListener(onZoneMenuListener);
        viewHolder.ivZoneAlarm.setOnClickListener(onZoneMenuListener);
        viewHolder.ivZoneEdit.setOnClickListener(onZoneMenuListener);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private View.OnClickListener onZoneMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int menuItemIndex = 0;
            switch (v.getId()) {
                case R.id.iv_zone_device:
                    menuItemIndex = 0;
                    break;
                case R.id.iv_zone_panel:
                    menuItemIndex = 1;
                    break;
                case R.id.iv_zone_breath:
                    menuItemIndex = 2;
                    break;
                case R.id.iv_zone_music:
                    menuItemIndex = 3;
                    break;
                case R.id.iv_zone_alarm:
                    menuItemIndex = 4;
                    break;
                case R.id.iv_zone_edit:
                    menuItemIndex = 5;
                    break;
            }
            if (onZoneClickListener != null){
                onZoneClickListener.onMenu(menuItemIndex, expandPosition);
            }
        }
    };

    private OnZoneClickListener onZoneClickListener;

    //群组点击事件接口
    public interface OnZoneClickListener {
        void onMenu(int menuItemIndex, int position);

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
