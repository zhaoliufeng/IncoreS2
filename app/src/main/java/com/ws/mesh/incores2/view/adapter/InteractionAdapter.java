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

public class InteractionAdapter extends RecyclerView.Adapter<InteractionAdapter.InteractionViewHolder> {
    private int[] icons = {R.drawable.icon_interaction_breath, R.drawable.icon_interaction_music,
            R.drawable.icon_interaction_edit_timer, R.drawable.icon_interaction_edit_device};
    private int[] names = {R.string.breath, R.string.music,
            R.string.schedules, R.string.device_editor};

    public InteractionAdapter(boolean isAllDevices) {
        if (isAllDevices)
            icons = new int[]{R.drawable.icon_interaction_breath, R.drawable.icon_interaction_music};
    }

    @NonNull
    @Override
    public InteractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_interaction, parent, false);
        return new InteractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InteractionViewHolder holder, int position) {
        holder.ivIcon.setImageResource(icons[holder.getAdapterPosition()]);
        holder.tvName.setText(names[holder.getAdapterPosition()]);
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.ItemSelected(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    private OnItemSelectedListener onItemSelectedListener;

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    public class InteractionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;

        InteractionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
