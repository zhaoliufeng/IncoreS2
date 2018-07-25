package com.ws.mesh.incores2.db;

import android.util.SparseArray;

import com.we_smart.sqldao.BaseDAO;
import com.ws.mesh.incores2.bean.FavoriteColor;

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
        return insert(fColor);
    }

    public void deleteFColor(FavoriteColor fColor){
        delete(fColor, "cIndex", "parentChannel");
    }

    public SparseArray<FavoriteColor> queryFColor(String parentChannel){
        List<FavoriteColor> favoriteColorList = query(new String[]{parentChannel}, "parentChannel");
        SparseArray<FavoriteColor> sparseArray = new SparseArray<>();
        for (int i = 0; i < favoriteColorList.size(); i++){
            FavoriteColor favoriteColor = favoriteColorList.get(i);
            sparseArray.append(favoriteColor.cIndex, favoriteColor);
        }
        return sparseArray;
    }

    public boolean updateFColor(FavoriteColor favoriteColor){
        return update(favoriteColor, "index", "parentChannel");
    }
}
