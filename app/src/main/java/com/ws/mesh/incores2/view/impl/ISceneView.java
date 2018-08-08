package com.ws.mesh.incores2.view.impl;

import com.ws.mesh.incores2.view.base.IBaseView;

public interface ISceneView extends IBaseView {

    void addScene(int sceneId);

    void deleteScene(boolean success);
}
