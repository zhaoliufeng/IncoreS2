package com.ws.mesh.incores2.bean;

import android.text.TextUtils;
import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.we_smart.sqldao.Annotation.DBFiled;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.constant.ColorParamsConstant;

/**
 * 场景
 * Created by we_smart on 2017/11/15.
 */

public class Scene {

    public Scene(){}

    public Scene(int mSceneId, String mSceneName, String mSceneIcon) {
        this.mSceneId = mSceneId;
        this.mSceneName = mSceneName;
        this.mSceneIcon = mSceneIcon;
    }

    //场景的id
    @DBFiled
    public int mSceneId;

    //群组所属网络名
    @DBFiled
    public String mSceneMeshName;

    //场景的名称
    @DBFiled
    public String mSceneName;

    //场景的图标
    @DBFiled
    public String mSceneIcon;

    //场景中设备集合绑定动作的字符串 用于存储数据库
    @DBFiled(isText = true)
    public String mDeviceScenesStr;

    //场景的设备
    public SparseArray<SceneColor> mDevSceneSparseArray;

    //解析场景的数据
    public static SparseArray<SceneColor> stringToScene(String sceneText) {
        SparseArray<SceneColor> sceneSparseArray = new SparseArray<>();
        if (TextUtils.isEmpty(sceneText)) return sceneSparseArray;
        JSONArray jsonArray = JSONArray.parseArray(sceneText);
        for (int x = 0 ; x < jsonArray.size() ; x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            if (jsonObject != null) {
                SceneColor sceneColor = new SceneColor();
                sceneColor.mDeviceID = jsonObject.getInteger(AppConstant.ID);
                sceneColor.mSceneBlue = jsonObject.getIntValue(ColorParamsConstant.BLUE);
                sceneColor.mSceneRed = jsonObject.getIntValue(ColorParamsConstant.RED);
                sceneColor.mSceneGreen = jsonObject.getIntValue(ColorParamsConstant.GREEN);
                sceneColor.mSceneWarm = jsonObject.getIntValue(ColorParamsConstant.WARM);
                sceneColor.mSceneCold = jsonObject.getIntValue(ColorParamsConstant.COLD);
                sceneColor.mSceneMode = jsonObject.getIntValue(ColorParamsConstant.MODE);
                sceneColor.mSceneBrightness = jsonObject.getIntValue(ColorParamsConstant.BRIGHTNESS);
                sceneSparseArray.put(sceneColor.mDeviceID, sceneColor);
            }
        }
        return sceneSparseArray;
    }

    //封装场景成String,其中ObjectId可以为群组和设备的ID
    public static String sceneToString(SparseArray<SceneColor> sceneColorSparseArray) {
        if (sceneColorSparseArray == null || sceneColorSparseArray.size() == 0) return "";
        JSONArray jsonArray = new JSONArray();
        for (int x = 0 ; x < sceneColorSparseArray.size() ; x++) {
            SceneColor color = sceneColorSparseArray.valueAt(x);
            if (color != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(ColorParamsConstant.RED, color.mSceneRed);
                jsonObject.put(ColorParamsConstant.GREEN, color.mSceneGreen);
                jsonObject.put(ColorParamsConstant.BLUE, color.mSceneBlue);
                jsonObject.put(ColorParamsConstant.WARM, color.mSceneWarm);
                jsonObject.put(ColorParamsConstant.COLD, color.mSceneCold);
                jsonObject.put(ColorParamsConstant.MODE, color.mSceneMode);
                jsonObject.put(ColorParamsConstant.BRIGHTNESS, color.mSceneBrightness);
                jsonObject.put(AppConstant.ID, color.mDeviceID);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray.toJSONString();
    }
}
