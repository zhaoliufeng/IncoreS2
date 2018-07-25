package com.ws.mesh.incores2.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.FavoriteColor;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.utils.SendMsg;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.adapter.ColorTagAdapter;
import com.ws.mesh.incores2.view.adapter.DeleteFavoriteAdapter;
import com.ws.mesh.incores2.view.adapter.FavoriteAdapter;
import com.ws.mesh.incores2.view.adapter.InteractionAdapter;
import com.ws.mesh.incores2.view.base.BaseContentActivity;
import com.ws.mesh.incores2.view.control.ColorRoundView;
import com.ws.mesh.incores2.view.control.seekbar.CustomSeekBar;
import com.ws.mesh.incores2.view.control.seekbar.OnSeekBarDragListener;
import com.ws.mesh.incores2.view.impl.IControlView;
import com.ws.mesh.incores2.view.impl.OnItemSelectedListener;
import com.ws.mesh.incores2.view.presenter.ControlPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ControlActivity extends BaseContentActivity<IControlView, ControlPresenter> implements IControlView {

    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.ll_color_tag)
    LinearLayout llColorTag;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
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

    @BindView(R.id.rl_favorite_color)
    RecyclerView rlFavoriteColor;
    @BindView(R.id.rl_color_tag)
    RecyclerView rlColorTag;
    @BindView(R.id.rl_interaction)
    RecyclerView rlInteraction;
    @BindView(R.id.ll_control)
    LinearLayout llControl;

    private FavoriteAdapter favoriteAdapter;
    private ColorTagAdapter colorTagAdapter;
    private InteractionAdapter interactionAdapter;

    private int red, green, blue, warm, clod, brightness;
    private int meshAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_control;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        meshAddress = getIntent().getIntExtra(
                IntentConstant.MESH_ADDRESS, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
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
                presenter.controlColor(red, green, blue, warm, clod);
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
                presenter.controlColor(red, green, blue, warm, clod);
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
                presenter.controlColor(red, green, blue, warm, clod);
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
                tvWcPreview.setText(String.format("CoolWarm(%d,%d)", warm, clod));
                //如果当没有设置rgb彩色值时，预览颜色才显示色温的状态
                if (red == 0 && green == 0 && blue == 0)
                    crvPreview.setBackgroundColor(ViewUtils.interpolate(0xEBF1F1F1, 0xB9FEB800, warm / 255f));
                presenter.controlColor(red, green, blue, warm, clod);
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
                clod = (int) (process / 100f * 255);
                tvWcPreview.setText(String.format("CoolWarm(%d,%d)", warm, clod));
                presenter.controlColor(red, green, blue, warm, clod);
            }

            @Override
            public void stopDragging(int endProcess) {

            }
        });

        favoriteAdapter = new FavoriteAdapter(presenter.colorSparseArray);
        rlFavoriteColor.setLayoutManager(new GridLayoutManager(this, 8));
        rlFavoriteColor.setAdapter(favoriteAdapter);
        favoriteAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void ItemSelected(int position) {
                FavoriteColor favoriteColor = presenter.colorSparseArray.valueAt(position);
                int color = Color.rgb(favoriteColor.red, favoriteColor.green, favoriteColor.blue);
                SendMsg.setDevColor(meshAddress, color, favoriteColor.warm, favoriteColor.cold, false, (byte) 0x1F);
            }
        });

        colorTagAdapter = new ColorTagAdapter();
        rlColorTag.setLayoutManager(new GridLayoutManager(this, 8));
        rlColorTag.setAdapter(colorTagAdapter);
        colorTagAdapter.setOnItemSelectedListener(new ColorTagAdapter.OnColorTagSelectedListener() {
            @Override
            public void OnColorTagSelected(int color) {
                if (color == 0xFFFFFF) {
                    SendMsg.setDevColor(meshAddress, 0, 0, 255, false, (byte) 0x1F);
                } else if (color == 0xFDA100) {
                    SendMsg.setDevColor(meshAddress, 0, 255, 0, false, (byte) 0x1F);
                } else {
                    SendMsg.setDevColor(meshAddress, color, 0, 0, false, (byte) 0x1F);
                }
            }
        });

        //Interaction
        interactionAdapter = new InteractionAdapter();
        rlInteraction.setAdapter(interactionAdapter);
        rlInteraction.setLayoutManager(new GridLayoutManager(this, 3));
        interactionAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void ItemSelected(int position) {
                //Interaction item click
                switch (position){
                    case 0:
                        //呼吸
                        pushActivity(StageTwoActivity.class, PageId.BREATH, meshAddress);
                        break;
                    case 1:
                        //音乐
                        pushActivity(StageTwoActivity.class, PageId.MUSIC);
                        break;
                    case 2:
                        //定时
                        pushActivity(StageTwoActivity.class, PageId.TIMING);
                        break;
                    case 3:
                        //编辑设备
                        pushActivity(StageTwoActivity.class, PageId.EDIT_DEVICE);
                        break;
                }
            }
        });
    }

    @Override
    protected ControlPresenter createPresenter() {
        return new ControlPresenter(meshAddress);
    }

    @OnClick(R.id.tv_title)
    public void onSwitch() {
        if (llColorTag.getVisibility() == View.VISIBLE) {
            llColorTag.setVisibility(View.GONE);
            tvFinish.setVisibility(View.VISIBLE);
            llBg.setBackgroundColor(getResources().getColor(R.color.black_333));
        } else {
            llColorTag.setVisibility(View.VISIBLE);
            tvFinish.setVisibility(View.GONE);
            llBg.setBackgroundColor(getResources().getColor(R.color.app_bg));
        }
    }

    @OnClick({R.id.tv_interaction, R.id.tv_palette})
    public void onTitleSwitch(View view) {
        if (view.getId() == R.id.tv_palette) {
            if (llControl.getVisibility() == View.GONE) {
                llControl.setVisibility(View.VISIBLE);
                tvPalette.setTextColor(getResources().getColor(R.color.white));
                tvInteraction.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvPalette.setBackground(getResources().getDrawable(R.drawable.bg_title_palette_selected));
                tvInteraction.setBackground(getResources().getDrawable(R.drawable.bg_title_interaction_normal));
            }
        } else {
            if (llControl.getVisibility() == View.VISIBLE) {
                llControl.setVisibility(View.GONE);
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
        presenter.addFavorite(red, green, blue, warm, clod, brightness);
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
}
