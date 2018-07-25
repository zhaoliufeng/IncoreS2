package com.ws.mesh.incores2.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.ws.mesh.incores2.bean.FavoriteColor;
import com.ws.mesh.incores2.utils.ViewUtils;

import java.util.List;

public class DeleteFavoriteAdapter extends FavoriteAdapter {

    public DeleteFavoriteAdapter(SparseArray<FavoriteColor> data) {
        super(data);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        final ColorTagViewHolder viewHolder = (ColorTagViewHolder) holder;
        FavoriteColor favoriteColor = data.valueAt(position);
        if (favoriteColor.red != 0 || favoriteColor.green != 0 || favoriteColor.blue != 0){
            viewHolder.crvColor.setBackgroundColor(Color.rgb(favoriteColor.red, favoriteColor.green, favoriteColor.blue));
        }else {
            viewHolder.crvColor.setBackgroundColor(ViewUtils.interpolate(0xEBF1F1F1, 0xB9FEB800, favoriteColor.warm / 255f));
        }
        viewHolder.tvBrightness.setText(String.format("%d%%", favoriteColor.brightness));
        viewHolder.crvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectedListener != null){
                    onItemSelectedListener.ItemSelected(viewHolder.getAdapterPosition());
                }
            }
        });
        viewHolder.ivDelete.setVisibility(View.VISIBLE);
    }
}
