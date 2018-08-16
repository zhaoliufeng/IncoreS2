package com.ws.mesh.incores2.view.fragment.music;

import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.service.music.PlayMusicService;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.control.seekbar.CustomSeekBar;
import com.ws.mesh.incores2.view.control.seekbar.OnSeekBarDragListener;

import butterknife.BindView;
import butterknife.OnClick;

//当前音乐播放状态
public class MusicInfoFragment extends BaseFragment {

    @BindView(R.id.tv_music_name)
    TextView tvMusicName;
    @BindView(R.id.tv_music_singer)
    TextView tvMusicSinger;
    @BindView(R.id.iv_music_mode)
    ImageView ivMusicMode;
    @BindView(R.id.csb_process)
    CustomSeekBar csbProcess;

    //当前音乐播放模式 列表循环
    private int playMode = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music_info;
    }

    @Override
    protected void initData() {
        csbProcess.setOnSeekBarDragListener(new OnSeekBarDragListener() {
            @Override
            public void startDrag() {

            }

            @Override
            public void dragging(int process) {

            }

            @Override
            public void stopDragging(int endProcess) {
                if (onControlListener != null){
                    onControlListener.setProcess(endProcess);
                }
            }
        });
    }

    //设置音乐信息
    public void setMusicInfo(String name, String singer) {
        if (name.length() > 25){
            tvMusicName.setText(String.format("%s...", name.substring(0, 25)));
        }else {
            tvMusicName.setText(name);
        }

        tvMusicSinger.setText(singer);
    }

    //切换音乐播放模式
    @OnClick(R.id.iv_music_mode)
    public void onChangeMode(){
        if (onControlListener != null){
            playMode = playMode == 3 ? 1: playMode + 1;
            switch (playMode){
                case 1:
                    ivMusicMode.setImageResource(R.drawable.icon_music_random);
                    break;
                case 2:
                    ivMusicMode.setImageResource(R.drawable.icon_music_single_cycle);
                    break;
                case 3:
                    ivMusicMode.setImageResource(R.drawable.icon_music_cycle);

                    break;
            }
            onControlListener.changeMusicMode(playMode);
        }
    }

    //音乐列表点击
    @OnClick(R.id.iv_music_list)
    public void onShowMusicList(){
        if (onControlListener != null){
            onControlListener.selectMusicList();
        }
    }

    //设置音乐进度
    public void setMusicProcess(final int process){
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    csbProcess.setProcessWithValue(process);
                }
            });
        }
    }

    public interface OnControlListener{
        void setProcess(int process);

        void selectMusicList();

        void changeMusicMode(int playMode);
    }

    private OnControlListener onControlListener;
    public void setOnControlListener(OnControlListener listener){
        this.onControlListener = listener;
    }
}
