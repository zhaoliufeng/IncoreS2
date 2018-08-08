package com.ws.mesh.incores2.view.fragment

import android.widget.TextView
import com.ws.mesh.incores2.R
import com.ws.mesh.incores2.view.base.BaseFragment

//空界面 如果出现fragment 跳转时出现pageId丢失或者误传的情况 就会展示这个界面
class NullFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_null
    }

    override fun initData() {
        activity?.findViewById<TextView>(R.id.tv_title)?.setText(R.string.null_fragment)
    }

}