package com.ws.mesh.incores2.view.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.view.adapter.TimingAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScheduleFragment extends BaseFragment {

    @BindView(R.id.rl_timing_list)
    RecyclerView rlTimingList;

    private TimingAdapter timingAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initData() {
        List<Timing> timings = new ArrayList<>();
        timings.add(new Timing());
        timings.add(new Timing());
        timings.add(new Timing());
        timingAdapter = new TimingAdapter(timings);
        rlTimingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlTimingList.setAdapter(timingAdapter);
    }
}
