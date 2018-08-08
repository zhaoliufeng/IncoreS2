package com.ws.mesh.incores2.view.presenter;

import android.content.Context;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.db.SceneDAO;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.adapter.SceneAdapter;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.ISceneAddView;

public class SceneAddPresenter extends IBasePresenter<ISceneAddView>{

    private Context context;
    private Scene scene;

    public SceneAddPresenter(Context context){
        this.context = context;
    }

    public void init(int sceneId){
        scene = CoreData.core().mSceneSparseArray.get(sceneId);
    }
    //修改场景名称
    public void editName(String newName){
        scene.mSceneName = newName;
        if (SceneDAO.getInstance().updateScene(scene)){
            getView().editName(true);
        }else {
            getView().editName(false);
        }
    }

    public String getName(){
        return scene.mSceneName;
    }

    //查询定时表中的场景相关的定时
    public void getSchedule(){
        Timing timing = TimingDAO.getInstance().queryScheduleWithSceneId(scene.mSceneId);
        getView().getSceneSchedule(timing);
    }

    public String getExecuteInfo(int weekNum) {
        if (weekNum == 0)
            return context.getString(R.string.never_repeat);
        if (weekNum == 127)
            return context.getString(R.string.every_day);
        if (weekNum == 62)
            return context.getString(R.string.work_day);

        byte[] weeks = ViewUtils.reverseBytes(ViewUtils.weekNumToBinaryByteArray(weekNum));
        String[] weekString = context.getResources().getStringArray(R.array.custom_week_data);
        StringBuilder showString = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (weeks[i] == 1) {
                showString.append(weekString[i]).append(",");
            }
        }
        return showString.substring(0, showString.length() - 1);
    }
}
