package com.ws.mesh.incores2.view.fragment.timing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.AppConstant;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.utils.SPUtils;
import com.ws.mesh.incores2.view.activity.StageThreeActivity;
import com.ws.mesh.incores2.view.adapter.TimingAdapter;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.base.BaseFragment;
import com.ws.mesh.incores2.view.control.CustomTimePicker;
import com.ws.mesh.incores2.view.impl.ITimingView;
import com.ws.mesh.incores2.view.presenter.TimingPresenter;

import butterknife.BindView;
import butterknife.OnClick;

//设备/房间定时列表
public class TimingFragment extends BaseContentFragment<ITimingView, TimingPresenter> implements ITimingView {

    @BindView(R.id.tv_sunrise_time)
    TextView tvRiseTime;
    @BindView(R.id.tv_sunset_time)
    TextView tvSetTime;
    @BindView(R.id.tv_sunrise_durtime)
    TextView tvRiseDurtime;
    @BindView(R.id.tv_sunset_durtime)
    TextView tvSetDurtime;

    @BindView(R.id.iv_sunrise_switch)
    ImageView ivRiseSwitch;
    @BindView(R.id.iv_sunset_switch)
    ImageView ivSetSwitch;

    @BindView(R.id.rl_timing_list)
    RecyclerView rlTimingList;
    @BindView(R.id.rl_main_frame)
    RelativeLayout rlMainFrame;
    @BindView(R.id.iv_add_timing)
    ImageView ivAddTiming;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.view_space)
    View viewSpace;
    @BindView(R.id.iv_edit)
    ImageView ivEditTiming;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String titleName;
    private TimingAdapter timingAdapter;
    private int meshAddress;
    private boolean isEditMode;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing;
    }

    @Override
    protected void initData() {
        if (getActivity() != null) {
            meshAddress = getActivity().getIntent()
                    .getIntExtra(IntentConstant.MESH_ADDRESS, -1);
            if (meshAddress == -1) {
                toast("meshAddress 传值 -1");
                return;
            }
        }

        presenter.init(meshAddress);
        //房间 设备 分别标题显示 Zone Schedule / Device Schedule
        titleName = getString(
                presenter.isRoom() ? R.string.zone_schedule : R.string.device_schedule);

        rlTimingList.setLayoutManager(new LinearLayoutManager(getActivity()));

        tvRiseTime.setText(presenter.getSunriseTime());
        tvRiseDurtime.setText(presenter.getSunriseDurtime());
        tvSetTime.setText(presenter.getSunsetTime());
        tvSetDurtime.setText(presenter.getSunsetDurtime());

        ivRiseSwitch.setImageResource(presenter.getSunriseSwitchRes());
        ivSetSwitch.setImageResource(presenter.getSunsetSwitchRes());

        ivSetSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShareMesh()) {
                    return;
                }
                presenter.switchSunset();
            }
        });

        ivRiseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShareMesh()) {
                    return;
                }
                presenter.switchSunrise();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        timingAdapter = new TimingAdapter(presenter.getAlarmList());
        tvTitle.setText(String.format(getString(R.string.title_format),
                titleName, presenter.getAlarmListSize()));
        rlTimingList.setAdapter(timingAdapter);

        timingAdapter.setOnTimingActionListener(new TimingAdapter.OnTimingActionListener() {
            @Override
            public void onAction(TimingAdapter.Action action, int alarmId) {
                if (isShareMesh()) {
                    return;
                }
                switch (action) {
                    case SWITCH:
                        presenter.switchTiming(alarmId);
                        break;
                    case EDIT:
                        pushStageActivity(PageId.EDIT_TIMING, meshAddress, alarmId);
                        break;
                    case DELETE:
                        presenter.deleteAlarm(alarmId);
                        break;
                    default:
                }
            }
        });

        switchEditMode(isEditMode);
    }

    @OnClick(R.id.iv_add_timing)
    public void addTiming() {
        if (isShareMesh()) {
            return;
        }
        //跳转添加定时界面
        pushStageActivity(PageId.ADD_TIMING, meshAddress);
    }

    @OnClick(R.id.iv_edit)
    public void editTiming() {
        if (isShareMesh()) {
            return;
        }
        isEditMode = true;
        switchEditMode(true);
    }

    @OnClick(R.id.tv_finish)
    public void onFinish() {
        isEditMode = false;
        switchEditMode(false);
    }

    //切换编辑模式
    private void switchEditMode(boolean isEditMode) {
        timingAdapter.setEditMode(isEditMode);
        rlMainFrame.setBackgroundColor(
                getResources().getColor(isEditMode ? R.color.black_333 : R.color.app_bg));
        ivEditTiming.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
        ivAddTiming.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
        viewSpace.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        tvFinish.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        if (getActivity() != null)
            tvTitle.setTextColor(isEditMode ?
                    ContextCompat.getColor(getActivity(), R.color.white)
                    : ContextCompat.getColor(getActivity(), R.color.black_333));
    }

    @Override
    protected TimingPresenter createPresent() {
        return new TimingPresenter();
    }

    @Override
    public void deleteAlarm(boolean success) {
        if (success) {
            onResume();
        } else {
            toast(R.string.delete_fail);
        }
    }

    @Override
    public void switchCircadian() {
        ivRiseSwitch.setImageResource(presenter.getSunriseSwitchRes());
        ivSetSwitch.setImageResource(presenter.getSunsetSwitchRes());
    }

    @Override
    public void openAlarm(boolean success) {
        if (success)
            timingAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeAlarm(boolean success) {
        if (success)
            timingAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshCircadian() {
        tvRiseTime.setText(presenter.getSunriseTime());
        tvRiseDurtime.setText(presenter.getSunriseDurtime());
        tvSetTime.setText(presenter.getSunsetTime());
        tvSetDurtime.setText(presenter.getSunsetDurtime());
    }

    @OnClick({R.id.ll_sunrise_time, R.id.ll_sunset_time})
    public void onSetTime(View view) {
        if (isShareMesh()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_sunrise_time:
                popSetTimeDialog(true,
                        presenter.getDayHour(),
                        presenter.getDayMin(),
                        presenter.getDayDurTime());
                break;
            case R.id.ll_sunset_time:
                popSetTimeDialog(false,
                        presenter.getNightHour(),
                        presenter.getNightMin(),
                        presenter.getNightDurTime());
                break;
        }
    }

    private int hour;
    private int min;
    private int durtime;

    //设置昼夜节律时间
    private void popSetTimeDialog(final boolean isRise, int hour_, int min_, final int durtime_) {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        Window window = dialog.getWindow();

        dialog.show();
        if (window != null) {
            window.setContentView(R.layout.dialog_set_rise_set_time);
            CustomTimePicker timePicker = window.findViewById(R.id.ctp_time);
            TextView tvConfirm = window.findViewById(R.id.tv_confirm);
            TextView tvCancel = window.findViewById(R.id.tv_cancel);
            timePicker.setCurrentHour(hour_);
            timePicker.setCurrentMinute(min_);

            hour = timePicker.getCurrentHour();

            min = timePicker.getCurrentMinute();
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    hour = hourOfDay;
                    min = minute;
                }
            });
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //选择持续时间
                    popSetDurTimeDialog(isRise, hour, min, durtime_);
                    dialog.dismiss();
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    //设置昼夜节律时间
    private void popSetDurTimeDialog(final boolean isRise, final int hour, final int min, int durtime_) {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        Window window = dialog.getWindow();

        dialog.show();
        if (window != null) {
            window.setContentView(R.layout.dialog_set_durtime);
            NumberPicker numberPicker = window.findViewById(R.id.np_durtime);
            TextView tvConfirm = window.findViewById(R.id.tv_confirm);
            TextView tvCancel = window.findViewById(R.id.tv_cancel);
            durtime = durtime_;
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(30);
            numberPicker.setValue(durtime);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    durtime = newVal;
                }
            });
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.updateCircadian(isRise, hour, min, durtime);
                    dialog.dismiss();
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}
