package com.ws.mesh.incores2.bean;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.we_smart.sqldao.Annotation.DBFiled;
import com.ws.mesh.incores2.constant.AppConstant;

/**
 * 群组
 * Created by we_smart on 2017/11/15.
 */

public class Room {

    @DBFiled
    public int mRoomId;

    //群组所属网络名
    @DBFiled
    public String mRoomMeshName;

    //群组的名称
    @DBFiled
    public String mRoomName;

    //群组的图标
    @DBFiled
    public String mRoomIcon;

    //群组中的设备id列表字符串 用于存储在数据库中
    @DBFiled(isText = true)
    public String mDeviceIdsStr;

    //群组中的设备id列表集合 用于操作增删
    public SparseArray<Integer> mDeviceIds;

    public Circadian circadian;

    @DBFiled
    public String circadianString;

    public static String devIdToString(SparseArray<Integer> mDevSparseArray) {
        if (mDevSparseArray == null || mDevSparseArray.size() == 0) return "";
        JSONArray jsonArray = new JSONArray();
        for (int x = 0; x < mDevSparseArray.size(); x++) {
            int id = mDevSparseArray.valueAt(x);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(AppConstant.ID, id);
            jsonArray.add(jsonObject);
        }
        return jsonArray.toJSONString();
    }

    @SuppressLint("UseSparseArrays")
    public static SparseArray<Integer> stringToDevId(String devIdText) {
        SparseArray<Integer> mDevIdSparseArray = new SparseArray<>();
        if (TextUtils.isEmpty(devIdText)) return mDevIdSparseArray;
        JSONArray jsonArray = JSON.parseArray(devIdText);
        for (int x = 0; x < jsonArray.size(); x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            int id = jsonObject.getIntValue(AppConstant.ID);
            mDevIdSparseArray.put(id, id);
        }
        return mDevIdSparseArray;
    }

    //Circadian
    public static String circadianToString(Circadian circadian) {
        if (circadian == null) return "";
        return JSON.toJSONString(circadian);
    }

    public static Circadian stringToCircadian(String circadian){
        return JSON.parseObject(circadian, Circadian.class);
    }
}
