package com.yxc.chartlib.util

import android.graphics.PointF
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.component.StockYAxis
import com.yxc.chartlib.component.YAxis
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.entrys.model.AvgType
import com.yxc.chartlib.entrys.stock.MACDEntry
import com.yxc.chartlib.utils.DisplayUtil
import com.yxc.customercomposeview.utils.dp
import com.yxc.customercomposeview.utils.dpf
import kotlin.math.abs

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

    fun <E : YAxis> getAttacheYPosition(
        yValue: Float,
        parent: RecyclerView,
        yAxis: E,
        mAttrs: StockChartAttrs
    ): Float {
        val contentBottom = parent.bottom - parent.paddingBottom
        val contentTop = parent.bottom - parent.paddingBottom - mAttrs.contentPaddingBottom
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

     fun <E : YAxis> getStockRectF(
        child: View, parent: RecyclerView, yAxis: E,
        mAttrs: StockChartAttrs, stockEntry: StockEntry): RectF {
        val rectF = RectF()
        val maxY = stockEntry.mClose.coerceAtLeast(stockEntry.mOpen)
        val minY = stockEntry.mClose.coerceAtMost(stockEntry.mOpen)
        val contentBottom = parent.bottom - parent.paddingBottom - mAttrs.contentPaddingBottom
        val contentTop = parent.paddingTop + mAttrs.contentPaddingTop
        val realYAxisLabelHeight = contentBottom - contentTop
        val yMin = yAxis.axisMinimum
        val maxHeight = (maxY - yMin) / (yAxis.axisMaximum - yMin) * realYAxisLabelHeight
        val minHeight = (minY - yMin) / (yAxis.axisMaximum - yMin) * realYAxisLabelHeight
        val rectFTop = contentBottom - maxHeight
        var rectFBottom = contentBottom - minHeight
        if (rectFTop == rectFBottom && rectFTop != contentBottom) rectFBottom += DisplayUtil.dip2px(
            2f
        ).toFloat()
        val width = child.width.toFloat()
        val barSpaceWidth = width * mAttrs.barSpace
        val barChartWidth = width - barSpaceWidth //柱子的宽度
        val left = child.left + barSpaceWidth / 2
        val right = left + barChartWidth
        val top = rectFTop.coerceAtLeast(contentTop)
        if ((rectFBottom - top) < 1f.dpf) rectFBottom = top + 1f.dpf
        rectF[left, top, right] = rectFBottom
        return rectF
    }

    fun getAttacheStockRectF(child: View, parent: RecyclerView, attacheYAxis: StockYAxis,
                                     mAttrs: StockChartAttrs, stockEntry: StockEntry): RectF {
        val rectF = RectF()
        val contentBottom = parent.bottom - parent.paddingBottom - 18.dpf
        val contentTop = parent.bottom - parent.paddingBottom - mAttrs.contentPaddingBottom + 27.dp
        val realYAxisLabelHeight = contentBottom - contentTop
        val rectHeight = stockEntry.volume / (attacheYAxis.axisMaximum - attacheYAxis.axisMinimum) * realYAxisLabelHeight
        val rectFTop = contentBottom - rectHeight
        var rectFBottom = contentBottom
        if (rectFTop == rectFBottom && rectFTop != contentBottom) rectFBottom += DisplayUtil.dip2px(
            2f
        ).toFloat()
        val width = child.width.toFloat()
        val barSpaceWidth = width * mAttrs.barSpace
        val barChartWidth = width - barSpaceWidth //柱子的宽度
        val left = child.left + barSpaceWidth / 2
        val right = left + barChartWidth
        val top = rectFTop.coerceAtLeast(contentTop)
        if ((rectFBottom - top) < 1f.dpf) rectFBottom = top + 1f.dpf
        rectF[left, top, right] = rectFBottom
        return rectF
    }

    fun getAttacheMACDRectF(child: View, parent: RecyclerView, attacheYAxis: StockYAxis,
                             mAttrs: StockChartAttrs, stockEntry: StockEntry): RectF {
        val rectF = RectF()
        val contentBottom = parent.bottom - parent.paddingBottom - 18.dpf
        val contentTop = parent.bottom - parent.paddingBottom - mAttrs.contentPaddingBottom + 27.dp
        val realYAxisLabelHeight = contentBottom - contentTop
        val centerY = contentTop + realYAxisLabelHeight/2
        val macdEntry = stockEntry.macdEntry as MACDEntry
        val rectHeight = abs(macdEntry.macd -  50) / 50 * (realYAxisLabelHeight/2)

        val rectTop = if (stockEntry.isRise) centerY - rectHeight else centerY
        val rectFBottom = if (stockEntry.isRise) centerY else centerY + rectHeight

        val width = child.width.toFloat()
        val barSpaceWidth = width * mAttrs.barSpace
        val barChartWidth = width - barSpaceWidth //柱子的宽度
        val left = child.left + barSpaceWidth / 2
        val right = left + barChartWidth
        rectF[left, rectTop, right] = rectFBottom

        return rectF
    }

    fun createNearPoint(avgType:AvgType, parent: RecyclerView,mAttrs: StockChartAttrs, barEntryNear: StockEntry,
                                currentPoint: PointF, viewWidth: Int, yAxis: YAxis, leftOrRight: Boolean): PointF {
        val xNear = if (leftOrRight) currentPoint.x - viewWidth else currentPoint.x + viewWidth
        val yNear = getYPosition(getAvgValue(avgType, barEntryNear), parent, yAxis, mAttrs)
        return PointF(xNear, yNear)
    }


}