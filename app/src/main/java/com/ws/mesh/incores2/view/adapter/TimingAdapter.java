package com.ws.mesh.incores2.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Timing;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimingAdapter extends RecyclerView.Adapter {

    private List<Timing> timingList;

    public TimingAdapter(List<Timing> data) {
        this.timingList = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_timing, parent, false);
        return new TimingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimingViewHolder viewHolder = (TimingViewHolder) holder;
        int listRes = R.drawable.icon_list_center;
        if (position == 0){
            listRes = R.drawable.icon_list_top;
        }else if (position == getItemCount() - 1){
            listRes = R.drawable.icon_list_bottom;
        }
        viewHolder.ivListIcon.setImageResource(listRes);
    }

    @Override
    public int getItemCount() {
        return timingList.size();
    }

    public class TimingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_list_icon)
        ImageView ivListIcon;

        TimingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
