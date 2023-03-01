package com.yxc.chartlib.render

import android.graphics.Canvas
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.component.YAxis
import com.yxc.chartlib.entrys.MaxMinEntry
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.formatter.ValueFormatter
import com.yxc.chartlib.util.CanvasUtil
import com.yxc.chartlib.util.RoundRectType
import com.yxc.chartlib.utils.DecimalUtil
import com.yxc.chartlib.utils.DisplayUtil.dip2px
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class  StockChartRender<T:ValueFormatter> constructor(var mStockAttrs: StockChartAttrs, var valueFormatter:T)
    :BaseChartRender<StockEntry, StockChartAttrs>(mStockAttrs, valueFormatter) {

    override fun <E : RecyclerBarEntry?> getChartColor(entry: E): Int {
        TODO("Not yet implemented")
    }

    fun <Y : YAxis> drawStockChart(canvas: Canvas, parent: RecyclerView, yAxis: Y) {
        val parentRight = (parent.width - parent.paddingRight).toFloat()
        val parentLeft = parent.paddingLeft.toFloat()
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val rateEntry = child.tag as MaxMinEntry
            val rectFMinMax = getMinMaxRectF(child, parent, yAxis, mStockAttrs, rateEntry)
            val radius = 1f
            drawChart(canvas, rectFMinMax, parentLeft, parentRight, radius, calculateItemColor())
        }
    }

    private fun calculateItemColor():Int{
        return mStockAttrs.riseColor
    }

    protected fun drawChart(
        canvas: Canvas,
        rectF: RectF,
        parentLeft: Float,
        parentRight: Float,
        radius: Float,
        paintColor: Int
    ) {
        if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            mBarChartPaint.color = paintColor
            val path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_ALL)
            canvas.drawPath(path, mBarChartPaint)
        }else{
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
        }
    }

    private fun <E : YAxis> getMinMaxRectF(
        child: View, parent: RecyclerView, yAxis: E,
        mAttrs: StockChartAttrs, rateEntry: MaxMinEntry
    ): RectF {
        val rectF = RectF()
        val contentBottom =
            parent.height - parent.paddingBottom - mAttrs.contentPaddingBottom
        val realYAxisLabelHeight =
            contentBottom - parent.paddingTop - mAttrs.contentPaddingTop
        val contentTop = parent.paddingTop + mAttrs.contentPaddingTop
        val yMin = mAttrs.yAxisMinimum
        val maxHeight = (rateEntry.maxY - yMin) / (yAxis.axisMaximum - yMin) * realYAxisLabelHeight
        val minHeight = (rateEntry.minY - yMin) / (yAxis.axisMaximum - yMin) * realYAxisLabelHeight
        val rectFTop = contentBottom - maxHeight
        var rectFBottom = contentBottom - minHeight
        if (rectFTop == rectFBottom && rectFTop != contentBottom) rectFBottom += dip2px(2f).toFloat()
        val width = child.width.toFloat()
        val barSpaceWidth = width * mAttrs.barSpace
        val barChartWidth = width - barSpaceWidth //柱子的宽度
        val left = child.left + barSpaceWidth / 2
        val right = left + barChartWidth
        val top = Math.max(rectFTop, contentTop)
        rectF[left, top, right] = rectFBottom
        return rectF
    }

    fun drawHighLight(canvas: Canvas, parent: RecyclerView, yAxis: YAxis){

    }

}