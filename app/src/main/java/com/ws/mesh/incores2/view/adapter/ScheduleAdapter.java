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
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.TimingType;
import com.ws.mesh.incores2.db.DeviceDAO;
import com.ws.mesh.incores2.db.RoomDAO;
import com.ws.mesh.incores2.db.SceneDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.ViewUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleAdapter extends RecyclerView.Adapter {

    private List<Timing> timingList;
    private Context context;

    public ScheduleAdapter(List<Timing> data) {
        this.timingList = new ArrayList<>();
        for (Timing timing : data){
            if (timing.mAlarmType == TimingType.DEVICE.getValue()){
                //如果是设备的定时 则需要判断设备在不在先 如果在线才显示
                Device device = CoreData.core().mDeviceSparseArray.get(timing.mParentId);
                if (device != null
                        && device.mConnectionStatus != ConnectionStatus.OFFLINE){
                    this.timingList.add(timing);
                }
            }else {
                this.timingList.add(timing);
            }
        }

        //按照执行时间排序
        Collections.sort(timingList, new Comparator<Timing>() {
            @Override
            public int compare(Timing o1, Timing o2) {
                //比较执行小时 * 100 + 执行分钟的大小
                if (o1.mHours * 100 + o1.mMins > o2.mHours * 100 + o2.mMins)
                    return 1;
                else if (o1.mHours * 100 + o1.mMins == o2.mHours * 100 + o2.mMins)
                    return 0;
                return -1;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        context = parent.getContext();
        return new TimingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimingViewHolder viewHolder = (TimingViewHolder) holder;
        int viewPosition = viewHolder.getAdapterPosition();
        Timing timing = timingList.get(viewPosition);
        int listRes = R.drawable.icon_list_center;
        if (getItemCount() == 1) {
            listRes = R.drawable.icon_list_single;
        } else if (position == 0) {
            listRes = R.drawable.icon_list_top;
        } else if (position == getItemCount() - 1) {
            listRes = R.drawable.icon_list_bottom;
        }
        viewHolder.ivListIcon.setImageResource(listRes);

        String[] timeShow = ViewUtils.getAlarmShow(timing.mHours, timing.mMins);
        String time = String.format("%s %s", timeShow[0], timeShow[1]);
        viewHolder.tvTime.setText(time);
        viewHolder.tvName.setText(getParentName(timing.mParentId, timing.mAlarmType));
        viewHolder.tvRepeatDay.setText(getExecuteInfo(timing.mWeekNum));
        viewHolder.tvEvent.setText(getExecuteEvent(timing.mAlarmEvent));
    }

    @Override
    public int getItemCount() {
        return timingList.size();
    }

    public class TimingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_list_icon)
        ImageView ivListIcon;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_event)
        TextView tvEvent;
        @BindView(R.id.tv_repeat_day)
        TextView tvRepeatDay;

        TimingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //获取执行的时间
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

    //获取执行的动作
    private String getExecuteEvent(int eventId) {
        return context.getResources().getStringArray(R.array.timing_events)[eventId];
    }


    //获取定时所属父类的名称
    private String getParentName(int parentId, int alarmType) {
        if (alarmType == TimingType.SCENE.getValue()) {
            return CoreData.core().mSceneSparseArray.get(parentId).mSceneName;
        } else if (alarmType == TimingType.DEVICE.getValue()) {
            return CoreData.core().mDeviceSparseArray.get(parentId).mDevName;
        } else if (alarmType == TimingType.ZONE.getValue()) {
            return CoreData.core().mRoomSparseArray.get(parentId).mRoomName;
        } else {
            throw new RuntimeException("No Such AlarmType");
        }
    }
}
