package com.yxc.chartlib.listener

import android.view.View
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author xiuchengyin
 *
 * @date 2023/3/6
 *
 */
interface OnStockItemGestureListener<T : RecyclerBarEntry?> : OnItemGestureListener<T> {
    fun onStockItemBottomClick(view: View, position: Int)
    fun showBottomPopWindow(view: View)
}