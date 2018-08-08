package com.ws.mesh.incores2.view.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.AppLifeStatusConstant;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.utils.CoreData;
import com.ws.mesh.incores2.utils.StatusBarUpper;
import com.ws.mesh.incores2.view.activity.LauncherActivity;
import com.ws.mesh.incores2.view.fragment.BreathFragment;
import com.ws.mesh.incores2.view.fragment.EditFragment;
import com.ws.mesh.incores2.view.fragment.NetworkListFragment;
import com.ws.mesh.incores2.view.fragment.SceneAddFragment;
import com.ws.mesh.incores2.view.fragment.SettingFragment;
import com.ws.mesh.incores2.view.fragment.ShareManageFragment;
import com.ws.mesh.incores2.view.fragment.TimingEventsFragment;
import com.ws.mesh.incores2.view.fragment.ZoneDeviceManageFragment;
import com.ws.mesh.incores2.view.fragment.music.MusicFragment;
import com.ws.mesh.incores2.view.fragment.SceneAddDeviceFragment;
import com.ws.mesh.incores2.view.fragment.SceneAddTimingFragment;
import com.ws.mesh.incores2.view.fragment.TimingEditFragment;
import com.ws.mesh.incores2.view.fragment.TimingFragment;
import com.ws.mesh.incores2.view.fragment.share.ChooseShareMeshFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareReceiveFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareChooseRoleFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareUserFragment;

import butterknife.ButterKnife;

public abstract class BaseActivity extends FragmentActivity {

    protected abstract int getLayoutId();

    protected abstract void initData();

    /**
     * 由上层界面传递过来的pageId
     */
    protected int mPageId;

    protected BaseFragment mCurrFragment;

    private TextView title;

    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (CoreData.core().getCurrAppStatus() == AppLifeStatusConstant.KILL_PROGRESS) {
            //初始化状态
            Intent intent = new Intent(getApplicationContext(), LauncherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        ButterKnife.bind(this);
        //设置状态栏高度
        if (findViewById(R.id.view_status_bar) != null) {
            setStatusBar(findViewById(R.id.view_status_bar));
        }
        if (findViewById(R.id.tv_title) != null) {
            title = findViewById(R.id.tv_title);
        }

        if (findViewById(R.id.img_back) != null) {
            findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        initData();
        CoreData.addActivity(this, getClass().getSimpleName());
    }

    protected void toast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pushActivity(Class<? extends BaseActivity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    public void pushActivity(Class<? extends BaseActivity> activityClass, int pageId) {
        startActivity(new Intent(this, activityClass)
                .putExtra(IntentConstant.PAGE_TYPE, pageId));
    }

    public void pushActivity(Class<? extends BaseActivity> activityClass, int pageId, int meshAddress) {
        startActivity(new Intent(this, activityClass)
                .putExtra(IntentConstant.PAGE_TYPE, pageId)
                .putExtra(IntentConstant.MESH_ADDRESS, meshAddress));
    }

    public void pushActivity(Class<? extends BaseActivity> activityClass, boolean extra) {
        startActivity(new Intent(this, activityClass)
                .putExtra(IntentConstant.ENTER_SCAN_VIEW, extra));
    }

    protected void toast(int strId) {
        toast(getString(strId));
    }

    protected int getResourcesColor(int colorId) {
        return getResources().getColor(colorId);
    }

    /**
     * 设置状态栏 高度
     */
    protected void setStatusBar(View statusBar) {
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = StatusBarUpper.getStatusBarHeight(this);
        statusBar.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoreData.removeActivity(getClass().getSimpleName());
    }

    /**
     * 渲染fragment
     */
    public void setPage(BaseFragment fragment) {
        mCurrFragment = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_frame, mCurrFragment);
        fragmentTransaction.commit();
        setPageTitle(fragment);
    }

    private void setPageTitle(BaseFragment fragment) {
        if (fragment instanceof BreathFragment) {
            title.setText(R.string.breath);
        }

        if (fragment instanceof MusicFragment) {
            title.setText(R.string.music);
        }

        if (fragment instanceof TimingFragment ||
                fragment instanceof TimingEditFragment ||
                fragment instanceof SceneAddTimingFragment) {
            title.setText(R.string.schedule_editor);
        }

        if (fragment instanceof TimingEventsFragment){
            title.setText(R.string.schedule_events);
        }

        if (fragment instanceof EditFragment) {
            title.setText(R.string.zone_editor);
        }

        if (fragment instanceof SceneAddTimingFragment) {
            title.setText(R.string.schedule_editor);
        }

        if (fragment instanceof SceneAddDeviceFragment ||
                fragment instanceof SceneAddFragment) {
            title.setText(R.string.add_scene);
        }

        if (fragment instanceof ZoneDeviceManageFragment){
            title.setText(R.string.zone_manager);
        }

        if (fragment instanceof SettingFragment) {
            title.setText(R.string.settings);
        }

        if (fragment instanceof NetworkListFragment) {
            title.setText(R.string.network_manager);
        }

        if (fragment instanceof ShareManageFragment){
            title.setText(R.string.share_history);
        }

        if (fragment instanceof ShareChooseRoleFragment ||
                fragment instanceof ShareUserFragment ||
                fragment instanceof ChooseShareMeshFragment ||
                fragment instanceof ShareReceiveFragment) {
            title.setText(R.string.network_share);
        }

    }

    public int getPageId() {
        mPageId = getIntent().getIntExtra(IntentConstant.PAGE_TYPE, -1);
        return mPageId;
    }
}
