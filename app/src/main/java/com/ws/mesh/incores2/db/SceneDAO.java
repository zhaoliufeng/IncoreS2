package com.ws.mesh.incores2.db;

import android.util.SparseArray;

import com.we_smart.sqldao.BaseDAO;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.utils.CoreData;

import java.util.List;

/**
 * Created by zhaol on 2018/4/24.
 */

public class SceneDAO extends BaseDAO<Scene> {

    private SceneDAO() {
        super(Scene.class);
    }

    private static SceneDAO mSceneDAO;

    public static SceneDAO getInstance() {
        if (mSceneDAO == null) {
            synchronized (MeshDAO.class) {
                if (mSceneDAO == null) {
                    mSceneDAO = new SceneDAO();
                }
            }
        }
        return mSceneDAO;
    }

    public boolean insertScene(Scene scene) {
        scene.mSceneMeshName = CoreData.core().getCurrMesh().mMeshName;
        scene.mDeviceScenesStr = Scene.sceneToString(scene.mDevSceneSparseArray);
        return insert(scene);
    }

    /**
     * 插入接收到分享的数据 这时不做meshName的更正
     */
    public boolean insertShareScene(Scene scene) {
        scene.mDeviceScenesStr = Scene.sceneToString(scene.mDevSceneSparseArray);
        return insert(scene);
    }

    public boolean deleteScene(Scene scene) {
        return delete(scene, "mSceneMeshName", "mSceneId");
    }

    public boolean clearMeshScenes(String meshName) {
        Scene scene = new Scene();
        scene.mSceneName = meshName;
        return delete(scene, "mSceneMeshName");
    }

    public boolean updateScene(Scene scene) {
        scene.mSceneMeshName = CoreData.core().getCurrMesh().mMeshName;
        scene.mDeviceScenesStr = Scene.sceneToString(scene.mDevSceneSparseArray);
        return update(scene, "mSceneMeshName", "mSceneId");
    }


    /**
     * 查询mesh集合
     */
    public SparseArray<Scene> queryScene() {
        return queryScene(new String[]{CoreData.core().getCurrMesh().mMeshName}, "mRoomMeshName");
    }


    private SparseArray<Scene> queryScene(String[] whereValue, String... whereKey) {
        List<Scene> scenes = query(whereValue, whereKey);
        SparseArray<Scene> deviceSparseArray = new SparseArray<>();
        for (Scene scene : scenes) {
            scene.mDevSceneSparseArray = Scene.stringToScene(scene.mDeviceScenesStr);
            deviceSparseArray.put(scene.mSceneId, scene);
        }
        return deviceSparseArray;
    }
}
