package com.ws.mesh.incores2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Song;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicItemAdapter extends RecyclerView.Adapter {

    private List<Song> data;
    private Context context;
    private OnItemSelectedListener onItemSelectedListener;
    private int currPosition;

    public MusicItemAdapter(List<Song> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_music, parent, false);
        context = parent.getContext();
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MusicViewHolder viewHolder = (MusicViewHolder) holder;
        if (position == currPosition){
            viewHolder.llMusicFrame.setBackground(
                    context.getResources().getDrawable(R.drawable.bg_round_white_with_border));
        }else {
            viewHolder.llMusicFrame.setBackground(
                    context.getResources().getDrawable(R.drawable.bg_round_white));
        }
        viewHolder.tvMusicName.setText(data.get(viewHolder.getAdapterPosition()).getMusicName());
        viewHolder.tvMusicSinger.setText(data.get(viewHolder.getAdapterPosition()).getArtist());
        viewHolder.llMusicFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectedListener != null){
                    onItemSelectedListener.ItemSelected(viewHolder.getAdapterPosition());
                    currPosition = position;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setCurrPosition(int position){
        currPosition = position;
        this.notifyDataSetChanged();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_music_name)
        TextView tvMusicName;
        @BindView(R.id.tv_music_singer)
        TextView tvMusicSinger;
        @BindView(R.id.ll_music_frame)
        LinearLayout llMusicFrame;

        MusicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener){
        this.onItemSelectedListener = listener;
    }
}
