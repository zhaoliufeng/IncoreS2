package com.ws.mesh.incores2.view.fragment.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.view.base.BaseFragment;

import butterknife.BindView;

public class AboutUsFragment extends BaseFragment {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.about_us;
    }

    @Override
    protected void initData() {
        if (getContext() == null) return;

        //设置软件版本号
        try {
            PackageManager pm = getContext().getPackageManager();
            PackageInfo info = pm.getPackageInfo(getContext().getPackageName(), 0);
            tvVersion.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
