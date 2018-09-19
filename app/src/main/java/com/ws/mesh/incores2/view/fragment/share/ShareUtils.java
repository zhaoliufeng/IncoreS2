package com.ws.mesh.incores2.view.fragment.share;

import android.util.Log;

import com.we_smart.lansharelib.LanShareSender;
import com.we_smart.lansharelib.bean.Client;
import com.we_smart.lansharelib.listener.OnSenderDataListener;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

/**
 * Created by we_smart on 2018/4/30.
 */

public class ShareUtils {

    private static final String TAG = "ShareUtils";

    private ShareUtils() {
    }

    private LanShareSender mLanShareSender;

    private static volatile ShareUtils mShareUtils;

    public HashMap<String, Client> mClientHashMap = new HashMap<>();

    public static ShareUtils getInstance() {
        if (mShareUtils == null) {
            synchronized (ShareUtils.class) {
                if (mShareUtils == null) {
                    mShareUtils = new ShareUtils();
                }
            }
        }
        return mShareUtils;
    }

    public void init() {
        try {
            mLanShareSender = new LanShareSender();
            mLanShareSender.setOnSenderDataListener(mOnSenderDataListener);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendData(InetAddress inetAddress, String data) {
        Log.i(TAG, "sendData: " + data);
        if (mLanShareSender == null) {
            init();
        }
        mLanShareSender.sendDataToReceive(inetAddress, data);
    }


    private OnSenderDataListener mOnSenderDataListener = new OnSenderDataListener() {
        @Override
        public void clientOnline(Client client) {
            if (mClientHashMap.get(client.mIpAddress.getHostAddress()) == null) {
                mClientHashMap.put(client.mIpAddress.getHostAddress(), client);
            }
            if (mStatusChangeListener != null) {
                mStatusChangeListener.Online(client);
            }
        }

        @Override
        public void clientOffline(InetAddress clientIp) {
            if (mClientHashMap.get(clientIp.getHostAddress()) == null) {
                mClientHashMap.remove(clientIp.getHostAddress());
            }
            if (mStatusChangeListener != null) {
                mStatusChangeListener.Offline(clientIp);
            }
        }

        @Override
        public void clientReceiveConfirm(InetAddress clientIp) {
            if (mShareInfoStatus != null) {
                mShareInfoStatus.onSuccess(clientIp);
            }
        }
    };


    private StatusChangeListener mStatusChangeListener;

    private ShareInfoStatus mShareInfoStatus;

    public void setStatusChangeListener(StatusChangeListener statusChangeListener) {
        mStatusChangeListener = statusChangeListener;
    }

    public void setShareInfoStatus(ShareInfoStatus shareInfoStatus) {
        mShareInfoStatus = shareInfoStatus;
    }

    public interface StatusChangeListener {

        void Online(Client client);

        void Offline(InetAddress client);
    }

    public interface ShareInfoStatus {

        void onSuccess(InetAddress inetAddress);
    }

    public void onDestroy() {
        if (mLanShareSender != null) {
            mLanShareSender.onDestory();
            mLanShareSender = null;
        }
    }
}
