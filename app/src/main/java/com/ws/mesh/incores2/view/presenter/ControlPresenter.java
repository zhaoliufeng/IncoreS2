package com.ws.mesh.incores2.view.presenter;

import android.graphics.Color;
import android.util.SparseArray;

import com.telink.bluetooth.event.NotificationEvent;
import com.telink.bluetooth.light.NotificationInfo;
import com.telink.bluetooth.light.OnlineStatusNotificationParser;
import com.telink.util.Event;
import com.telink.util.EventListener;
import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.FavoriteColor;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.bean.SceneColor;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.SceneMode;
import com.ws.mesh.incores2.db.FColorDAO;
import com.ws.mesh.incores2.db.SceneDAO;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.IControlView;

import java.util.ArrayList;
import java.util.List;

public class ControlPresenter extends IBasePresenter<IControlView> implements EventListener<String> {

    public SparseArray<FavoriteColor> colorSparseArray;
    //需要删除自定义颜色的下标
    private List<Integer> deleteIndexList;
    public int meshAddress;
    private int channel = 0;

    public ControlPresenter() {
    }

    public void init(int meshAddress) {
        this.meshAddress = meshAddress;
        getChannel(meshAddress);
        deleteIndexList = new ArrayList<>();
        colorSparseArray = FColorDAO.getInstance().queryFColorWithChannel(String.valueOf(channel));
    }

    //添加自定义颜色
    public void addFavorite(int r, int g, int b, int w, int c, int brightness) {
        FavoriteColor favoriteColor = new FavoriteColor();
        favoriteColor.cIndex = colorSparseArray.size();
        favoriteColor.deviceType= channel;
        favoriteColor.red = r;
        favoriteColor.green = g;
        favoriteColor.blue = b;
        favoriteColor.warm = w;
        favoriteColor.cold = c;
        favoriteColor.brightness = brightness;

        if (FColorDAO.getInstance().insertFColor(favoriteColor)) {
            colorSparseArray.append(favoriteColor.cIndex, favoriteColor);
            getView().addFavoriteColor(true);
        } else {
            getView().addFavoriteColor(false);
        }
    }

    /*
       最多有 8 个自定义颜色 判断能不能继续添加自定义颜色
       ture 不能继续添加
     */
    public boolean cantAddMoreFavoriteColor(){
        return colorSparseArray.size() >= 8;
    }

    //获取路数
    private void getChannel(int meshAddress) {
        if (isRoom()) {
            Room room = CoreData.core().mRoomSparseArray.get(meshAddress);
            // TODO: 2018/8/18 根据路数适配对应的界面
        } else {
            //设备
            Device device = CoreData.core().mDeviceSparseArray.get(meshAddress);
            addListener();
        }
    }

    //设置favoriteColor
    public void setFavoriteColor(FavoriteColor favoriteColor){
        int color = Color.rgb(favoriteColor.red, favoriteColor.green, favoriteColor.blue);
        SendMsg.setDevColor(meshAddress, color, favoriteColor.warm, favoriteColor.cold, false, (byte) 0x1F);
        SendMsg.setDevBrightness(meshAddress, favoriteColor.brightness);
    }

    //设置Color tag的颜色值
    public void setTagColor(int color){
        if (color == 0xFFFFFF) {
            SendMsg.setDevColor(meshAddress, 0, 0, 255, false, (byte) 0x1F);
        } else if (color == 0xFDA100) {
            SendMsg.setDevColor(meshAddress, 0, 255, 0, false, (byte) 0x1F);
        } else {
            SendMsg.setDevColor(meshAddress, color, 0, 0, false, (byte) 0x1F);
        }
    }

    public void getDeviceColor() {
        if (!isRoom()) {
            SendMsg.getDevColor(meshAddress);
        }
    }

    //颜色控制
    public void controlColor(int red, int green, int blue, int warm, int clod) {
        SendMsg.setDevColor(meshAddress, Color.rgb(red, green, blue), warm, clod, false, (byte) 0x1F);
    }

    //选择想要删除的自定义颜色
    public void selectDeleteFavoriteColor(int position) {
        deleteIndexList.add(position);
    }

    //删除自定义颜色
    public void deleteFavoriteColor() {
        for (Integer index : deleteIndexList) {
            FColorDAO.getInstance().deleteFColor(colorSparseArray.valueAt(index));
            colorSparseArray.removeAt(index);
        }

        SparseArray<FavoriteColor> tempArray = colorSparseArray.clone();
        colorSparseArray.clear();
        //删除后 将中间空缺的 index 填补起来
        for (int i = 0; i < tempArray.size(); i++){
            tempArray.valueAt(i).cIndex = i;
            FColorDAO.getInstance().updateFColor(tempArray.valueAt(i));
        }
        for (int i = 0; i < tempArray.size(); i++){
            FavoriteColor favoriteColor = tempArray.valueAt(i);
            colorSparseArray.append(favoriteColor.cIndex, favoriteColor);
        }
        deleteIndexList.clear();
    }

    public boolean isRoom() {
        return meshAddress > 0x8000;
    }
    //下面的接口为 scene 选择场景中设备状态 留用

    private Scene scene;

    public void initScene(int sceneId) {
        scene = CoreData.core().mSceneSparseArray.get(sceneId);
    }

