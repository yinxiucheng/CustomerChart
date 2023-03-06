package com.yxc.chartlib.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author yxc
 * @date 2019-09-17
 */
interface OnItemGestureListener<T : RecyclerBarEntry?> {
    fun onItemClick(view: View, position: Int)
    fun onLongItemClick(view: View, position: Int)
    fun onItemSelected(barEntry: RecyclerBarEntry?, position: Int)
    fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
    fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
}