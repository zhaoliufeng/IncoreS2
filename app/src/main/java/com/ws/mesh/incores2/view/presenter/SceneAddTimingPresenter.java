package com.ws.mesh.incores2.view.presenter;

import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.constant.TimingType;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.control.CustomTimePicker;
import com.ws.mesh.incores2.view.impl.ISceneAddTimingView;

import java.util.Calendar;

public class SceneAddTimingPresenter extends IBasePresenter<ISceneAddTimingView>{

    private Scene scene;
    private SparseArray<Timing> timingSparseArray;
    private Timing timing;

    public void init(int sceneId, int alarmId){
        timingSparseArray = TimingDAO.getInstance().querySceneAlarm();
        scene = CoreData.core().mSceneSparseArray.get(sceneId);
        if (alarmId != -1){
            timing = timingSparseArray.get(alarmId);
            getView().setTimingInfo(timing);
        }else {
            timing = new Timing();
            timing.mAId = getAlarmId();
            if (timing.mAId == -1){
                //定时数量达到上限
                return;
            }
        }
    }

    public void onSaveSchedule(CustomTimePicker ctp, int weekNum){
        boolean saveSuccess = false;
        if (weekNum != -1){
            //保存当前定时
            timing.mHours = ctp.getCurrentHour();
            timing.mMins = ctp.getCurrentMinute();
            timing.mIsOpen = true;
            timing.mAlarmType = TimingType.SCENE.getValue();
            timing.mParentId = scene.mSceneId;
            if (weekNum == 0) {
                //日月年模式
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timing.mHours);
                calendar.set(Calendar.MINUTE, timing.mMins);
                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    timing.mUtcTime = calendar.getTimeInMillis() + AppConstant.DAY_TIME;
                    timing.mMonths = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    timing.mDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1;
                } else {
                    timing.mUtcTime = calendar.getTimeInMillis();
                    timing.mMonths = calendar.get(Calendar.MONTH) + 1;
                    timing.mDate = calendar.get(Calendar.DAY_OF_MONTH);
                }
            } else {
                //周期模式
                timing.mWeekNum = weekNum;
            }

            if (timingSparseArray.get(timing.mAId) != null){
                saveSuccess = TimingDAO.getInstance().updateTiming(timing);
            }else {
                saveSuccess = TimingDAO.getInstance().insertTiming(timing);
            }

            if (scene.mDevSceneSparseArray.size() > 0) {
                //场景中带有设备 需要一个个发送添加场景定时指令
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0 ; i < scene.mDevSceneSparseArray.size() ; i++) {
                            try {
                                SendMsg.addAlarmScene(scene.mDevSceneSparseArray.valueAt(i).mDeviceID, timing, scene.mSceneId);
                                Thread.sleep(320);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            }
        }else {
            //删除定时
            if (timingSparseArray.get(timing.mAId) != null){
                if (TimingDAO.getInstance().deleteTiming(timing)) {
                    saveSuccess = true;
                    SendMsg.deleteAlarm(0xFFFF, timing.mAId);
                }
            }
        }

        getView().saveSchedule(saveSuccess);
    }

    private int getAlarmId() {
        int finalId = -1;
        for (int id = 7 ; id <= 14 ; id++) {
            boolean haveSameId = false;
            for (int i = 0 ; i < timingSparseArray.size() ; i++) {
                if (timingSparseArray.valueAt(i).mAId == id) {
                    haveSameId = true;
                }
            }
            if (!haveSameId) {
                finalId = id;
                break;
            }
        }
        return finalId;
    }
}
