package com.ws.mesh.incores2.view.activity;

import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.adapter.ScanAddDeviceAdapter;
import com.ws.mesh.incores2.view.base.BaseContentActivity;
import com.ws.mesh.incores2.view.control.MultiScrollNumber;
import com.ws.mesh.incores2.view.impl.IScanView;
import com.ws.mesh.incores2.view.presenter.ScanPresenter;

import butterknife.BindView;
import butterknife.OnClick;

//扫描设备入网界面
public class ScanDeviceActivity extends BaseContentActivity<IScanView, ScanPresenter> implements IScanView {

    @BindView(R.id.tv_start)
    TextView tvStart;

    @BindView(R.id.current_status)
    TextView tvCurrStatus;

    @BindView(R.id.add_device_num)
    MultiScrollNumber mMultiScrollNumber;

    @BindView(R.id.list_devices)
    GridView gridView;
    private ScanAddDeviceAdapter deviceAdapter;

    @Override
    protected ScanPresenter createPresenter() {
        return new ScanPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_device;
    }

    @Override
    protected void initData() {
        deviceAdapter = new ScanAddDeviceAdapter(this);
        gridView.setAdapter(deviceAdapter);

        presenter.checkBle(this);
    }

    @OnClick(R.id.tv_start)
    public void onStartScan() {
        if (tvStart.getText().equals(getResources().getString(R.string.add_device))) {
            tvStart.setText(R.string.stop_scan);
            presenter.startScan();
        } else {
            presenter.stopScan();
        }
    }

    @OnClick(R.id.iv_scan_back)
    void OnBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        OnBack();
    }

    @Override
    public void addDeviceSuccess(SparseArray<Device> sparseArray) {
        mMultiScrollNumber.setNumber(sparseArray.size());
        deviceAdapter.setSparseArray(sparseArray);
    }

    @Override
    public void addDeviceFinish(int num) {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialog).create();
        if (!dialog.isShowing()) dialog.show();
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setContentView(R.layout.dialog_scan_view);
        TextView tvCancel = window.findViewById(R.id.tv_cancel);
        TextView tvConfirm = window.findViewById(R.id.tv_confirm);
        TextView mTipMessage = window.findViewById(R.id.tv_msg);
        mTipMessage.setText(getResources().getString(R.string.continue_add));
        dialog.setCanceledOnTouchOutside(false);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startScan();
                dialog.dismiss();
            }
        });
        tvCancel.setText(getString(R.string.no));
        tvConfirm.setText(getString(R.string.yes));
    }

    @Override
    public void addDeviceStatus(int status) {
        tvCurrStatus.setText(status);
    }

    @Override
    public void onStopScan() {
        tvCurrStatus.setText(R.string.stop_scan);
        tvStart.setText(R.string.add_device);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onBleClose() {
        tvCurrStatus.setText(getString(R.string.ble_close));
    }
}
