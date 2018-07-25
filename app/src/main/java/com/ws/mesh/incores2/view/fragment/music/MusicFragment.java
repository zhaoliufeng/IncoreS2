package com.ws.mesh.incores2.view.fragment.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Song;
import com.ws.mesh.incores2.service.music.IMusicPlayListener;
import com.ws.mesh.incores2.service.music.PlayMusicService;
import com.ws.mesh.incores2.view.adapter.MusicViewPagerAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.BIND_AUTO_CREATE;

public class MusicFragment extends BaseFragment {

    private static final String TAG = "MusicFragment";

    @BindView(R.id.vp_music)
    ViewPager vpMusic;

    private MusicViewPagerAdapter adapter;
    private Intent mMusicIntent;
    private PlayMusicService mMusicService;
    private PlayConnection mConn;
    private List<Song> songList;

    private MusicInfoFragment musicInfoFragment;
    private MusicListFragment musicListFragment;
    //当前播放音乐的总时长
    private int currDur;

    IMusicPlayListener musicPlayListener = new IMusicPlayListener() {
        @Override
        public void MusicPlaying(int currPosition, int dur) {
            Log.i(TAG, "MusicPlaying: currPosition --> " + currPosition + " dur --> " + dur);
            currDur = dur;

            musicInfoFragment.setMusicProcess((int) ((currPosition / (float)dur) * 100));
        }

        @Override
        public void MusicChange(int currIndex, int dur) {
            Log.i(TAG, "MusicChange: currPosition --> " + currIndex + " dur --> " + dur);
        }

        @Override
        public void PlayModChanged(int oldMode, int newMode) {
            Log.i(TAG, "PlayModChanged: oldMode --> " + oldMode + " newMode --> " + newMode);
        }

        @Override
        public void MusicListChanged() {
            Log.i(TAG, "MusicListChanged");
        }

        @Override
        public void NewPlay(String musicName, String singer, int index) {
            Log.i(TAG, "NewPlay: musicName --> " + musicName + " singer --> " + singer);
            musicInfoFragment.setMusicInfo(musicName, singer);
        }

        @Override
        public void MusicPaused() {
            Log.i(TAG, "MusicPaused");
        }

        @Override
        public void MusicStart() {
            Log.i(TAG, "MusicStart");
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music;
    }

    @Override
    protected void initData() {
        List<BaseFragment> mFragmentList = new ArrayList<>();
        musicInfoFragment = new MusicInfoFragment();
        musicListFragment = new MusicListFragment();
        mFragmentList.add(musicInfoFragment);
        mFragmentList.add(musicListFragment);
        if (isAdded() && getActivity() != null){
            adapter = new MusicViewPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        }

        vpMusic.setAdapter(adapter);
        bindService();

        musicInfoFragment.setOnProcessListener(new MusicInfoFragment.OnProcessListener() {
            @Override
            public void setProcess(int process) {
                //拖动进度条
                int msec = (int) ((process / 100f) * currDur);
                mMusicService.seekTo(msec);
            }
        });

        musicListFragment.setOnMusicSelectedListener(new MusicListFragment.OnMusicSelectedListener() {
            @Override
            public void OnMusicSelected(int position) {
                mMusicService.selectMusic(position);
            }
        });

    }

    private void bindService() {
        if (mMusicService == null) {
            mMusicIntent = new Intent(MeshApplication.getInstance(), PlayMusicService.class);
            mConn = new PlayConnection();
            if (isAdded() && getActivity() != null) {
                getActivity().bindService(mMusicIntent, mConn, BIND_AUTO_CREATE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isAdded() && getActivity() != null) {
            getActivity().unbindService(mConn);
        }
    }

    @OnClick({R.id.iv_last, R.id.iv_play, R.id.iv_next})
    public void onControl(View view){
        switch (view.getId()){
            case R.id.iv_last:
                mMusicService.prevMusic();
                break;
            case R.id.iv_play:

                break;
            case R.id.iv_next:
                mMusicService.nextMusic();
                break;
        }
    }

    private class PlayConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicService = ((PlayMusicService.PlayBinder) service).getService();
            mMusicService.setMusicPlayListener(musicPlayListener);

            songList = mMusicService.getMusicList();
            if (songList.size() == 0) {
                toast(R.string.no_music);
                return;
            }

            musicListFragment.binData(songList);
            mMusicService.initMediaData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
