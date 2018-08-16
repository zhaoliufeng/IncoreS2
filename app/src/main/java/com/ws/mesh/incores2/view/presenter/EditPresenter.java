package com.ws.mesh.incores2.view.presenter;

import android.util.Log;
import android.util.SparseArray;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.bean.SceneColor;
import com.ws.mesh.incores2.db.DeviceDAO;
import com.ws.mesh.incores2.db.RoomDAO;
import com.ws.mesh.incores2.db.SceneDAO;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.IEditView;

public class EditPresenter extends IBasePresenter<IEditView> {

    private static final String TAG = "EditPresenter";
    private Device device;
    private Room room;
    private int meshAddress;

    public void init(int meshAddress) {
        this.meshAddress = meshAddress;
        if (isRoom()) {
            room = CoreData.core().mRoomSparseArray.get(meshAddress);
        } else {
            device = CoreData.core().mDeviceSparseArray.get(meshAddress);
        }
    }

    //获取编辑标题
    public int getTitle() {
        return isRoom() ? R.string.zone_name : R.string.device_name;
    }

    public int getRemoveContent() {
        return isRoom() ? R.string.remove_zone : R.string.remove_device;
    }

    //判断是房间还是设备
    public boolean isRoom() {
        return meshAddress > 0x8000;
    }

    //编辑名称
    public void editName(String newName) {
        if (isRoom())
            getView().editName(editRoomName(newName));
        else
            getView().editName(editDeviceName(newName));
    }

    private boolean editDeviceName(String newName) {
        device.mDevName = newName;
        return DeviceDAO.getInstance().updateDevice(device);
    }

    private boolean editRoomName(String newName) {
        room.mRoomName = newName;
        return RoomDAO.getInstance().updateRoom(room);
    }

    //移除设备/房间
    public void remove() {
        if (isRoom())
            getView().remove(removeRoom());
        else
            getView().remove(removeDevice());
    }

    private boolean removeDevice() {
        if (DeviceDAO.getInstance().deleteDevice(device)) {
            CoreData.core().mDeviceSparseArray.remove(device.mDevMeshId);
            //删除定时表中相关的定时
            TimingDAO.getInstance().deleteTimingByDeviceId(device.mDevMeshId);
            //删除房间表中的设备
            for (int i = 0; i < CoreData.core().mRoomSparseArray.size(); i++) {
                Room room = CoreData.core().mRoomSparseArray.valueAt(i);
                SparseArray deviceIds = room.mDeviceIds;
                if (deviceIds.get(device.mDevMeshId) != null) {
                    Log.i(TAG, "removeDevice: 删除了房间中的设备 房间-" + room.mRoomName);
                    deviceIds.remove(device.mDevMeshId);
                    RoomDAO.getInstance().updateRoom(room);
                }
            }
            //删除场景表中的相关设备
            for (int i = 0; i < CoreData.core().mSceneSparseArray.size(); i++) {
                Scene scene = CoreData.core().mSceneSparseArray.valueAt(i);
                SparseArray<SceneColor> sparseArray = scene.mDevSceneSparseArray;
                if (sparseArray.get(device.mDevMeshId) != null){
                    Log.i(TAG, "removeDevice: 删除了场景中相关的设备 场景-" + scene.mSceneName);
                    sparseArray.remove(device.mDevMeshId);
                    SceneDAO.getInstance().updateScene(scene);
                }
            }
            SendMsg.kickOut(device.mDevMeshId);
            return true;
        }
        return false;
    }

    private boolean removeRoom() {
        if (RoomDAO.getInstance().deleteRoom(room)) {
            CoreData.core().mRoomSparseArray.remove(room.mRoomId);
            //删除定时表中相关的定时
            TimingDAO.getInstance().deleteTimingByRoomId(room.mRoomId);
            SendMsg.deleteGroup(room.mRoomId);
            return true;
        }
        return false;
    }

    //获取设备/房间名称
    public String getName() {
        if (device != null) {
            return device.mDevName;
        } else if (room != null) {
            return room.mRoomName;
        } else {
            return "null";
        }
    }
}
