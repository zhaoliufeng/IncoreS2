package com.ws.mesh.incores2.utils;

import android.annotation.SuppressLint;

import java.util.Calendar;

/**
 * Created by zhaol on 2018/4/23.
 */

public class AccountUtil {
    private static final String accountVersion = "V1";

    //生成帐号
    @SuppressLint("DefaultLocale")
    public static String generateAccount(){
        //取utc秒级后8位
        Calendar cal = Calendar.getInstance();
        long utcInSecond = cal.getTimeInMillis()/1000;
        utcInSecond = utcInSecond % 100000000;
        //求utc后8位之和
        long utcTemp = utcInSecond;
        int sum = 0;
        while (utcTemp != 0){
            sum += utcTemp%10;
            utcTemp /= 10;
        }
        return String.format("%s%d%02d", accountVersion, utcInSecond, sum);
    }

    //生成密码
    public static String generatePassword(String account){
        return new StringBuffer(account).reverse().toString();
    }
}
