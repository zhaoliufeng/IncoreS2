package com.ws.mesh.incores2.utils.shareuitls;

import android.util.Log;
import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.we_smart.lansharelib.shareconstant.ShareConstant;
import com.ws.mesh.incores2.bean.FavoriteColor;
import com.ws.mesh.incores2.db.FColorDAO;

public class ShareFColorUtils {
    private static final String TAG = "ShareFColorUtils";

    public static JSONArray getGroupData(SparseArray<com.ws.mesh.incores2.bean.FavoriteColor> sparseArray) {
        JSONArray jsonArray = new JSONArray();
        if (sparseArray == null || sparseArray.size() == 0) return jsonArray;
        for (int x = 0; x < sparseArray.size(); x++) {
            JSONObject jsonObject = new JSONObject();
            com.ws.mesh.incores2.bean.FavoriteColor fColor = sparseArray.valueAt(x);
            jsonObject.put(ShareConstant.FavoriteColor.C_INDEX, fColor.cIndex);
            jsonObject.put(ShareConstant.FavoriteColor.MESH_NAME, fColor.meshName);
            jsonObject.put(ShareConstant.FavoriteColor.DEVICE_TYPE, fColor.deviceType);
            jsonObject.put(ShareConstant.FavoriteColor.RED, fColor.red);
            jsonObject.put(ShareConstant.FavoriteColor.GREEN, fColor.green);
            jsonObject.put(ShareConstant.FavoriteColor.BLUE, fColor.blue);
            jsonObject.put(ShareConstant.FavoriteColor.WARM, fColor.warm);
            jsonObject.put(ShareConstant.FavoriteColor.COLD, fColor.cold);
            jsonObject.put(ShareConstant.FavoriteColor.BRIGHTNESS, fColor.brightness);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public static void parseFColorData(JSONArray jsonArray, String meshName) {
        Log.i(TAG, "Room" + ":" + jsonArray.toJSONString());
        if (jsonArray.size() == 0) return;
        for (int x = 0; x < jsonArray.size(); x++) {
            FavoriteColor fColor = new FavoriteColor();
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            /**
             * FavoriteColor favoriteColor = new FavoriteColor();
             favoriteColor.cIndex = colorSparseArray.size();
             favoriteColor.parentChannel = channel;
             favoriteColor.red = r;
             favoriteColor.green = g;
             favoriteColor.blue = b;
             favoriteColor.warm = w;
             favoriteColor.cold = c;
             favoriteColor.brightness = brightness;
             */
            fColor.cIndex = jsonObject.getInteger(ShareConstant.FavoriteColor.C_INDEX);
            fColor.meshName = meshName;
            fColor.deviceType = jsonObject.getInteger(ShareConstant.FavoriteColor.DEVICE_TYPE);
            fColor.red = jsonObject.getInteger(ShareConstant.FavoriteColor.RED);
            fColor.green = jsonObject.getInteger(ShareConstant.FavoriteColor.GREEN);
            fColor.blue = jsonObject.getInteger(ShareConstant.FavoriteColor.BLUE);
            fColor.warm = jsonObject.getInteger(ShareConstant.FavoriteColor.COLD);
            fColor.cold = jsonObject.getInteger(ShareConstant.FavoriteColor.C_INDEX);
            fColor.brightness = jsonObject.getInteger(ShareConstant.FavoriteColor.BRIGHTNESS);
            boolean isOk = FColorDAO.getInstance().insertShareFColor(fColor);
            Log.i(TAG, fColor.meshName + ":" + isOk);
        }

    }
}
