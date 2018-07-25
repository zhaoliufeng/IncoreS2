package com.ws.mesh.incores2.service.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import com.ws.mesh.incores2.bean.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlayMusicService extends Service implements IMusicService {

    private PlayBinder binder;
    private MediaPlayer mediaPlayer;
    //当前播放音乐的下标 默认0
    private int mCurrIndex = 0;
    private List<Song> musicDatas;
    private IMusicPlayListener musicPlayListener = new IMusicPlayListener() {
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
    };

    private List<Integer> randomIndexList = new ArrayList<>();

    //播放模式 默认列表循环
    private PlayMode mMode = PlayMode.LIST_LOOP;

    public PlayMusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new PlayBinder();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        initListener();
    }

    private void initListener() {
        //设置音乐播放不循环
        mediaPlayer.setLooping(false);
        //播放完继续播放下一首
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //判断播放模式
                switch (mMode) {
                    case LIST_LOOP:
                        nextMusic();
                        break;
                    case SINGLE_LOOP:
                        singleMusic();
                        break;
                    case RANDOM_LOOP:
                        randomNextMusic();
                        break;
                }
            }
        });
    }

    /**
     * 随机下一曲
     */
    private void randomNextMusic() {
        //判断list是否已经大于列表数量
        if (randomIndexList.size() >= musicDatas.size()) {
            randomIndexList.clear();
            return;
        }
        //生成随机数 判断生成的随机数是否在list中 是则重新生成 确保生成的随机数不会与上一次重复
        int randomIndex;
        do {
            randomIndex = (int) (Math.random() * musicDatas.size());
        }
        while (randomIndexList.contains(randomIndex));
        mCurrIndex = randomIndex;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());

        randomIndexList.add(randomIndex);
    }

    private void singleMusic() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initMediaData() {
        try {
            if (musicDatas.size() > 0) {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
                prepareMediaPlayer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Timer timer = new Timer();
    private void startTimeCount() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        musicPlayListener.MusicPlaying(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                    }
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }

            }
        }, 0, 1000);
    }

    private void prepareMediaPlayer() throws IOException {
        if (musicDatas.size() > 0) {
            mediaPlayer.prepare();
            mediaPlayer.start();
            musicPlayListener.NewPlay(musicDatas.get(mCurrIndex).getMusicName(), musicDatas.get(mCurrIndex).getArtist(), mCurrIndex);
            startTimeCount();
        }
    }

    //获取音乐列表
    @Override
    public List<Song> getMusicList() {
        musicDatas = MusicTools.getMusicList(this);
        return musicDatas;
    }

    //选择音乐
    @Override
    public void selectMusic(int index) {
        if (mCurrIndex == index) return;
        mCurrIndex = index;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void seekTo(int msec) {
        mediaPlayer.seekTo(msec);
    }

    //上一首
    @Override
    public int prevMusic() {
        if (musicDatas.size() <= 0) return -1;
        if (mMode == PlayMode.RANDOM_LOOP) {
            randomNextMusic();
        } else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicDatas.get(mCurrIndex = mCurrIndex == 0 ? musicDatas.size() - 1 : --mCurrIndex).getPath());
                prepareMediaPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());
        }
        return mCurrIndex;
    }

    //下一首
    @Override
    public int nextMusic() {
        if (musicDatas.size() <= 0) return -1;
        if (mMode == PlayMode.RANDOM_LOOP) {
            randomNextMusic();
        } else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicDatas.get(
                        mCurrIndex = mCurrIndex == musicDatas.size() - 1 ? 0 : ++mCurrIndex).getPath());
                prepareMediaPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());
        }
        return mCurrIndex;
    }

    //暂停
    @Override
    public void pause() {
        mediaPlayer.pause();
        musicPlayListener.MusicPaused();
    }

    //继续 开始播放
    @Override
    public void start() {
        mediaPlayer.start();
        musicPlayListener.MusicStart();
    }

    //设置播放模式
    @Override
    public void setPlayMode(PlayMode mode) {
        mMode = mode;
        if (mMode == PlayMode.RANDOM_LOOP) {
            randomIndexList.clear();
            randomIndexList.add(mCurrIndex);
        }
    }

    public class PlayBinder extends Binder {
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }

    //音乐播放监听
    public void setMusicPlayListener(IMusicPlayListener musicPlayListener) {
        this.musicPlayListener = musicPlayListener;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
