package com.yxc.chartlib.util

import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.component.YAxis
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.entrys.model.AvgType

/**
 * @author xiuchengyin
 *
 * @date 2023/3/3
 *
 */
object StockDrawHelper {


    fun <E : YAxis> getYPosition(
        yValue: Float,
        parent: RecyclerView,
        yAxis: E,
        mAttrs: StockChartAttrs
    ): Float {
        val contentBottom = parent.bottom - parent.paddingBottom - mAttrs.contentPaddingBottom
        val contentTop = parent.paddingTop + mAttrs.contentPaddingTop
        val realYAxisLabelHeight = contentBottom - contentTop
        val yMin = yAxis.axisMinimum
        val height: Float = (yValue - yMin) / (yAxis.axisMaximum - yMin) * realYAxisLabelHeight
        return contentBottom - height
    }


    fun getAvgValue(avgType: AvgType, entry: StockEntry): Float {
        return when (avgType) {
            is AvgType.Avg5Type -> entry.ma5
            is AvgType.Avg10Type -> entry.ma10
            is AvgType.Avg20Type -> entry.ma20
        }
    }

    fun getAvgColor(avgType: AvgType, mStockAttrs: StockChartAttrs): Int {
        return when (avgType) {
            is AvgType.Avg5Type -> mStockAttrs.avg5Color
            is AvgType.Avg10Type -> mStockAttrs.avg10Color
            is AvgType.Avg20Type -> mStockAttrs.avg20Color
        }
    }
}