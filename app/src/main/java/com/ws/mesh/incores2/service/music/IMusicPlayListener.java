package com.ws.mesh.incores2.service.music;

public interface IMusicPlayListener {
    public void NewPlay(String musicName, String singer, int index);

    public void MusicPaused();

    public void MusicStart();

    public void MusicPlaying(int currPosition, int dur);

    public void MusicChange(int currIndex, int dur);

    public void PlayModChanged(int oldMode, int newMode);

    public void MusicListChanged();
}
