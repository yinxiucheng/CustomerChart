package com.yxc.chartlib.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author xiuchengyin
 *
 * @date 2023/3/6
 *
 */
open class SimpleStockItemGestureListener: OnStockItemGestureListener<RecyclerBarEntry?> {
    override fun onItemClick(view: View, position: Int) {}
    override fun onLongItemClick(view: View, position: Int) {}
    override fun onItemSelected(barEntry: RecyclerBarEntry?, position: Int) {}
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
    override fun onStockItemBottomClick(view: View){}
    override fun showBottomPopWindow(){}
}