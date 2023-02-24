package com.yxc.fitness.chart.barchart.diff

import androidx.recyclerview.widget.DiffUtil
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

class BarChartDiffCallback<T: RecyclerBarEntry> (private val oldList:List<T>, private val newList:List<T>):DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition].timestamp == newList[newItemPosition].timestamp
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldBarEntry = oldList[oldItemPosition]
        val newBarEntry = oldList[newItemPosition]
        return oldBarEntry == newBarEntry
    }
}