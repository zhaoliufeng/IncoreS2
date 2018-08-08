package com.ws.mesh.incores2.view.presenter;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Circadian;
import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.RoomDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SPUtils;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.IZoneView;

public class ZonePresenter extends IBasePresenter<IZoneView> {

    @SuppressLint("UseSparseArrays")
    public void addZone() {
        int zoneId = getZoneId();
        if (zoneId == -1) {
            getView().maxRoomNum();
            return;
        }
        Room room = new Room();
        room.mRoomId = zoneId;
        room.mRoomName = "Zone " + (zoneId - AppConstant.ZONE_START_ID);
        room.circadian = new Circadian();
        room.mDeviceIds = new SparseArray<>();
        room.circadian = new Circadian();
        if (RoomDAO.getInstance().insertRoom(room)) {
            CoreData.core().mRoomSparseArray.append(room.mRoomId, room);
            getView().addRoom(true);
        } else {
            getView().addRoom(false);
        }
    }

    private int getZoneId() {
        int zoneStartId = AppConstant.ZONE_START_ID;
        int zoneEndId = AppConstant.ZONE_START_ID + AppConstant.ZONE_MAX_NUM;
        for (int i = zoneStartId; i < zoneEndId; i++) {
            if (CoreData.core().mRoomSparseArray.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

//    //设置设备的昼夜节律时间 昼夜节律默认关闭
//    private void setCircadian(Room room){
//        //如果获取的昼夜节律时间是空的 就使用默认的昼夜节律 不然会出现数组越界的异常
//        String sunRiseString = SPUtils.getSunrise().equals("") ? AppConstant.DEFAULT_SUNRISE_TIME : SPUtils.getSunrise();
//        String sunSetString = SPUtils.getSunset().equals("") ? AppConstant.DEFAULT_SUNSET_TIME : SPUtils.getSunset();
//        String[] sunRise = sunRiseString.split(":");
//        String[] sunSet = sunSetString.split(":");
//
//        room.circadian = new Circadian();
//        room.circadian.dayDurTime = 1;
////        room.circadian.dayStartHours = Integer.parseInt(sunRise[0]);
////        room.circadian.dayStartMinutes = Integer.parseInt(sunRise[1]);
//        room.circadian.dayStartHours =;
//        room.circadian.dayStartMinutes = -1;
//
//        room.circadian.nightDurTime = 1;
//        room.circadian.nightStartHours = Integer.parseInt(sunSet[0]);
//        room.circadian.nightStartMinutes = Integer.parseInt(sunSet[1]);
//
//        room.circadian.isDayOpen = false;
//        room.circadian.isNightOpen = false;
//    }
}
