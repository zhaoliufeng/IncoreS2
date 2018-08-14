package com.ws.mesh.incores2.view.fragment.music;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Song;
import com.ws.mesh.incores2.view.adapter.MusicItemAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;

import java.util.List;

import butterknife.BindView;

//音乐列表
public class MusicListFragment extends BaseFragment {
    @BindView(R.id.rl_music_list)
    RecyclerView rlMusicList;

    private MusicItemAdapter musicItemAdapter;
    private OnMusicSelectedListener onMusicSelectedListener;

    List<Song> songList;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music_list;
    }

    @Override
    protected void initData() {
        rlMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //绑定音乐数据
    public void binData(List<Song> data){
        this.songList = data;
        musicItemAdapter = new MusicItemAdapter(songList);
        rlMusicList.setAdapter(musicItemAdapter);

        musicItemAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void ItemSelected(int position) {
                if (onMusicSelectedListener != null){
                    onMusicSelectedListener.OnMusicSelected(position);
                }
            }
        });
    }

    //设置选择音乐监听
    public interface OnMusicSelectedListener{
        void OnMusicSelected(int position);
    }

    public void setOnMusicSelectedListener(OnMusicSelectedListener listener){
        this.onMusicSelectedListener = listener;
    }

    //设置当前播放音乐的position
    public void setCurrMusicPosition(int position){
        this.musicItemAdapter.setCurrPosition(position);
    }
}
