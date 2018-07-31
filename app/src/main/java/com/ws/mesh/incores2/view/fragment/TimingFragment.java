package com.ws.mesh.incores2.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.view.activity.StageThreeActivity;
import com.ws.mesh.incores2.view.adapter.TimingAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.impl.ITimingView;
import com.ws.mesh.incores2.view.presenter.TimingPresenter;

import butterknife.BindView;
import butterknife.OnClick;

//设备/房间定时列表
public class TimingFragment extends BaseContentFragment<ITimingView, TimingPresenter> implements ITimingView {

    @BindView(R.id.rl_timing_list)
    RecyclerView rlTimingList;

    private TimingAdapter timingAdapter;
    private int meshAddress;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing;
    }

    @Override
    protected void initData() {
        if (getActivity() != null){
            meshAddress = getActivity().getIntent().getIntExtra(IntentConstant.MESH_ADDRESS, -1);
            if (meshAddress == -1){
                toast("meshAddress 传值 -1");
                return;
            }
        }

        presenter.init(meshAddress);
        rlTimingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        timingAdapter = new TimingAdapter(presenter.getAlarmList());
        rlTimingList.setAdapter(timingAdapter);
    }

    @OnClick(R.id.iv_add_timing)
    public void addTiming(){
        //跳转添加定时界面
        pushStageActivity(PageId.ADD_TIMING, meshAddress);
    }

    @Override
    protected TimingPresenter createPresent() {
        return new TimingPresenter();
    }
}
