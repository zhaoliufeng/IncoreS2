package com.ws.mesh.incores2.db;

import android.util.SparseArray;

import com.we_smart.sqldao.BaseDAO;
import com.ws.mesh.incores2.bean.Scene;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.constant.TimingType;
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

    public boolean deleteTimingByDeviceId(int deviceId) {
        Timing timing = new Timing();
        timing.mParentId = deviceId;
        timing.mAlarmType = TimingType.DEVICE.getValue();
        timing.mAlarmMeshName = CoreData.core().getCurrMesh().mMeshName;
        return delete(timing,
                "mParentId", "mAlarmMeshName", "mAlarmType");
    }

    public boolean deleteTimingBySceneId(int sceneId) {
        Timing timing = new Timing();
        timing.mParentId = sceneId;
        timing.mAlarmType = TimingType.SCENE.getValue();
        timing.mAlarmMeshName = CoreData.core().getCurrMesh().mMeshName;
        return delete(timing,
                "mParentId", "mAlarmMeshName", "mAlarmType");
    }

    public boolean deleteTimingByRoomId(int roomId) {
        Timing timing = new Timing();
        timing.mParentId = roomId;
        timing.mAlarmType = TimingType.ZONE.getValue();
        timing.mAlarmMeshName = CoreData.core().getCurrMesh().mMeshName;
        return delete(timing,
                "mParentId", "mAlarmMeshName", "mAlarmType");
    }

    public boolean updateTiming(Timing timing) {
        return update(
                timing,
                "mParentId", "mAlarmMeshName", "mAlarmType", "mAId");
    }

    public SparseArray<Timing> queryTiming() {
        return queryTiming(
                new String[]{CoreData.core().getCurrMesh().mMeshName},
                "mAlarmMeshName");
    }

    public SparseArray<Timing> queryAlarmByMeshId(int meshAddress) {
        int alarmType = meshAddress > 0x8000 ? TimingType.ZONE.getValue() : TimingType.DEVICE.getValue();
        if (meshAddress < 0x8000) {
            return queryTiming(
                    new String[]{
                            String.valueOf(meshAddress),
                            String.valueOf(alarmType),
                            CoreData.core().getCurrMesh().mMeshName},
                    "mParentId", "mAlarmType", "mAlarmMeshName");
        } else {
            return queryTiming(
                    new String[]{
                            String.valueOf(alarmType),
                            CoreData.core().getCurrMesh().mMeshName},
                    "mAlarmType", "mAlarmMeshName");
        }
    }

    public SparseArray<Timing> queryDeviceAlarmByMeshId(int meshAddress, String meshName) {
        return queryTiming(
                new String[]{
                        String.valueOf(meshAddress),
                        String.valueOf(TimingType.DEVICE.getValue()),
                        meshName},
                "mParentId", "mAlarmType", "mAlarmMeshName");
    }

    public SparseArray<Timing> queryRoomAlarm(String meshName) {
        return queryTiming(
                new String[]{
                        String.valueOf(TimingType.ZONE.getValue()),
                        meshName},
                "mAlarmType", "mAlarmMeshName");
    }

    public SparseArray<Timing> querySceneAlarmWithSceneId(int sceneId, String meshName) {
        return queryTiming(
                new String[]{
                        String.valueOf(sceneId),
                        String.valueOf(TimingType.SCENE.getValue()),
                        meshName},
                "mParentId", "mAlarmType", "mAlarmMeshName");
    }

    public Timing queryScheduleWithSceneId(int sceneId) {
        int alarmType = TimingType.SCENE.getValue();
        return queryTiming(
                new String[]{
                        String.valueOf(sceneId),
                        String.valueOf(alarmType),
                        CoreData.core().getCurrMesh().mMeshName},
                "mParentId", "mAlarmType", "mAlarmMeshName").valueAt(0);
    }

    public Timing queryScheduleWithSceneId(int sceneId, String meshName) {
        int alarmType = TimingType.SCENE.getValue();
        return queryTiming(
                new String[]{
                        String.valueOf(sceneId),
                        String.valueOf(alarmType),
                        meshName},
                "mParentId", "mAlarmType", "mAlarmMeshName").valueAt(0);
    }

    public SparseArray<Timing> querySceneAlarm() {
        return queryTiming(
                new String[]{String.valueOf(TimingType.SCENE.getValue()), CoreData.core().getCurrMesh().mMeshName},
                "mAlarmType", "mAlarmMeshName");
    }

    private SparseArray<Timing> queryTiming(String[] values, String... keys) {
        List<Timing> timings = query(values, keys);
        SparseArray<Timing> timingSparseArray = new SparseArray<>();
        for (Timing timing : timings) {
            timingSparseArray.put(timing.mAId, timing);
        }
        return timingSparseArray;
    }

    public List<Timing> queryTotalTiming() {
        return query(new String[]{CoreData.core().getCurrMesh().mMeshName},
                "mAlarmMeshName");
    }

    /**
     * 插入接收到分享的数据 这时不做meshName的更正
     */
    public boolean insertShareAlarm(Timing timing) {
        return insert(timing);
    }

    public SparseArray<Timing> queryAlarmByMeshName(String mMeshName) {
        return queryTiming(new String[]{mMeshName},
                "mAlarmMeshName");
    }
}
