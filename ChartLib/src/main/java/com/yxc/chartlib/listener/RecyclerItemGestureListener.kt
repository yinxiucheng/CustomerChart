package com.yxc.chartlib.listener

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.yxc.chartlib.barchart.SpeedRatioLayoutManager
import com.yxc.chartlib.view.BaseChartRecyclerView
import com.yxc.chartlib.view.BaseChartRecyclerView.OnChartTouchListener
import com.yxc.chartlib.view.StockChartRecyclerView
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author yxc
 * @since 2019/4/23
 */
class RecyclerItemGestureListener<T : RecyclerBarEntry?>(context: Context?,
    private val recyclerView: BaseChartRecyclerView<*, *>,
    private val mListener: OnItemGestureListener<*>?) : OnItemTouchListener {

    var isLongPressing = false
    private var selectBarEntry: T? = null
    private val mGestureListener: MyOnGestureListener by lazy { MyOnGestureListener() }
    private val mGestureDetector: GestureDetectorCompat by lazy { GestureDetectorCompat(context, mGestureListener) }
    private val onChartTouchListener: OnChartTouchListener by lazy { MyOnChartTouchListener() }
    private val myScrollListener: MyOnScrollListener by lazy { MyOnScrollListener() }
    private val layoutManager: SpeedRatioLayoutManager
    private val mAdapter: RecyclerView.Adapter<*>
    private var lastPosition = 0

    init {
        layoutManager = recyclerView.layoutManager as SpeedRatioLayoutManager
        mAdapter = recyclerView.adapter as RecyclerView.Adapter
        recyclerView.addOnChartTouchListener(onChartTouchListener)
        recyclerView.addOnScrollListener(myScrollListener)
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(parent: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    fun resetSelectedBarEntry() {
        if (null != selectBarEntry) {
            this.selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
            selectBarEntry = null
        }
        isLongPressing = false
        layoutManager.resetRatioSpeed()
    }

    inner class MyOnGestureListener:SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val x = e.x
            val y = e.y
            val child = recyclerView.findChildViewUnder(x, y)
            val parentRight = (recyclerView.width - recyclerView.paddingRight).toFloat()
            if (child != null && mListener != null) {
//                    Logger.d("BarChart Render click begin time:" + System.currentTimeMillis());
                val reservedWidth = child.width / 2.0f
                if (x < recyclerView.paddingLeft || x > parentRight) {
                    return false
                }
                val position = recyclerView.getChildAdapterPosition(child)
                if (position != RecyclerView.NO_POSITION) {
                    val barEntry = child.tag as T
                    Log.d("MPChart", "BarChart Render notify change + SingleTapUp$barEntry")
                    if (barEntry!!.y <= 0) {
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
                    } else if (!barEntry.equals(selectBarEntry)) {
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

        override fun onLongPress(e: MotionEvent) {
            val x = e.x
            val y = e.y
            val child = recyclerView.findChildViewUnder(x, y)
            val parentRight = (recyclerView.width - recyclerView.paddingRight).toFloat()
            isLongPressing = true
            if (null != layoutManager) {
                layoutManager.ratioSpeed = 0.0
            }
            if (child != null && mListener != null) {
                val reservedWidth = child.width / 2.0f
                if (x < recyclerView.paddingLeft || x > parentRight) {
                    return
                }
                val position = recyclerView.getChildAdapterPosition(child)
                if (position != RecyclerView.NO_POSITION) {
                    val barEntry = child.tag as T
                    Log.i("MPChart", "BarChart Render notify change + LongPress$barEntry")
                    if (barEntry!!.y <= 0) {
                        return
                    } else if (!barEntry.equals(selectBarEntry)) {
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
                    mAdapter?.notifyItemChanged(position, 0)
                    mListener.onLongItemClick(child, position)
                    mListener.onItemSelected(barEntry, position)
                }
            }
        }

        override fun onDown(e: MotionEvent): Boolean {
        //Log.d("OnItemTouch", " onDown: " + System.currentTimeMillis() / 1000);
            return super.onDown(e)
        }
    }

    inner class MyOnChartTouchListener:OnChartTouchListener{
        override fun onChartGestureStart(e: MotionEvent) {}

        override fun onChartGestureEnd(e: MotionEvent) {
//                Log.d("OnItemTouch", " onChartGestureEnd： " + System.currentTimeMillis() / 1000);
            isLongPressing = false
            layoutManager.resetRatioSpeed()
        }

        override fun onChartGestureMovingOn(e: MotionEvent) {
//                Log.d("OnItemTouch", " onChartGestureMovingOn： " + System.currentTimeMillis() / 1000);
            val x = e.x
            val y = e.y
            val child: View?
            child = recyclerView.findChildViewUnder(x, y)
            val parentRight = (recyclerView.width - recyclerView.paddingRight).toFloat()
            if (child != null && isLongPressing) {
                // longPress not action end, then moving the item is touched should be set selected
                val reservedWidth = child.width / 2.0f
                //deal with the condition of the edge
                if (x < recyclerView.paddingLeft || x > parentRight) {
                    return
                }
                val position = recyclerView.getChildAdapterPosition(child)
                lastPosition = position
                if (position != RecyclerView.NO_POSITION) {
                    val barEntry = child.tag as T
                    if (barEntry!!.y <= 0) {
                        return
                    }
                    if (!barEntry.equals(selectBarEntry)) {
                        if (selectBarEntry != null) {
                            selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
                        }
                        selectBarEntry = barEntry
                        barEntry.selected = RecyclerBarEntry.TYPE_LONG_PRESS_SELECTED
                        mAdapter.notifyItemChanged(position, 0)
                    }
                    mListener!!.onItemSelected(barEntry, position)
                }
            } else {
                //when is not longPress, normal condition reset the selected BarEntry
                if (null != selectBarEntry && selectBarEntry!!.selected == RecyclerBarEntry.TYPE_LONG_PRESS_SELECTED && isLongPressing) {
                    selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
                    selectBarEntry = null
                    if (child == null) {
                        mAdapter.notifyItemChanged(lastPosition, 0)
                    }
                    Log.i(
                        "MPChart",
                        "BarChart Render notify change lastPosition" + lastPosition + "child" + child + "isLongPressing" + isLongPressing
                    )
                    //                        Log.d("OnItemTouch", " onItemSelected 释放 在 onChartGestureMovingOn： " + System.currentTimeMillis() / 1000);
                    mListener!!.onItemSelected(null, -1)
                }
            }
        }
    }

    inner class MyOnScrollListener: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            mListener?.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (null != mListener) {
                if (null != selectBarEntry && selectBarEntry!!.selected != RecyclerBarEntry.TYPE_UNSELECTED && !isLongPressing) {
                    if (Math.abs(dx) > 4) {
                        selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
                        selectBarEntry = null
                        //                            Log.d("OnItemTouch", " onItemSelected 释放 在 onScrolled： " + System.currentTimeMillis() / 1000);
                        mListener.onItemSelected(null, -1)
                    }
                }
                mListener.onScrolled(recyclerView, dx, dy)
            }
        }
    }
}