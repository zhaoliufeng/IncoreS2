package com.ws.mesh.incores2.bean;

import com.we_smart.sqldao.Annotation.DBFiled;

/**
 * Created by we_smart on 2017/11/15.
 */

public class Mesh {

    //网络名称
    @DBFiled(isPrimary = true)
    public String mMeshName;

    //网络密码
    @DBFiled
    public String mMeshPassword;

    //默认的网络帐号
    @DBFiled
    public String mMeshFactoryName;

    //默认的网络密码
    @DBFiled
    public String mMeshFactoryPassword;

    //显示名
    @DBFiled
    public String mMeshShowName;

    @DBFiled
    public boolean mIsShare;

    @DBFiled
    public String mMeshEditPassword;
}
