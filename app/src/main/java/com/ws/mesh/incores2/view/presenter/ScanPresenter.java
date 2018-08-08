package com.ws.mesh.incores2.view.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.SparseArray;

import com.telink.bluetooth.LeBluetooth;
import com.telink.bluetooth.WeSmartLog;
import com.telink.bluetooth.event.DeviceEvent;
import com.telink.bluetooth.event.LeScanEvent;
import com.telink.bluetooth.event.MeshEvent;
import com.telink.bluetooth.light.ConnectionStatus;
import com.telink.bluetooth.light.DeviceInfo;
import com.telink.bluetooth.light.LeScanParameters;
import com.telink.bluetooth.light.LeUpdateParameters;
import com.telink.bluetooth.light.LightAdapter;
import com.telink.bluetooth.light.Parameters;
import com.telink.util.Event;
import com.telink.util.EventListener;
import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.DeviceDAO;
import com.ws.mesh.incores2.service.WeSmartService;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.utils.TaskPool;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.IScanView;

import java.util.HashMap;

/**
 * Created by we_smart on 2017/12/27.
 */

public class ScanPresenter extends IBasePresenter<IScanView> implements EventListener<String> {


    private SparseArray<Device> mDeviceSparseArray = new SparseArray<>();
    private HashMap<String, DeviceInfo> mAllDevice = new HashMap<>();

