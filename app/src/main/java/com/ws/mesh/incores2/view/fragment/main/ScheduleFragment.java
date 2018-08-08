package com.ws.mesh.incores2.view.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.view.adapter.ScheduleAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;

import butterknife.BindView;

public class ScheduleFragment extends BaseFragment {

    @BindView(R.id.rl_timing_list)
    RecyclerView rlTimingList;

    private ScheduleAdapter timingAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        timingAdapter = new ScheduleAdapter(TimingDAO.getInstance().queryTotalTiming());
        rlTimingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlTimingList.setAdapter(timingAdapter);
    }
}
