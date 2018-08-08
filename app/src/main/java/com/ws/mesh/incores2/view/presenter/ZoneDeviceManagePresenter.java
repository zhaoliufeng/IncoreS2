package com.ws.mesh.incores2.view.presenter;

import android.util.SparseArray;

import com.telink.bluetooth.event.NotificationEvent;
import com.telink.bluetooth.light.ConnectionStatus;
import com.telink.bluetooth.light.OnlineStatusNotificationParser;
import com.telink.util.Event;
import com.telink.util.EventListener;
import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.db.RoomDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.IZoneDeviceManageView;

import java.util.List;

public class ZoneDeviceManagePresenter extends IBasePresenter<IZoneDeviceManageView> implements EventListener<String> {

    private SparseArray<Device> zoneDevices;
    private Room room;

    public void init(int meshAddress) {
        this.room = CoreData.core().mRoomSparseArray.get(meshAddress);
        getZoneDevices();
        addListener();
    }

    //获取房间名称
    public String getZoneName() {
        return room.mRoomName;
    }

    //获取房间下的设备列表
    public SparseArray<Device> getZoneDevices() {
        zoneDevices = new SparseArray<>();
        for (int i = 0; i < room.mDeviceIds.size(); i++) {
            Device device = CoreData.core().mDeviceSparseArray.get(room.mDeviceIds.valueAt(i));
            zoneDevices.append(device.mDevMeshId, device);
        }
        return zoneDevices;
    }

    //获取所有设备
    public SparseArray<Device> getDevices() {
        return CoreData.core().mDeviceSparseArray;
    }

    //添加设备到房间
    public void addDevice(int deviceId) {
        SendMsg.allocationGroup(deviceId, room.mRoomId);
        room.mDeviceIds.append(deviceId, deviceId);
        updateDevice();
    }

    //移除房间中的设备
    public void removeDevice(int deviceId) {
        SendMsg.cancelAllocationGroup(deviceId, room.mRoomId);
        room.mDeviceIds.remove(deviceId);
        updateDevice();
    }

    private void updateDevice() {
        if (RoomDAO.getInstance().updateRoom(room)) {
            getView().updateDeviceList(true);
        } else {
            getView().updateDeviceList(false);
        }
    }

    //获取设备开关状态
    @Override
    public void performed(Event<String> event) {
        switch (event.getType()) {
            case NotificationEvent.ONLINE_STATUS:
                onOnlineStatusNotify((NotificationEvent) event);
                break;
        }
    }

    private void addListener() {
        MeshApplication.getInstance().addEventListener(NotificationEvent.ONLINE_STATUS, this);
    }

    public void destory(){
        removeListener();
    }

    private void removeListener(){
        MeshApplication.getInstance().removeEventListener(this);
    }

    /**
     * 蓝牙状态数据上报
     */
    @SuppressWarnings("unchecked")
    private synchronized void onOnlineStatusNotify(NotificationEvent event) {
        List<OnlineStatusNotificationParser.DeviceNotificationInfo> mNotificationInfoList =
                (List<OnlineStatusNotificationParser.DeviceNotificationInfo>) event.parse();
        for (OnlineStatusNotificationParser.DeviceNotificationInfo notificationInfo : mNotificationInfoList) {
            int status = notificationInfo.status;
            int address = notificationInfo.meshAddress;
            ConnectionStatus connectionStatus = notificationInfo.connectStatus;

            Device device = zoneDevices.get(address);
            if (device != null) {
                //是当前群组的设备
                if (status != 0) {
                    device.mConnectionStatus = connectionStatus;
                } else {
                    device.mConnectionStatus = ConnectionStatus.OFFLINE;
                }
                if (getView() != null)
                    getView().statusUpdate(device);
            }
        }
    }
}
