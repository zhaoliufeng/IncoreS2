package com.ws.mesh.incores2;

import com.telink.WSMeshApplication;
import com.we_smart.sqldao.DBHelper;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.db.DBOpenHelper;
import com.ws.mesh.incores2.service.WeSmartService;
import com.ws.mesh.incores2.utils.SPUtils;

public class MeshApplication extends WSMeshApplication {


    //获取到app对象
    private static MeshApplication mApplication;

    public static MeshApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        DBHelper.getInstance().initDBHelper(
                new DBOpenHelper(this, AppConstant.SQL_NAME));
        SPUtils.init(this);
    }

    @Override
    public void doInit() {
        super.doInit();
        startLightService(WeSmartService.class);
    }
}
