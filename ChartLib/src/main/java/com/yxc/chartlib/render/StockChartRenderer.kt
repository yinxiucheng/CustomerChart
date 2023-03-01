package com.yxc.chartlib.render

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
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
import com.yxc.chartlib.utils.TextUtil
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class  StockChartRenderer<T:ValueFormatter> constructor(var mStockAttrs: StockChartAttrs, var valueFormatter:T)
    :BaseChartRender<StockEntry, StockChartAttrs>(mStockAttrs, valueFormatter) {

    private lateinit var  mLineChartPaint: Paint
    init {
        initLinePaint()
    }

    private fun initLinePaint() {
        mLineChartPaint = Paint()
        mLineChartPaint.reset()
        mLineChartPaint.isAntiAlias = true
        mLineChartPaint.style = Paint.Style.FILL
        mLineChartPaint.strokeWidth = dip2px(1f).toFloat()
        mLineChartPaint.color = mStockAttrs.chartColor
    }

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
            //todo 注意RTL
            drawChart(canvas, rectMain, parent.left.toFloat(), parent.right.toFloat(), radius)
            mHighLightLinePaint.color = color
            if (stockEntry.mShadowHigh > Math.max(stockEntry.mClose, stockEntry.mOpen)){
                drawTopLine(stockEntry.mShadowHigh, canvas, rectMain, yAxis, parent)
            }
            if (stockEntry.mShadowLow < Math.min(stockEntry.mClose, stockEntry.mOpen)){
                drawDownLine(stockEntry.mShadowLow, canvas, rectMain, yAxis, parent)
            }
        }
    }

    //绘制上引线、下引线
    private fun drawTopLine(value:Float, canvas: Canvas, rectF: RectF, yAxis: YAxis,  parent: RecyclerView){
        canvas.save()
        val y = getYPosition(value, parent, yAxis, mStockAttrs)
        val x = (rectF.left + rectF.right)/2
        if (DecimalUtil.bigOrEquals(x, parent.left.toFloat()) && DecimalUtil.smallOrEquals(x, parent.right.toFloat())){
            canvas.drawLine(x, rectF.top, x, y, mHighLightLinePaint)
            canvas.restore()
        }
    }

    private fun drawDownLine(value:Float, canvas: Canvas, rectF: RectF, yAxis: YAxis,  parent: RecyclerView){
        canvas.save()
        val y = getYPosition(value, parent, yAxis, mStockAttrs)
        val x = (rectF.left + rectF.right)/2
        if (DecimalUtil.bigOrEquals(x, parent.left.toFloat()) && DecimalUtil.smallOrEquals(x, parent.right.toFloat())){
            canvas.drawLine(x, rectF.bottom, x, y, mHighLightLinePaint)
            canvas.restore()
        }
    }

    private fun drawChart(canvas: Canvas, rectF: RectF, parentLeft: Float, parentRight: Float, radius: Float) {
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft
            val path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_RIGHT_TOP)
            mBarChartPaint.color = mBarChartAttrs.chartEdgeColor
            canvas.drawPath(path, mBarChartPaint)
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            // 中间部分的Item
            val path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_ALL)
            canvas.drawPath(path, mBarChartPaint)
            //            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            val distance = parentRight - rectF.left
            rectF.right = rectF.left + distance
            val path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_LEFT_TOP)
            mBarChartPaint.color = mBarChartAttrs.chartEdgeColor
            canvas.drawPath(path, mBarChartPaint)
        }
    }

    private fun <E : YAxis> getYPosition(yValue:Float, parent: RecyclerView,yAxis: E, mAttrs: StockChartAttrs):Float{
        val contentBottom = parent.bottom - parent.paddingBottom - mAttrs.contentPaddingBottom
        val contentTop = parent.paddingTop + mAttrs.contentPaddingTop
        val realYAxisLabelHeight = contentBottom - contentTop
        val yMin = yAxis.axisMinimum
        val height: Float = (yValue - yMin) / (yAxis.axisMaximum - yMin) * realYAxisLabelHeight
        return contentBottom - height
    }

    private fun <E : YAxis> getStockRectF(
        child: View, parent: RecyclerView, yAxis: E,
        mAttrs: StockChartAttrs, stockEntry: StockEntry
    ): RectF {
        val rectF = RectF()
        val maxY = Math.max(stockEntry.mClose, stockEntry.mOpen)
        val minY = Math.min(stockEntry.mClose, stockEntry.mOpen)
        val contentBottom = parent.bottom - parent.paddingBottom - mAttrs.contentPaddingBottom
        val contentTop = parent.paddingTop + mAttrs.contentPaddingTop
        val realYAxisLabelHeight = contentBottom - contentTop
        val yMin = yAxis.axisMinimum

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

    //绘制选中时 highLight 标线及浮框。
    fun drawHighLight(canvas: Canvas, parent: RecyclerView, yAxis: YAxis) {
        if (mStockAttrs.enableValueMark) {
            val parentTop = parent.paddingTop.toFloat()
            val childCount = parent.childCount
            val contentRight = (parent.width - parent.paddingRight).toFloat()
            val contentBottom: Float = parent.height - parent.paddingBottom - mStockAttrs.contentPaddingBottom
            val contentLeft = parent.paddingLeft.toFloat()
            var child: View
            for (i in 0 until childCount) {
                child = parent.getChildAt(i)
                val barEntry = child.tag as StockEntry
                val width = child.width.toFloat()
                val childCenter = child.left + width / 2
                val valueStr = mHighLightValueFormatter.getBarLabel(barEntry)
                val points = floatArrayOf(childCenter, contentBottom, childCenter, parentTop)
                if (barEntry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    val chartColor: Int = mStockAttrs.chartColor
                    drawHighLightLine(canvas, points, barChartColor = chartColor)
                    drawHighLightValue(canvas, valueStr, childCenter, contentLeft, contentRight, parentTop, chartColor)
                }
            }
        }
    }

    //绘制柱状图顶部value文字
    private fun drawHighLightValue(
        canvas: Canvas, valueStr: String, childCenter: Float,
        contentLeft: Float, contentRight: Float, contentTop: Float, barChartColor: Int
    ) {
        val leftEdgeDistance = Math.abs(childCenter - contentLeft)
        val rightEdgeDistance = Math.abs(contentRight - childCenter)
        val leftPadding = dip2px(8f).toFloat()
        val rightPadding = dip2px(8f).toFloat()
        val txtTopPadding = dip2px(8f).toFloat()
        val rectFHeight = TextUtil.getTxtHeight1(mHighLightDescPaint) + txtTopPadding * 2
        val txtWidth = mHighLightDescPaint.measureText(valueStr) + leftPadding + rightPadding
        val edgeDistance = txtWidth / 2.0f
        val rectTop = contentTop - rectFHeight

        //绘制RectF
        val rectF = RectF()
        mHighLightLinePaint.color = barChartColor
        if (leftEdgeDistance <= edgeDistance) { //矩形框靠左对齐
            rectF[contentLeft, rectTop, contentLeft + txtWidth] = contentTop
            val radius = dip2px(8f).toFloat()
            canvas.drawRoundRect(rectF, radius, radius, mHighLightLinePaint)
        } else if (rightEdgeDistance <= edgeDistance) { //矩形框靠右对齐
            rectF[contentRight - txtWidth, rectTop, contentRight] = contentTop
            val radius = dip2px(8f).toFloat()
            canvas.drawRoundRect(rectF, radius, radius, mHighLightLinePaint)
        } else { //居中对齐。
            rectF[childCenter - edgeDistance, rectTop, childCenter + edgeDistance] = contentTop
            val radius = dip2px(8f).toFloat()
            canvas.drawRoundRect(rectF, radius, radius, mHighLightLinePaint)
        }

        //绘文字
        val leftRectF = RectF(rectF.left + leftPadding, rectTop + txtTopPadding,
            rectF.left + leftPadding + txtWidth, rectTop + txtTopPadding + rectFHeight
        )
        mHighLightDescPaint.setTextAlign(Paint.Align.LEFT)
        val fontMetrics: Paint.FontMetrics =
            mHighLightDescPaint.getFontMetrics()
        val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
        val baseLineY = (leftRectF.centerY() + (top + bottom) / 2).toInt() //基线中间点的y轴计算公式
        canvas.drawText(
            valueStr,
            rectF.left + leftPadding,
            baseLineY.toFloat(),
            mHighLightDescPaint
        )
    }

    private fun drawHighLightLine(canvas: Canvas, floats: FloatArray, barChartColor: Int) {
        val previous = mLineChartPaint.style
        val strokeWidth = mLineChartPaint.strokeWidth
        val color = mLineChartPaint.color
        // set
        mLineChartPaint.style = Paint.Style.FILL
        mLineChartPaint.strokeWidth = dip2px(1f).toFloat()
        mLineChartPaint.color = barChartColor
        canvas.drawLines(floats, mLineChartPaint)
        // restore
        mLineChartPaint.style = previous
        mLineChartPaint.strokeWidth = strokeWidth
        mLineChartPaint.color = color
    }


}