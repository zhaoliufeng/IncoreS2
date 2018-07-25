package com.ws.mesh.incores2.view.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.view.base.BaseContentActivity;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.fragment.main.DeviceFragment;
import com.ws.mesh.incores2.view.fragment.main.SceneFragment;
import com.ws.mesh.incores2.view.fragment.main.ScheduleFragment;
import com.ws.mesh.incores2.view.fragment.main.ZoneFragment;
import com.ws.mesh.incores2.view.impl.IMainView;
import com.ws.mesh.incores2.view.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseContentActivity<IMainView, MainPresenter> implements IMainView {

    private BaseFragment mCurrFragment;
    private ZoneFragment zoneFragment;
    private DeviceFragment deviceFragment;
    private SceneFragment sceneFragment;
    private ScheduleFragment scheduleFragment;

    private FragmentManager mFragmentManager;

    /***nav bar***/
    @BindView(R.id.img_main_zone)
    ImageView imgZone;
    @BindView(R.id.img_main_device)
    ImageView imgDevice;
    @BindView(R.id.img_main_scene)
    ImageView imgScene;
    @BindView(R.id.img_main_schedules)
    ImageView imgSchedules;
    @BindView(R.id.tv_main_zone)
    TextView tvZone;
    @BindView(R.id.tv_main_device)
    TextView tvDevice;
    @BindView(R.id.tv_main_scene)
    TextView tvScene;
    @BindView(R.id.tv_main_schedules)
    TextView tvSchedules;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        boolean enterScanView = getIntent().getBooleanExtra(IntentConstant.ENTER_SCAN_VIEW, false);
        if (enterScanView){
            //跳转到设备扫描入网界面

        }
        this.mFragmentManager = this.getSupportFragmentManager();
        zoneFragment = new ZoneFragment();
        deviceFragment = new DeviceFragment();
        sceneFragment = new SceneFragment();
        scheduleFragment = new ScheduleFragment();
        if (mCurrFragment == null) {
            FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_main_frame, this.deviceFragment).commit();
            mCurrFragment = this.deviceFragment;
        }
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.checkBle();
    }

    @OnClick({R.id.ll_main_zone, R.id.ll_main_device, R.id.ll_main_scene, R.id.ll_main_schedules})
    public void onNavBarSwitch(View v) {
        resetNavbarTextIcon();
        switch (v.getId()) {
            case R.id.ll_main_zone:
                switchFragment(zoneFragment);
                imgZone.setImageResource(R.drawable.icon_tab_zones_selected);
                tvZone.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.ll_main_device:
                switchFragment(deviceFragment);
                imgDevice.setImageResource(R.drawable.icon_tab_device_selected);
                tvDevice.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.ll_main_scene:
                switchFragment(sceneFragment);
                imgScene.setImageResource(R.drawable.icon_tab_scenes_selected);
                tvScene.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.ll_main_schedules:
                switchFragment(scheduleFragment);
                imgSchedules.setImageResource(R.drawable.icon_tab_schedules_selected);
                tvSchedules.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    private void resetNavbarTextIcon() {
        tvZone.setTextColor(getResources().getColor(R.color.grey_929292));
        tvDevice.setTextColor(getResources().getColor(R.color.grey_929292));
        tvScene.setTextColor(getResources().getColor(R.color.grey_929292));
        tvSchedules.setTextColor(getResources().getColor(R.color.grey_929292));

        imgZone.setImageResource(R.drawable.icon_tab_zones_unselected);
        imgDevice.setImageResource(R.drawable.icon_tab_device_unselected);
        imgScene.setImageResource(R.drawable.icon_tab_scenes_unselected);
        imgSchedules.setImageResource(R.drawable.icon_tab_schedules_unselected);
    }

    //切换fragment
    private void switchFragment(BaseFragment to) {
        if (mCurrFragment != to) {
            FragmentTransaction transaction = this.mFragmentManager.beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mCurrFragment).add(R.id.fl_main_frame, to);
            } else {
                transaction.hide(mCurrFragment).show(to);
            }
            mCurrFragment = to;
            transaction.commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onFindDevice() {
        toast("Connecting Device");
    }

    @Override
    public void onLoginSuccess() {
        toast("Connect Device Success");
    }

    @Override
    public void offline(Device device) {

    }

    @Override
    public void online(SparseArray<Device> sparseArray) {

    }

    @Override
    public void statusUpdate(final Device device) {
        //刷新设备状态
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceFragment.refreshDevice(device);
            }
        });
    }

    @Override
    public void onLoginOut() {

    }

    @Override
    public void updateDeviceType() {

    }

    @Override
    public void updateDevice(SparseArray<Device> deviceSparseArray) {

    }
}
