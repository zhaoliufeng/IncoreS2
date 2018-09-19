package com.ws.mesh.incores2.view.presenter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.telink.bluetooth.LeBluetooth;
import com.telink.bluetooth.event.DeviceEvent;
import com.telink.bluetooth.event.LeScanEvent;
import com.telink.bluetooth.event.MeshEvent;
import com.telink.bluetooth.event.NotificationEvent;
import com.telink.bluetooth.event.ServiceEvent;
import com.telink.bluetooth.light.ConnectionStatus;
import com.telink.bluetooth.light.DeviceInfo;
import com.telink.bluetooth.light.LeAutoConnectParameters;
import com.telink.bluetooth.light.LeRefreshNotifyParameters;
import com.telink.bluetooth.light.LightAdapter;
import com.telink.bluetooth.light.NotificationInfo;
import com.telink.bluetooth.light.OnlineStatusNotificationParser;
import com.telink.bluetooth.light.Parameters;
import com.telink.util.Event;
import com.telink.util.EventListener;
import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.bean.Circadian;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.DeviceDAO;
import com.ws.mesh.incores2.service.WeSmartService;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.DeviceParamsDeal;
import com.ws.mesh.incores2.utils.SPUtils;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.utils.TaskPool;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.IMainView;

import java.util.List;

import static com.telink.bluetooth.light.LightAdapter.MODE_AUTO_CONNECT_MESH;
import static com.ws.mesh.incores2.constant.AppConstant.DEVICE_DEFAULT_NAME;

public class MainPresenter extends IBasePresenter<IMainView> implements EventListener<String> {

    private static final String TAG = "MainPresenter";
    //上下文对象
    private Activity mActivity;

    //网络
    private Mesh mCurrMesh;

