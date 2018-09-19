package com.ws.mesh.incores2.view.fragment.share;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.we_smart.lansharelib.bean.Client;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.view.adapter.ShareUserAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.IUserStatusView;
import com.ws.mesh.incores2.view.presenter.ShareUserPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 等待接收端上线
 * 选择需要分享的接收方
 */
public class ShareUserFragment extends BaseContentFragment<IUserStatusView, ShareUserPresenter> implements IUserStatusView {

    @BindView(R.id.ll_user_data)
    View llUserListView;

    @BindView(R.id.ll_wait_online)
    View llWaitUserOnline;

    @BindView(R.id.user_data_list)
    RecyclerView mRecyclerView;

    private ShareUserAdapter mShareUsesrAdapter;

    private Client mCheckClient;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_share_user;
    }

    @Override
    protected void initData() {
        mShareUsesrAdapter = new ShareUserAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mShareUsesrAdapter);
        mShareUsesrAdapter.setClickListener(new ShareUserAdapter.onClickListener() {
            @Override
            public void onChooseItem(Client client) {
                mCheckClient = client;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    protected ShareUserPresenter createPresent() {
        return new ShareUserPresenter();
    }


    @Override
    public void onOnline(List<Client> clients) {
        Log.i(AppConstant.DEFAULT_MESH_NAME, "new Clients == " + clients.size());
        llUserListView.setVisibility(View.VISIBLE);
        llWaitUserOnline.setVisibility(View.GONE);
        mShareUsesrAdapter.setShareUserList(clients);
    }

    @Override
    public void onOffline(List<Client> clients) {
        if (clients.size() == 0) {
            llWaitUserOnline.setVisibility(View.VISIBLE);
            llUserListView.setVisibility(View.GONE);
        } else {
            llUserListView.setVisibility(View.VISIBLE);
            llWaitUserOnline.setVisibility(View.GONE);
            mShareUsesrAdapter.setShareUserList(clients);
        }

    }

    @OnClick(R.id.tv_next)
    public void onNext(){
        if (mCheckClient == null) {
            toast(R.string.check_user_reminder);
        }
        pushStageActivity(PageId.SHARE_NETWORK, mCheckClient.mIpAddress);
    }
}
