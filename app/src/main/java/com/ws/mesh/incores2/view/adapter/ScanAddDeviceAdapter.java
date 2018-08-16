package com.ws.mesh.incores2.view.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Device;


/**
 * Created by we_smart on 2017/12/27.
 */

public class ScanAddDeviceAdapter extends BaseAdapter {

    private SparseArray<Device> mDeviceSparseArray;

    private Context mContext;

    public void setSparseArray(SparseArray<Device> deviceSparseArray) {
        this.mDeviceSparseArray = deviceSparseArray;
        notifyDataSetChanged();
    }

    public ScanAddDeviceAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if (mDeviceSparseArray == null) {
            return 0;
        }
        return mDeviceSparseArray.size();
    }

    @Override
    public Device getItem(int position) {
        return mDeviceSparseArray.valueAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceItemHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_grid_device, null);
            holder = new DeviceItemHolder();
            holder.txtName = convertView.findViewById(R.id.tv_name);
            holder.ivIcon = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (DeviceItemHolder) convertView.getTag();
        }

        Device device = this.getItem(position);
        holder.txtName.setText(device.mDevName);
        holder.ivIcon.setImageResource(device.getOnlineIcon());

        return convertView;
    }

    private static class DeviceItemHolder {
        TextView txtName;
        ImageView ivIcon;
    }
}
