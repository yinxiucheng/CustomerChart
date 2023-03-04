package com.yxc.chartlib.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.BaseChartAttrs
import com.yxc.chartlib.barchart.itemdecoration.BaseChartItemDecoration
import kotlin.math.max

/**
 * @author yxc
 * @since  2019-05-10
 */
abstract class BaseChartRecyclerView<T : BaseChartAttrs, I : BaseChartItemDecoration<*, *>>(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {
    protected lateinit var onChartTouchListener: OnChartTouchListener
    @JvmField
    var mAttrs: T
    var mItemDecoration: I? = null
    var displayNumber = 0


    override fun addItemDecoration(decor: ItemDecoration) {
        super.addItemDecoration(decor)
        if (decor is BaseChartItemDecoration<*, *>) {
            mItemDecoration = decor as I
        }
    }

    init {
        mAttrs = getAttrs(context, attrs)
        displayNumber = mAttrs.displayNumbers
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                onChartTouchListener.onChartGestureStart(event)
            }
            MotionEvent.ACTION_MOVE -> {
                onChartTouchListener.onChartGestureMovingOn(event)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                onChartTouchListener.onChartGestureEnd(event)
            }
        }
        return super.onTouchEvent(event)
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

    companion object{
        const val TAG = "BaseChartRecyclerView"
    }
}