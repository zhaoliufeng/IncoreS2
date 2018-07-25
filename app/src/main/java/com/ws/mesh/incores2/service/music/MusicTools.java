package com.ws.mesh.incores2.service.music;

import android.app.Service;
import android.database.Cursor;
import android.provider.MediaStore;

import com.ws.mesh.incores2.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicTools {

    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static List<Song> getMusicList(Service service) {
        List<Song> list = new ArrayList<>();
        Cursor cursor = service.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setMusicName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if (song.getSize() > 1000 * 800) {
                    if (song.getMusicName().contains("mkv")){
                        continue;
                    }
                    // 按 歌曲名 - 歌手 格式分割标题，分离出歌曲名和歌手
                    // 如果歌曲名中包含 “-” 则是复合歌手名
                    if (song.getArtist().equals("<unknown>")) {
                        //如果获取不到歌手名 就去拆分歌曲名
                        if (song.getMusicName().contains("-")) {
                            //按 - 标记拆分成两串字符串
                            String[] str = song.getMusicName().split("-");
                            song.setArtist(str[0].trim());
                            song.setMusicName(str[1].trim());
                        }else if (song.getMusicName().contains("_")){
                            String[] str = song.getMusicName().split("_");
                            for (int i = 0; i < str.length; i++){
                                if (str[i].equals(song.getArtist())){
                                    if (i == 0){
                                        song.setMusicName(str[1]);
                                    }else if (i == 1){
                                        song.setMusicName(str[0]);
                                    }
                                }
                            }
                        }
                    } else {
                        if (song.getMusicName().contains("-")) {
                            String[] str = song.getMusicName().split("-");
                            if (str[0].trim().equals(song.getArtist())) {
                                song.setMusicName(str[1].trim());
                            } else {
                                song.setMusicName(str[0].trim());
                            }
                        }else if (song.getMusicName().contains("_")){
                            String[] str = song.getMusicName().split("_");
                            for (int i = 0; i < str.length; i++){
                                if (str[i].equals(song.getArtist())){
                                    if (i == 0){
                                        song.setMusicName(str[1]);
                                    }else if (i == 1){
                                        song.setMusicName(str[0]);
                                    }
                                }
                            }
                        }
                    }

                    //除去末尾的文件格式后缀
                    if (song.getMusicName().contains(".mp3")) {
                        song.setMusicName(song.getMusicName().replace(".mp3", "").trim());
                    }
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }
        return list;
    }
}
