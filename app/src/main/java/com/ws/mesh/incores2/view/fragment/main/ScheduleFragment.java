package com.ws.mesh.incores2.view.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.db.TimingDAO;
import com.ws.mesh.incores2.view.adapter.ScheduleAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;

import java.util.List;

import butterknife.BindView;

public class ScheduleFragment extends BaseFragment {

    @BindView(R.id.rl_timing_list)
    RecyclerView rlTimingList;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private ScheduleAdapter timingAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initData() {
        refreshList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
           refreshList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList(){
        List<Timing> data = TimingDAO.getInstance().queryTotalTiming();
        tvTitle.setText(String.format(getString(R.string.title_schedules), data.size()));
        timingAdapter = new ScheduleAdapter(data);
        rlTimingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlTimingList.setAdapter(timingAdapter);

    }
}
