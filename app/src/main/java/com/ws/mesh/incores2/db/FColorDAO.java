package com.ws.mesh.incores2.db;

import android.util.SparseArray;

import com.we_smart.sqldao.BaseDAO;
import com.ws.mesh.incores2.bean.FavoriteColor;
import com.ws.mesh.incores2.utils.CoreData;

import java.util.List;

/**
 * FavoriteColor DAO
 */
public class FColorDAO extends BaseDAO<FavoriteColor> {

    private FColorDAO() {
        super(FavoriteColor.class);
    }

    private static FColorDAO dao;
    public static FColorDAO getInstance(){
        if (dao == null){
            synchronized (FColorDAO.class){
                if (dao == null){
                    dao = new FColorDAO();
                }
            }
        }
        return dao;
    }

    public boolean insertFColor(FavoriteColor fColor){
        fColor.meshName = CoreData.core().getCurrMesh().mMeshName;
        return insert(fColor);
    }

    public boolean insertShareFColor(FavoriteColor fColor){
        return insert(fColor);
    }

    public void deleteFColor(FavoriteColor fColor){
        delete(fColor, "cIndex", "deviceType");
    }

    public SparseArray<FavoriteColor> queryFColorWithMeshName(String meshName){
        return queryFColor(new String[]{meshName}, "meshName");
    }

    public SparseArray<FavoriteColor> queryFColorWithChannel(String channel){
        return queryFColor(new String[]{channel, CoreData.core().getCurrMesh().mMeshName}, "deviceType", "meshName");
    }

    public SparseArray<FavoriteColor> queryFColor(String[] values, String... key){
        List<FavoriteColor> favoriteColorList = query(values, key);
        SparseArray<FavoriteColor> sparseArray = new SparseArray<>();
        for (int i = 0; i < favoriteColorList.size(); i++){
            FavoriteColor favoriteColor = favoriteColorList.get(i);
            sparseArray.append(favoriteColor.cIndex, favoriteColor);
        }
        return sparseArray;
    }

    public boolean updateFColor(FavoriteColor favoriteColor){
        return update(favoriteColor, "cIndex", "deviceType");
    }
}
