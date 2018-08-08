package com.ws.mesh.incores2.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.ShareManageBean;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.control.SwipeMenuLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by we_smart on 2018/5/1.
 */

public class ShareManageAdapter extends RecyclerView.Adapter<ShareManageAdapter.ShareManageViewHolder> {


    private List<ShareManageBean> mShareManageBeans;

    public void setShareData(List<ShareManageBean> shareManageBeans) {
        mShareManageBeans = shareManageBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShareManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_share_manage, parent, false);
        return new ShareManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShareManageViewHolder holder, int position) {
        final ShareManageBean shareManageBean = mShareManageBeans.get(position);
        holder.mTvShareName.setText(shareManageBean.mUserName);
        holder.mShareNetwork.setText(shareManageBean.mNetworkName);
        holder.mTvShareDate.setText(ViewUtils.showUtcTime(shareManageBean.utc));
        holder.mShareDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mSwipeMenuLayout.quickClose();
                if (mShareManageListener != null) {
                    mShareManageListener.delete(shareManageBean);
                }
            }
        });
        holder.mShareRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mSwipeMenuLayout.quickClose();
                if (mShareManageListener != null) {
                    mShareManageListener.reName(shareManageBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mShareManageBeans == null || mShareManageBeans.size() == 0) return 0;
        return mShareManageBeans.size();
    }

    private ShareManageListener mShareManageListener;

    public void setShareManageListener(ShareManageListener shareManageListener) {
        mShareManageListener = shareManageListener;
    }

    public interface ShareManageListener {

        void reName(ShareManageBean shareManageBean);

        void delete(ShareManageBean shareManageBean);
    }

    class ShareManageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_share_user_name)
        TextView mTvShareName;

        @BindView(R.id.tv_share_date)
        TextView mTvShareDate;

        @BindView(R.id.iv_rename_share)
        ImageView mShareRename;

        @BindView(R.id.tv_share_network)
        TextView mShareNetwork;

        @BindView(R.id.iv_delete_share)
        ImageView mShareDelete;

        @BindView(R.id.sm_share_manage)
        SwipeMenuLayout mSwipeMenuLayout;

        public ShareManageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
