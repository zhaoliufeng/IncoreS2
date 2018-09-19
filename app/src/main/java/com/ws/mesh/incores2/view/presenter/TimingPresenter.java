package com.ws.mesh.incores2.view.presenter;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Circadian;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.DeviceDAO;
import com.ws.mesh.incores2.db.RoomDAO;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SPUtils;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.ITimingView;

import java.util.Calendar;
import java.util.List;

public class TimingPresenter extends IBasePresenter<ITimingView> {

    private int meshAddress;
    private Device device;
    private Room room;
    private Circadian circadian;
    private SparseArray<Timing> timingSparseArray;

    public SparseArray<Timing> getAlarmList() {
        timingSparseArray = TimingDAO.getInstance().queryAlarmByMeshId(meshAddress);
        return timingSparseArray;
    }

    public void init(int meshAddress) {
        this.meshAddress = meshAddress;
        if (isRoom()) {
            room = CoreData.core().mRoomSparseArray.get(meshAddress);
            circadian = room.circadian;
        } else {
            device = CoreData.core().mDeviceSparseArray.get(meshAddress);
            circadian = device.circadian;
        }
    }

    @SuppressLint("DefaultLocale")
    public String getSunriseTime() {
        if (circadian.dayDurTime == 0) {
            return String.format("%02d:%02d", getDefaultSunriseTime()[0], getDefaultSunriseTime()[1]);
        }
        return String.format("%02d:%02d", circadian.dayStartHours, circadian.dayStartMinutes);
    }

    @SuppressLint("DefaultLocale")
    public String getSunriseDurtime() {
        if (circadian.dayDurTime == 0 || circadian.dayDurTime == 1) {
            return "1 Min";
        }
        return String.format("%d Min", circadian.dayDurTime);
    }

    @SuppressLint("DefaultLocale")
    public String getSunsetTime() {
        if (circadian.nightDurTime == 0) {
            return String.format("%02d:%02d", getDefaultSunsetTime()[0], getDefaultSunsetTime()[1]);
        }
        return String.format("%02d:%02d", circadian.nightStartHours, circadian.nightStartMinutes);
    }

    @SuppressLint("DefaultLocale")
    public String getSunsetDurtime() {
        if (circadian.nightDurTime == 0 || circadian.nightDurTime == 1) {
            return "1 Min";
        }
        return String.format("%d Min", circadian.nightDurTime);
    }

    public int getSunriseSwitchRes() {
        return circadian.isDayOpen ? R.drawable.schedules_switch_on : R.drawable.schedules_switch_off;
    }

    public int getSunsetSwitchRes() {
        return circadian.isNightOpen ? R.drawable.schedules_switch_on : R.drawable.schedules_switch_off;
    }

    private boolean isSunriseOpen() {
        return circadian.isDayOpen;
    }

    private boolean isSunsetOpen() {
        return circadian.isNightOpen;
    }

    /**
     * 关闭定时
     *
     * @param alarmId 定时id
     */
    public void closeAlarm(int alarmId) {
        Timing alarm = timingSparseArray.get(alarmId);
        if (alarm != null) {
            alarm.mIsOpen = false;
            if (TimingDAO.getInstance().updateTiming(alarm)) {
                SendMsg.deleteAlarm(meshAddress, alarmId);
                getView().closeAlarm(true);
            } else {
                getView().closeAlarm(false);
            }
        }
    }

    /**
     * 打开定时
     *
     * @param alarmId 定时id
     */
    public void openAlarm(int alarmId) {
        Timing alarm = timingSparseArray.get(alarmId);
        if (alarm != null) {
            alarm.mIsOpen = true;
            if (alarm.mWeekNum == 0) {
                if (alarm.mUtcTime < System.currentTimeMillis()) {
                    alarm.mUtcTime = alarm.mUtcTime + (1000L * 60L * 60L * 24L);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(alarm.mUtcTime);
                    alarm.mMonths = calendar.get(Calendar.MONTH) + 1;
                    alarm.mDate = calendar.get(Calendar.DAY_OF_MONTH);
                    alarm.mHours = calendar.get(Calendar.HOUR_OF_DAY);
                    alarm.mMins = calendar.get(Calendar.MINUTE);
                }
            }
            setAlarm(alarm);
            if (TimingDAO.getInstance().updateTiming(alarm)) {
                getView().openAlarm(true);
            } else {
                getView().openAlarm(false);
            }
        }
    }

    private void setAlarm(Timing alarm) {
        setDeviceRoomAlarm(alarm);
    }

