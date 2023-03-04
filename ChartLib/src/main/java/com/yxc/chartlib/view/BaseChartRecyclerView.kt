package com.yxc.chartlib.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.BaseChartAttrs
import com.yxc.chartlib.barchart.itemdecoration.BaseChartItemDecoration

/**
 * @author yxc
 * @since  2019-05-10
 */
abstract class BaseChartRecyclerView<T : BaseChartAttrs, I : BaseChartItemDecoration<*, *>>(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    private lateinit var onChartTouchListener: OnChartTouchListener
    @JvmField
    var mAttrs: T
    var mItemDecoration: I? = null
    override fun addItemDecoration(decor: ItemDecoration) {
        super.addItemDecoration(decor)
        if (decor is BaseChartItemDecoration<*, *>) {
            mItemDecoration = decor as I
        }
    }

    init {
        mAttrs = getAttrs(context, attrs)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (e.actionMasked == MotionEvent.ACTION_DOWN) {
            onChartTouchListener.onChartGestureStart(e)
        } else if (e.action == MotionEvent.ACTION_UP
            || e.action == MotionEvent.ACTION_CANCEL
        ) {
            onChartTouchListener.onChartGestureEnd(e)
        } else if (e.actionMasked == MotionEvent.ACTION_MOVE) {
            onChartTouchListener.onChartGestureMovingOn(e)
        }
        return super.onTouchEvent(e)
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        val velocityXF = velocityX * mAttrs.ratioVelocity
        val velocityYF = velocityY * mAttrs.ratioVelocity
        return super.fling(velocityXF.toInt(), velocityYF.toInt())
    }

    fun addOnChartTouchListener(onChartTouchListener: OnChartTouchListener) {
        this.onChartTouchListener = onChartTouchListener
    }

    interface OnChartTouchListener {
        fun onChartGestureStart(e: MotionEvent)
        fun onChartGestureEnd(e: MotionEvent)
        fun onChartGestureMovingOn(e: MotionEvent)
    }

    protected abstract fun getAttrs(context: Context, attrs: AttributeSet): T
}