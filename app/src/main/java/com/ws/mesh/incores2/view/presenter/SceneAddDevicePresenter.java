package com.ws.mesh.incores2.view.presenter;

import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.bean.SceneColor;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.ISceneAddDeviceView;

public class SceneAddDevicePresenter extends IBasePresenter<ISceneAddDeviceView>{

    private int sceneId;
    private Scene scene;

    public void init(int sceneId){
        this.sceneId = sceneId;
        this.scene = CoreData.core().mSceneSparseArray.get(sceneId);
    }

    public SparseArray<Device> getDeviceArray(){
        return CoreData.core().mDeviceSparseArray;
    }

    public SparseArray<SceneColor> getSceneColorArray(){
        return scene.mDevSceneSparseArray;
    }

}