    //广播监听
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        autoConnect();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        getView().onLoginOut();
                        break;
                }
            }
        }
    };

    public MainPresenter(Activity activity) {
        mActivity = activity;
        mCurrMesh = CoreData.core().getCurrMesh();
        addListener();
        registerReceiver();
        if (TextUtils.isEmpty(SPUtils.getLatelyMesh())) {
            SPUtils.setLatelyMesh(mCurrMesh.mMeshName);
        }
    }

    //请求蓝牙开启
    public void checkBle() {
        if (!CoreData.core().getBLERequestStatus()) {
            BluetoothAdapter mBluetoothAdapter =
                    LeBluetooth.getInstance().getAdapter(mActivity);
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(enableBtIntent, 1);
                CoreData.core().setBLERequest(true);
            }
        }

        //延迟2s 自动回连 否则 service 将报空
        TaskPool.DefRandTaskPool().PushTask(new Runnable() {
            @Override
            public void run() {
                autoConnect();
            }
        }, 2000);
    }

    private void addListener() {
        MeshApplication.getInstance().addEventListener(NotificationEvent.GET_DEVICE_TYPE, this);
        MeshApplication.getInstance().addEventListener(LeScanEvent.LE_SCAN, this);
        MeshApplication.getInstance().addEventListener(DeviceEvent.STATUS_CHANGED, this);
        MeshApplication.getInstance().addEventListener(NotificationEvent.ONLINE_STATUS, this);
        MeshApplication.getInstance().addEventListener(ServiceEvent.SERVICE_CONNECTED, this);
        MeshApplication.getInstance().addEventListener(MeshEvent.OFFLINE, this);
        MeshApplication.getInstance().addEventListener(MeshEvent.ERROR, this);
        MeshApplication.getInstance().addEventListener(LeScanEvent.LE_SCAN_TIMEOUT, this);
    }

    //监听广播
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY - 1);
        MeshApplication.getInstance().registerReceiver(mReceiver, filter);
    }

    private void autoConnect() {
        if (WeSmartService.Instance() != null) {
            if (!WeSmartService.Instance().isLogin()) {
                onMeshOffline();
            }

            if (WeSmartService.Instance().getMode() != MODE_AUTO_CONNECT_MESH) {
                //自动重连参数
                LeAutoConnectParameters connectParams = Parameters.createAutoConnectParameters();
                connectParams.setMeshName(CoreData.core().getCurrMesh().mMeshName);
                connectParams.setPassword(CoreData.core().getCurrMesh().mMeshPassword);
                connectParams.setTimeoutSeconds(15);
                connectParams.autoEnableNotification(true);
                WeSmartService.Instance().autoConnect(connectParams);
            }
            ReFreshNotify();
        }else {
            Log.e(TAG, "Service is null");
        }
    }

    private void ReFreshNotify() {
        //刷新Notify参数
        LeRefreshNotifyParameters refreshNotifyParams = Parameters.createRefreshNotifyParameters();
        refreshNotifyParams.setRefreshRepeatCount(2);
        refreshNotifyParams.setRefreshInterval(2000);
        //开启自动刷新Notify
        WeSmartService.Instance().autoRefreshNotify(refreshNotifyParams);
    }

    //切换网络
    private void switchNetwork() {
        Mesh mesh = CoreData.core().getCurrMesh();
        if (!mCurrMesh.mMeshName.equals(mesh.mMeshName)) {
            //更新设备信息
            getView().updateDevice(CoreData.core().mDeviceSparseArray);

            mCurrMesh = CoreData.core().getCurrMesh();
        }
    }

    public void destroy() {
        MeshApplication.getInstance().removeEventListener(this);
        MeshApplication.getInstance().unregisterReceiver(mReceiver);
        WeSmartService.Instance().onDestroy();
    }


    @Override
    public void performed(Event<String> event) {
        switch (event.getType()) {
            case NotificationEvent.ONLINE_STATUS:
                onOnlineStatusNotify((NotificationEvent) event);
                break;
            case DeviceEvent.STATUS_CHANGED:
                onDeviceStatusChanged((DeviceEvent) event);
                break;
            case MeshEvent.OFFLINE:
                onMeshOffline();
                break;
            case MeshEvent.ERROR:
                onMeshError((MeshEvent) event);
                break;
            case ServiceEvent.SERVICE_CONNECTED:
                onServiceConnected((ServiceEvent) event);
                break;
            case ServiceEvent.SERVICE_DISCONNECTED:
                onServiceDisconnected((ServiceEvent) event);
            case NotificationEvent.GET_DEVICE_TYPE:
                onDeviceType((NotificationEvent) event);
                break;
            default:
                break;
        }
    }

    /**
     * 设备的状态变化
     */
    private void onDeviceStatusChanged(DeviceEvent event) {
        DeviceInfo deviceInfo = event.getArgs();
        switch (deviceInfo.status) {
            case LightAdapter.STATUS_LOGIN:
                if (CoreData.mAddDeviceMode) return;
                SendMsg.updateDeviceTime();
                getView().onLoginSuccess();
                break;
            case LightAdapter.STATUS_CONNECTING:
                if (CoreData.mAddDeviceMode) return;
                getView().onFindDevice();
                break;
            case LightAdapter.STATUS_LOGOUT:
                getView().onLoginOut();
                if (CoreData.mAddDeviceMode) return;
                CoreData.core().clearDeleteDevList();
                switchNetwork();
                break;
            default:
                break;
        }
    }

    /**
     * 蓝牙状态数据上报
     */
    @SuppressWarnings("unchecked")
    private synchronized void onOnlineStatusNotify(NotificationEvent event) {
        List<OnlineStatusNotificationParser.DeviceNotificationInfo> mNotificationInfoList =
                (List<OnlineStatusNotificationParser.DeviceNotificationInfo>) event.parse();
        for (OnlineStatusNotificationParser.DeviceNotificationInfo notificationInfo : mNotificationInfoList) {
            int meshAddress = notificationInfo.meshAddress;
            int brightness = notificationInfo.brightness;
            int status = notificationInfo.status;
            int values = notificationInfo.reserve;
            Log.i(TAG, "brightness=" + brightness + ",values==" + values);
            ConnectionStatus connectionStatus = notificationInfo.connectStatus;
            if (CoreData.core().isDevDeleted(meshAddress)) {
                CoreData.core().mDeviceSparseArray.remove(meshAddress);
            } else {
                Device device = CoreData.core().mDeviceSparseArray.get(meshAddress);
                if (device != null) {
                    //已经获取过的设备
                    if (status != 0) {
                        device.mBrightness = brightness;
                        device.mConnectionStatus = connectionStatus;
                        device.mDevMeshId = meshAddress;
                        if (device.mDevType == AppConstant.DEFAULT_TYPE) {
                            SendMsg.getDeviceType(meshAddress);
                        }
                        device.updateIcon();
                    } else {
                        device.mConnectionStatus = ConnectionStatus.OFFLINE;
                    }
                    if (getView() != null)
                        getView().statusUpdate(device);
                } else {
                    if (status != 0) {
                        device = new Device();
                        device.mDeviceMeshName = mCurrMesh.mMeshName;
                        device.mDevName = DEVICE_DEFAULT_NAME + "-" + meshAddress;
                        device.mConnectionStatus = connectionStatus;
                        device.mBrightness = brightness;
                        device.mConnectionStatus = connectionStatus;
                        device.mDevMeshId = meshAddress;
                        device.mDevType = AppConstant.DEFAULT_TYPE;
                        device.circadian = new Circadian();
                        SendMsg.getDeviceType(meshAddress);

                        device.updateIcon();
//                        setCircadian(device);
                        CoreData.core().mDeviceSparseArray.put(device.mDevMeshId, device);
                        DeviceDAO.getInstance().insertDevice(device);
                        getView().updateDevice(CoreData.core().mDeviceSparseArray);

                    } else {
                        if (CoreData.core().isDevDeleted(meshAddress)) {
                            CoreData.core().mDeviceSparseArray.remove(meshAddress);
                            CoreData.core().deleteData(meshAddress);
                        }
                    }
                }
            }
        }
    }

    /**
     * 蓝牙连接服务连接
     */
    private void onServiceConnected(ServiceEvent event) {
        this.autoConnect();
    }

    /**
     * 蓝牙连接服务断开
     */
    private void onServiceDisconnected(ServiceEvent event) {

    }

    private void onMeshError(MeshEvent event) {
//        showToast(getResources().getString(R.string.faq_text_list), true);
    }

    /**
     * Mesh网络离线
     */
    private void onMeshOffline() {
        for (int x = 0; x < CoreData.core().mDeviceSparseArray.size(); x++) {
            Device device = CoreData.core().mDeviceSparseArray.valueAt(x);
            device.mConnectionStatus = ConnectionStatus.OFFLINE;
            device.updateIcon();
        }
        getView().onLoginOut();
    }

    /**
     * 设备类型解析
     */
    private synchronized void onDeviceType(NotificationEvent event) {
        synchronized (MainPresenter.this) {
            NotificationInfo info = event.getArgs();
            int srcAddress = info.src & 0xFF;
            byte[] params = info.params;
            Device device = CoreData.core().mDeviceSparseArray.get(srcAddress);
            int type;
            if (device != null && params[0] == 0x01) {
                int one = ((int) params[1] & 0xFF) << 8;
                int two = params[2] & 0xFF;
                type = one + two;
                if (device.mDevType != type) {
                    String name = DeviceParamsDeal.getDeviceName(type);
                    device.mDevType = type;
                    device.mDevName = name + "-" + Integer.toString(srcAddress);
                    device.circadian = new Circadian();
//                    setCircadian(device);
                    CoreData.core().mDeviceSparseArray.put(device.mDevMeshId, device);
                    if (DeviceDAO.getInstance().updateDevice(device)) {
                        getView().statusUpdate(device);
                    }
                }
            }
            //判断是否全部获取到了设备类型
            int notTypeDeviceAddress = hasAllType();
            if (notTypeDeviceAddress != -1) {
                SendMsg.getDeviceType(notTypeDeviceAddress);
            }
        }
    }

    private int hasAllType() {
        for (int i = 0; i < CoreData.core().mDeviceSparseArray.size(); i++) {
            if (CoreData.core().mDeviceSparseArray.valueAt(i).mDevType == AppConstant.DEFAULT_TYPE) {
                return CoreData.core().mDeviceSparseArray.valueAt(i).mDevMeshId;
            }
        }
        return -1;
    }

    //设置设备的昼夜节律时间 昼夜节律默认关闭
    private void setCircadian(Device device) {
        //如果获取的昼夜节律时间是空的 就使用默认的昼夜节律 不然会出现数组越界的异常
        String sunRiseString = SPUtils.getSunrise().equals("") ? AppConstant.DEFAULT_SUNRISE_TIME : SPUtils.getSunrise();
        String sunSetString = SPUtils.getSunset().equals("") ? AppConstant.DEFAULT_SUNSET_TIME : SPUtils.getSunset();
        String[] sunRise = sunRiseString.split(":");
        String[] sunSet = sunSetString.split(":");

        device.circadian = new Circadian();
        device.circadian.dayDurTime = 1;
        device.circadian.dayStartHours = Integer.parseInt(sunRise[0]);
        device.circadian.dayStartMinutes = Integer.parseInt(sunRise[1]);

        device.circadian.nightDurTime = 1;
        device.circadian.nightStartHours = Integer.parseInt(sunSet[0]);
        device.circadian.nightStartMinutes = Integer.parseInt(sunSet[1]);

        device.circadian.isDayOpen = false;
        device.circadian.isNightOpen = false;
    }
}
