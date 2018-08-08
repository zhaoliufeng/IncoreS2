package com.ws.mesh.incores2.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Mesh;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkAdapter extends RecyclerView.Adapter {

    private List<Mesh> data;
    private boolean editMode;
    private OnNetActionListener onNetActionListener;

    public NetworkAdapter() {
        data = new ArrayList<>();
        for (Map.Entry<String, Mesh> mesh : CoreData.core().mMeshMap.entrySet()) {
            if (mesh.getKey().equals(SPUtils.getDefaultMesh())) {
                data.add(0, mesh.getValue());
                continue;
            }
            data.add(mesh.getValue());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_network, parent, false);
        return new NetworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NetworkViewHolder viewHolder = (NetworkViewHolder) holder;
        final Mesh mesh = data.get(viewHolder.getAdapterPosition());
        viewHolder.tvNetName.setText(mesh.mMeshShowName);
        if (mesh.mMeshName.equals(CoreData.core().getCurrMesh().mMeshName)) {
            viewHolder.ivSelect.setImageResource(R.drawable.radio_item_selected);
        } else {
            viewHolder.ivSelect.setImageResource(R.drawable.radio_item);
        }

        if (editMode) {
            viewHolder.ivSelect.setVisibility(View.GONE);
            viewHolder.ivDelNet.setVisibility(View.GONE);
            viewHolder.ivAddDevice.setVisibility(View.GONE);

            //默认网络不可删除 当前网络不可删除 只有当前网络可以添加设备
            if (mesh.mMeshName.equals(CoreData.core().getCurrMesh().mMeshName) &&
                    !mesh.mMeshName.equals(SPUtils.getDefaultMesh())){
                viewHolder.ivAddDevice.setVisibility(View.VISIBLE);
            }else if (!mesh.mMeshName.equals(SPUtils.getDefaultMesh())){
                viewHolder.ivDelNet.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
            viewHolder.ivDelNet.setVisibility(View.GONE);
            viewHolder.ivAddDevice.setVisibility(View.GONE);
        }

        viewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换网络
                if (onNetActionListener != null)
                    onNetActionListener.switchNet(mesh);
            }
        });
        viewHolder.ivDelNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除网络
                if (onNetActionListener != null)
                    onNetActionListener.delNet(mesh);
            }
        });
        viewHolder.ivAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加设备
                if (onNetActionListener != null)
                    onNetActionListener.addDevice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public void refreshMeshList() {
        data.clear();
        for (Map.Entry<String, Mesh> mesh : CoreData.core().mMeshMap.entrySet()) {
            if (mesh.getKey().equals(SPUtils.getDefaultMesh())) {
                data.add(0, mesh.getValue());
                continue;
            }
            data.add(mesh.getValue());
        }
        notifyDataSetChanged();
    }

    class NetworkViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_net_name)
        TextView tvNetName;
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.iv_add_device)
        ImageView ivAddDevice;
        @BindView(R.id.iv_del_network)
        ImageView ivDelNet;

        NetworkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnNetActionListener {
        void switchNet(Mesh mesh);

        void delNet(Mesh mesh);

        void addDevice();
    }

    public void setOnNetActionListener(OnNetActionListener listener) {
        this.onNetActionListener = listener;
    }
}
