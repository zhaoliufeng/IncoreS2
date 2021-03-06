package com.ws.mesh.incores2.db;

import android.util.SparseArray;

import com.we_smart.sqldao.BaseDAO;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.utils.CoreData;

import java.util.List;

/**
 * Created by zhaol on 2018/4/24.
 */

public class DeviceDAO extends BaseDAO<Device> {

    private DeviceDAO() {
        super(Device.class);
    }

    private static DeviceDAO dao;

    public static DeviceDAO getInstance() {
        if (dao == null) {
            synchronized (DeviceDAO.class) {
                if (dao == null) {
                    dao = new DeviceDAO();
                }
            }
        }
        return dao;
    }

    public boolean insertDevice(Device device) {
        device.mDeviceMeshName = CoreData.core().getCurrMesh().mMeshName;
        device.circadianString = Device.circadianToString(device.circadian);
        return insert(device);
    }

    /**
     * 插入接收到分享的设备数据 这时候不做meshName的更正
     */
    public boolean insertShareDevice(Device device) {
        return insert(device);
    }

    public boolean deleteDevice(Device device) {
        return delete(device,
                "mDeviceMeshName", "mDevMeshId");
    }

    public boolean clearMeshDevice(String meshName) {
        Device device = new Device();
        device.mDeviceMeshName = meshName;
        return delete(device, "mDeviceMeshName");
    }

    public boolean updateDevice(Device device) {
        device.mDeviceMeshName = CoreData.core().getCurrMesh().mMeshName;
        device.circadianString = Device.circadianToString(device.circadian);
        return update(device, "mDeviceMeshName", "mDevMeshId");
    }

    /**
     * 查询mesh集合
     */
    public SparseArray<Device> queryDevice() {
        return queryDevice(new String[]{
                        CoreData.core().getCurrMesh().mMeshName
                },
                "mDeviceMeshName");
    }

    public Device queryDeviceByMeshId(int meshId) {
        return queryDevice(new String[]{
                        CoreData.core().getCurrMesh().mMeshName,
                        String.valueOf(meshId)
                },
                "mDeviceMeshName", "mDevMeshId").valueAt(0);
    }

    private SparseArray<Device> queryDevice(String[] whereValue, String... whereKey) {
        List<Device> devices = query(whereValue, whereKey);
        SparseArray<Device> deviceSparseArray = new SparseArray<>();
        for (Device device : devices) {
            device.circadian = Device.stringToCircadian(device.circadianString);
            deviceSparseArray.put(device.mDevMeshId, device);
        }
        return deviceSparseArray;
    }

    public SparseArray<Device> queryDeviceByMeshName(String meshName) {
        return queryDevice(new String[]{meshName}, "mDeviceMeshName");
    }
}
