package com.yxc.fitness.chart.render

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.BaseChartAttrs
import com.yxc.chartlib.utils.AppUtil
import com.yxc.mylibrary.TimeDateUtil

class SleepDayXAxisRender<V : BaseChartAttrs?>(private val mBarChartAttrs: V) {

    private var mTextPaint: Paint? = null
    private var sleepTime: Long? = null//s
    private var wakeUpTime: Long? = null//s

    fun setTime(sleep: Long, wakeUp: Long) {
        sleepTime = sleep
        wakeUpTime = wakeUp
    }

    private fun initTextPaint() {
        mTextPaint = Paint()
        mTextPaint!!.reset()
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.style = Paint.Style.FILL
        mTextPaint!!.color = mBarChartAttrs!!.xAxisTxtColor
        mTextPaint!!.textSize = mBarChartAttrs!!.xAxisTxtSize
    }

    fun drawXAxisLabel(
        parent: RecyclerView,
        canvas: Canvas
    ) {
        val parentEnd = parent.width - parent.paddingEnd.toFloat()
        val parentStart = parent.paddingStart.toFloat()
        //xAxis
        var bottom = parent.height - parent.paddingBottom - mBarChartAttrs!!.contentPaddingBottom
        var textBottomBaseLineY = bottom + 64
        if (!AppUtil.isRTLDirection()) {
            //ltr default
            if (sleepTime != null) {
                val leftStr = TimeDateUtil.getDateStr(sleepTime!!, "HH:mm")
                canvas.drawText(leftStr, parentStart, textBottomBaseLineY, mTextPaint!!)
            }
            if (wakeUpTime != null) {
                val rightStr = TimeDateUtil.getDateStr(wakeUpTime!!, "HH:mm")
                canvas.drawText(
                    rightStr, parentEnd - mTextPaint!!.measureText(rightStr),
                    textBottomBaseLineY, mTextPaint!!
                )
            }
        } else {
            if (wakeUpTime != null) {
                val leftStr = TimeDateUtil.getDateStr(wakeUpTime!!, "HH:mm")
                canvas.drawText(leftStr, parentStart, textBottomBaseLineY, mTextPaint!!)
            }
            if (sleepTime != null) {
                val rightStr = TimeDateUtil.getDateStr(sleepTime!!, "HH:mm")
                canvas.drawText(
                    rightStr, parentEnd - mTextPaint!!.measureText(rightStr),
                    textBottomBaseLineY, mTextPaint!!
                )
            }
        }
    }

    init {
        initTextPaint()
    }
}