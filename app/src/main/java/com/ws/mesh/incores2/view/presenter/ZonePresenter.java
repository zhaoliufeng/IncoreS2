package com.ws.mesh.incores2.view.presenter;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Circadian;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.RoomDAO;
import com.ws.mesh.incores2.utils.CoreData;
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
}
