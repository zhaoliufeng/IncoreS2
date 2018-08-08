package com.ws.mesh.incores2.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.we_smart.lansharelib.bean.Client;
import com.ws.mesh.incores2.MeshApplication;
import com.ws.mesh.incores2.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by we_smart on 2018/4/30.
 */

public class ShareUserAdapter extends RecyclerView.Adapter<ShareUserAdapter.UserInfoViewHolder> {

    private List<Client> mClientList;
    private onClickListener mClickListener;

    public void setShareUserList(List<Client> clients) {
        this.mClientList = clients;
        notifyDataSetChanged();
    }

    private Client mCheckClient;


    @Override
    public UserInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserInfoViewHolder(View.inflate(MeshApplication.getInstance(), R.layout.item_share_user_info, null));
    }

    @Override
    public void onBindViewHolder(UserInfoViewHolder holder, int position) {
        final Client client = mClientList.get(position);
        holder.mTvShareUserName.setText(client.mClientName);
        if (mCheckClient != null) {
            if (mCheckClient.mIpAddress.getHostAddress().equals(client.mIpAddress.getHostAddress())) {
                holder.mIvCheckStatus.setImageResource(R.drawable.radio_item_selected);
            } else {
                holder.mIvCheckStatus.setImageResource(R.drawable.radio_item);
            }
        } else {
            holder.mIvCheckStatus.setImageResource(R.drawable.radio_item);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckClient != null) {
                    if (!mCheckClient.mIpAddress.getHostAddress().equals(client.mIpAddress.getHostAddress())) {
                        mCheckClient = client;
                        if (mClickListener != null) {
                            notifyDataSetChanged();
                            mClickListener.onChooseItem(client);
                        }
                    }
                } else {
                    mCheckClient = client;
                    if (mClickListener != null) {
                        notifyDataSetChanged();
                        mClickListener.onChooseItem(client);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mClientList == null) return 0;
        return mClientList.size();
    }

    public interface onClickListener {
        void onChooseItem(Client client);
    }

    public void setClickListener(onClickListener clickListener) {
        this.mClickListener = clickListener;
    }


    class UserInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_info_name)
        TextView mTvShareUserName;

        @BindView(R.id.user_info_check_status)
        ImageView mIvCheckStatus;


        public UserInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
