package com.yxc.chartlib.listener

import android.content.Context
import android.util.Log
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.barchart.SpeedRatioLayoutManager
import com.yxc.chartlib.view.BaseChartRecyclerView
import com.yxc.chartlib.view.BaseChartRecyclerView.OnChartTouchListener
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
import kotlin.math.abs

/**
 * @author yxc
 * @since 2019/4/23
 */
class RecyclerStockItemGestureListener<T : RecyclerBarEntry?>(
    context: Context?,
    recyclerView: BaseChartRecyclerView<*, *>,
    mListener: OnItemGestureListener<*>
) : RecyclerItemGestureListener<T>(context, recyclerView, mListener) {

    private val mOnGestureListener: SimpleOnGestureListener by lazy { MyOnGestureListener() }
    private val mGestureDetector: GestureDetectorCompat by lazy { GestureDetectorCompat(context, mOnGestureListener) }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(parent: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    inner class MyOnGestureListener: SimpleOnGestureListener(){
        private fun onMainChartClick(child: View?, x:Float):Boolean{
            child?.let {
                val parentRight = (recyclerView.width - recyclerView.paddingRight).toFloat()
                //Logger.d("BarChart Render click begin time:" + System.currentTimeMillis());
//                val reservedWidth = child.width / 2.0f
                if (x < recyclerView.paddingLeft || x > parentRight) {
                    return false
                }
                val position = recyclerView.getChildAdapterPosition(child)
                if (position != RecyclerView.NO_POSITION) {
                    val barEntry = child.tag as RecyclerBarEntry
                    Log.d("MPChart", "BarChart Render notify change + SingleTapUp$barEntry")
                    if (barEntry.y <= 0) {
                        if (null != selectBarEntry) {
                            selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
                        }
                        selectBarEntry = null
                        Log.i(
                            "MPChart",
                            "BarChart Render notify change + SingleTapUp null $barEntry"
                        )
                        mListener.onItemSelected(null, position)
                        mAdapter.notifyItemChanged(position, false)
                        return false
                    } else if (barEntry != selectBarEntry) {
                        //重置原来的SelectBarEntry
                        if (null != selectBarEntry) {
                            selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
                        }
                        selectBarEntry = barEntry
                        barEntry.selected = RecyclerBarEntry.TYPE_SINGLE_TAP_UP_SELECTED
                    } else {
                        selectBarEntry = null
                        barEntry.selected = RecyclerBarEntry.TYPE_UNSELECTED //再次被点击
                    }
                    mListener.onItemSelected(barEntry, position)
                    mListener.onItemClick(child, position)
                    mAdapter.notifyItemChanged(position, false)
                    return true
                }
            }
            return false
        }


        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val x = e.x
            val y = e.y
            val descTop = recyclerView.bottom - recyclerView.paddingBottom - recyclerView.mAttrs.contentPaddingBottom
            val descBottom = descTop + (recyclerView.mAttrs as StockChartAttrs).mAttachedDescHeight
            val child = recyclerView.findChildViewUnder(x, y)
            return if (y > descTop && y <= descBottom) {
                onAttacheDescClick()
            } else if (y > descBottom) {
                onAttacheChartClick(child)
            } else {
                onMainChartClick(child, x)
            }
        }

        private fun onAttacheDescClick():Boolean{
            (mListener as SimpleStockItemGestureListener).showBottomPopWindow()
            return true
        }

        private fun onAttacheChartClick(view:View?):Boolean{
            view?.let {
                (mListener as SimpleStockItemGestureListener).onStockItemBottomClick(it)
                return true
            }
            return false
        }

        override fun onLongPress(e: MotionEvent) {
            val x = e.x
            val y = e.y
            val child = recyclerView.findChildViewUnder(x, y)
            val parentRight = (recyclerView.width - recyclerView.paddingRight).toFloat()
            isLongPressing = true
            layoutManager.ratioSpeed = 0.0
            if (child != null ) {
//                    val reservedWidth = child.width / 2.0f
                if (x < recyclerView.paddingLeft || x > parentRight) {
                    return
                }
                val position = recyclerView.getChildAdapterPosition(child)
                if (position != RecyclerView.NO_POSITION) {
                    val barEntry = child.tag as RecyclerBarEntry
                    Log.i("MPChart", "BarChart Render notify change + LongPress$barEntry")
                    if (barEntry.y <= 0) {
                        return
                    } else if (barEntry != selectBarEntry) {
                        //重置原来的SelectBarEntry
                        if (null != selectBarEntry) {
                            selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
                        }
                        selectBarEntry = barEntry
                        barEntry.selected = RecyclerBarEntry.TYPE_LONG_PRESS_SELECTED
                    } else {
                        selectBarEntry = null
                        barEntry.selected = RecyclerBarEntry.TYPE_UNSELECTED //再次被点击
                    }
                    mAdapter.notifyItemChanged(position, 0)
                    mListener.onLongItemClick(child, position)
                    mListener.onItemSelected(barEntry, position)
                }
            }
        }
        override fun onDown(e: MotionEvent): Boolean {
//                Log.d("OnItemTouch", " onDown: " + System.currentTimeMillis() / 1000);
            return super.onDown(e)
        }
    }
}