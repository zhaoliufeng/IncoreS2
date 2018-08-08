package com.ws.mesh.incores2.utils.shareuitls;

import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.we_smart.lansharelib.shareconstant.ShareConstant;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.TimingType;
import com.ws.mesh.incores2.db.TimingDAO;


/**
 * Created by we_smart on 2018/4/26.
 */

public class ShareAlarmUtils {

    //设备定时
    public static JSONArray getDevAlarmJsonArray(SparseArray<Timing> sparseArray) {
        JSONArray jsonArray = new JSONArray();
        if (sparseArray == null || sparseArray.size() == 0) return jsonArray;
        for (int x = 0 ; x < sparseArray.size() ; x++) {
            Timing alarm = sparseArray.valueAt(x);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ShareConstant.AlarmConstant.ID, alarm.mAId);
            jsonObject.put(ShareConstant.AlarmConstant.HOURS, alarm.mHours);
            jsonObject.put(ShareConstant.AlarmConstant.MINUTE, alarm.mMins);
            jsonObject.put(ShareConstant.AlarmConstant.EVENTS, alarm.mAlarmEvent);
            jsonObject.put(ShareConstant.AlarmConstant.WEEK, alarm.mWeekNum);
            jsonObject.put(ShareConstant.AlarmConstant.UTC, alarm.mUtcTime);
            jsonObject.put(ShareConstant.AlarmConstant.STATUS, alarm.mIsOpen);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    //群组定时
    public static JSONArray getGroAlarmJsonArray(SparseArray<Timing> sparseArray) {
        JSONArray jsonArray = new JSONArray();
        if (sparseArray == null || sparseArray.size() == 0) return jsonArray;
        for (int x = 0 ; x < sparseArray.size() ; x++) {
            Timing alarm = sparseArray.valueAt(x);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ShareConstant.AlarmConstant.ID, alarm.mAId);
            jsonObject.put(ShareConstant.AlarmConstant.HOURS, alarm.mHours);
            jsonObject.put(ShareConstant.AlarmConstant.MINUTE, alarm.mMins);
            jsonObject.put(ShareConstant.AlarmConstant.EVENTS, alarm.mAlarmEvent);
            jsonObject.put(ShareConstant.AlarmConstant.WEEK, alarm.mWeekNum);
            jsonObject.put(ShareConstant.AlarmConstant.UTC, alarm.mUtcTime);
            jsonObject.put(ShareConstant.AlarmConstant.STATUS, alarm.mIsOpen);
            jsonObject.put(ShareConstant.AlarmConstant.GROUPID, alarm.mParentId);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    //场景的条件
    public static JSONArray getSceneAlarmJsonArray(SparseArray<Timing> sparseArray) {
        JSONArray jsonArray = new JSONArray();
        if (sparseArray == null || sparseArray.size() == 0) return jsonArray;
        for (int x = 0 ; x < sparseArray.size() ; x++) {
            Timing alarm = sparseArray.valueAt(x);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ShareConstant.AlarmConstant.ID, alarm.mAId);
            jsonObject.put(ShareConstant.AlarmConstant.HOURS, alarm.mHours);
            jsonObject.put(ShareConstant.AlarmConstant.MINUTE, alarm.mMins);
            jsonObject.put(ShareConstant.AlarmConstant.WEEK, alarm.mWeekNum);
            jsonObject.put(ShareConstant.AlarmConstant.STATUS, alarm.mIsOpen);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    //解析设备的定时
    public static void parseDevAlarmJson(JSONArray jsonArray, String meshName, int meshId) {
        if (jsonArray == null || jsonArray.size() == 0) return;
        for (int x = 0 ; x < jsonArray.size() ; x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            Timing alarm = new Timing();
            alarm.mAlarmMeshName = meshName;
            alarm.mAId = jsonObject.getIntValue(ShareConstant.AlarmConstant.ID);
            alarm.mAlarmEvent = jsonObject.getIntValue(ShareConstant.AlarmConstant.EVENTS);
            alarm.mAlarmType = TimingType.DEVICE.getValue();
            alarm.mHours = jsonObject.getIntValue(ShareConstant.AlarmConstant.HOURS);
            alarm.mMins = jsonObject.getIntValue(ShareConstant.AlarmConstant.MINUTE);
            alarm.mIsOpen = jsonObject.getBooleanValue(ShareConstant.AlarmConstant.STATUS);
            alarm.mUtcTime = jsonObject.getLongValue(ShareConstant.AlarmConstant.UTC);
            alarm.mParentId = meshId;
            alarm.mWeekNum = jsonObject.getIntValue(ShareConstant.AlarmConstant.WEEK);
            TimingDAO.getInstance().insertShareAlarm(alarm);
        }
    }

    //解析群组的定时
    public static void parseGroAlarmJson(JSONArray jsonArray, String meshName) {
        if (jsonArray == null || jsonArray.size() == 0) return;
        for (int x = 0 ; x < jsonArray.size() ; x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            Timing alarm = new Timing();
            alarm.mAlarmMeshName = meshName;
            alarm.mAId = jsonObject.getIntValue(ShareConstant.AlarmConstant.ID);
            alarm.mAlarmEvent = jsonObject.getIntValue(ShareConstant.AlarmConstant.EVENTS);
            alarm.mAlarmType = TimingType.ZONE.getValue();
            alarm.mHours = jsonObject.getIntValue(ShareConstant.AlarmConstant.HOURS);
            alarm.mMins = jsonObject.getIntValue(ShareConstant.AlarmConstant.MINUTE);
            alarm.mIsOpen = jsonObject.getBooleanValue(ShareConstant.AlarmConstant.STATUS);
            alarm.mUtcTime = jsonObject.getLongValue(ShareConstant.AlarmConstant.UTC);
            alarm.mParentId = jsonObject.getIntValue(ShareConstant.AlarmConstant.GROUPID);
            alarm.mWeekNum = jsonObject.getIntValue(ShareConstant.AlarmConstant.WEEK);
            TimingDAO.getInstance().insertShareAlarm(alarm);
        }
    }

    //解析场景的定时
    public static void parseSceneAlarmJson(JSONArray jsonArray, String meshName, int sceneId) {
        if (jsonArray == null || jsonArray.size() == 0) return;
        for (int x = 0 ; x < jsonArray.size() ; x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            Timing alarm = new Timing();
            alarm.mAlarmMeshName = meshName;
            alarm.mAId = jsonObject.getIntValue(ShareConstant.AlarmConstant.ID);
            alarm.mAlarmType = TimingType.SCENE.getValue();
            alarm.mHours = jsonObject.getIntValue(ShareConstant.AlarmConstant.HOURS);
            alarm.mMins = jsonObject.getIntValue(ShareConstant.AlarmConstant.MINUTE);
            alarm.mIsOpen = jsonObject.getBooleanValue(ShareConstant.AlarmConstant.STATUS);
            alarm.mParentId = sceneId;
            alarm.mWeekNum = jsonObject.getIntValue(ShareConstant.AlarmConstant.WEEK);
            TimingDAO.getInstance().insertShareAlarm(alarm);
        }
    }

}
