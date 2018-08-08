package com.ws.mesh.incores2.utils.shareuitls;

import android.util.Log;
import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.we_smart.lansharelib.shareconstant.ShareConstant;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.db.RoomDAO;

/**
 * Created by we_smart on 2018/4/26.
 */

public class ShareRoomUtils {
    private static final String TAG = "ShareRoomUtils";

    public static JSONArray getGroupData(SparseArray<Room> sparseArray) {
        JSONArray jsonArray = new JSONArray();
        if (sparseArray == null || sparseArray.size() == 0) return jsonArray;
        for (int x = 0 ; x < sparseArray.size() ; x++) {
            JSONObject jsonObject = new JSONObject();
            Room room = sparseArray.valueAt(x);
            jsonObject.put(ShareConstant.RoomConstant.ICON, room.mRoomIcon);
            jsonObject.put(ShareConstant.RoomConstant.NAME, room.mRoomName);
            jsonObject.put(ShareConstant.RoomConstant.MESH_ID, room.mRoomId);
            jsonObject.put(ShareConstant.RoomConstant.DEV_ARRAY, room.mDeviceIdsStr);
            jsonObject.put(ShareConstant.RoomConstant.CIRADIAN, room.circadianString);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public static void parseGroupData(JSONArray jsonArray, String meshName) {
        Log.i(TAG, "Room" + ":" + jsonArray.toJSONString());
        if (jsonArray.size() == 0) return;
        for (int x = 0 ; x < jsonArray.size() ; x++) {
            Room room = new Room();
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            room.mDeviceIdsStr = jsonObject.getString(ShareConstant.RoomConstant.DEV_ARRAY);
            room.mDeviceIds = Room.stringToDevId(room.mDeviceIdsStr);
            room.mRoomMeshName = meshName;
            room.mRoomIcon = jsonObject.getString(ShareConstant.RoomConstant.ICON);
            room.mRoomName = jsonObject.getString(ShareConstant.RoomConstant.NAME);
            room.mRoomId = jsonObject.getIntValue(ShareConstant.RoomConstant.MESH_ID);
            room.circadianString = jsonObject.getString(ShareConstant.RoomConstant.CIRADIAN);
            room.circadian = Room.stringToCircadian(room.circadianString);
            boolean isOk = RoomDAO.getInstance().insertShareRoom(room);
            Log.i(TAG, room.mRoomName + ":" + isOk);
        }

    }
}
