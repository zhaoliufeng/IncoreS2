package com.ws.mesh.incores2.view.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.adapter.SceneAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.impl.ISceneView;
import com.ws.mesh.incores2.view.presenter.ScenePresenter;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class SceneFragment extends BaseContentFragment<ISceneView, ScenePresenter> implements ISceneView {

    @BindView(R.id.ll_main_frame)
    LinearLayout llMainFrame;
    @BindView(R.id.iv_add_scene)
    ImageView ivAddScene;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.tv_scene_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.view_space)
    View viewSpace;
    @BindView(R.id.rl_scene_list)
    RecyclerView rlSceneList;

    private SceneAdapter sceneAdapter;
    private boolean editMode;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scene;
    }

    @Override
    protected void initData() {
        rlSceneList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected ScenePresenter createPresent() {
        return new ScenePresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvTitle.setText(String.format(getString(R.string.title_scenes), presenter.getListSize()));
        sceneAdapter = new SceneAdapter(presenter.getSceneList());
        rlSceneList.setAdapter(sceneAdapter);
        sceneAdapter.setEditMode(editMode);

        sceneAdapter.setOnSceneActionListener(new SceneAdapter.OnSceneActionListener() {
            @Override
            public void onRun(int sceneId) {
                SendMsg.loadScene(0xFFFF, sceneId);
            }

            @Override
            public void onEdit(int sceneId) {
                pushStageActivity(PageId.ADD_SCENE, sceneId);
            }

            @Override
            public void onDelete(int sceneId) {
                presenter.deleteScene(sceneId);
            }
        });
    }

    @OnClick({R.id.iv_add_scene, R.id.iv_edit, R.id.tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_scene:
                if (isShareMesh()) {
                    return;
                }
                //添加场景
                presenter.addScene();
                break;
            case R.id.iv_edit:
                if (isShareMesh()) {
                    return;
                }
                editMode = true;
                //编辑
                llMainFrame.setBackgroundColor(getResources().getColor(R.color.black_333));
                tvTitle.setTextColor(getResources().getColor(R.color.white));
                sceneAdapter.setEditMode(true);
                ivAddScene.setVisibility(View.GONE);
                ivEdit.setVisibility(View.GONE);
                viewSpace.setVisibility(View.VISIBLE);
                tvFinish.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_finish:
                editMode = false;

                llMainFrame.setBackgroundColor(getResources().getColor(R.color.app_bg));
                tvTitle.setTextColor(getResources().getColor(R.color.black_333));
                sceneAdapter.setEditMode(false);
                ivAddScene.setVisibility(View.VISIBLE);
                ivEdit.setVisibility(View.VISIBLE);
                viewSpace.setVisibility(View.GONE);
                tvFinish.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void addScene(int sceneId) {
        if (sceneId != -1) {
            pushStageActivity(PageId.ADD_SCENE, sceneId);
        } else {
            toast(R.string.cannot_add_any_scene);
        }
    }

    @Override
    public void deleteScene(boolean success) {
        if (success) {
            sceneAdapter.notifyDataSetChanged();
        } else {
            toast(R.string.remove_failed);
        }
    }
}
