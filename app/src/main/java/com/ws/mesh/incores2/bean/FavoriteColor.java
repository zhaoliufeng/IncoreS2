package com.ws.mesh.incores2.bean;

import com.we_smart.sqldao.Annotation.DBFiled;


/**
 * 自定义颜色（favoriteColor）
 */
public class FavoriteColor {
    @DBFiled
    public int cIndex;
    //自定义颜色与路数绑定
    @DBFiled
    public int parentChannel;
    @DBFiled
    public int red;
    @DBFiled
    public int green;
    @DBFiled
    public int blue;
    @DBFiled
    public int warm;
    @DBFiled
    public int cold;
    @DBFiled
    public int brightness;
}
