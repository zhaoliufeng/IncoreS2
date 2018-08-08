package com.ws.mesh.incores2.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Mesh;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by we_smart on 2018/4/30.
 */

public class ChooseShareNetworkAdapter extends RecyclerView.Adapter<ChooseShareNetworkAdapter.MeshInfoViewHolder> {

    private List<Mesh> mMeshList;
    private onClickListener mClickListener;

    public void setShareUserList(List<Mesh> meshList) {
        this.mMeshList = meshList;
        notifyDataSetChanged();
    }

    private Mesh mCheckMesh;


    @Override
    public MeshInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeshInfoViewHolder(View.inflate(MeshApplication.getInstance(), R.layout.item_choose_share_mesh, null));
    }

    @Override
    public void onBindViewHolder(MeshInfoViewHolder holder, int position) {
        final Mesh mesh = mMeshList.get(position);
        holder.mTvMeshName.setText(mesh.mMeshShowName);
        if (mCheckMesh != null) {
            if (mCheckMesh.mMeshShowName.equals(mesh.mMeshShowName)) {
                holder.mIvMeshCheckStatus.setImageResource(R.drawable.radio_item_selected);
            } else {
                holder.mIvMeshCheckStatus.setImageResource(R.drawable.radio_item);
            }
        } else {
            holder.mIvMeshCheckStatus.setImageResource(R.drawable.radio_item);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckMesh != null) {
                    if (!mCheckMesh.mMeshShowName.equals(mesh.mMeshShowName)) {
                        mCheckMesh = mesh;
                        if (mClickListener != null) {
                            notifyDataSetChanged();
                            mClickListener.onChooseItem(mesh);
                        }
                    }
                } else {
                    mCheckMesh = mesh;
                    if (mClickListener != null) {
                        notifyDataSetChanged();
                        mClickListener.onChooseItem(mesh);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mMeshList == null) return 0;
        return mMeshList.size();
    }

    public interface onClickListener {
        void onChooseItem(Mesh mesh);
    }

    public void setClickListener(onClickListener clickListener) {
        this.mClickListener = clickListener;
    }


    class MeshInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mesh_name)
        TextView mTvMeshName;

        @BindView(R.id.mesh_check_status)
        ImageView mIvMeshCheckStatus;


        public MeshInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