    //监听蓝牙数据
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        startScan(1000);
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        //蓝牙关闭
                        getView().onBleClose();
                        break;
                }
            }
        }
    };
    private volatile int mMeshAddress;

    public ScanPresenter() {
        CoreData.mAddDeviceMode = true;
        addListener();
        registerReceiver();
    }

    public void checkBle(Activity activity) {
        BluetoothAdapter mBluetoothAdapter = LeBluetooth.getInstance().getAdapter(MeshApplication.getInstance());
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
        }
    }

    public void startScan(){
        startScan(1000);
    }

    private void addListener() {
        MeshApplication.getInstance().addEventListener(LeScanEvent.LE_SCAN, this);
        MeshApplication.getInstance().addEventListener(LeScanEvent.LE_SCAN_TIMEOUT, this);
        MeshApplication.getInstance().addEventListener(DeviceEvent.STATUS_CHANGED, this);
        MeshApplication.getInstance().addEventListener(MeshEvent.UPDATE_COMPLETED, this);
        MeshApplication.getInstance().addEventListener(MeshEvent.ERROR, this);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY - 1);
        MeshApplication.getInstance().registerReceiver(mReceiver, filter);
    }

    /**
     * 事件处理方法
     *
     * @param event
     */
    @Override
    public void performed(Event<String> event) {
        switch (event.getType()) {
            case LeScanEvent.LE_SCAN:
                this.onLeScan((LeScanEvent) event);
                break;
            case LeScanEvent.LE_SCAN_TIMEOUT:
                this.onLeScanTimeout((LeScanEvent) event);
                break;
            case DeviceEvent.STATUS_CHANGED:
                this.onDeviceStatusChanged((DeviceEvent) event);
                break;
            case MeshEvent.ERROR:
                this.onMeshEvent((MeshEvent) event);
                break;
        }
    }

    private void onLeScanTimeout(LeScanEvent event) {
        getView().addDeviceStatus(R.string.done);
        getView().addDeviceFinish(mDeviceSparseArray.size());
    }

    private void onMeshEvent(MeshEvent event) {
        getView().addDeviceStatus(R.string.please_restart_ble);
    }

    private void onLeScan(LeScanEvent event) {
        getView().addDeviceStatus(R.string.find_device);
        DeviceInfo deviceInfo = event.getArgs();
        if (null != deviceInfo) {
            if (!TextUtils.isEmpty(deviceInfo.macAddress))
                mAllDevice.put(deviceInfo.macAddress, deviceInfo);
        }
        if (deviceInfo != null) {
            if (deviceInfo.productUUID == 0x04) {
                switch (deviceInfo.meshUUID) {
                    default:
                        mMeshAddress = ViewUtils.getVaildMeshAddress(CoreData.core().mDeviceSparseArray) & 0xFF;
                        break;
                }
            } else if (deviceInfo.productUUID == 0x20) {
                mMeshAddress = 255;
            }
        }
        if (mMeshAddress == -1) {
            getView().addDeviceStatus(R.string.cannot_add_any_device);
            return;
        }

        //更新参数
        LeUpdateParameters params = Parameters.createUpdateParameters();
        params.setOldMeshName(CoreData.core().getCurrMesh().mMeshFactoryName);
        params.setOldPassword(CoreData.core().getCurrMesh().mMeshFactoryPassword);
        params.setNewMeshName(CoreData.core().getCurrMesh().mMeshName);
        params.setNewPassword(CoreData.core().getCurrMesh().mMeshPassword);
        if (deviceInfo != null) {
            deviceInfo.meshAddress = mMeshAddress & 0xFF;
        }
        params.setUpdateDeviceList(deviceInfo);
        WeSmartService.Instance().idleMode(true);
        //加灯
        WeSmartService.Instance().updateMesh(params);
    }

    @SuppressLint("DefaultLocale")
    private void onDeviceStatusChanged(DeviceEvent event) {
        WeSmartLog.i(event.getType());
        DeviceInfo deviceInfo = event.getArgs();
        switch (deviceInfo.status) {
            case LightAdapter.STATUS_UPDATE_MESH_COMPLETED:
                //加灯完成继续扫描,直到扫不到设备
                if (!TextUtils.isEmpty(deviceInfo.macAddress)) {
                    Device device = new Device();
                    device.mConnectionStatus = ConnectionStatus.OFFLINE;
                    device.mDevMeshId = deviceInfo.meshAddress;
                    if (mAllDevice.get(deviceInfo.macAddress) == null) {
                        device.mDevType = AppConstant.DEFAULT_TYPE;
                    } else {
                        device.mDevType = mAllDevice.get(deviceInfo.macAddress).meshUUID;
                    }

                    device.mDevName = String.format("Device-%d", device.mDevMeshId);
                    device.mDevMacAddress = deviceInfo.macAddress;
                    DeviceDAO.getInstance().insertDevice(device);
                    CoreData.core().mDeviceSparseArray.put(device.mDevMeshId, device);
                    mDeviceSparseArray.put(device.mDevMeshId, device);
                    getView().addDeviceSuccess(mDeviceSparseArray);
                    startScan(500);
                } else {
                    SendMsg.kickOut(deviceInfo.meshAddress);
                    startScan(500);
                    getView().addDeviceStatus(R.string.add_dev_fail);
                }
                break;
            case LightAdapter.STATUS_UPDATE_MESH_FAILURE:
                getView().addDeviceStatus(R.string.add_dev_fail);
                //加灯失败继续扫描
                this.startScan(500);
                break;
        }
    }

    private void startScan(int delay) {
        getView().addDeviceStatus(R.string.search);
        TaskPool.DefRandTaskPool().PushTask(new Runnable() {
            @Override
            public void run() {
                if (CoreData.core().isEmptyMesh()) return;
                //扫描参数
                LeScanParameters params = LeScanParameters.create();
                params.setMeshName(CoreData.core().getCurrMesh().mMeshFactoryName);
                params.setOutOfMeshName(CoreData.core().getCurrMesh().mMeshFactoryName);
                params.setTimeoutSeconds(15);
                params.setScanMode(true);
                WeSmartService.Instance().startScan(params);
            }
        }, delay);
    }

    public void stopScan(){
        WeSmartService.Instance().stopScan();
        getView().onStopScan();
    }

    public void destroy() {
        MeshApplication.getInstance().removeEventListener(this);
        MeshApplication.getInstance().unregisterReceiver(mReceiver);
        CoreData.mAddDeviceMode = false;
        mAllDevice = null;
        mReceiver = null;
    }
}
