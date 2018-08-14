package com.ws.mesh.incores2.view.fragment.scene;

import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.control.CustomTimePicker;
import com.ws.mesh.incores2.view.impl.ISceneAddTimingView;
import com.ws.mesh.incores2.view.presenter.SceneAddTimingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//场景添加定时
public class SceneAddTimingFragment extends BaseContentFragment<ISceneAddTimingView, SceneAddTimingPresenter> implements ISceneAddTimingView {

    @BindView(R.id.ctp_time)
    CustomTimePicker ctpTime;
    @BindView(R.id.iv_everyday)
    ImageView ivEveryDay;
    @BindView(R.id.iv_workday)
    ImageView ivWorkday;
    @BindView(R.id.iv_custom)
    ImageView ivCustom;
    @BindView(R.id.iv_not_schedule)
    ImageView ivNotSchedule;

    //默认循环模式为everyday
    private int weekNum = 127;
    private int sceneId;
    private int alarmId;
    private byte weekBytes[];

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scene_add_timing;
    }

    @Override
    protected void initData() {
        if (getActivity() != null) {
            sceneId = getActivity().getIntent().getIntExtra(IntentConstant.MESH_ADDRESS, -1);
            alarmId = getActivity().getIntent().getIntExtra(IntentConstant.ALARM_ID, -1);
        }
        presenter.init(sceneId, alarmId);
    }

    @OnClick({R.id.rl_everyday, R.id.rl_workday, R.id.rl_custom, R.id.rl_not_schedule})
    public void onClick(View view) {
        resetRadioImg();
        switch (view.getId()) {
            case R.id.rl_everyday:
                ivEveryDay.setImageResource(R.drawable.radio_item_selected);
                weekNum = 127;
                break;
            case R.id.rl_workday:
                ivWorkday.setImageResource(R.drawable.radio_item_selected);
                weekNum = 62;
                break;
            case R.id.rl_custom:
                ivCustom.setImageResource(R.drawable.radio_item_selected);
                showCustomWeekDialog();
                break;
            case R.id.rl_not_schedule:
                ivNotSchedule.setImageResource(R.drawable.radio_item_selected);
                weekNum = -1;
                break;
        }
    }

    private void resetRadioImg() {
        ivEveryDay.setImageResource(R.drawable.radio_item);
        ivWorkday.setImageResource(R.drawable.radio_item);
        ivCustom.setImageResource(R.drawable.radio_item);
        ivNotSchedule.setImageResource(R.drawable.radio_item);
    }

    private void showCustomWeekDialog() {
        weekBytes = ViewUtils.reverseBytes(ViewUtils.weekNumToBinaryByteArray(weekNum));
        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog).create();
        dialog.show();
        if (dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_custom_week);
            RecyclerView rlContent = window.findViewById(R.id.rl_content);
            Button btnCancel = window.findViewById(R.id.btn_cancel);
            Button btnConfirm = window.findViewById(R.id.btn_confirm);

            rlContent.setLayoutManager(new LinearLayoutManager(getActivity()));
            AlarmCustomWeekAdapter adapter = new AlarmCustomWeekAdapter();
            rlContent.setAdapter(adapter);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weekNum = ViewUtils.byteArrayToWeekNum(ViewUtils.reverseBytes(weekBytes));
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    protected SceneAddTimingPresenter createPresent() {
        return new SceneAddTimingPresenter();
    }

    @Override
    public void setTimingInfo(Timing timing) {
        ctpTime.setCurrentHour(timing.mHours);
        ctpTime.setCurrentMinute(timing.mMins);
        weekNum = timing.mWeekNum;

        switch (weekNum) {
            case 127:
                ivEveryDay.setImageResource(R.drawable.radio_item_selected);
                break;
            case 62:
                ivWorkday.setImageResource(R.drawable.radio_item_selected);
                break;
            default:
                ivCustom.setImageResource(R.drawable.radio_item_selected);
        }
    }

    class AlarmCustomWeekAdapter extends RecyclerView.Adapter {

        private String[] weekList;

        AlarmCustomWeekAdapter() {
            weekList = getResources().getStringArray(R.array.custom_week_data);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_timing_week, parent, false);
            return new AlarmEventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            AlarmEventViewHolder viewHolder = (AlarmEventViewHolder) holder;
            final int viewHolderPosition = viewHolder.getAdapterPosition();
            viewHolder.mAlarmEventName.setText(weekList[position]);
            if (weekBytes[position] == 1) {
                viewHolder.mAlarmEventSelectedStatus.setImageResource(R.drawable.radio_item_selected);
            } else {
                viewHolder.mAlarmEventSelectedStatus.setImageResource(R.drawable.radio_item);
            }
            viewHolder.mAlarmEventSelectedStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weekBytes[viewHolderPosition] = (weekBytes[viewHolderPosition] == 1 ? (byte) 0 : (byte) 1);
                    notifyDataSetChanged();
                }
            });

            viewHolder.mWeekFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weekBytes[viewHolderPosition] = (weekBytes[viewHolderPosition] == 1 ? (byte) 0 : (byte) 1);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (weekList == null) return 0;
            return weekList.length;
        }
    }

    class AlarmEventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_timing_event_name)
        TextView mAlarmEventName;
        @BindView(R.id.img_timing_event_selected_status)
        ImageView mAlarmEventSelectedStatus;
        @BindView(R.id.rl_week_frame)
        RelativeLayout mWeekFrame;

        AlarmEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @OnClick(R.id.tv_save)
    public void onSave() {
        //保存场景定时
        presenter.onSaveSchedule(ctpTime, weekNum);
    }

    @Override
    public void saveSchedule(boolean success) {
        if (success) {
            if (getActivity() != null)
                getActivity().finish();
        } else {
            toast(R.string.save_failed);
        }
    }
}