    //保存场景颜色
    public boolean saveSceneColor(int red, int green, int blue, int warm, int clod, int brightness) {
        SceneColor sceneColor = scene.mDevSceneSparseArray.get(meshAddress);
        if (sceneColor == null) {
            sceneColor = new SceneColor();
            sceneColor.mDeviceID = meshAddress;
            scene.mDevSceneSparseArray.append(meshAddress, sceneColor);
        }

        sceneColor.mSceneMode = SceneMode.PALETTE.getValue();
        sceneColor.mSceneRed = red;
        sceneColor.mSceneGreen = green;
        sceneColor.mSceneBlue = blue;
        sceneColor.mSceneWarm = warm;
        sceneColor.mSceneCold = clod;
        sceneColor.mSceneBrightness = brightness;

        bindTimingWithDevice(sceneColor);

        return SceneDAO.getInstance().updateScene(scene);
    }

    //保存场景开关状态
    public boolean saveSceneOnOff(boolean isOn) {
        SceneColor sceneColor = scene.mDevSceneSparseArray.get(meshAddress);
        if (sceneColor == null) {
            sceneColor = new SceneColor();
            sceneColor.mDeviceID = meshAddress;
            scene.mDevSceneSparseArray.append(meshAddress, sceneColor);
        }

        sceneColor.mSceneMode = isOn ? SceneMode.ON.getValue() : SceneMode.OFF.getValue();
        sceneColor.mSceneRed = 0;
        sceneColor.mSceneGreen = 0;
        sceneColor.mSceneBlue = 0;
        sceneColor.mSceneWarm = 0;
        sceneColor.mSceneCold = 0;
        sceneColor.mSceneBrightness = 0;

        bindTimingWithDevice(sceneColor);

        return SceneDAO.getInstance().updateScene(scene);
    }

    private void bindTimingWithDevice(SceneColor sceneColor) {
        //更新场景信息
        SendMsg.addNewScene(meshAddress, scene.mSceneId, sceneColor);
        CoreData.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Timing alarm = TimingDAO.getInstance().queryScheduleWithSceneId(scene.mSceneId);
                if (alarm != null) {
                    SendMsg.addAlarmScene(meshAddress, alarm, scene.mSceneId);
                }
            }
        }, 320);
    }

    //清除场景设置
    public boolean clearSceneSetting() {
        //移除场景
        SendMsg.deleteScene(meshAddress, scene.mSceneId);
        //如果场景绑定了定时 则同时移除定时
        Timing alarm = TimingDAO.getInstance().queryScheduleWithSceneId(scene.mSceneId);
        if (alarm != null) {
            SendMsg.deleteAlarm(meshAddress, alarm.mAId);
        }

        scene.mDevSceneSparseArray.remove(meshAddress);
        return SceneDAO.getInstance().updateScene(scene);

    }

    //设备状态上报
    @Override
    public void performed(Event<String> event) {
        switch (event.getType()) {
            case NotificationEvent.GET_COLOR:
                onCurrColorValue((NotificationEvent) event);
                break;
            case NotificationEvent.ONLINE_STATUS:
                onOnlineStatusNotify((NotificationEvent) event);
                break;
            default:
                break;
        }
    }

    private synchronized void onOnlineStatusNotify(NotificationEvent event) {
        List<OnlineStatusNotificationParser.DeviceNotificationInfo> mNotificationInfoList =
                (List<OnlineStatusNotificationParser.DeviceNotificationInfo>) event.parse();
        if (mNotificationInfoList == null || mNotificationInfoList.size() <= 0) return;
        for (OnlineStatusNotificationParser.DeviceNotificationInfo notificationInfo : mNotificationInfoList) {
            int meshAddress = notificationInfo.meshAddress;
            int brightness = notificationInfo.brightness;
            int status = notificationInfo.status;
            if (this.meshAddress == meshAddress) {
                if (status != 0) {
                    getView().setBrightness(brightness);
                } else {
                    getView().setBrightness(0);
                }
                getView().setStatus(notificationInfo.connectStatus);
            }
        }
    }

    private void onCurrColorValue(NotificationEvent event) {
        NotificationInfo info = event.getArgs();
        int srcAddress = info.src & 0xFF;
        byte[] params = info.params;
        if (srcAddress != meshAddress) return;
        if (params != null) {
            int mCurrRed = (params[0] & 0xFF);
            int mCurrGreen = (params[1] & 0xFF);
            int mCurrBlue = (params[2] & 0xFF);
            int mCurrWarm = params[3] & 0xFF;
            int mCurrCold = params[4] & 0xFF;
            int mLuminanceValues = (params[6] & 0xFF);
            int color = Color.rgb(mCurrRed, mCurrGreen, mCurrBlue);
            getView().setColor(color, mCurrWarm, mCurrCold);
            getView().setBrightness(mLuminanceValues);
        }
    }

    private void addListener() {
        MeshApplication.getInstance().addEventListener(NotificationEvent.GET_COLOR, this);
        MeshApplication.getInstance().addEventListener(NotificationEvent.ONLINE_STATUS, this);
    }

    public void destory() {
        MeshApplication.getInstance().removeEventListener(this);
    }
}
