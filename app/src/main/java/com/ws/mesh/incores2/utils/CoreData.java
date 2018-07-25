package com.ws.mesh.incores2.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.telink.bluetooth.light.ConnectionStatus;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.constant.AppLifeStatusConstant;
import com.ws.mesh.incores2.db.DeviceDAO;
import com.ws.mesh.incores2.db.MeshDAO;
import com.ws.mesh.incores2.db.RoomDAO;
import com.ws.mesh.incores2.db.SceneDAO;
import com.ws.mesh.incores2.view.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by we_smart on 2017/11/20.
 */

public class CoreData {

    //单例
    private static CoreData instance = null;

    public static CoreData core() {
        if (instance == null) {
            synchronized (CoreData.class) {
                if (instance == null) {
                    instance = new CoreData();
                }
            }
        }
        return instance;
    }

    //是否是添加设备模式
    public static boolean mAddDeviceMode = false;

    //应用当前状态
    private int mCurrAppStatus = AppLifeStatusConstant.KILL_PROGRESS;

    //获取app状态
    public int getCurrAppStatus() {
        return mCurrAppStatus;
    }

    //设置app状态
    public void setCurrAppStatus(int status) {
        mCurrAppStatus = status;
    }

    /*
    * 当前操作的网络
    * */
    private Mesh mCurrMesh;

    public Mesh getCurrMesh() {
        return mCurrMesh;
    }

    public void setCurrMesh(Mesh mesh) {
        mCurrMesh = mesh;
    }


    /*
    * 全局数据
    * */
    //当前网络的所有设备
    public SparseArray<Device> mDeviceSparseArray;
    //当前网络的所有群组
    public SparseArray<Room> mRoomSparseArray;
    //当前网络的所有场景
    public SparseArray<Scene> mSceneSparseArray;
    //所有的网络
    public Map<String, Mesh> mMeshMap;

    public void initMeshData() {
        mDeviceSparseArray = DeviceDAO.getInstance().queryDevice();
        mRoomSparseArray = RoomDAO.getInstance().queryRoom();
        mSceneSparseArray = SceneDAO.getInstance().queryScene();
    }

    public void switchMesh(Mesh mesh) {
        if (mesh != null) {
            setCurrMesh(mesh);
            initMeshData();
        }
    }

    //获取在线设备
    public SparseArray getOnlineDevice() {
        SparseArray<Device> sparseArray = new SparseArray();
        if (mDeviceSparseArray != null && mDeviceSparseArray.size() != 0) {
            for (int x = 0 ; x < mDeviceSparseArray.size() ; x++) {
                Device device = mDeviceSparseArray.valueAt(x);
                if (device.mConnectionStatus != ConnectionStatus.OFFLINE) {
                    sparseArray.put(device.mDevMeshId, device);
                }
            }
        }
        return sparseArray;
    }

    public void initMesh() {
        mMeshMap = MeshDAO.getInstance().queryMesh();
    }


    private boolean mRequestBluetooth;

    public boolean getBLERequestStatus() {
        return mRequestBluetooth;
    }

    public void setBLERequest(boolean requestBluetooth) {
        this.mRequestBluetooth = requestBluetooth;
    }


    /*
    * 删除设备的时的操作
    * */
    private SparseArray<Integer> mDeletedDevice;

    public void addNewDevDelete(int meshAddress) {
        if (mDeletedDevice == null) {
            mDeletedDevice = new SparseArray<>();
        }
        mDeletedDevice.put(meshAddress, meshAddress);
    }

    public boolean isDevDeleted(int meshAddress) {
        return mDeletedDevice != null && mDeletedDevice.get(meshAddress) != null;
    }

    public void clearDeleteDevList() {
        if (mDeletedDevice != null) {
            mDeletedDevice.clear();
        }
    }

    public void deleteData(int meshAddress) {
        if (mDeletedDevice != null) {
            mDeletedDevice.remove(meshAddress);
        }
    }

    /**** Activity 管理 ****/
    public static Map<String, BaseActivity> mMangerActivity = new HashMap<>();

    public static void addActivity(BaseActivity baseActivity, String activityName) {
        mMangerActivity.put(activityName, baseActivity);
    }

    public static void removeActivity(String activityName) {
        if (mMangerActivity.get(activityName) == null)
            return;
        mMangerActivity.remove(activityName);
    }

    public static Handler mHandler = new Handler(Looper.getMainLooper());
}
