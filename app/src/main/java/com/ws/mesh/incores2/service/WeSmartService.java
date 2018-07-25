package com.ws.mesh.incores2.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.telink.bluetooth.light.LightAdapter;
import com.telink.bluetooth.light.LightService;

public final class WeSmartService extends LightService {

    private static WeSmartService mThis;

    public static WeSmartService Instance() {
        return mThis;
    }

    @Override
    public IBinder onBind(Intent intent) {

        if (this.mBinder == null)
            this.mBinder = new LocalBinder();

        return super.onBind(intent);
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mThis = this;

        if (this.mAdapter == null)
            this.mAdapter = new LightAdapter();
        this.mAdapter.start(this);
    }

    public class LocalBinder extends Binder {
        public WeSmartService getService() {
            return WeSmartService.this;
        }
    }
}