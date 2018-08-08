package com.ws.mesh.incores2.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Scene;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class SceneAdapter extends RecyclerView.Adapter {

    private SparseArray<Scene> data;
    private boolean editMode;

    public SceneAdapter(SparseArray<Scene> sceneSparseArray) {
        this.data = sceneSparseArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scene, parent, false);
        return new SceneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SceneViewHolder viewHolder = (SceneViewHolder) holder;
        final Scene scene = data.valueAt(viewHolder.getAdapterPosition());
        viewHolder.tvSceneName.setText(scene.mSceneName);
        if (editMode) {
            viewHolder.ivDelete.setVisibility(View.VISIBLE);
            viewHolder.ivEdit.setVisibility(View.VISIBLE);
            viewHolder.tvRun.setVisibility(View.GONE);
        } else {
            viewHolder.ivDelete.setVisibility(View.GONE);
            viewHolder.ivEdit.setVisibility(View.GONE);
            viewHolder.tvRun.setVisibility(View.VISIBLE);
        }

        viewHolder.tvRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSceneActionListener != null)
                    onSceneActionListener.onRun(scene.mSceneId);
            }
        });

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSceneActionListener != null)
                    onSceneActionListener.onEdit(scene.mSceneId);
            }
        });

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSceneActionListener != null)
                    onSceneActionListener.onDelete(scene.mSceneId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SceneViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.tv_scene_name)
        TextView tvSceneName;
        @BindView(R.id.tv_run)
        TextView tvRun;

        SceneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    private OnSceneActionListener onSceneActionListener;

    public interface OnSceneActionListener {
        void onRun(int sceneId);

        void onEdit(int sceneId);

        void onDelete(int sceneId);
    }

    public void setOnSceneActionListener(OnSceneActionListener onSceneActionListener) {
        this.onSceneActionListener = onSceneActionListener;
    }
}
