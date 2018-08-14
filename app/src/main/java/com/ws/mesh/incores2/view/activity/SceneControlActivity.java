package com.ws.mesh.incores2.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telink.bluetooth.light.ConnectionStatus;
import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.FavoriteColor;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.adapter.ActionAdapter;
import com.ws.mesh.incores2.view.adapter.ColorTagAdapter;
import com.ws.mesh.incores2.view.adapter.DeleteFavoriteAdapter;
import com.ws.mesh.incores2.view.adapter.FavoriteAdapter;
import com.ws.mesh.incores2.view.base.BaseContentActivity;
import com.ws.mesh.incores2.view.control.ColorRoundView;
import com.ws.mesh.incores2.view.control.seekbar.CustomSeekBar;
import com.ws.mesh.incores2.view.control.seekbar.OnSeekBarDragListener;
import com.ws.mesh.incores2.view.impl.IControlView;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;
import com.ws.mesh.incores2.view.presenter.ControlPresenter;

import butterknife.BindView;
import butterknife.OnClick;

//选择场景设备状态
public class SceneControlActivity extends BaseContentActivity<IControlView, ControlPresenter> implements IControlView {

    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.ll_color_tag)
    LinearLayout llColorTag;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_menu_save)
    TextView tvSave;
    @BindView(R.id.tv_palette)
    TextView tvPalette;
    @BindView(R.id.tv_interaction)
    TextView tvInteraction;

    @BindView(R.id.crv_preview)
    ColorRoundView crvPreview;
    @BindView(R.id.tv_rgb_preview)
    TextView tvRgbPreview;
    @BindView(R.id.tv_wc_preview)
    TextView tvWcPreview;
    @BindView(R.id.tv_brightness)
    TextView tvBrightness;
    @BindView(R.id.csb_brightness)
    CustomSeekBar csbBrightness;
    @BindView(R.id.csb_red)
    CustomSeekBar csbRed;
    @BindView(R.id.csb_green)
    CustomSeekBar csbGreen;
    @BindView(R.id.csb_blue)
    CustomSeekBar csbBlue;
    @BindView(R.id.csb_warm)
    CustomSeekBar csbWarm;
    @BindView(R.id.csb_cold)
    CustomSeekBar csbCold;

    @BindView(R.id.iv_add_favorite_color)
    ImageView ivAddFavoriteColor;
    @BindView(R.id.iv_delete_color)
    ImageView ivDeleteColor;
    @BindView(R.id.img_menu_add)
    ImageView ivMenuAdd;

    @BindView(R.id.rl_favorite_color)
    RecyclerView rlFavoriteColor;
    @BindView(R.id.rl_color_tag)
    RecyclerView rlColorTag;
    @BindView(R.id.rl_interaction)
    RecyclerView rlAction;
    @BindView(R.id.ll_control)
    LinearLayout llControl;
    @BindView(R.id.tv_clear_set)
    TextView tvClearSet;

    private FavoriteAdapter favoriteAdapter;
    private ColorTagAdapter colorTagAdapter;
    private ActionAdapter actionAdapter;

    private int red, green, blue, warm, cold, brightness;
    private int meshAddress;
    private int sceneId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_control;
    }

    @Override
    protected void initData() {
        meshAddress = getIntent().getIntExtra(
                IntentConstant.MESH_ADDRESS, -1);
        sceneId = getIntent().getIntExtra(
                IntentConstant.SCENE_ID, -1);
        presenter.init(meshAddress);
        //额外需要赋值sceneId
        presenter.initScene(sceneId);
        tvInteraction.setText(R.string.action);
        tvSave.setVisibility(View.VISIBLE);
        ivMenuAdd.setVisibility(View.GONE);

        csbBrightness.setOnSeekBarDragListener(new OnSeekBarDragListener() {
            @Override
            public void startDrag() {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void dragging(int process) {
                brightness = process;
                tvBrightness.setText(String.format("%d%%", process));
                SendMsg.setDevBrightness(meshAddress, process);
            }

            @Override
            public void stopDragging(int endProcess) {

            }
        });
        csbRed.setOnSeekBarDragListener(new OnSeekBarDragListener() {
            @Override
            public void startDrag() {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void dragging(int process) {
                red = (int) (process / 100f * 255);
                crvPreview.setBackgroundColor(Color.rgb(red, green, blue));
                tvRgbPreview.setText(String.format("RGB(%d,%d,%d)", red, green, blue));
                presenter.controlColor(red, green, blue, warm, cold);
            }

            @Override
            public void stopDragging(int endProcess) {

            }
        });
        csbGreen.setOnSeekBarDragListener(new OnSeekBarDragListener() {
            @Override
            public void startDrag() {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void dragging(int process) {
                green = (int) (process / 100f * 255);
                crvPreview.setBackgroundColor(Color.rgb(red, green, blue));
                tvRgbPreview.setText(String.format("RGB(%d,%d,%d)", red, green, blue));
                presenter.controlColor(red, green, blue, warm, cold);
            }

            @Override
            public void stopDragging(int endProcess) {

            }
        });
        csbBlue.setOnSeekBarDragListener(new OnSeekBarDragListener() {
            @Override
            public void startDrag() {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void dragging(int process) {
                blue = (int) (process / 100f * 255);
                crvPreview.setBackgroundColor(Color.rgb(red, green, blue));
                tvRgbPreview.setText(String.format("RGB(%d,%d,%d)", red, green, blue));
                presenter.controlColor(red, green, blue, warm, cold);
            }

            @Override
            public void stopDragging(int endProcess) {

            }
        });
        csbWarm.setOnSeekBarDragListener(new OnSeekBarDragListener() {
            @Override
            public void startDrag() {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void dragging(int process) {
                warm = (int) (process / 100f * 255);
                tvWcPreview.setText(String.format("CoolWarm(%d,%d)", warm, cold));
                //如果当没有设置rgb彩色值时，预览颜色才显示色温的状态
                if (red == 0 && green == 0 && blue == 0)
                    crvPreview.setBackgroundColor(ViewUtils.interpolate(0xEBF1F1F1, 0xB9FEB800, warm / 255f));
                presenter.controlColor(red, green, blue, warm, cold);
            }

            @Override
            public void stopDragging(int endProcess) {

            }
        });
        csbCold.setOnSeekBarDragListener(new OnSeekBarDragListener() {
            @Override
            public void startDrag() {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void dragging(int process) {
                cold = (int) (process / 100f * 255);
                tvWcPreview.setText(String.format("CoolWarm(%d,%d)", warm, cold));
                presenter.controlColor(red, green, blue, warm, cold);
            }

            @Override
            public void stopDragging(int endProcess) {

            }
        });

        //自定义颜色
        favoriteAdapter = new FavoriteAdapter(presenter.colorSparseArray);
        rlFavoriteColor.setLayoutManager(new GridLayoutManager(this, 8));
        rlFavoriteColor.setAdapter(favoriteAdapter);
        favoriteAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void ItemSelected(int position) {
                FavoriteColor favoriteColor = presenter.colorSparseArray.valueAt(position);
                int color = Color.rgb(favoriteColor.red, favoriteColor.green, favoriteColor.blue);
                SendMsg.setDevColor(meshAddress, color, favoriteColor.warm, favoriteColor.cold, false, (byte) 0x1F);
                presenter.getDeviceColor();

                brightness = favoriteColor.brightness;
                setBrightnessStatus();
            }
        });

        //默认颜色 colorTag
        colorTagAdapter = new ColorTagAdapter();
        rlColorTag.setLayoutManager(new GridLayoutManager(this, 8));
        rlColorTag.setAdapter(colorTagAdapter);
        colorTagAdapter.setOnItemSelectedListener(new ColorTagAdapter.OnColorTagSelectedListener() {
            @Override
            public void OnColorTagSelected(int color) {
                presenter.setTagColor(color);

                presenter.getDeviceColor();
            }
        });

        //Action
        actionAdapter = new ActionAdapter();
        rlAction.setAdapter(actionAdapter);
        rlAction.setLayoutManager(new GridLayoutManager(this, 3));
        actionAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void ItemSelected(int position) {
                boolean saveSuccess;
                //Action item click
                switch (position) {
                    case 0:
                        //on
                        saveSuccess = presenter.saveSceneOnOff(true);
                        break;
                    case 1:
                        //off
                        saveSuccess = presenter.saveSceneOnOff(true);
                        break;
                    default:
                        saveSuccess = false;
                }
                if (saveSuccess) {
                    finish();
                } else {
                    toast(R.string.save_failed);
                }
            }
        });
    }

    @Override
    protected ControlPresenter createPresenter() {
        return new ControlPresenter();
    }

    @OnClick(R.id.tv_menu_save)
    public void onSave() {
        //保存设备状态到场景
        if (presenter.saveSceneColor(red, green, blue, warm, cold, brightness)) {
            finish();
        } else {
            toast(R.string.save_failed);
        }
    }

    @OnClick({R.id.tv_interaction, R.id.tv_palette})
    public void onTitleSwitch(View view) {
        if (view.getId() == R.id.tv_palette) {
            if (llControl.getVisibility() == View.GONE) {
                llControl.setVisibility(View.VISIBLE);
                tvClearSet.setVisibility(View.GONE);
                tvPalette.setTextColor(getResources().getColor(R.color.white));
                tvInteraction.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvPalette.setBackground(getResources().getDrawable(R.drawable.bg_title_palette_selected));
                tvInteraction.setBackground(getResources().getDrawable(R.drawable.bg_title_interaction_normal));
            }
        } else {
            if (llControl.getVisibility() == View.VISIBLE) {
                llControl.setVisibility(View.GONE);
                tvClearSet.setVisibility(View.VISIBLE);
                tvPalette.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvInteraction.setTextColor(getResources().getColor(R.color.white));
                tvPalette.setBackground(getResources().getDrawable(R.drawable.bg_title_palette_normal));
                tvInteraction.setBackground(getResources().getDrawable(R.drawable.bg_title_interaction_selected));
            }
        }
    }

    @OnClick(R.id.iv_add_favorite_color)
    public void onAddFavoriteColor() {
        if (llColorTag.getVisibility() == View.VISIBLE) {
            llColorTag.setVisibility(View.GONE);
            tvFinish.setVisibility(View.VISIBLE);
            llBg.setBackgroundColor(getResources().getColor(R.color.black_333));
            ivAddFavoriteColor.setVisibility(View.INVISIBLE);
            ivDeleteColor.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.iv_delete_color)
    public void onDeleteColor() {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialog).create();
        Window window = dialog.getWindow();
        dialog.show();
        if (window != null) {
            window.setContentView(R.layout.dialog_delete_favorite_color);
            RecyclerView rlDeleteList = window.findViewById(R.id.rl_favorite_color_list);
            TextView tvFinish = window.findViewById(R.id.tv_finish);

            final SparseArray<FavoriteColor> deleteColorTemp = presenter.colorSparseArray.clone();
            final DeleteFavoriteAdapter adapter = new DeleteFavoriteAdapter(deleteColorTemp);
            rlDeleteList.setAdapter(adapter);
            rlDeleteList.setLayoutManager(new GridLayoutManager(this, 6));
            adapter.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void ItemSelected(int position) {
                    presenter.selectDeleteFavoriteColor(position);
                    deleteColorTemp.removeAt(position);
                    adapter.notifyItemRemoved(position);
                }
            });
            tvFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.deleteFavoriteColor();
                    favoriteAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.img_back)
    public void onBack() {
        if (llColorTag.getVisibility() != View.VISIBLE) {
            //如果是在添加favorite color模式下 返回取消模式
            llColorTag.setVisibility(View.VISIBLE);
            tvFinish.setVisibility(View.GONE);
            llBg.setBackgroundColor(getResources().getColor(R.color.app_bg));
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @OnClick(R.id.tv_finish)
    public void onAddFavoriteColorConfirm() {
        presenter.addFavorite(red, green, blue, warm, cold, brightness);
        ivAddFavoriteColor.setVisibility(View.VISIBLE);
        ivDeleteColor.setVisibility(View.VISIBLE);
    }

    @Override
    public void addFavoriteColor(boolean success) {
        if (success) {
            favoriteAdapter.notifyDataSetChanged();
            if (llColorTag.getVisibility() != View.VISIBLE) {
                llColorTag.setVisibility(View.VISIBLE);
                tvFinish.setVisibility(View.GONE);
                llBg.setBackgroundColor(getResources().getColor(R.color.app_bg));
            }
        } else {
            toast(R.string.save_failed);
        }
    }

    @Override
    public void setColor(int color, int warm, int cold) {
        this.red = Color.red(color);
        this.green = Color.green(color);
        this.blue = Color.blue(color);
        this.warm = warm;
        this.cold = cold;
        setColorBarStatus();
    }

    //亮度只有刚进入控制界面才需要设置
    private boolean alreadySetBrightness;

    @Override
    public void setBrightness(final int brightness) {
        if (!alreadySetBrightness) {
            alreadySetBrightness = true;
            this.brightness = brightness;
            runOnUiThread(new Runnable() {
                @SuppressLint("DefaultLocale")
                @Override
                public void run() {
                    csbBrightness.setProcessWithAnimation(brightness);
                    tvBrightness.setText(String.format("%d%%", brightness));
                }
            });
        }
    }

    @Override
    public void setStatus(ConnectionStatus connectStatus) {
        if (connectStatus == ConnectionStatus.OFF) {
            this.red = 0;
            this.green = 0;
            this.blue = 0;
            this.green = 0;
            this.warm = 0;
            this.cold = 0;
            this.brightness = 0;
            setColorBarStatus();
        }
    }

    private void setColorBarStatus() {
        runOnUiThread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                csbRed.setProcessWithAnimation(getProcessVal(red));
                csbGreen.setProcessWithAnimation(getProcessVal(green));
                csbBlue.setProcessWithAnimation(getProcessVal(blue));
                csbWarm.setProcessWithAnimation(getProcessVal(warm));
                csbCold.setProcessWithAnimation(getProcessVal(cold));
                if (red != 0 || green != 0 || blue != 0) {
                    crvPreview.setBackgroundColor(Color.rgb(red, green, blue));
                    tvRgbPreview.setText(String.format("RGB(%d,%d,%d)", red, green, blue));
                } else {
                    crvPreview.setBackgroundColor(ViewUtils.interpolate(0xEBF1F1F1, 0xB9FEB800, warm / 255f));
                    tvWcPreview.setText(String.format("CollWarm(%d,%d)", warm, cold));
                }
            }
        });
    }

    private void setBrightnessStatus() {
        runOnUiThread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                csbBrightness.setProcessWithAnimation(brightness);
                tvBrightness.setText(String.format("%d%%", brightness));
            }
        });
    }

    //将 0 - 255 的值压缩到 0 - 100
    private int getProcessVal(int val) {
        return (int) ((val / 255.0f) * 100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick(R.id.tv_clear_set)
    public void onClearSet() {
        if (presenter.clearSceneSetting()) {
            finish();
        } else {
            toast(R.string.save_failed);
        }
    }
}
