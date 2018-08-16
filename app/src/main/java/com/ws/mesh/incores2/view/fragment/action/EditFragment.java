package com.ws.mesh.incores2.view.fragment.action;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws.mesh.incores2.R;
import com.ws.mesh.incores2.constant.IntentConstant;
import com.ws.mesh.incores2.view.base.BaseContentFragment;
import com.ws.mesh.incores2.view.impl.IEditView;
import com.ws.mesh.incores2.view.presenter.EditPresenter;

import butterknife.BindView;
import butterknife.OnClick;

//编辑设备/房间
public class EditFragment extends BaseContentFragment<IEditView, EditPresenter> implements IEditView {
    private static final String TAG = "EditFragment";

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.iv_edit_name)
    ImageView ivEditName;
    @BindView(R.id.tv_enter)
    TextView tvEnter;
    @BindView(R.id.tv_edit_title)
    TextView tvTitle;
    @BindView(R.id.tv_remove)
    TextView tvRemove;

    private int meshAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit;
    }

    @Override
    protected void initData() {
        if (getActivity() != null) {
            meshAddress = getActivity().getIntent()
                    .getIntExtra(IntentConstant.MESH_ADDRESS, -1);
        }
        presenter.init(meshAddress);

        edtName.setText(presenter.getName());
        tvTitle.setText(presenter.getTitle());
        tvRemove.setText(presenter.getRemoveContent());
    }

    @OnClick(R.id.iv_edit_name)
    public void editName() {
        ivEditName.setVisibility(View.GONE);
        tvEnter.setVisibility(View.VISIBLE);
        edtName.setEnabled(true);
        edtName.setSelection(edtName.getText().length());
    }

    @OnClick(R.id.tv_enter)
    public void enter() {
        String newName = edtName.getText().toString().trim();
        Log.i(TAG, "enter: " + newName);
        presenter.editName(newName);
    }

    @OnClick(R.id.tv_remove)
    public void remove() {
        presenter.remove();
    }

    @Override
    protected EditPresenter createPresent() {
        return new EditPresenter();
    }

    @Override
    public void editName(boolean success) {
        if (success) {
            toast(R.string.save_success);
            ivEditName.setVisibility(View.VISIBLE);
            tvEnter.setVisibility(View.GONE);
            edtName.setEnabled(false);
        } else {
            toast(R.string.save_failed);
        }
    }

    @Override
    public void remove(boolean success) {
        if (success) {
            if (getActivity() != null)
                backToMainActivity();
        } else {
            toast(R.string.remove_failed);
        }
    }
}
