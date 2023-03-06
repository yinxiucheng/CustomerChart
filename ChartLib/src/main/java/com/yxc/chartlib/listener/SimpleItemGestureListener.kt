package com.yxc.chartlib.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author yxc
 * @since  2019/4/24
 */
open class SimpleItemGestureListener<T:RecyclerBarEntry?> : OnItemGestureListener<RecyclerBarEntry> {
    override fun onItemClick(view: View, position: Int) {}
    override fun onLongItemClick(view: View, position: Int) {}
    override fun onItemSelected(barEntry: RecyclerBarEntry?, position: Int) {}
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
}