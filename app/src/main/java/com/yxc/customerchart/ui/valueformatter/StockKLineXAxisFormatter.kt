package com.yxc.customerchart.ui.valueformatter

import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.formatter.ValueFormatter
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
import com.yxc.mylibrary.TimeDateUtil

/**
 * @author xiuchengyin
 *
 * @date 2023/3/1
 *
 */
class StockKLineXAxisFormatter: ValueFormatter() {

    override fun getBarLabel(barEntry: RecyclerBarEntry): String {
        if (barEntry.type == RecyclerBarEntry.TYPE_XAXIS_FIRST){
            return TimeDateUtil.getDateYYYYMM(barEntry.timestamp * 1000)
        }
        return ""
    }
}