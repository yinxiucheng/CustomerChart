package com.yxc.chartlib.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.yxc.chartlib.attrs.ChartAttrsUtil
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.barchart.itemdecoration.StockChartItemDecoration

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class StockChartRecyclerView : BaseChartRecyclerView<StockChartAttrs, StockChartItemDecoration> {

    constructor(context: Context, attrs:AttributeSet):super(context, attrs)

    override fun getAttrs(context: Context, attrs: AttributeSet): StockChartAttrs {
       return ChartAttrsUtil.getStockChartAttrs(context, attrs)
    }

    private val scaleGestureListener = MyScaleGestureListener()
    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)

    var minDisplayNumber = 30
    var maxDisplayNumber = 400

    private var downX = 0f
    private var originalOffestX = 0f
    private var offsetX = 0f

    public var resetDisplayNumber: ((displayNumber:Int) -> Unit)? = null
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

    inner class MyScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            Log.i(TAG, "scale = " + detector.scaleFactor)   // 缩放因子
            Log.i(TAG, "currentSpanX = " + detector.currentSpanX)
            return  if (detector.currentSpanX > 4) {
                if (detector.scaleFactor >= 1) {//放大
                    displayNumber -= (displayNumber * (detector.scaleFactor - 1)).toInt()
                } else {
                    displayNumber += (displayNumber * (1 - detector.scaleFactor)).toInt()
                }
                displayNumber = displayNumber.coerceAtLeast(minDisplayNumber).coerceAtMost(maxDisplayNumber)
                mAttrs.displayNumbers = displayNumber
                Log.i(TAG, "displayNumber = ${mAttrs.displayNumbers}")
                resetDisplayNumber?.invoke(displayNumber)
                true
            } else  false
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            Log.i(TAG, "onScaleBegin = " +  detector.currentSpanX)
            return true
        }
        override fun onScaleEnd(p0: ScaleGestureDetector) {

        }
    }
}