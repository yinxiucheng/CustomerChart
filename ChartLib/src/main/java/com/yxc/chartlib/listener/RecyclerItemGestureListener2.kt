package com.yxc.chartlib.listener

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.yxc.chartlib.barchart.SpeedRatioLayoutManager
import com.yxc.chartlib.view.BaseChartRecyclerView
import com.yxc.chartlib.view.BaseChartRecyclerView.OnChartTouchListener
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author yxc
 * @since 2019/4/23
 */
class RecyclerItemGestureListener2<T : RecyclerBarEntry?> constructor(context: Context?,
    private val parent: BaseChartRecyclerView<*, *>,
    private val mListener: OnItemGestureListener<*>?
) : OnItemTouchListener {
    private var isLongPressing = false
    var selectBarEntry: T? = null
        private set
    private val mGestureListener: MyOnGestureListener by lazy { MyOnGestureListener() }
    private val mGestureDetector: GestureDetectorCompat by lazy { GestureDetectorCompat(context, mGestureListener) }
    private val onChartTouchListener: OnChartTouchListener by lazy { MyOnChartTouchListener() }
    private val mScrollListener: RecyclerView.OnScrollListener by lazy { MyOnScrollListener() }
    private val layoutManager: SpeedRatioLayoutManager?
    private val mAdapter: RecyclerView.Adapter<*>?

    init {
        layoutManager = parent.layoutManager as SpeedRatioLayoutManager?
        mAdapter = parent.adapter
        parent.addOnChartTouchListener(onChartTouchListener)
        parent.addOnScrollListener(mScrollListener)
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
            selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
            selectBarEntry = null
        }
    }

    fun setSelectBarEntry(barEntry: T) {
        selectBarEntry = barEntry
        selectBarEntry!!.selected = RecyclerBarEntry.TYPE_SINGLE_TAP_UP_SELECTED
    }

    inner class MyOnGestureListener:SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val x = e.x
            val y = e.y
            val child = parent.findChildViewUnder(x, y)
            val parentRight = (parent.width - parent.paddingRight).toFloat()
            if (child != null && mListener != null) {
                val reservedWidth = child.width / 2.0f
                if (x < parent.paddingLeft + reservedWidth || x > parentRight - reservedWidth) {
                    return false
                }
                val position = parent.getChildAdapterPosition(child)
                if (position != RecyclerView.NO_POSITION) {
                    val barEntry = child.tag as T
                    if (!barEntry!!.equals(selectBarEntry)) {
                        //重置原来的SelectBarEntry
                        if (null != selectBarEntry) {
                            selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
                        }
                        selectBarEntry = barEntry
                        barEntry.selected = RecyclerBarEntry.TYPE_SINGLE_TAP_UP_SELECTED
                    }
                    mListener.onItemSelected(barEntry, position)
                    mListener.onItemClick(child, position)
                    mAdapter?.notifyItemChanged(position, false)
                    return true
                }
            }
            return false
        }

        override fun onLongPress(e: MotionEvent) {
            val x = e.x
            val y = e.y
            val child = parent.findChildViewUnder(x, y)
            val parentRight = (parent.width - parent.paddingRight).toFloat()
            isLongPressing = true
            if (null != layoutManager) {
                layoutManager.ratioSpeed = 0.0
            }
            if (child != null && mListener != null) {
                val reservedWidth = child.width / 2.0f
                if (x < parent.paddingLeft + reservedWidth || x > parentRight - reservedWidth) {
                    return
                }
                val position = parent.getChildAdapterPosition(child)
                if (position != RecyclerView.NO_POSITION) {
                    val barEntry = child.tag as T
                    if (barEntry!!.y <= 0) {
                        return
                    } else if (!barEntry.equals(selectBarEntry)) {
                        //重置原来的SelectBarEntry
                        if (null != selectBarEntry) {
                            selectBarEntry!!.selected = RecyclerBarEntry.TYPE_UNSELECTED
                        }
                        selectBarEntry = barEntry
                        barEntry.selected = RecyclerBarEntry.TYPE_LONG_PRESS_SELECTED
                    }
                    mAdapter?.notifyItemChanged(position, false)
                    mListener.onLongItemClick(child, position)
                    mListener.onItemSelected(barEntry, position)
                }
            }
        }

        override fun onDown(e: MotionEvent): Boolean {
            Log.d("OnItemTouch", " onDown: " + System.currentTimeMillis() / 1000)
            return super.onDown(e)
        }
    }

    inner class MyOnChartTouchListener: OnChartTouchListener{
        override fun onChartGestureStart(e: MotionEvent) {}

        override fun onChartGestureEnd(e: MotionEvent) {
            Log.d("OnItemTouch", " onChartGestureEnd： " + System.currentTimeMillis() / 1000)
            isLongPressing = false
            layoutManager?.resetRatioSpeed()
        }

        override fun onChartGestureMovingOn(e: MotionEvent) {
            Log.d(
                "OnItemTouch",
                " onChartGestureMovingOn： " + System.currentTimeMillis() / 1000
            )
            val x = e.x
            val y = e.y
            val child: View?
            child = parent.findChildViewUnder(x, y)
            val parentRight = (parent.width - parent.paddingRight).toFloat()
            if (child != null && isLongPressing) {
                // longPress not action end, then moving the item is touched should be set selected
                val reservedWidth = child.width / 2.0f
                //deal with the condition of the edge
                if (x < parent.paddingLeft + reservedWidth || x > parentRight - reservedWidth) {
                    return
                }
                val position = parent.getChildAdapterPosition(child)
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
                        mAdapter?.notifyItemChanged(position, false)
                    }
                    mListener!!.onItemSelected(barEntry, position)
                }
            } else {
                //when is not longPress, normal condition reset the selected BarEntry
//                    if (null != selectBarEntry && selectBarEntry.isSelected == BarEntry.TYPE_LONG_PRESS_SELECTED && isLongPressing) {
//                        selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
//                        selectBarEntry = null;
//                        Log.d("OnItemTouch", " onItemSelected 释放 在 onChartGestureMovingOn： " + System.currentTimeMillis() / 1000);
//                        mListener.onItemSelected(null, -1);
//                    }
            }
        }
    }

    inner class MyOnScrollListener:RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            mListener?.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            mListener?.onScrolled(recyclerView, dx, dy)
        }
    }
}