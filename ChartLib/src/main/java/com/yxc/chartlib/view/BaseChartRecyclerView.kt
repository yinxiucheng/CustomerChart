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

/**
 * @author yxc
 * @since  2019-05-10
 */
abstract class BaseChartRecyclerView<T : BaseChartAttrs, I : BaseChartItemDecoration<*, *>>(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    private lateinit var onChartTouchListener: OnChartTouchListener
    private var onRestDisplayNumberListener: OnResetDisplayNumberListener? = null
    private val scaleGestureListener = MyScaleGestureListener()
    private val scaleGestureDetector:ScaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)

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

    var downX = 0f
    var originalOffestX = 0f
    var offsetX = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointCount = event.pointerCount
        var sum = 0f
        var focusX = 0f
        for (i in 0 until pointCount){
            sum += event.getX(i)
        }
        focusX = sum / pointCount

        val result = scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    onChartTouchListener.onChartGestureStart(event)
                    downX = focusX
                    originalOffestX = offsetX
                }
                MotionEvent.ACTION_MOVE -> {
                    onChartTouchListener.onChartGestureMovingOn(event)
                    offsetX = focusX - downX + originalOffestX
                    if (event.pointerCount > 1 && (offsetX < 3)) { //todo ,原本这里想禁掉多指协同滑动。
                        return false
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    onChartTouchListener.onChartGestureEnd(event)
                }

                MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP ->{
                    downX = focusX
                    originalOffestX = offsetX
                }
            }
//            Log.d(TAG, "scaleGestureDetector is not in Progress")
            return super.onTouchEvent(event)
        } else {
            Log.d(TAG, "scaleGestureDetector is in Progress")
            return result
        }
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        val velocityXF = velocityX * mAttrs.ratioVelocity
        val velocityYF = velocityY * mAttrs.ratioVelocity
        return super.fling(velocityXF.toInt(), velocityYF.toInt())
    }

    fun addOnChartTouchListener(onChartTouchListener: OnChartTouchListener) {
        this.onChartTouchListener = onChartTouchListener
    }

    fun addOnDisplayNumberListener(resetDisplayNumberListener: OnResetDisplayNumberListener){
        this.onRestDisplayNumberListener = resetDisplayNumberListener
    }

    inner class MyScaleGestureListener: SimpleOnScaleGestureListener(){
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            Log.i(TAG, "scale = " + detector.scaleFactor)   // 缩放因子
            Log.i(TAG, "currentSpanX = " +  detector.currentSpanX)
            if (detector.currentSpanX > 4) {
                if (detector.scaleFactor >= 1) {//放大
                    displayNumber -= (displayNumber * (detector.scaleFactor - 1)).toInt()
                } else {
                    displayNumber += (displayNumber * (1 - detector.scaleFactor)).toInt()
                }
                mAttrs.displayNumbers = displayNumber
                Log.i(TAG, "displayNumber = ${mAttrs.displayNumbers}")
                onRestDisplayNumberListener?.resetDisplayNumber(displayNumber)
                return true
            } else {
                return false
            }
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            Log.i(TAG, "onScaleBegin = " +  detector.currentSpanX)
            return true
        }
        override fun onScaleEnd(p0: ScaleGestureDetector) {

        }
    }

    interface OnChartTouchListener {
        fun onChartGestureStart(e: MotionEvent)
        fun onChartGestureEnd(e: MotionEvent)
        fun onChartGestureMovingOn(e: MotionEvent)
    }

    interface OnResetDisplayNumberListener{
        fun resetDisplayNumber(displayNumber: Int)
    }

    protected abstract fun getAttrs(context: Context, attrs: AttributeSet): T


    companion object{
        const val TAG = "BaseChartRecyclerView"
    }
}