    private void setDeviceRoomAlarm(Timing alarm) {
        if (alarm.mAlarmEvent < 1) {
            //开关
            SendMsg.openAlarm(meshAddress, alarm.mAId);
        } else {
            //绑定默认场景
            SendMsg.addAlarmScene(meshAddress, alarm, alarm.mAlarmEvent - 1);
        }
    }

    /**
     * 删除定时
     *
     * @param alarmId 定时id
     */
    public void deleteAlarm(int alarmId) {
        if (TimingDAO.getInstance().deleteTiming(timingSparseArray.get(alarmId))) {
            SendMsg.deleteAlarm(meshAddress, alarmId);
            getView().deleteAlarm(true);
        } else {
            getView().deleteAlarm(false);
        }
    }

    public void switchSunset() {
        if (isSunsetOpen()) {
            SendMsg.deleteSunset(meshAddress);
            circadian.isNightOpen = false;
        } else {
            SendMsg.addSunset(circadian.dayStartHours, circadian.dayStartMinutes, circadian.dayDurTime, meshAddress);
            circadian.isNightOpen = true;
        }
        saveCircadian();
        getView().switchCircadian();
    }

    public void switchSunrise() {
        if (isSunriseOpen()) {
            SendMsg.deleteSunrise(meshAddress);
            circadian.isDayOpen = false;
        } else {
            SendMsg.addSunrise(circadian.nightStartHours, circadian.nightStartMinutes, circadian.nightDurTime, meshAddress);
            circadian.isDayOpen = true;
        }
        saveCircadian();
        getView().switchCircadian();
    }

    public void updateCircadian(boolean isRise, int hour, int min, int durtime) {
        if (isRise) {
            updateSunrise(hour, min, durtime);
        } else {
            updateSunset(hour, min, durtime);
        }
        getView().refreshCircadian();
    }

    private void updateSunrise(int hour, int min, int durtime) {
        circadian.dayStartHours = hour;
        circadian.dayStartMinutes = min;
        circadian.dayDurTime = durtime;
        if (circadian.isDayOpen) {
            //如果当前昼夜节律是开启的 就需要更新设备的昼夜节律时间
            SendMsg.addSunrise(hour, min, durtime, meshAddress);
        }
        saveCircadian();
    }

    private void updateSunset(int hour, int min, int durtime) {
        circadian.nightStartHours = hour;
        circadian.nightStartMinutes = min;
        circadian.nightDurTime = durtime;
        if (circadian.isDayOpen) {
            //如果当前昼夜节律是开启的 就需要更新设备的昼夜节律时间
            SendMsg.addSunset(hour, min, durtime, meshAddress);
        }
        saveCircadian();
    }

    private void saveCircadian() {
        if (isRoom()) {
            RoomDAO.getInstance().updateRoom(room);
        } else {
            device.setSunrise(device.circadian.isDayOpen);
            device.setSunset(device.circadian.isNightOpen);
            DeviceDAO.getInstance().updateDevice(device);
        }
    }

    //获取系统昼夜节律时间
    private int[] getDefaultSunriseTime() {
        String[] sunRise = (SPUtils.getSunrise().equals("") ?
                AppConstant.DEFAULT_SUNRISE_TIME : SPUtils.getSunrise()).split(":");

        return new int[]{Integer.valueOf(sunRise[0]), Integer.valueOf(sunRise[1])};
    }

    private int[] getDefaultSunsetTime() {
        String[] sunSet = (SPUtils.getSunset().equals("") ?
                AppConstant.DEFAULT_SUNSET_TIME : SPUtils.getSunset()).split(":");
        return new int[]{Integer.valueOf(sunSet[0]), Integer.valueOf(sunSet[1])};
    }

    //获取小时
    public int getDayHour() {
        return Integer.parseInt(getSunriseTime().split(":")[0]);
    }

    public int getNightHour() {
        return Integer.parseInt(getSunsetTime().split(":")[0]);
    }

    public int getDayMin() {
        return Integer.parseInt(getSunriseTime().split(":")[1]);
    }

    public int getNightMin() {
        return Integer.parseInt(getSunsetTime().split(":")[1]);
    }

    //获取间隔时间
    public int getDayDurTime() {
        return circadian.dayDurTime == 0 ? 1 : circadian.dayDurTime;
    }

    public int getNightDurTime() {
        return circadian.nightDurTime == 0 ? 1 : circadian.nightDurTime;
    }

    public void switchTiming(int alarmId) {
        Timing alarm = timingSparseArray.get(alarmId);
        if (alarm.mIsOpen) {
            closeAlarm(alarmId);
        } else {
            openAlarm(alarmId);
        }
    }

    public boolean isRoom() {
        return meshAddress >= AppConstant.ZONE_START_ID;
    }
}
