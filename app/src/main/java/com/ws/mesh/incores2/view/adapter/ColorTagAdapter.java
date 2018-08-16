package com.ws.mesh.incores2.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.view.control.ColorCircleView;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorTagAdapter extends RecyclerView.Adapter<ColorTagAdapter.ColorTagViewHolder> {
    private int[] colorTag = {0xFF0000, 0xFFFF00, 0x00FF00, 0x0000FF,
            0x00FFFF, 0xFF00FF, 0xFFFFFF, 0xFDA100};

    @NonNull
    @Override
    public ColorTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_color_tag, parent, false);
        return new ColorTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ColorTagViewHolder holder, int position) {
        holder.ccvColorTag.setBackgroundColor(colorTag[holder.getAdapterPosition()]);
        holder.ccvColorTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onColorTagSelectedListener != null) {
                    onColorTagSelectedListener.OnColorTagSelected(colorTag[holder.getAdapterPosition()]);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return colorTag.length;
    }

    private OnColorTagSelectedListener onColorTagSelectedListener;

    public interface OnColorTagSelectedListener {
        void OnColorTagSelected(int color);
    }

    public void setOnItemSelectedListener(OnColorTagSelectedListener listener) {
        this.onColorTagSelectedListener = listener;
    }

    class ColorTagViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ccv_color_tag)
        ColorCircleView ccvColorTag;

        ColorTagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
