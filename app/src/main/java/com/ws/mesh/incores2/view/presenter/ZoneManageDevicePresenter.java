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
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.IZoneManageDeviceView;

import java.util.List;

public class ZoneManageDevicePresenter extends IBasePresenter<IZoneManageDeviceView> implements EventListener<String> {

    private SparseArray<Device> zoneDevices;
    private int meshAddress;
    private Room room;

    public void init(int meshAddress) {
        this.meshAddress = meshAddress;
        this.room = CoreData.core().mRoomSparseArray.get(meshAddress);
        getZoneDevices();
        addListener();
    }

    //获取房间名称
    public String getZoneName(){
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

    //开关设备
    public void switchDevice(boolean isOpen) {
        SendMsg.switchDevice(meshAddress, isOpen);
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

    /**
     * 蓝牙状态数据上报
     */
    @SuppressWarnings("unchecked")
    private synchronized void onOnlineStatusNotify(NotificationEvent event) {
        List<OnlineStatusNotificationParser.DeviceNotificationInfo> mNotificationInfoList =
                (List<OnlineStatusNotificationParser.DeviceNotificationInfo>) event.parse();
        for (OnlineStatusNotificationParser.DeviceNotificationInfo notificationInfo : mNotificationInfoList) {
            int status = notificationInfo.status;
            ConnectionStatus connectionStatus = notificationInfo.connectStatus;

            Device device = zoneDevices.get(meshAddress);
            if (device != null) {
                //是当前群组的设备
                if (status != 0) {
                    device.mConnectionStatus = connectionStatus;
                } else {
                    device.mConnectionStatus = ConnectionStatus.OFFLINE;
                }
                getView().statusUpdate(device);
            }
        }
    }
}
