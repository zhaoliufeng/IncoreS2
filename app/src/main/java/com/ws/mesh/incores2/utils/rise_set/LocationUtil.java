package com.ws.mesh.incores2.utils.rise_set;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;

//通过坐标获取当前位置的昼夜节律 并保存在SP中
public class LocationUtil {
    private static final String TAG = "LocationUtil";

    private LocationUtil() {
    }

    private static LocationUtil instance;

    public static LocationUtil getInstance() {
        if (instance == null) {
            synchronized (LocationUtil.class) {
                if (instance == null) {
                    instance = new LocationUtil();
                }
            }
        }
        return instance;
    }

    public void getLocation(Context context, final OnLocationListener listener){
        LocationClient locationClient = new LocationClient(context);
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.i(TAG, "onLocation: lat --> " + bdLocation.getLatitude() + " lng --> " + bdLocation.getLongitude());
                if (String.valueOf(bdLocation.getLatitude()).startsWith("4.9E")){
                    //定位失败的情况不保存
                    return;
                }
                listener.onLocate(bdLocation);
            }
        });

        locationClient.start();
    }

    public interface OnLocationListener {
        void onLocate(BDLocation location);

    }
}
