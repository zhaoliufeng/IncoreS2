package com.ws.mesh.incores2.view.fragment.scene;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.bean.Timing;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.constant.PageId;
import com.ws.mesh.incores2.utils.ViewUtils;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.ISceneAddView;
import com.ws.mesh.incores2.view.presenter.SceneAddPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class SceneAddFragment extends BaseContentFragment<ISceneAddView, SceneAddPresenter> implements ISceneAddView {

    private static final String TAG = "SceneAddFragment";
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.iv_edit_name)
    ImageView ivEditName;
    @BindView(R.id.tv_enter)
    TextView tvEnter;
    @BindView(R.id.ll_time_info)
    LinearLayout llTimeInfo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_week_num)
    TextView tvWeekNum;
    @BindView(R.id.tv_no_schedule)
    TextView tvNoSchedule;

    private int sceneId;
    private int alarmId = -1;

    @Override
    protected SceneAddPresenter createPresent() {
        return new SceneAddPresenter(getActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scene_add;
    }

    @Override
    protected void initData() {
        if (getActivity() != null) {
            sceneId = getActivity().getIntent()
                    .getIntExtra(IntentConstant.MESH_ADDRESS, -1);
        }
        presenter.init(sceneId);

        edtName.setText(presenter.getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getSchedule();
    }

    @Override
    public void editName(boolean success) {
        if (success) {
            edtName.setEnabled(false);
            ivEditName.setVisibility(View.VISIBLE);
            tvEnter.setVisibility(View.GONE);
        } else {
            toast(R.string.save_failed);
        }
    }

    @Override
    public void getSceneSchedule(Timing timing) {
        if (timing != null) {
            llTimeInfo.setVisibility(View.VISIBLE);
            tvNoSchedule.setVisibility(View.GONE);
            String[] time = ViewUtils.getAlarmShow(timing.mHours, timing.mMins);
            String showTime = String.format("%s %s", time[0], time[1]);
            tvTime.setText(showTime);
            tvWeekNum.setText(presenter.getExecuteInfo(timing.mWeekNum));
            alarmId = timing.mAId;
        } else {
            llTimeInfo.setVisibility(View.GONE);
            tvNoSchedule.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.iv_edit_name, R.id.tv_enter, R.id.rl_set_schedule, R.id.rl_select_device})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_name:
                ivEditName.setVisibility(View.GONE);
                tvEnter.setVisibility(View.VISIBLE);
                edtName.setEnabled(true);
                edtName.setSelection(edtName.getText().length());
                break;
            case R.id.tv_enter:
                String newName = edtName.getText().toString();
                Log.i(TAG, "newName " + newName);
                presenter.editName(newName);
                break;
            case R.id.rl_set_schedule:
                pushStageActivity(PageId.ADD_SCENE_TIMING, sceneId, alarmId);
                break;
            case R.id.rl_select_device:
                pushStageActivity(PageId.ADD_SCENE_DEVICE, sceneId);
                break;
        }
    }
}
