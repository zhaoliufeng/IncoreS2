package com.ws.mesh.incores2.view.fragment;


import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.ShareManageBean;
import com.ws.mesh.incores2.utils.shareuitls.ShareDataMangeUtils;
import com.ws.mesh.incores2.view.adapter.ShareManageAdapter;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.control.DividerGridItemDecoration;

import butterknife.BindView;

public class ShareManageFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, ShareManageAdapter.ShareManageListener {

    @BindView(R.id.tbl_share)
    TabLayout mShareTable;

    @BindView(R.id.share_info_list)
    RecyclerView mRecyclerView;
    private ShareManageAdapter mShareManageAdapter;

    private boolean isSendShare = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_share_manage;
    }

    @Override
    protected void initData() {
        mShareTable.addOnTabSelectedListener(this);
        mShareManageAdapter = new ShareManageAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mShareManageAdapter);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(), getResources().getColor(R.color.app_bg)));
        mShareManageAdapter.setShareManageListener(this);
        mShareManageAdapter.setShareData(ShareDataMangeUtils.getSendShareData());
    }

    @Override
    public void onDestroy() {
        if (mShareTable != null) {
            mShareTable.removeOnTabSelectedListener(this);
        }
        super.onDestroy();

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            isSendShare = true;
            mShareManageAdapter.setShareData(ShareDataMangeUtils.getSendShareData());
        } else if (tab.getPosition() == 1) {
            isSendShare = false;
            mShareManageAdapter.setShareData(ShareDataMangeUtils.getReceiveShareData());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void reName(ShareManageBean shareManageBean) {

    }

    @Override
    public void delete(ShareManageBean shareManageBean) {
        if (isSendShare) {
            ShareDataMangeUtils.deleteSendShareData(shareManageBean);
            mShareManageAdapter.setShareData(ShareDataMangeUtils.getSendShareData());
        } else {
            ShareDataMangeUtils.deleteReceive(shareManageBean);
            mShareManageAdapter.setShareData(ShareDataMangeUtils.getReceiveShareData());
        }

    }
}

   
