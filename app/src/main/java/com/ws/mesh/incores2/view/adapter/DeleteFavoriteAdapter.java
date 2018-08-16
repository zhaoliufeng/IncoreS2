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
    public FavoriteColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final FavoriteColorViewHolder holder, int position, @NonNull List payloads) {
        FavoriteColor favoriteColor = data.valueAt(position);
        if (favoriteColor.red != 0 || favoriteColor.green != 0 || favoriteColor.blue != 0){
            holder.crvColor.setBackgroundColor(Color.rgb(favoriteColor.red, favoriteColor.green, favoriteColor.blue));
        }else {
            holder.crvColor.setBackgroundColor(ViewUtils.interpolate(0xEBF1F1F1, 0xB9FEB800, favoriteColor.warm / 255f));
        }
        holder.tvBrightness.setText(String.format("%d%%", favoriteColor.brightness));
        holder.crvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectedListener != null){
                    onItemSelectedListener.ItemSelected(holder.getAdapterPosition());
                }
            }
        });
        holder.ivDelete.setVisibility(View.VISIBLE);
    }
}
