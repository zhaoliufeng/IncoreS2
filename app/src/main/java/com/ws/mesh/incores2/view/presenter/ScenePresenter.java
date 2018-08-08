package com.ws.mesh.incores2.view.presenter;

import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.SceneDAO;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.ISceneView;

public class ScenePresenter extends IBasePresenter<ISceneView> {

    //获取当前网络的场景列表
    public SparseArray<Scene> getSceneList(){
        return CoreData.core().mSceneSparseArray;
    }

    //添加场景 判断是否有可用的场景id
    public void addScene(){
        int sceneId = getSceneId();
        if (sceneId != -1){
            //如果有可用id则新增sceneId
            Scene scene = new Scene();
            scene.mSceneName = "Scene " + sceneId;
            scene.mDevSceneSparseArray = new SparseArray<>();
            scene.mSceneId = sceneId;
            CoreData.core().mSceneSparseArray.append(scene.mSceneId, scene);
            SceneDAO.getInstance().insertScene(scene);
        }

        getView().addScene(sceneId);
    }

    //获取可用的场景id
    private int getSceneId(){
        for (int id = AppConstant.SCENE_START_ID; id <= AppConstant.SCENE_LAST_ID; id++) {
            if (CoreData.core().mSceneSparseArray.get(id) == null) {
                return id;
            }
        }
        return -1;
    }

    public void deleteScene(int sceneId) {
        Scene scene = CoreData.core().mSceneSparseArray.get(sceneId);
        //如果场景绑定了定时 那么在删除场景表的同时 也需要删除场景定时
        if (SceneDAO.getInstance().deleteScene(scene)){
            TimingDAO.getInstance().deleteTimingBySceneId(sceneId);

            CoreData.core().mSceneSparseArray.remove(sceneId);
            getView().deleteScene(true);
        }else {
            getView().deleteScene(false);
        }
    }
}
