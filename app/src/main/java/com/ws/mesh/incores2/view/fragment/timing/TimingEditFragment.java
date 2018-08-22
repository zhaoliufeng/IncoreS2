package com.ws.mesh.incores2.view.fragment.timing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.control.CustomTimePicker;
import com.ws.mesh.incores2.view.impl.ITimingEditView;
import com.ws.mesh.incores2.view.presenter.TimingEditPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//编辑定时
public class TimingEditFragment extends BaseContentFragment<ITimingEditView, TimingEditPresenter> implements ITimingEditView {

    private static final String TAG = "TimingEditFragment";
    @BindView(R.id.tv_event)
    TextView tvEvent;
    @BindView(R.id.img_everyday)
    ImageView imgEveryDay;
    @BindView(R.id.img_workday)
    ImageView imgWorkday;
    @BindView(R.id.img_custom)
    ImageView imgCustom;
    @BindView(R.id.ctp_time)
    CustomTimePicker ctpTime;
    String[] events;
    private int weekNum;
    private byte weekBytes[];
    private int alarmId;
    private int eventId;
    private int meshAddress;
    //判断是编辑定时还是添加定时
    private boolean isEdit = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing_edit;
    }

    @Override
    protected void initData() {
        events = getResources().getStringArray(R.array.timing_events);
        if (getActivity() != null) {
            alarmId = getActivity().getIntent().getIntExtra(IntentConstant.ALARM_ID, -1);
            meshAddress = getActivity().getIntent().getIntExtra(IntentConstant.MESH_ADDRESS, -1);
            if (meshAddress == -1) {
                toast("meshAddress 传值 -1");
                return;
            }
        }

        presenter.init(meshAddress);

        if (alarmId != -1) {
            isEdit = true;
            Timing timing = presenter.mAlarmSparseArray.get(alarmId);
            weekNum = timing.mWeekNum;
            eventId = timing.mAlarmEvent;
            ctpTime.setCurrentHour(timing.mHours);
            ctpTime.setCurrentMinute(timing.mMins);
            tvEvent.setText(presenter.getExecuteEvent(timing.mAlarmEvent));
        } else {
            isEdit = false;
            //创建新的timing
            if (presenter.getAlarmId() == -1) {
                toast(R.string.cannot_add_any_timing);
                getActivity().finish();
                return;
            }
            alarmId = presenter.getAlarmId();
            weekNum = 127;
        }

        switch (weekNum) {
            case 127:
                imgEveryDay.setImageResource(R.drawable.radio_item_selected);
                break;
            case 62:
                imgWorkday.setImageResource(R.drawable.radio_item_selected);
                break;
            default:
                imgCustom.setImageResource(R.drawable.radio_item_selected);
        }
    }

    @OnClick(R.id.rl_events)
    public void onEvents() {
        //跳转到定时选择动作界面
        pushStageActivityForResult(PageId.ADD_TIMING_EVENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentConstant.REQUEST_CODE &&
                resultCode == Activity.RESULT_OK) {
            eventId = data.getIntExtra(IntentConstant.RESULT, -1);
            if (eventId != -1) {
                tvEvent.setText(events[eventId]);
                Log.i(TAG, "onActivityResult: 选择的执行动作 --> " + events[eventId] + " eventId --> " + eventId);
            }
        }
    }

    @OnClick(R.id.tv_finish)
    public void onFinish() {
        //添加定时
        int hour = ctpTime.getCurrentHour();
        int min = ctpTime.getCurrentMinute();
        if (isEdit) {
            presenter.updateAlarm(hour, min, eventId, weekNum, meshAddress, alarmId);
        } else {
            presenter.addAlarm(hour, min, eventId, weekNum, meshAddress, alarmId);
        }
    }

    @OnClick({R.id.rl_everyday, R.id.rl_workday, R.id.rl_custom})
    public void onRepeat(View view) {
        resetRadioImg();
        switch (view.getId()) {
            case R.id.rl_everyday:
                imgEveryDay.setImageResource(R.drawable.radio_item_selected);
                weekNum = 127;
                break;
            case R.id.rl_workday:
                imgWorkday.setImageResource(R.drawable.radio_item_selected);
                weekNum = 62;
                break;
            case R.id.rl_custom:
                imgCustom.setImageResource(R.drawable.radio_item_selected);
                showCustomWeekDialog();
                break;
        }
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
                    if (weekNum == 0){
                        toast(R.string.pls_choose_execute_date);
                        return;
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    private void resetRadioImg() {
        imgEveryDay.setImageResource(R.drawable.radio_item);
        imgWorkday.setImageResource(R.drawable.radio_item);
        imgCustom.setImageResource(R.drawable.radio_item);
    }

    @Override
    protected TimingEditPresenter createPresent() {
        return new TimingEditPresenter(getActivity());
    }

    @Override
    public void deleteAlarm(boolean success) {

    }

    @Override
    public void addAlarm(boolean success) {
        if (success) {
            if (getActivity() != null)
                getActivity().finish();
        } else {
            toast(R.string.add_failed);
        }
    }

    @Override
    public void updateAlarm(boolean success) {
        if (success) {
            if (getActivity() != null)
                getActivity().finish();
        } else {
            toast(R.string.add_failed);
        }
    }

    @Override
    public void maximumNumber() {
        toast(R.string.cannot_add_any_timing);
    }

    class AlarmCustomWeekAdapter extends RecyclerView.Adapter<AlarmEventViewHolder> {

        private String[] weekList;

        AlarmCustomWeekAdapter() {
            weekList = getResources().getStringArray(R.array.custom_week_data);
        }

        @NonNull
        @Override
        public AlarmEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_timing_week, parent, false);
            return new AlarmEventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlarmEventViewHolder holder, int position) {
            final int viewHolderPosition = holder.getAdapterPosition();
            holder.mAlarmEventName.setText(weekList[position]);
            if (weekBytes[position] == 1) {
                holder.mAlarmEventSelectedStatus.setImageResource(R.drawable.radio_item_selected);
            } else {
                holder.mAlarmEventSelectedStatus.setImageResource(R.drawable.radio_item);
            }
            holder.mAlarmEventSelectedStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weekBytes[viewHolderPosition] = (weekBytes[viewHolderPosition] == 1 ? (byte) 0 : (byte) 1);
                    notifyDataSetChanged();
                }
            });

            holder.mWeekFrame.setOnClickListener(new View.OnClickListener() {
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
}
