package com.ws.mesh.incores2.db;

import com.we_smart.sqldao.BaseDAO;
import com.ws.mesh.incores2.bean.Mesh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mesh 网络数据操作
 * Created by zhaol on 2018/4/23.
 */

public class MeshDAO extends BaseDAO<Mesh> {

    private MeshDAO() {
        super(Mesh.class);
    }

    private static MeshDAO dao;

    public static MeshDAO getInstance() {
        if (dao == null) {
            synchronized (MeshDAO.class) {
                if (dao == null) {
                    dao = new MeshDAO();
                }
            }
        }
        return dao;
    }

    public boolean insertMesh(Mesh mesh) {
        //记录当前创建网络的时间 秒级
        mesh.createUtcTime = System.currentTimeMillis() / 1000;
        return insert(mesh);
    }

    public boolean deleteMesh(Mesh mesh) {
        //删除mesh网络下的设备 房间 场景信息
        DeviceDAO.getInstance().clearMeshDevice(mesh.mMeshName);
        RoomDAO.getInstance().clearMeshRooms(mesh.mMeshName);
        SceneDAO.getInstance().clearMeshScenes(mesh.mMeshName);
        TimingDAO.getInstance().clearMeshTiming(mesh.mMeshName);
        return delete(mesh,"mMeshName");
    }

    public boolean updateMesh(Mesh mesh) {
        return update(mesh, "mMeshName");
    }

    /**
     * 查询mesh集合
     */
    public Map<String, Mesh> queryMesh() {
        return queryMesh(null, null);
    }

    public Map<String, Mesh> queryMesh(String[] whereKey, String[] whereValue) {
        List<Mesh> meshes = query(whereKey, whereValue);
        Map<String, Mesh> meshMap = new HashMap<>();
        for (Mesh mesh : meshes) {
            meshMap.put(mesh.mMeshName, mesh);
        }
        return meshMap;
    }
}
