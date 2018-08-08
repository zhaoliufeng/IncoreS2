package com.ws.mesh.incores2.utils.shareuitls;

import android.util.Log;
import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.we_smart.lansharelib.shareconstant.ShareConstant;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.db.SceneDAO;
import com.ws.mesh.incores2.db.TimingDAO;

/**
 * Created by we_smart on 2018/4/26.
 */

public class ShareSceneUtils {

    private static final String TAG = "ShareSceneUtils";

    public static JSONArray getSceneData(SparseArray<Scene> sparseArray, String meshName) {
        JSONArray jsonArray = new JSONArray();
        if (sparseArray == null || sparseArray.size() == 0) return jsonArray;
        for (int x = 0 ; x < sparseArray.size() ; x++) {
            JSONObject jsonObject = new JSONObject();
            Scene scene = sparseArray.valueAt(x);
            jsonObject.put(ShareConstant.Scene.ID, scene.mSceneId);
            jsonObject.put(ShareConstant.Scene.NAME, scene.mSceneName);
            jsonObject.put(ShareConstant.Scene.ICON, scene.mSceneIcon);
            jsonObject.put(ShareConstant.Scene.CONDITION_TYPE, 1);
            jsonObject.put(ShareConstant.Scene.CONDITION, ShareAlarmUtils.getSceneAlarmJsonArray
                    (TimingDAO.getInstance().querySceneAlarmWithSceneId(scene.mSceneId, meshName)));
            jsonObject.put(ShareConstant.Scene.TASK, Scene.sceneToString(scene.mDevSceneSparseArray));
            jsonArray.add(jsonObject);
        }
        Log.i(TAG, JSONObject.toJSONString(jsonArray));
        return jsonArray;
    }

    public static void parseSceneData(JSONArray jsonArray, String meshName) {
        if (jsonArray == null || jsonArray.size() == 0) return;
        for (int x = 0 ; x < jsonArray.size() ; x++) {
            Scene scene = new Scene();
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            scene.mDeviceScenesStr = jsonObject.getString(ShareConstant.Scene.TASK);
            scene.mDevSceneSparseArray = Scene.stringToScene(scene.mDeviceScenesStr);
            Log.i(TAG, "mDeviceScenesStr==" + scene.mDeviceScenesStr);
            scene.mSceneMeshName = meshName;
            scene.mSceneId = jsonObject.getIntValue(ShareConstant.Scene.ID);
            scene.mSceneIcon = jsonObject.getString(ShareConstant.Scene.ICON);
            scene.mSceneName = jsonObject.getString(ShareConstant.Scene.NAME);
            if (jsonObject.getIntValue(ShareConstant.Scene.CONDITION_TYPE) == 1) {
                ShareAlarmUtils.parseSceneAlarmJson(JSONArray.parseArray(jsonObject.getString(ShareConstant.Scene.CONDITION)), meshName, scene.mSceneId);
            }
            SceneDAO.getInstance().insertShareScene(scene);
        }

    }
}
