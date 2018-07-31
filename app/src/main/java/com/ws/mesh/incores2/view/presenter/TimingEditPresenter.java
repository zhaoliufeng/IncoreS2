package com.ws.mesh.incores2.view.presenter;

import android.content.Context;
import android.util.SparseArray;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.ITimingEditView;

import java.util.Calendar;

/**
 * Created by we_smart on 2017/11/23.
 */

public class TimingEditPresenter extends IBasePresenter<ITimingEditView> {

    public SparseArray<Timing> mAlarmSparseArray;
    private int mMeshAddress;
    private Context mContext;

    private final int DEVICE = 0;
    private final int ROOM = 1;
    private final int SCENE = 2;

    public TimingEditPresenter(Context context) {
        mContext = context;
        mAlarmSparseArray = new SparseArray<>();
    }

    public void init(int meshAddress) {
        this.mMeshAddress = meshAddress;
        getAlarmList();
    }

    /**
     * 删除定时
     *
     * @param alarmId 定时id
     */
    public void deleteAlarm(int alarmId) {
        if (TimingDAO.getInstance().deleteTiming(mAlarmSparseArray.get(alarmId))) {
            SendMsg.deleteAlarm(mMeshAddress, alarmId);
            getView().deleteAlarm(true);
        } else {
            getView().deleteAlarm(false);
        }
    }

    //创建新的闹钟
    public void addAlarm(int hours, int mins, int sceneId, int weeknums, int meshAddress, int alarmId) {
        SendMsg.updateDeviceTime();
        Timing alarm = packAlarm(hours, mins, sceneId, weeknums, meshAddress, alarmId);
        if (alarm != null) {
            if (TimingDAO.getInstance().insertTiming(alarm)) {
                //发送指令
                SendMsg.addAlarm(meshAddress, alarm);
                getView().addAlarm(true);
            } else {
                getView().addAlarm(false);
            }
        } else {
            getView().addAlarm(false);
        }
    }

    public void updateAlarm(int hours, int mins, int sceneId, int weeknums, int meshAddress, int alarmId) {
        SendMsg.updateDeviceTime();
        Timing alarm = packAlarm(hours, mins, sceneId, weeknums, meshAddress, alarmId);
        if (alarm != null) {
            alarm.mAlarmType = meshAddress < 0x80000 ? 1 : 2;
            if (TimingDAO.getInstance().updateTiming(alarm)) {
                //发送指令
                SendMsg.addAlarm(meshAddress, alarm);
                getView().updateAlarm(true);
            } else {
                getView().updateAlarm(false);
            }
        } else {
            getView().updateAlarm(false);
        }
    }

    private Timing packAlarm(int hours, int mins, int sceneId, int weeknums, int meshAddress, int alarmId) {
        Timing alarm = new Timing();
        alarm.mAId = alarmId == -1 ? getAlarmId() : alarmId;
        if (alarm.mAId == -1) {
            //无可用Id
            getView().maximumNumber();
            return null;
        }

        alarm.mWeekNum = 0;
        alarm.mAlarmType = mMeshAddress < 0x8000 ? 1 : 2;
        alarm.mAlarmEvent = sceneId;
        alarm.mHours = hours;
        alarm.mMins = mins;
        alarm.mIsOpen = true;
        alarm.mMonths = 0;
        alarm.mTotalTime = hours * 60 + mins;
        alarm.mSec = 0;
        alarm.mDesc = "";

        if (weeknums == 0) {
            //日月年模式
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, mins);
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                alarm.mUtcTime = calendar.getTimeInMillis() + AppConstant.DAY_TIME;
                alarm.mMonths = Calendar.getInstance().get(Calendar.MONTH) + 1;
                alarm.mDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1;
            } else {
                alarm.mUtcTime = calendar.getTimeInMillis();
                alarm.mMonths = calendar.get(Calendar.MONTH) + 1;
                alarm.mDate = calendar.get(Calendar.DAY_OF_MONTH);
            }
        } else {
            //周期模式
            alarm.mWeekNum = weeknums;
        }

        alarm.mParentId = meshAddress;
        return alarm;
    }

    //获取当前可用闹钟id
    public int getAlarmId() {
        if (mMeshAddress < 0x8000) {
            //设备有2个定时 1 - 2
            for (int id = 1; id <= 2; id++) {
                if (mAlarmSparseArray.get(id) == null) {
                    return id;
                }
            }
        } else {
            //房间有4个定时 3 - 6
            for (int id = 3; id <= 6; id++) {
                if (mAlarmSparseArray.get(id) == null) {
                    return id;
                }
            }
        }
        return -1;
    }

    public SparseArray<Timing> getAlarmList() {
        mAlarmSparseArray = TimingDAO.getInstance().queryAlarmByMeshId(mMeshAddress);
        return mAlarmSparseArray;
    }

    //获取执行的时间
    public String getExecuteInfo(int weekNum) {
        if (weekNum == 0)
            return mContext.getString(R.string.never_repeat);
        if (weekNum == 127)
            return mContext.getString(R.string.every_day);
        if (weekNum == 62)
            return mContext.getString(R.string.work_day);

        byte[] weeks = ViewUtils.reverseBytes(ViewUtils.weekNumToBinaryByteArray(weekNum));
        String[] weekString = mContext.getResources().getStringArray(R.array.custom_week_data);
        StringBuilder showString = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (weeks[i] == 1) {
                showString.append(weekString[i]).append(",");
            }
        }
        return showString.substring(0, showString.length() - 1);
    }

    //获取执行的动作
    public String getExecuteEvent(int eventId) {
        return mContext.getResources().getStringArray(R.array.timing_events)[eventId];
    }
}
