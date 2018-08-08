package com.ws.mesh.incores2.view.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.service.music.IMusicPlayListener;
import com.ws.mesh.incores2.service.music.PlayMusicService;
import com.ws.mesh.incores2.view.base.BaseActivity;

public class MusicActivity extends BaseActivity implements IMusicPlayListener{
    private Intent mMusicIntent;
    private PlayMusicService mMusicService;
    private PlayConnection mConn;
    private int meshAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music;
    }

    @Override
    protected void initData() {
        bindService();
    }

    private void bindService() {
        if (mMusicService == null) {
            mMusicIntent = new Intent(this, PlayMusicService.class);
            mConn = new PlayConnection();
            bindService(mMusicIntent, mConn, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void NewPlay(String musicName, String singer, int index) {

    }

    @Override
    public void MusicPaused() {

    }

    @Override
    public void MusicStart() {

    }

    @Override
    public void MusicPlaying(int currPosition, int dur) {

    }

    @Override
    public void MusicChange(int currIndex, int dur) {

    }

    @Override
    public void PlayModChanged(int oldMode, int newMode) {

    }

    @Override
    public void MusicListChanged() {

    }

    //服务连接回调
    private class PlayConnection implements ServiceConnection {

        @SuppressLint("DefaultLocale")
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接之后开始查找音乐
            mMusicService = ((PlayMusicService.PlayBinder) service).getService();
            mMusicService.setMeshAddress(meshAddress);
            mMusicService.setMusicPlayListener(MusicActivity.this);
            mMusicService.getMusicList();
            mMusicService.initMediaData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
