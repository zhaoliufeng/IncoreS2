package com.ws.mesh.incores2.view.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.view.adapter.ScheduleAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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
        SparseArray<Timing> timingSparseArray = TimingDAO.getInstance().queryTiming();
        timingAdapter = new ScheduleAdapter(timingSparseArray);
        rlTimingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlTimingList.setAdapter(timingAdapter);
    }
}
