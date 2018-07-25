package com.ws.mesh.incores2.view.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.telink.WSMeshApplication;
import com.ws.mesh.incores2.constant.IntentConstant;

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
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WSMeshApplication.getInstance(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void pushActivity(Class<? extends BaseActivity> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    public void pushActivity(Class<? extends BaseActivity> clazz, int meshaddress) {
        startActivity(new Intent(getActivity(), clazz)
                .putExtra(IntentConstant.MESH_ADDRESS, meshaddress));
    }
    public void pushActivityWithPageId(Class<? extends BaseActivity> clazz, int pageId) {
        startActivity(new Intent(getActivity(), clazz)
                .putExtra(IntentConstant.PAGE_TYPE, pageId));
    }
}
