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
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.NetworkViewHolder> {

    private List<Mesh> data;
    private boolean editMode;
    private OnNetActionListener onNetActionListener;

    public NetworkAdapter() {
        data = new ArrayList<>();
        sortNetworklist();
    }

    @NonNull
    @Override
    public NetworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_network, parent, false);
        return new NetworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkViewHolder holder, int position) {
        final Mesh mesh = data.get(holder.getAdapterPosition());
        holder.tvNetName.setText(mesh.mMeshShowName);
        if (isCurrMesh(mesh)) {
            holder.ivSelect.setImageResource(R.drawable.radio_item_selected);
        } else {
            holder.ivSelect.setImageResource(R.drawable.radio_item);
        }

        if (editMode) {
            holder.ivSelect.setVisibility(View.GONE);
            holder.ivDelNet.setVisibility(View.GONE);
            holder.ivAddDevice.setVisibility(View.GONE);

            //默认网络不可删除 当前网络不可删除 只有当前网络可以添加设备
            //需求增加 当默认网络的showName 不为 Fulife 即分享获得的网络时，可以删除当前默认网络，并新建默认网络
            if (mesh.mMeshName.equals(CoreData.core().getCurrMesh().mMeshName) &&
                    !mesh.mMeshName.equals(SPUtils.getDefaultMesh())){
                holder.ivAddDevice.setVisibility(View.VISIBLE);
            }else if (!mesh.mMeshShowName.equals(AppConstant.DEFAULT_MESH_NAME)){
                holder.ivDelNet.setVisibility(View.VISIBLE);
            }
        } else {
            holder.ivSelect.setVisibility(View.VISIBLE);
            holder.ivDelNet.setVisibility(View.GONE);
            holder.ivAddDevice.setVisibility(View.GONE);
        }

        holder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换网络
                if (onNetActionListener != null &&
                        !isCurrMesh(mesh))
                    onNetActionListener.switchNet(mesh);
            }
        });
        holder.ivDelNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除网络
                if (onNetActionListener != null)
                    onNetActionListener.delNet(mesh);
            }
        });
        holder.ivAddDevice.setOnClickListener(new View.OnClickListener() {
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
        sortNetworklist();
        notifyDataSetChanged();
    }

    //判断是不是当前 mesh
    private boolean isCurrMesh(Mesh mesh){
        return mesh.mMeshName.equals(CoreData.core().getCurrMesh().mMeshName);
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

    /**
     * 列表排序
     * 默认网络排第一个 其他网络按照创建时间排序
     */
    private void sortNetworklist(){
        data.clear();
        for (Map.Entry<String, Mesh> mesh : CoreData.core().mMeshMap.entrySet()) {
            data.add(mesh.getValue());
        }
        Collections.sort(data, new Comparator<Mesh>() {
            @Override
            public int compare(Mesh o1, Mesh o2) {
                return (int) (o1.createUtcTime - o2.createUtcTime);
            }
        });
        for (int i = 0; i < data.size(); i++){
            if (data.get(i).mMeshName.equals(SPUtils.getDefaultMesh())){
                data.add(0, data.remove(i));
            }
        }
    }
}
