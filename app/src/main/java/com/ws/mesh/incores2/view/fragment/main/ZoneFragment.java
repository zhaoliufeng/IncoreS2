package com.ws.mesh.incores2.view.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.view.adapter.RoomAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.IZoneView;
import com.ws.mesh.incores2.view.presenter.ZonePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ZoneFragment extends BaseContentFragment<IZoneView, ZonePresenter> implements IZoneView{

    @BindView(R.id.rl_zone_list)
    RecyclerView rlZoneList;

    private RoomAdapter roomAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zone;
    }

    @Override
    protected void initData() {
        roomAdapter = new RoomAdapter(CoreData.core().mRoomSparseArray);
        rlZoneList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlZoneList.setAdapter(roomAdapter);
    }

    @Override
    protected ZonePresenter createPresent() {
        return new ZonePresenter();
    }

    @OnClick(R.id.iv_add_zone)
    public void addZone(){
        presenter.addZone();
    }

    @Override
    public void maxRoomNum() {
        toast(R.string.cannot_add_any_room);
    }

    @Override
    public void addRoom(boolean success) {
        if (success){
            toast(R.string.add_success);
            roomAdapter.notifyDataSetChanged();
        }else {
            toast(R.string.add_failed);

        }
    }
}
