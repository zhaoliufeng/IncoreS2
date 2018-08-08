package com.ws.mesh.incores2.utils.shareuitls;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.we_smart.lansharelib.shareconstant.ShareConstant;
import com.we_smart.lansharelib.tools.SystemUtil;
import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.bean.ShareManageBean;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.DeviceDAO;
import com.ws.mesh.incores2.db.FColorDAO;
import com.ws.mesh.incores2.db.RoomDAO;
import com.ws.mesh.incores2.db.SceneDAO;
import com.ws.mesh.incores2.utils.CoreData;

/**
 * Created by we_smart on 2018/4/26.
 */

public class ShareDataUtils {

    public static String getShareData(Mesh mesh) {
        JSONObject jsonObject = new JSONObject();
        if (mesh == null) return jsonObject.toJSONString();
        String info = SystemUtil.getDeviceBrand() + SystemUtil.getSystemModel();
        jsonObject.put(ShareConstant.SHARE_UTC, System.currentTimeMillis());
        String packageName = "com.ws.mesh.Smart-FlexLink";
        jsonObject.put(ShareConstant.PROPERTY, packageName);
        jsonObject.put(ShareConstant.VERSION, AppConstant.SHARE_VERSION);
        jsonObject.put(ShareConstant.SHARER_INFO, info);
        jsonObject.put(ShareConstant.MESH_DATA, getData(mesh, info));
        return jsonObject.toJSONString();
    }

    public static ShareManageBean parseShareData(String str) {
        if (TextUtils.isEmpty(str)) return null;
        JSONObject jsonObject = JSON.parseObject(str);
        String property = jsonObject.getString(ShareConstant.PROPERTY);
        String packageName = "com.ws.mesh.Smart-FlexLink";
//        String packageName = MeshApplication.
//                getInstance().getApplicationInfo().packageName;
        if (property.equals(packageName)) {
            String meshName = parseData(jsonObject.getJSONObject(ShareConstant.MESH_DATA));
            ShareManageBean shareManageBean = new ShareManageBean();
            shareManageBean.mUserName = jsonObject.getString(ShareConstant.SHARER_INFO);
            shareManageBean.mNetworkName = meshName;
            shareManageBean.utc = jsonObject.getLongValue(ShareConstant.SHARE_UTC);
            ShareDataMangeUtils.addNewReceiveShareData(shareManageBean);
            return shareManageBean;
        } else {
            return null;
        }
    }

    private static JSONObject getData(Mesh mesh, String info) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ShareConstant.DataConstant.MESH, ShareMeshUtils.getMeshData(mesh, info));
        jsonObject.put(ShareConstant.DataConstant.DEVICE,
                ShareDevUtils.getDevJsonArray(DeviceDAO.getInstance().queryDeviceByMeshName(mesh.mMeshName), mesh.mMeshName));
        jsonObject.put(ShareConstant.DataConstant.GROUP,
                ShareRoomUtils.getGroupData(RoomDAO.getInstance().queryRoomByMeshName(mesh.mMeshName)));
        jsonObject.put(ShareConstant.DataConstant.SCENE,
                ShareSceneUtils.getSceneData(SceneDAO.getInstance().querySceneByMeshName(mesh.mMeshName), mesh.mMeshName));
        jsonObject.put(ShareConstant.DataConstant.FCOLOR,
                ShareFColorUtils.getGroupData(FColorDAO.getInstance().queryFColorWithMeshName(mesh.mMeshName)));
        return jsonObject;
    }

    private static String parseData(JSONObject jsonObject) {
        if (jsonObject == null) return "";
        Mesh mesh = ShareMeshUtils.parseMeshData(jsonObject.getJSONObject(ShareConstant.DataConstant.MESH));
        if (mesh != null) {
            ShareDevUtils.parseDevJsonArray(jsonObject.getJSONArray(ShareConstant.DataConstant.DEVICE), mesh.mMeshName);
            ShareRoomUtils.parseGroupData(jsonObject.getJSONArray(ShareConstant.DataConstant.GROUP), mesh.mMeshName);
            ShareSceneUtils.parseSceneData(jsonObject.getJSONArray(ShareConstant.DataConstant.SCENE), mesh.mMeshName);
            ShareFColorUtils.parseFColorData(jsonObject.getJSONArray(ShareConstant.DataConstant.FCOLOR), mesh.mMeshName);
            if (CoreData.core().getCurrMesh().mMeshName.equals(mesh.mMeshName)) {
                CoreData.core().initMeshData();
            }
        }
        return mesh.mMeshShowName;
    }
}
