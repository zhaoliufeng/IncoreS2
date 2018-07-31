package com.ws.mesh.incores2.view.fragment.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Song;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.service.music.IMusicPlayListener;
import com.ws.mesh.incores2.service.music.PlayMode;
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
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_music)
    ImageView ivMusic;
    @BindView(R.id.iv_microphone)
    ImageView ivMicrophone;

    private MusicViewPagerAdapter adapter;
    private Intent mMusicIntent;
    private PlayMusicService musicService;
    private PlayConnection mConn;
    private List<Song> songList;

    private MusicInfoFragment musicInfoFragment;
    private MusicListFragment musicListFragment;
    //当前播放音乐的总时长
    private int currDur;
    //控制的设备地址
    private int meshAddress;

    //音乐播放监听
    IMusicPlayListener musicPlayListener = new IMusicPlayListener() {
        @Override
        public void MusicPlaying(int currPosition, int dur) {
            Log.i(TAG, "MusicPlaying: currPosition --> " + currPosition + " dur --> " + dur);
            currDur = dur;

            musicInfoFragment.setMusicProcess((int) ((currPosition / (float) dur) * 100));
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
            musicListFragment.setCurrMusicPosition(index);
            ivPlay.setImageResource(R.drawable.music_pause);
        }

        @Override
        public void MusicPaused() {
            Log.i(TAG, "MusicPaused");
            ivPlay.setImageResource(R.drawable.music_play);
        }

        @Override
        public void MusicStart() {
            Log.i(TAG, "MusicStart");
            ivPlay.setImageResource(R.drawable.music_pause);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music;
    }

    @Override
    protected void initData() {
        if (getActivity() != null){
            meshAddress = getActivity().getIntent().getIntExtra(IntentConstant.MESH_ADDRESS, -1);
        }

        List<BaseFragment> mFragmentList = new ArrayList<>();
        musicInfoFragment = new MusicInfoFragment();
        musicListFragment = new MusicListFragment();
        mFragmentList.add(musicInfoFragment);
        mFragmentList.add(musicListFragment);
        if (isAdded() && getActivity() != null) {
            adapter = new MusicViewPagerAdapter(
                    getActivity().getSupportFragmentManager(), mFragmentList);
        }

        vpMusic.setAdapter(adapter);
        bindService();

        musicInfoFragment.setOnControlListener(new MusicInfoFragment.OnControlListener() {
            @Override
            public void setProcess(int process) {
                //拖动进度条
                int msec = (int) ((process / 100f) * currDur);
                musicService.seekTo(msec);
            }

            @Override
            public void selectMusicList() {
                vpMusic.setCurrentItem(1);
            }

            @Override
            public void changeMusicMode(int mode) {
                switch (mode){
                    case 1:
                        musicService.setPlayMode(PlayMode.RANDOM_LOOP);
                        break;
                    case 2:
                        musicService.setPlayMode(PlayMode.SINGLE_LOOP);
                        break;
                    case 3:
                        musicService.setPlayMode(PlayMode.LIST_LOOP);
                        break;
                }
            }
        });

        musicListFragment.setOnMusicSelectedListener(new MusicListFragment.OnMusicSelectedListener() {
            @Override
            public void OnMusicSelected(int position) {
                musicService.selectMusic(position);
            }
        });

    }

    //绑定并启动音乐服务
    private void bindService() {
        if (musicService == null) {
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

    @OnClick({R.id.iv_music, R.id.iv_microphone})
    public void onMusicMicrophone(View view){
        switch (view.getId()){
            case R.id.iv_music:
                //切换到音乐模式
                ivMusic.setImageResource(R.drawable.music_dance_music);
                ivMicrophone.setImageResource(R.drawable.music_micro_phone_dance_unselected);

                break;
            case R.id.iv_microphone:
                //切换到麦克风输入模式
                ivMusic.setImageResource(R.drawable.music_dance_music_unselected);
                ivMicrophone.setImageResource(R.drawable.music_micro_phone_dance);

                break;
        }
    }

    @OnClick({R.id.iv_last, R.id.iv_play, R.id.iv_next})
    public void onControl(View view) {
        switch (view.getId()) {
            case R.id.iv_last:
                musicService.prevMusic();
                break;
            case R.id.iv_play:
                if (musicService.isPlaying()){
                    musicService.pause();
                }else {
                    musicService.start();
                }
                break;
            case R.id.iv_next:
                musicService.nextMusic();
                break;
        }
    }

    private class PlayConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((PlayMusicService.PlayBinder) service).getService();
            musicService.setMusicPlayListener(musicPlayListener);

            songList = musicService.getMusicList();
            if (songList.size() == 0) {
                toast(R.string.no_music);
                return;
            }

            musicListFragment.binData(songList);
            musicService.initMediaData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
