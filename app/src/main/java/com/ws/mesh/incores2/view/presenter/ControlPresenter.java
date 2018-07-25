package com.ws.mesh.incores2.view.presenter;

import android.graphics.Color;
import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Device;
import com.ws.mesh.incores2.bean.FavoriteColor;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.db.FColorDAO;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.IControlView;

import java.util.ArrayList;
import java.util.List;

public class ControlPresenter extends IBasePresenter<IControlView> {

    public SparseArray<FavoriteColor> colorSparseArray;
    public List<Integer> deleteIndexList;
    public int meshAddress;
    private int channel = 5;

    public ControlPresenter(int meshAddress) {
        this.meshAddress = meshAddress;
        getChannel(meshAddress);
        deleteIndexList = new ArrayList<>();
        colorSparseArray = FColorDAO.getInstance().queryFColor(String.valueOf(channel));
    }

    public void addFavorite(int r, int g, int b, int w, int c, int brightness) {
        FavoriteColor favoriteColor = new FavoriteColor();
        favoriteColor.cIndex = colorSparseArray.size();
        favoriteColor.parentChannel = channel;
        favoriteColor.red = r;
        favoriteColor.green = g;
        favoriteColor.blue = b;
        favoriteColor.warm = w;
        favoriteColor.cold = c;
        favoriteColor.brightness = brightness;

        if (FColorDAO.getInstance().insertFColor(favoriteColor)) {
            colorSparseArray.append(favoriteColor.cIndex, favoriteColor);
            getView().addFavoriteColor(true);
        } else {
            getView().addFavoriteColor(false);
        }
    }

    private void getChannel(int meshAddress) {
        if (meshAddress < 0x8000) {
            //设备
            Device device = CoreData.core().mDeviceSparseArray.get(meshAddress);

        } else {
            Room room = CoreData.core().mRoomSparseArray.get(meshAddress);
        }
    }

    public void controlColor(int red, int green, int blue, int warm, int clod) {
        SendMsg.setDevColor(meshAddress, Color.rgb(red, green, blue), warm, clod, false, (byte) 0x1F);
    }

    public void selectDeleteFavoriteColor(int position){
        deleteIndexList.add(position);
    }

    public void deleteFavoriteColor(){
        for (Integer index : deleteIndexList){
            FColorDAO.getInstance().deleteFColor(colorSparseArray.valueAt(index));
            colorSparseArray.removeAt(index);
        }
        deleteIndexList.clear();
    }
}
