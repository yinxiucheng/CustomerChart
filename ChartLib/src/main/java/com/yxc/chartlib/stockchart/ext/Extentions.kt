package com.yxc.chartlib.stockchart.ext

import com.github.mikephil.charting.charts.Chart

/**
 * @author xiuchengyin
 *
 * @date 2023/2/27
 *
 */

//fun IDataSet.getPrecision(): Int = {}

val StringBuilder.lastChar: String
    get() {
        if (this.isEmpty()) {
            return ""
        }
        return this.substring(length - 1)
    }