package com.ws.mesh.incores2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ws.mesh.incores2.MeshApplication;

/**
 * Created by we_smart on 2017/11/20.
 */

public class SPUtils {

    private static SharedPreferences mSharePreferences;

    private static final String MESH = "mesh";

    private static final String ADD_DEVICE = "add_device";

    private static final String SHARE_DATA = "send_data";

    private static final String RECEIVE_DATA = "receive_data";

    private static final String SKIN = "skin";

    private static final String LOCAL_NEWEST_VERSION = "local_newest_version";

    private static final String APP_NAME = "Mesh_Life";

    private static final String NEED_UPDATE = "need_update";

    //初始化
    public static void init(Context context) {
        mSharePreferences = context.getSharedPreferences
                (MESH, Context.MODE_PRIVATE);
    }

    //存string类型的值
    public static void saveString(String key, String values) {
        checkNull();
        SharedPreferences.Editor editor = mSharePreferences.edit();
        editor.putString(key, values);
        editor.apply();
    }

    //存boolean类型的值
    public static void saveBoolean(String key, boolean values) {
        checkNull();
        SharedPreferences.Editor editor = mSharePreferences.edit();
        editor.putBoolean(key, values);
        editor.apply();
    }

    //存int类型的值
    public static void saveInteger(String key, int values) {
        checkNull();
        SharedPreferences.Editor editor = mSharePreferences.edit();
        editor.putInt(key, values);
        editor.apply();
    }

    private static void checkNull() {
        if (mSharePreferences == null) init(MeshApplication.getInstance());
    }

    public static String getString(String key) {
        checkNull();
        return mSharePreferences.getString(key, "");
    }

    public static boolean getBoolean(String key, boolean defValue) {
        checkNull();
        return mSharePreferences.getBoolean(key, defValue);
    }

    public static int getInteger(String key, int defValue) {
        checkNull();
        return mSharePreferences.getInt(key, defValue);
    }

    public static void setLatelyMesh(String meshName) {
        saveString(MESH, meshName);
    }

    public static String getLatelyMesh() {
        return getString(MESH);
    }

    public static void setAddDevice(boolean isFirstAdd) {
        saveBoolean(ADD_DEVICE, isFirstAdd);
    }

    public static boolean getAddDevice() {
        return getBoolean(ADD_DEVICE, false);
    }

    public static String getSendShareData() {
        return getString(SHARE_DATA);
    }

    public static void setSendShareData(String shareStr) {
        saveString(SHARE_DATA, shareStr);
    }

    public static String getReceiveShareData() {
        return getString(RECEIVE_DATA);
    }

    public static void setReceiveShareData(String receiveStr) {
        saveString(RECEIVE_DATA, receiveStr);
    }

    public static void saveSkin(int skin) {
        saveInteger(SKIN, skin);
    }

    public static int getSkin() {
        return getInteger(SKIN, 0);
    }

    //保存读取当前最新的版本号
    public static void saveNewestVersion(String version) {
        saveString(LOCAL_NEWEST_VERSION, version);
    }

    public static String getNewestVersion() {
        return getString(LOCAL_NEWEST_VERSION);
    }

    //保存读取是否需要更新
    public static void saveNeedUpdate(boolean needUpdate) {
        saveBoolean(NEED_UPDATE, needUpdate);
    }

    public static boolean getNeedUpdate() {
        return getBoolean(NEED_UPDATE, false);
    }
}
