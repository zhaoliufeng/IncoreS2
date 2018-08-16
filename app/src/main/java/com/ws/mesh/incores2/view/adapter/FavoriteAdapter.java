package com.ws.mesh.incores2.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.FavoriteColor;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.control.ColorRoundView;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteColorViewHolder> {

    protected SparseArray<FavoriteColor> data;

    public FavoriteAdapter(SparseArray<FavoriteColor> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public FavoriteColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_favorite_color, parent, false);
        return new FavoriteColorViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final FavoriteColorViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    OnItemSelectedListener onItemSelectedListener;

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    class FavoriteColorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.crv_color)
        ColorRoundView crvColor;
        @BindView(R.id.tv_brightness)
        TextView tvBrightness;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;

        FavoriteColorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
