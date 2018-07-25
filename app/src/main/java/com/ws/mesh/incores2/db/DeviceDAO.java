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

    private static DeviceDAO mDeviceDAO;

    public static DeviceDAO getInstance() {
        if (mDeviceDAO == null) {
            synchronized (MeshDAO.class) {
                if (mDeviceDAO == null) {
                    mDeviceDAO = new DeviceDAO();
                }
            }
        }
        return mDeviceDAO;
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
        return delete(device, "mDeviceMeshName", "mDevMeshId");
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
        return queryDevice(new String[]{CoreData.core().getCurrMesh().mMeshName}, "mDeviceMeshName");
    }

    private SparseArray<Device> queryDevice(String[] whereValue, String... whereKey) {
        List<Device> devices = query(whereValue, whereKey);
        SparseArray<Device> deviceSparseArray = new SparseArray<>();
        for (Device device : devices) {
            deviceSparseArray.put(device.mDevMeshId, device);
        }
        return deviceSparseArray;
    }
}
