package com.ws.mesh.incores2;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.telink.TelinkApplication;
import com.we_smart.sqldao.DBHelper;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.DBOpenHelper;
import com.ws.mesh.incores2.service.WeSmartService;
import com.ws.mesh.incores2.utils.SPUtils;
import com.ws.mesh.incores2.utils.rise_set.LocationUtil;
import com.ws.mesh.incores2.utils.rise_set.SunriseSunset;

import java.util.Calendar;
import java.util.TimeZone;

public class MeshApplication extends TelinkApplication {

    private static final String TAG = "MeshApplication";
    //获取到app对象
    private static MeshApplication mApplication;

    public static MeshApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //初始化数据库
        DBHelper.getInstance().initDBHelper(
                new DBOpenHelper(this, AppConstant.SQL_NAME));

        SPUtils.init(this);

        String sunRiseStr = SPUtils.getSunrise();
        Log.i(TAG, "onCreate: " + sunRiseStr);
        if (SPUtils.getSunrise() == null ||
                SPUtils.getSunrise().equals("")){
            //如果没有保存昼夜节律 则请求坐标信息 计算昼夜节律的时间
            LocationUtil.getInstance().getLocation(this, new LocationUtil.OnLocationListener() {
                @Override
                public void onLocate(BDLocation location) {
                    SunriseSunset sunriseSunset = new SunriseSunset(
                            TimeZone.getDefault(), location.getLatitude(), location.getLongitude());
                    Log.i(TAG, "onLocate: " + location.getLatitude());

                    String sunRise = sunriseSunset.getOfficialSunrise(Calendar.getInstance());
                    String sunSet = sunriseSunset.getOfficialSunset(Calendar.getInstance());

                    Log.i(TAG, "onLocate: lat --> " + location.getLatitude() +
                            " lng --> " + location.getLongitude() +
                            " sunRise --> " + sunRise +
                            " sunSet --> " + sunSet);
                    SPUtils.saveSunrise(sunRise);
                    SPUtils.saveSunset(sunSet);
                }
            });
        }

    }

    @Override
    public void doInit() {
        super.doInit();
        startLightService(WeSmartService.class);
    }
}
