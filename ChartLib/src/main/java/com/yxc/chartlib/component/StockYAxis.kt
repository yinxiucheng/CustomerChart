package com.yxc.chartlib.component

import com.yxc.chartlib.attrs.StockChartAttrs

/**
 * @author xiuchengyin
 *
 * @date 2023/3/1
 *
 */
class StockYAxis(val mAttrs: StockChartAttrs) : YAxis(mAttrs) {


    companion object {
        fun createYAxisWithLabelCount(attrs: StockChartAttrs,  max:Float, min:Float, labelCount:Int):StockYAxis{
            val axis = StockYAxis(attrs)
            axis.mAxisMaximum = max
            axis.mAxisMinimum = min
            axis.setLabelCount(labelCount)
            axis.labelCount = labelCount
            return axis
        }

        fun resetStockYAxis(
            axis: StockYAxis,
            max: Float,
            min: Float,
            labelCount: Int
        ): StockYAxis {
            axis.axisMaximum = max
            axis.axisMinimum = min
            axis.setLabelCount(labelCount)
            return axis
        }
    }

}