package com.ws.mesh.incores2.view.presenter;

import android.util.SparseArray;

import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.view.base.IBasePresenter;
import com.ws.mesh.incores2.view.impl.ITimingView;

import java.util.List;

public class TimingPresenter extends IBasePresenter<ITimingView> {

    private int meshAddress;
    private SparseArray<Timing> timingSparseArray;

    public SparseArray<Timing> getAlarmList(){
        timingSparseArray = TimingDAO.getInstance().queryAlarmByMeshId(meshAddress);
        return timingSparseArray;
    }

    public void init(int meshAddress){
        this.meshAddress = meshAddress;
    }

}
