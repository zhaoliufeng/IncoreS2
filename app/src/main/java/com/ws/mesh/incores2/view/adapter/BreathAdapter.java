package com.ws.mesh.incores2.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreathAdapter extends RecyclerView.Adapter {

    private OnItemSelectedListener onItemSelectedListener;
    private int[] iconIds = {R.drawable.breath_rainbow, R.drawable.breath_heart, R.drawable.breath_greenbreath,
            R.drawable.breath_bluebreath, R.drawable.breath_alarm, R.drawable.breath_flash,
            R.drawable.breath_breathing, R.drawable.breath_smile, R.drawable.breath_sun};
    private String[] titles = {};

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_breath, parent, false);
        titles = parent.getContext().getResources().getStringArray(R.array.breath);
        return new BreathViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BreathViewHolder viewHolder = (BreathViewHolder) holder;
        viewHolder.ivIcon.setImageResource(iconIds[viewHolder.getAdapterPosition()]);
        viewHolder.tvName.setText(titles[viewHolder.getAdapterPosition()]);
        viewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.ItemSelected(viewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconIds.length;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    public class BreathViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;

        BreathViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
