package com.ws.mesh.incores2.db;

import android.util.SparseArray;

import com.we_smart.sqldao.BaseDAO;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.utils.CoreData;

import java.util.List;

public class TimingDAO extends BaseDAO<Timing> {

    private TimingDAO() {
        super(Timing.class);
    }

    private static TimingDAO dao;

    public static TimingDAO getInstance() {
        if (dao == null) {
            synchronized (TimingDAO.class) {
                if (dao == null) {
                    dao = new TimingDAO();
                }
            }
        }
        return dao;
    }


    public boolean insertTiming(Timing timing) {
        timing.mAlarmMeshName = CoreData.core().getCurrMesh().mMeshName;
        return insert(timing);
    }

    public boolean deleteTiming(Timing timing) {
        return delete(
                timing,
                "mParentId", "mAlarmMeshName", "mAlarmType", "mAId");
    }

    public boolean updateTiming(Timing timing) {
        return update(
                timing,
                "mParentId", "mAlarmMeshName", "mAlarmType", "mAId");
    }

    public SparseArray<Timing> queryTiming(){
        return queryTiming(
                new String[]{CoreData.core().getCurrMesh().mMeshName},
                        "mAlarmMeshName");
    }

    public SparseArray<Timing> queryAlarmByMeshId(int meshAddress) {
        int alarmType = meshAddress > 0x8000 ? AppConstant.ALARM_TYPE_GROUP : AppConstant.ALARM_TYPE_DEVICE;
        if (meshAddress < 0x8000) {
            return queryTiming(
                    new String[]{
                            String.valueOf(meshAddress),
                            String.valueOf(alarmType),
                            CoreData.core().getCurrMesh().mMeshName },
                    "mParentId", "mAlarmType", "mAlarmMeshName");
        } else {
            return queryTiming(
                    new String[]{
                            String.valueOf(alarmType),
                            CoreData.core().getCurrMesh().mMeshName },
                    "mAlarmType", "mAlarmMeshName");
        }
    }

    private SparseArray<Timing> queryTiming(String[] values, String... keys){
        List<Timing> timings = query(values, keys);
        SparseArray<Timing> timingSparseArray = new SparseArray<>();
        for (Timing timing : timings) {
            timingSparseArray.put(timing.mAId, timing);
        }
        return timingSparseArray;
    }

}
