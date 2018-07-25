package com.ws.mesh.incores2.view.fragment.music;

import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.control.seekbar.CustomSeekBar;
import com.ws.mesh.incores2.view.control.seekbar.OnSeekBarDragListener;

import butterknife.BindView;

public class MusicInfoFragment extends BaseFragment {

    @BindView(R.id.tv_music_name)
    TextView tvMusicName;
    @BindView(R.id.tv_music_singer)
    TextView tvMusicSinger;
    @BindView(R.id.csb_process)
    CustomSeekBar csbProcess;

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
                if (onProcessListener != null){
                    onProcessListener.setProcess(endProcess);
                }
            }
        });
    }

    //设置音乐信息
    public void setMusicInfo(String name, String singer) {
        tvMusicName.setText(name);
        tvMusicSinger.setText(singer);
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

    public interface OnProcessListener{
        void setProcess(int process);
    }

    private OnProcessListener onProcessListener;
    public void setOnProcessListener(OnProcessListener listener){
        this.onProcessListener = listener;
    }
}
