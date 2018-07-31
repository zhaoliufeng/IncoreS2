package com.ws.mesh.incores2.view.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Room;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.view.activity.ControlActivity;
import com.ws.mesh.incores2.view.activity.StageTwoActivity;
import com.ws.mesh.incores2.view.adapter.RoomAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.IZoneView;
import com.ws.mesh.incores2.view.presenter.ZonePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ZoneFragment extends BaseContentFragment<IZoneView, ZonePresenter> implements IZoneView {

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
        roomAdapter.setOnZoneMenuListener(new RoomAdapter.OnZoneClickListener() {
            @Override
            public void onMenu(int menuItemIndex, int position) {
                Room room = CoreData.core().mRoomSparseArray.valueAt(position);
                //菜单点击
                switch (menuItemIndex) {
                    case 0:
                        //群组设备管理

                        break;
                    case 1:
                        //颜色控制
                        pushActivity(ControlActivity.class, room.mRoomId);
                        break;
                    case 2:
                        //呼吸
                        pushStageActivity(PageId.BREATH, room.mRoomId);
                        break;
                    case 3:
                        //音乐
                        pushStageActivity(PageId.MUSIC, room.mRoomId);
                        break;
                    case 4:
                        //设定定时
                        pushStageActivity(PageId.TIMING, room.mRoomId);
                        break;
                    case 5:
                        //编辑群组
                        pushStageActivity(PageId.EDIT_ZONE, room.mRoomId);
                        break;
                }
            }

            @Override
            public void onSwitch(boolean isOpen, int position) {
                //群组开关
                Room room = CoreData.core().mRoomSparseArray.valueAt(position);

                SendMsg.switchDevice(room.mRoomId, isOpen);
            }
        });
    }

    @Override
    protected ZonePresenter createPresent() {
        return new ZonePresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        roomAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.iv_add_zone)
    public void addZone() {
        presenter.addZone();
    }

    @Override
    public void maxRoomNum() {
        toast(R.string.cannot_add_any_room);
    }

    @Override
    public void addRoom(boolean success) {
        if (success) {
            toast(R.string.add_success);
            roomAdapter.notifyDataSetChanged();
        } else {
            toast(R.string.add_failed);
        }
    }
}
