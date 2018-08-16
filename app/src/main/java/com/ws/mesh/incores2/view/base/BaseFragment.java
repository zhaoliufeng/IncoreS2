package com.ws.mesh.incores2.view.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.activity.MainActivity;
import com.ws.mesh.incores2.view.activity.StageThreeActivity;
import com.ws.mesh.incores2.view.activity.StageTwoActivity;

import java.net.InetAddress;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    /**
     * 获取当前绑定的视图id
     **/
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     **/
    protected abstract void initData();

    //保存 添加房间 场景情况需要保存信息
    public void onSave() {
    }

    //编辑 房间 设备管理有遍历界面
    public void onEdit() {
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void toast(int strId) {
        toast(getString(strId));
    }

    public void toast(final String str) {
        if (isAdded() && getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MeshApplication.getInstance(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //跳转层级Activity 没有具体业务逻辑 负责渲染fragment的界面
    public void pushStageActivity(int pageId) {
        startActivity(new Intent(getActivity(), findNextClass())
                .putExtra(IntentConstant.PAGE_TYPE, pageId));
    }

    public void pushStageActivity(int pageId, InetAddress inetAddress) {
        startActivity(new Intent(getActivity(), findNextClass())
                .putExtra(IntentConstant.PAGE_TYPE, pageId)
                .putExtra(IntentConstant.INTENT_OBJ, inetAddress));
    }

    public void pushStageActivity(int pageId, int meshAddress) {
        startActivity(new Intent(getActivity(), findNextClass())
                .putExtra(IntentConstant.PAGE_TYPE, pageId)
                .putExtra(IntentConstant.MESH_ADDRESS, meshAddress));
    }

    public void pushStageActivity(int pageId, int meshAddress, int alarmId) {
        startActivity(new Intent(getActivity(), findNextClass())
                .putExtra(IntentConstant.PAGE_TYPE, pageId)
                .putExtra(IntentConstant.MESH_ADDRESS, meshAddress)
                .putExtra(IntentConstant.ALARM_ID, alarmId));
    }

    public void pushStageActivityForResult(int pageId) {
        startActivityForResult(new Intent(getActivity(), findNextClass())
                        .putExtra(IntentConstant.PAGE_TYPE, pageId),
                IntentConstant.REQUEST_CODE);
    }

    public void pushActivity(Class<? extends BaseActivity> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    public void pushActivity(Class<? extends BaseActivity> clazz, int meshAddress) {
        startActivity(new Intent(getActivity(), clazz)
                .putExtra(IntentConstant.MESH_ADDRESS, meshAddress));
    }

    public void pushActivity(Class<? extends BaseActivity> clazz, int meshAddress, int sceneId) {
        startActivity(new Intent(getActivity(), clazz)
                .putExtra(IntentConstant.MESH_ADDRESS, meshAddress)
                .putExtra(IntentConstant.SCENE_ID, sceneId));
    }

    //寻找当前下一层的class
    public Class<?> findNextClass() {
        if (getActivity() instanceof StageTwoActivity) {
            return StageThreeActivity.class;
        } else {
            return StageTwoActivity.class;
        }
    }

    //判断是不是分享获得的网络 并提示有无修改权限
    protected boolean isShareMesh() {
        boolean isShare = CoreData.core().getCurrMesh().mIsShare;
        if (isShare) {
            toast(getString(R.string.no_permission));
        }
        return isShare;
    }

    //返回到主界面
    public void backToMainActivity(){
        for (Map.Entry<String, BaseActivity> activityMap : CoreData.mMangerActivity.entrySet()){
            if (!activityMap.getKey().equals(MainActivity.class.getSimpleName())){
                activityMap.getValue().finish();
            }
        }
    }

    //返回到第二级界面
    public void backToStageTwo(){
        for (Map.Entry<String, BaseActivity> activityMap : CoreData.mMangerActivity.entrySet()){
            if (!activityMap.getKey().equals(MainActivity.class.getSimpleName()) &&
                    !activityMap.getKey().equals(StageTwoActivity.class.getSimpleName())){
                activityMap.getValue().finish();
            }
        }
    }
}
