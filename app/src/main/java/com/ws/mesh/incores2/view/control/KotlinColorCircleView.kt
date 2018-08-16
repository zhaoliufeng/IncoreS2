package com.ws.mesh.incores2.view.control

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

class KotlinColorCircleView : View {

    var paint: Paint? = null
    var strokePaint: Paint? = null
    var bgColor: Int = Color.WHITE

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val bg: Drawable = background
        if (bg is ColorDrawable){
            bgColor = bg.color
        }

        initView()
        background = null
    }

    // x? ==  'if(x != null) return x else return null'
    // x!! == 'assert(x != null)'
    private fun initView() {
        paint = Paint()
        with(paint!!) {
            isAntiAlias = true
            color = bgColor
        }

        strokePaint = Paint()
        with(strokePaint!!) {
            isAntiAlias = true
            strokeWidth = 2f
            color = Color.GRAY
            style = Paint.Style.STROKE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawCircle(width/2f, height/2f, width/2f, paint)
        val color = paint?.color
        val colorVal = color!!.and(0xFFFFFF)
        if (colorVal == 0xFFFFFF){
            canvas.drawCircle(width/2f, height/2f, width/2f -1, strokePaint)
        }
    }

    override fun setBackgroundColor(color: Int) {
        paint?.color = Color.parseColor(String.format("#%06x", color.and(0xFFFFFF)))
        invalidate()
    }
}