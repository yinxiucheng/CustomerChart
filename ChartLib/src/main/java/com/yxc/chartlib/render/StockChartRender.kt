package com.yxc.chartlib.render

import android.graphics.Canvas
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.component.YAxis
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.formatter.ValueFormatter
import com.yxc.chartlib.util.CanvasUtil
import com.yxc.chartlib.util.RoundRectType
import com.yxc.chartlib.utils.DecimalUtil
import com.yxc.chartlib.utils.DisplayUtil.dip2px
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
import com.yxc.chartlib.util.ChartComputeUtil

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
            val stockEntry = child.tag as StockEntry
            val rectMain = getStockRectF(child, parent, yAxis, mStockAttrs, stockEntry)
            val radius = 1f
            val color = if (stockEntry.isRise) mStockAttrs.riseColor else mStockAttrs.downColor
            mBarChartPaint.color = color
            drawChart(canvas, rectMain, parentLeft, parentRight, radius)
            drawTopAndDownLine(stockEntry.mShadowHigh, canvas, rectMain, yAxis, parent)
            drawTopAndDownLine(stockEntry.mShadowLow, canvas, rectMain, yAxis, parent)
        }
    }

    //绘制上引线、下引线
    private fun drawTopAndDownLine(value:Float, canvas: Canvas, rectF: RectF, yAxis: YAxis,  parent: RecyclerView){
        val y = ChartComputeUtil.getYPosition<StockEntry>(value, parent, yAxis, mStockAttrs)
        val x = (rectF.left + rectF.right)/2
        canvas.drawLine(x, rectF.top, x, y, mBarChartPaint)
    }

    private fun drawChart(canvas: Canvas, rectF: RectF, parentLeft: Float, parentRight: Float, radius: Float) {
        if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            val path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_ALL)
            canvas.drawPath(path, mBarChartPaint)
        } else {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
        }
    }

    private fun <E : YAxis> getStockRectF(
        child: View, parent: RecyclerView, yAxis: E,
        mAttrs: StockChartAttrs, stockEntry: StockEntry
    ): RectF {
        val rectF = RectF()
        val maxY = Math.max(stockEntry.mClose, stockEntry.mOpen)
        val minY = Math.min(stockEntry.mClose, stockEntry.mOpen)
        val contentBottom = parent.height - parent.paddingBottom - mAttrs.contentPaddingBottom
        val realYAxisLabelHeight = contentBottom - parent.paddingTop - mAttrs.contentPaddingTop
        val contentTop = parent.paddingTop + mAttrs.contentPaddingTop
        val yMin = mAttrs.yAxisMinimum
        val maxHeight = (maxY - yMin) / (yAxis.axisMaximum - yMin) * realYAxisLabelHeight
        val minHeight = (minY - yMin) / (yAxis.axisMaximum - yMin) * realYAxisLabelHeight
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