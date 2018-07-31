package com.ws.mesh.incores2.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.view.adapter.BreathAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;

import butterknife.BindView;
import butterknife.OnClick;

//选择定时需要执行的动作
public class TimingEventsFragment extends BaseFragment {

    @BindView(R.id.rl_events)
    RecyclerView rlEvents;
    @BindView(R.id.ll_on)
    LinearLayout llOn;
    @BindView(R.id.ll_off)
    LinearLayout llOff;
    BreathAdapter breathAdapter;

    int resultVal = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing_event;
    }

    @Override
    protected void initData() {
        breathAdapter = new BreathAdapter();
        rlEvents.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rlEvents.setAdapter(breathAdapter);
        breathAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void ItemSelected(int position) {
                resultVal = position + 2;
                backWithResult();
            }
        });
    }

    @OnClick({R.id.ll_on, R.id.ll_off})
    public void onOff(View view){
        switch (view.getId()){
            case R.id.ll_on:
                resultVal = 0;
                break;
            case R.id.ll_off:
                resultVal = 1;
                break;
        }
        backWithResult();
    }

    //带值返回
    private void backWithResult(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(IntentConstant.RESULT, resultVal);
        if (getActivity() != null){
            getActivity().setResult(Activity.RESULT_OK, resultIntent);
            getActivity().finish();
        }
    }
}
