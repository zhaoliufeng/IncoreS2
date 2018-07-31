package com.ws.mesh.incores2.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.adapter.BreathAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;

import butterknife.BindView;

//默认呼吸
public class BreathFragment extends BaseFragment {
    @BindView(R.id.rl_breath)
    RecyclerView rlBreath;

    int meshAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_breath;
    }

    @Override
    protected void initData() {
        if (getActivity() != null){
            meshAddress = getActivity().getIntent().getIntExtra(IntentConstant.MESH_ADDRESS, -1);
        }

        BreathAdapter breathAdapter = new BreathAdapter();
        rlBreath.setAdapter(breathAdapter);
        rlBreath.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        breathAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void ItemSelected(int position) {
                //发送颜色控制指令
                SendMsg.loadBreath(meshAddress, position + 1);
            }
        });
    }
}
