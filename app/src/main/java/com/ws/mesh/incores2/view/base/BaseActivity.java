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
import com.ws.mesh.incores2.view.fragment.action.BreathFragment;
import com.ws.mesh.incores2.view.fragment.action.EditFragment;
import com.ws.mesh.incores2.view.fragment.action.KotlinEditFragment;
import com.ws.mesh.incores2.view.fragment.setting.AboutUsFragment;
import com.ws.mesh.incores2.view.fragment.setting.NetworkListFragment;
import com.ws.mesh.incores2.view.fragment.scene.SceneAddFragment;
import com.ws.mesh.incores2.view.fragment.setting.SettingFragment;
import com.ws.mesh.incores2.view.fragment.setting.ShareManageFragment;
import com.ws.mesh.incores2.view.fragment.timing.TimingEventsFragment;
import com.ws.mesh.incores2.view.fragment.action.ZoneDeviceManageFragment;
import com.ws.mesh.incores2.view.fragment.music.MusicFragment;
import com.ws.mesh.incores2.view.fragment.scene.SceneAddDeviceFragment;
import com.ws.mesh.incores2.view.fragment.scene.SceneAddTimingFragment;
import com.ws.mesh.incores2.view.fragment.timing.TimingEditFragment;
import com.ws.mesh.incores2.view.fragment.timing.TimingFragment;
import com.ws.mesh.incores2.view.fragment.share.ChooseShareMeshFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareReceiveFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareChooseRoleFragment;
import com.ws.mesh.incores2.view.fragment.share.ShareUserFragment;

import butterknife.ButterKnife;

public abstract class BaseActivity extends FragmentActivity {

    //绑定视图
    protected abstract int getLayoutId();
    //输出化数据
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
        //绑定标题栏
        if (findViewById(R.id.tv_title) != null) {
            title = findViewById(R.id.tv_title);
        }

        //绑定返回点击监听
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

    //跳转 Activity 携带 pageType
    public void pushActivity(Class<? extends BaseActivity> activityClass, int pageId) {
        startActivity(new Intent(this, activityClass)
                .putExtra(IntentConstant.PAGE_TYPE, pageId));
    }

    //跳转 Activity 携带 pageType meshAddress
    public void pushActivity(Class<? extends BaseActivity> activityClass, int pageId, int meshAddress) {
        startActivity(new Intent(this, activityClass)
                .putExtra(IntentConstant.PAGE_TYPE, pageId)
                .putExtra(IntentConstant.MESH_ADDRESS, meshAddress));
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

    /**
     * 根据 fragment 设置当前页面的标题
     * @param fragment 当前渲染的 fragment
     */
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

        if (fragment instanceof KotlinEditFragment) {
            int meshAddress = getIntent().getIntExtra(IntentConstant.MESH_ADDRESS, -1);
            if (meshAddress > 0x8000){
                title.setText(R.string.zone_editor);
            }else {
                title.setText(R.string.device_editor);
            }
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
            ((ZoneDeviceManageFragment) fragment).setOnChangeTitleListener(
                    new ZoneDeviceManageFragment.OnChangeTitle() {
                @Override
                public void onEdit(boolean editMode) {
                    title.setText(editMode ? R.string.allocate_devices : R.string.zone_manager);
                }
            });
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

        if (fragment instanceof AboutUsFragment){
            title.setText(R.string.about_us);
        }

    }

    //从当前 Intent 获取 pageId
    public int getPageId() {
        mPageId = getIntent().getIntExtra(IntentConstant.PAGE_TYPE, -1);
        return mPageId;
    }
}
