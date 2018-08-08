package com.ws.mesh.incores2.db;

import android.util.SparseArray;

import com.we_smart.sqldao.BaseDAO;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.utils.CoreData;

import java.util.List;

/**
 * Created by zhaol on 2018/4/24.
 */

public class RoomDAO extends BaseDAO<Room> {

    private RoomDAO() {
        super(Room.class);
    }

    private static RoomDAO dao;

    public static RoomDAO getInstance() {
        if (dao == null) {
            synchronized (MeshDAO.class) {
                if (dao == null) {
                    dao = new RoomDAO();
                }
            }
        }
        return dao;
    }

    /**
     * 插入房间
     */
    public boolean insertRoom(Room room) {
        room.mRoomMeshName = CoreData.core().getCurrMesh().mMeshName;
        room.mDeviceIdsStr = Room.devIdToString(room.mDeviceIds);
        room.circadianString = Room.circadianToString(room.circadian);
        return insert(room);
    }

    /**
     * 插入接收到分享的数据 这时不做meshName的更正
     */
    public boolean insertShareRoom(Room room) {
        room.mDeviceIdsStr = Room.devIdToString(room.mDeviceIds);
        return insert(room);
    }

    /**
     * 删除房间
     */
    public boolean deleteRoom(Room room) {
        return delete(room,
                "mRoomMeshName", "mRoomId");
    }

    /**
     * 清空网络中的房间 删除网络时需要调用这个方法
     * @param meshName 网络名
     * @return 是否删除成功
     */
    public boolean clearMeshRooms(String meshName) {
        Room room = new Room();
        room.mRoomMeshName = meshName;
        return delete(room,
                "mRoomMeshName");
    }

    /**
     * 更新房间信息
     */
    public boolean updateRoom(Room room) {
        room.mRoomMeshName = CoreData.core().getCurrMesh().mMeshName;
        room.mDeviceIdsStr = Room.devIdToString(room.mDeviceIds);
        room.circadianString = Room.circadianToString(room.circadian);
        return update(room,
                "mRoomMeshName", "mRoomId");
    }

    /**
     * 查询网络中的房间集合
     */
    public SparseArray<Room> queryRoom() {
        return queryRoom(new String[]{CoreData.core().getCurrMesh().mMeshName}, "mRoomMeshName");
    }

    public Room queryRoomByMeshId(int meshId) {
        return queryRoom(new String[]{CoreData.core().getCurrMesh().mMeshName, String.valueOf(meshId)},
                "mRoomMeshName", "mRoomId").valueAt(0);
    }

    private SparseArray<Room> queryRoom(String[] whereValue, String... whereKey) {
        List<Room> rooms = query(whereValue, whereKey);
        SparseArray<Room> roomSparseArray = new SparseArray<>();
        for (Room room : rooms) {
            room.mDeviceIds = Room.stringToDevId(room.mDeviceIdsStr);
            room.circadian = Room.stringToCircadian(room.circadianString);
            roomSparseArray.put(room.mRoomId, room);
        }
        return roomSparseArray;
    }

    public SparseArray<Room> queryRoomByMeshName(String mMeshName) {
        return queryRoom(new String[]{mMeshName}, "mRoomMeshName");
    }
}
