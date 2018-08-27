package com.ws.mesh.incores2.view.fragment.action

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.ws.mesh.incores2.R
import com.ws.mesh.incores2.constant.IntentConstant
import com.ws.mesh.incores2.view.base.BaseContentFragment
import com.ws.mesh.incores2.view.impl.IEditView
import com.ws.mesh.incores2.view.presenter.EditPresenter
import kotlinx.android.synthetic.main.fragment_edit.*

//编辑设备 房间 kotlin实现 可以用EditFragment代替
open class KotlinEditFragment : BaseContentFragment<IEditView, EditPresenter>(), IEditView, View.OnClickListener {

    companion object {
        private val TAG = KotlinEditFragment::class.java.simpleName
        var meshAddress: Int = -1
    }

    override fun createPresent(): EditPresenter {
        return EditPresenter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_edit
    }

    override fun initData() {
        meshAddress = activity?.intent?.extras?.getInt(IntentConstant.MESH_ADDRESS, -1)!!
        presenter.init(meshAddress)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //在完成onCreateView方法前 直接通过控件的id进行操作会报空异常
        edt_name?.setText(presenter.name)
        tv_edit_title?.setText(presenter.title)
        tv_remove?.setText(presenter.removeContent)

        iv_edit_name.setOnClickListener(this)
        tv_enter.setOnClickListener(this)
        tv_remove.setOnClickListener(this)
        super.onActivityCreated(savedInstanceState)
    }


    /*******function OnClick*******/

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_edit_name -> {
                editName()
            }
            R.id.tv_enter -> {
                enter()
            }
            R.id.tv_remove -> {
                remove()
            }
        }
    }

    private fun editName() {
        if (isShareMesh) {
            toast(R.string.no_permission)
            return
        }
        //设置输入框可用 设置输入框光标位置
        edt_name.isEnabled = true
        edt_name.setSelection(edt_name.text.length)
        tv_enter.visibility = View.VISIBLE
        iv_edit_name.visibility = View.GONE
    }

    private fun enter() {
        val newName = edt_name.text.toString().trim()

        Log.println(Log.INFO, TAG, "name enter : $newName")
        presenter.editName(newName)
    }

    private fun remove() {
        if (isShareMesh) {
            toast(R.string.no_permission)
            return
        }
        popDeleteRemindDialog()
    }

    /*******IEditView Interface********/

    override fun editName(success: Boolean) {
        if (success) {
            toast(R.string.save_success)
            //设置输入框不可编辑
            edt_name.isEnabled = false
            iv_edit_name.visibility = View.VISIBLE
            tv_enter.visibility = View.GONE
        } else
            toast(R.string.save_failed)
    }

    override fun remove(success: Boolean) {
        if (success)
            backToMainActivity()
        else
            toast(R.string.remove_failed)
    }

    //删除弹窗提示
    private fun popDeleteRemindDialog() {
        val dialog = AlertDialog.Builder(activity, R.style.CustomDialog).create()
        dialog.show()
        val window = dialog.window

        window.setContentView(
                if (presenter.isRoom)
                    R.layout.dialog_custom_delete_group
                else
                    R.layout.dialog_custom_delete_device
        )
        val btnConfirm = window.findViewById<Button>(R.id.btn_confirm)
        val btnCancel = window.findViewById<Button>(R.id.btn_cancel)

        btnConfirm.setOnClickListener {
            presenter.remove()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}