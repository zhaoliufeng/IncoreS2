package com.ws.mesh.incores2.utils.shareuitls;

import com.alibaba.fastjson.JSONObject;
import com.we_smart.lansharelib.shareconstant.ShareConstant;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.db.MeshDAO;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.utils.CoreData;

/**
 * Created by we_smart on 2018/4/26.
 */

public class ShareMeshUtils {

    public static JSONObject getMeshData(Mesh mesh, String info) {
        JSONObject jsonObject = new JSONObject();
        if (mesh == null) return jsonObject;
        jsonObject.put(ShareConstant.MeshConstant.MESH_SHOW_NAME, info + "_" + mesh.mMeshShowName);
        jsonObject.put(ShareConstant.MeshConstant.MESH_LOGIN_NAME, mesh.mMeshName);
        jsonObject.put(ShareConstant.MeshConstant.MESH_LOGIN_PASSWORD, mesh.mMeshPassword);
        jsonObject.put(ShareConstant.MeshConstant.MESH_EDIT_PASSWORD, mesh.mMeshEditPassword);
        jsonObject.put(ShareConstant.MeshConstant.MESH_CHANGE_UTC, System.currentTimeMillis());
        jsonObject.put(ShareConstant.MeshConstant.MESH_FACTORY_NAME, mesh.mMeshFactoryName);
        jsonObject.put(ShareConstant.MeshConstant.MESH_FACTORY_PASSWORD, mesh.mMeshFactoryPassword);
        jsonObject.put(ShareConstant.MeshConstant.ALARM,
                ShareAlarmUtils.getGroAlarmJsonArray(TimingDAO.getInstance().queryRoomAlarm(mesh.mMeshName)));
        return jsonObject;
    }

    public static Mesh parseMeshData(JSONObject object) {
        if (object == null) return null;
        Mesh mesh = new Mesh();
        mesh.mMeshShowName = object.getString(ShareConstant.MeshConstant.MESH_SHOW_NAME);
        mesh.mIsShare = true;
        mesh.mMeshPassword = object.getString(ShareConstant.MeshConstant.MESH_LOGIN_PASSWORD);
        mesh.mMeshName = object.getString(ShareConstant.MeshConstant.MESH_LOGIN_NAME);
        mesh.mMeshEditPassword = object.getString(ShareConstant.MeshConstant.MESH_EDIT_PASSWORD);
        mesh.mMeshFactoryName = object.getString(ShareConstant.MeshConstant.MESH_FACTORY_NAME);
        mesh.mMeshFactoryPassword = object.getString(ShareConstant.MeshConstant.MESH_FACTORY_PASSWORD);
        if (CoreData.core().mMeshMap.get(mesh.mMeshName) != null) {
            MeshDAO.getInstance().deleteMesh(mesh);
        }
        ShareAlarmUtils.parseGroAlarmJson(object.getJSONArray(ShareConstant.MeshConstant.ALARM), mesh.mMeshName);
        MeshDAO.getInstance().insertMesh(mesh);
        CoreData.core().mMeshMap.put(mesh.mMeshName, mesh);
        return mesh;
    }
}
