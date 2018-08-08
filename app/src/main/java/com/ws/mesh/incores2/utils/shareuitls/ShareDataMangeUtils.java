package com.ws.mesh.incores2.utils.shareuitls;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.mesh.incores2.bean.ShareManageBean;
import com.ws.mesh.incores2.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by we_smart on 2018/4/27.
 */

public class ShareDataMangeUtils {

    private static final String NETWORK_NAME = "network_name";

    private static final String NETWORK_UTC = "network_utc";

    private static final String USER_NAME = "user_name";

    public static List<ShareManageBean> getSendShareData() {
        String shareStr = SPUtils.getSendShareData();
        List<ShareManageBean> shareManageBeans = new ArrayList<>();
        if (TextUtils.isEmpty(shareStr)) return shareManageBeans;
        JSONArray jsonArray = JSONArray.parseArray(shareStr);
        for (int x = 0 ; x < jsonArray.size() ; x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            ShareManageBean shareManageBean = new ShareManageBean();
            shareManageBean.mNetworkName = jsonObject.getString(NETWORK_NAME);
            shareManageBean.utc = jsonObject.getLongValue(NETWORK_UTC);
            shareManageBean.mUserName = jsonObject.getString(USER_NAME);
            shareManageBeans.add(shareManageBean);
        }
        return shareManageBeans;
    }

    public static List<ShareManageBean> getReceiveShareData() {
        String shareStr = SPUtils.getReceiveShareData();
        List<ShareManageBean> shareManageBeans = new ArrayList<>();
        if (TextUtils.isEmpty(shareStr)) return shareManageBeans;
        JSONArray jsonArray = JSONArray.parseArray(shareStr);
        for (int x = 0 ; x < jsonArray.size() ; x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            ShareManageBean shareManageBean = new ShareManageBean();
            shareManageBean.mNetworkName = jsonObject.getString(NETWORK_NAME);
            shareManageBean.utc = jsonObject.getLongValue(NETWORK_UTC);
            shareManageBean.mUserName = jsonObject.getString(USER_NAME);
            shareManageBeans.add(shareManageBean);
        }
        return shareManageBeans;
    }

    public static void addNewSendShareData(ShareManageBean shareManageBean) {
        List<ShareManageBean> shareManageBeans = getSendShareData();
        shareManageBeans.add(shareManageBean);
        saveSendShareData(shareManageBeans);
    }

    public static void deleteSendShareData(ShareManageBean shareManageBean) {
        List<ShareManageBean> shareManageBeans = getSendShareData();
        shareManageBeans.remove(shareManageBean);
        saveSendShareData(shareManageBeans);
    }

    public static void saveSendShareData(List<ShareManageBean> shareManageBeans) {
        if (shareManageBeans != null || shareManageBeans.size() != 0) {
            JSONArray jsonArray = new JSONArray();
            for (int x = 0 ; x < shareManageBeans.size() ; x++) {
                JSONObject jsonObject = new JSONObject();
                ShareManageBean shareManageBean = shareManageBeans.get(x);
                jsonObject.put(NETWORK_NAME, shareManageBean.mNetworkName);
                jsonObject.put(NETWORK_UTC, shareManageBean.utc);
                jsonObject.put(USER_NAME, shareManageBean.mUserName);
                jsonArray.add(jsonObject);
            }
            SPUtils.setSendShareData(jsonArray.toJSONString());
        }
    }

    public static void saveReceiveShareData(List<ShareManageBean> shareManageBeans) {
        if (shareManageBeans != null || shareManageBeans.size() != 0) {
            JSONArray jsonArray = new JSONArray();
            for (int x = 0 ; x < shareManageBeans.size() ; x++) {
                JSONObject jsonObject = new JSONObject();
                ShareManageBean shareManageBean = shareManageBeans.get(x);
                jsonObject.put(NETWORK_NAME, shareManageBean.mNetworkName);
                jsonObject.put(NETWORK_UTC, shareManageBean.utc);
                jsonObject.put(USER_NAME, shareManageBean.mUserName);
                jsonArray.add(jsonObject);
            }
            SPUtils.setReceiveShareData(jsonArray.toJSONString());
        }
    }

    public static void deleteReceive(ShareManageBean shareManageBean) {
        List<ShareManageBean> shareManageBeans = getReceiveShareData();
        shareManageBeans.remove(shareManageBean);
        saveReceiveShareData(shareManageBeans);
    }


    public static void addNewReceiveShareData(ShareManageBean shareManageBean) {
        List<ShareManageBean> shareManageBeans = getReceiveShareData();
        shareManageBeans.add(shareManageBean);
        saveReceiveShareData(shareManageBeans);
    }


}
