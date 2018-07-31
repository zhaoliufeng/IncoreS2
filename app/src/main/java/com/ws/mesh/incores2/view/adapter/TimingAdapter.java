package com.ws.mesh.incores2.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimingAdapter extends RecyclerView.Adapter {

    private SparseArray<Timing> timingList;
    private Context context;
    //编辑模式
    private boolean editMode = false;
    private OnTimingActionListener onTimingActionListener;

    public enum Action {
        SWITCH, EDIT, DELETE
    }

    public TimingAdapter(SparseArray<Timing> data) {
        this.timingList = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_timing, parent, false);
        context = parent.getContext();
        return new TimingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final TimingViewHolder viewHolder = (TimingViewHolder) holder;
        Timing timing = timingList.valueAt(viewHolder.getAdapterPosition());
        //设置右边柱状指示
        int listRes = R.drawable.icon_list_center;
        if (getItemCount() == 1) {
            listRes = R.drawable.icon_list_single;
        } else if (position == 0) {
            listRes = R.drawable.icon_list_top;
        } else if (position == getItemCount() - 1) {
            listRes = R.drawable.icon_list_bottom;
        }
        viewHolder.ivListIcon.setImageResource(listRes);
        if (editMode) {
            viewHolder.ivEditTiming.setVisibility(View.VISIBLE);
            viewHolder.ivDeleteTiming.setVisibility(View.VISIBLE);
            viewHolder.ivTimingSwitch.setVisibility(View.GONE);
        } else {
            viewHolder.ivEditTiming.setVisibility(View.GONE);
            viewHolder.ivDeleteTiming.setVisibility(View.GONE);
            viewHolder.ivTimingSwitch.setVisibility(View.VISIBLE);
        }

        //设置定时显示信息
        String[] timeArray = ViewUtils.getAlarmShow(timing.mHours, timing.mMins);
        viewHolder.tvTime.setText(timeArray[0] + " " + timeArray[1]);
        viewHolder.tvRepeatDay.setText(getExecuteInfo(timing.mWeekNum));

        viewHolder.ivTimingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTimingActionListener != null) {
                    onTimingActionListener.onAction(Action.SWITCH, viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.ivEditTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTimingActionListener != null) {
                    onTimingActionListener.onAction(Action.EDIT, viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.ivDeleteTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTimingActionListener != null) {
                    onTimingActionListener.onAction(Action.DELETE, viewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return timingList.size();
    }

    public class TimingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_list_icon)
        ImageView ivListIcon;
        @BindView(R.id.iv_edit_timing)
        ImageView ivEditTiming;
        @BindView(R.id.iv_delete_timing)
        ImageView ivDeleteTiming;
        @BindView(R.id.iv_timing_switch)
        ImageView ivTimingSwitch;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_repeat_day)
        TextView tvRepeatDay;
        @BindView(R.id.iv_event)
        ImageView ivEvent;


        TimingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnTimingActionListener {
        void onAction(Action action, int position);
    }

    public void setOnTimingActionListener(OnTimingActionListener listener) {
        this.onTimingActionListener = listener;
    }

    //设置编辑模式
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    private String getExecuteInfo(int weekNum) {
        if (weekNum == 0)
            return context.getString(R.string.never_repeat);
        if (weekNum == 127)
            return context.getString(R.string.every_day);
        if (weekNum == 62)
            return context.getString(R.string.work_day);

        byte[] weeks = ViewUtils.reverseBytes(ViewUtils.weekNumToBinaryByteArray(weekNum));
        String[] weekString = context.getResources().getStringArray(R.array.custom_week_data);
        StringBuilder showString = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (weeks[i] == 1) {
                showString.append(weekString[i]).append(",");
            }
        }
        return showString.substring(0, showString.length() - 1);
    }
}
