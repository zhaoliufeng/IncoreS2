package com.ws.mesh.incores2.service.music;

import com.ws.mesh.incores2.bean.Song;

import java.util.List;

public interface IMusicService {
    //开始 继续播放
    void start();
    //暂停
    void pause();
    //上一首
    int prevMusic();
    //下一首
    int nextMusic();
    //获取音乐列表
    List<Song> getMusicList();
    //设置播放模式
    void setPlayMode(PlayMode mode);
    //选择音乐
    void selectMusic(int index);
    //拖动进度条
    void seekTo(int msec);
}
