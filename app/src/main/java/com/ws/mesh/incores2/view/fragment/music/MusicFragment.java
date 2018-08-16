package com.ws.mesh.incores2.view.fragment.music;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.we_smart.permissions.PermissionsListener;
import com.we_smart.permissions.PermissionsManager;
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
    private PermissionsManager permissionsManager;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};

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

        permissionsManager = new PermissionsManager(getActivity(), permissions);
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

        //设置音乐进度拖动监听
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

        permissionsManager.checkPermissions(new PermissionsListener() {
            @Override
            public void getAllPermissions() {
                bindService();
            }

            @Override
            public void PermissionsDenied(String... deniedPermissions) {
                permissionsManager.startRequestPermission(deniedPermissions);
            }

            @Override
            public void cancelPermissionRequest() {
                getActivity().finish();
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
        if (isAdded() &&
                getActivity() != null &&
                mConn != null) {
            getActivity().unbindService(mConn);
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
            musicService.setMeshAddress(meshAddress);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsManager.REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int audioPermission = PackageManager.PERMISSION_GRANTED;
                int sdPermission = PackageManager.PERMISSION_GRANTED;
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(this.permissions[0])) {
                        audioPermission = grantResults[i];
                    }
                    if (permissions[i].equals(this.permissions[1])) {
                        sdPermission = grantResults[i];
                    }
                }
                checkPermissionUseful(audioPermission, sdPermission);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsManager.REQUEST_SETTING_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int audioPermission = ContextCompat.checkSelfPermission(getActivity(), permissions[0]);
                int sdPermission = ContextCompat.checkSelfPermission(getActivity(), permissions[1]);
                checkPermissionUseful(audioPermission, sdPermission);
            }
        }
    }

    private void checkPermissionUseful(int audio, int sd) {
        String title;
        if (audio != PackageManager.PERMISSION_GRANTED && sd != PackageManager.PERMISSION_GRANTED) {
            title = getString(R.string.no_recode_sdcard_permission);
        } else if (audio != PackageManager.PERMISSION_GRANTED) {
            title = getString(R.string.no_recode_permission);
        } else if (sd != PackageManager.PERMISSION_GRANTED) {
            title = getString(R.string.no_sdcard_permission);
        } else {
            bindService();
            return;
        }
        // 提示用户应该去应用设置界面手动开启权限
        permissionsManager.showDialogTipUserGoToAppSettting(title, getString(R.string.tip_no_locate_permission));
    }
}
