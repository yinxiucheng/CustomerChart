package com.yxc.chartlib.render

import android.graphics.*
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.barchart.BaseBarChartAdapter
import com.yxc.chartlib.component.StockYAxis
import com.yxc.chartlib.component.YAxis
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.entrys.model.AvgType
import com.yxc.chartlib.formatter.ValueFormatter
import com.yxc.chartlib.util.CanvasUtil
import com.yxc.chartlib.util.ChartComputeUtil
import com.yxc.chartlib.util.RoundRectType
import com.yxc.chartlib.util.StockDrawHelper
import com.yxc.chartlib.util.StockDrawHelper.createNearPoint
import com.yxc.chartlib.util.StockDrawHelper.getAttacheStockRectF
import com.yxc.chartlib.util.StockDrawHelper.getAvgColor
import com.yxc.chartlib.util.StockDrawHelper.getAvgValue
import com.yxc.chartlib.util.StockDrawHelper.getStockRectF
import com.yxc.chartlib.util.StockDrawHelper.getYPosition
import com.yxc.chartlib.utils.DecimalUtil
import com.yxc.chartlib.utils.DisplayUtil.dip2px
import com.yxc.chartlib.utils.TextUtil
import com.yxc.customercomposeview.utils.dp
import com.yxc.customercomposeview.utils.dpf
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class  StockChartRenderer<T:ValueFormatter> :BaseChartRender<StockEntry, StockChartAttrs> {
    private val mStockAttrs: StockChartAttrs
    val valueFormatter: T
    private lateinit var  mLineChartPaint: Paint
    constructor( mStockAttrs: StockChartAttrs, valueFormatter:T):super(mStockAttrs, valueFormatter){
        this.mStockAttrs = mStockAttrs
        this.valueFormatter = valueFormatter
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

    fun <T:YAxis>drawStockChart(canvas: Canvas, parent: RecyclerView, yAxis: T, attacheYAxis: StockYAxis) {
        val parentRight = parent.right.toFloat()
        val parentLeft = parent.left.toFloat()
        val childCount = parent.childCount
        val adapter = parent.adapter as BaseBarChartAdapter<StockEntry, YAxis>
        val entryList: List<StockEntry> = adapter.entries
        drawAttacheTextAndDivide(canvas, parent, parentLeft, parentRight)
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val stockEntry = child.tag as StockEntry
            val rectMain = getStockRectF(child, parent, yAxis, mStockAttrs, stockEntry)

            val color = if (stockEntry.isRise) mStockAttrs.riseColor else mStockAttrs.downColor
            mBarChartPaint.color = color
            //todo 注意RTL
            drawChart(canvas, rectMain, parentLeft, parentRight, 1f)
            mHighLightLinePaint.color = color
            if (stockEntry.mHigh > Math.max(stockEntry.mClose, stockEntry.mOpen)){
                drawTopLine(stockEntry.mHigh, canvas, rectMain, yAxis, parent)
            }
            if (stockEntry.mLow < Math.min(stockEntry.mClose, stockEntry.mOpen)){
                drawDownLine(stockEntry.mLow, canvas, rectMain, yAxis, parent)
            }
            drawAvgLine(canvas, parent, yAxis, i, parentLeft, parentRight, rectMain, entryList, stockEntry, childCount, AvgType.Avg5Type)
            drawAvgLine(canvas, parent, yAxis, i, parentLeft, parentRight, rectMain, entryList, stockEntry, childCount, AvgType.Avg10Type)
            drawAvgLine(canvas, parent, yAxis, i, parentLeft, parentRight, rectMain, entryList, stockEntry, childCount, AvgType.Avg20Type)

            // draw Volume
            val rectAttache = getAttacheStockRectF(child, parent, attacheYAxis, mStockAttrs, stockEntry)
            drawChart(canvas, rectAttache, parentLeft, parentRight, 1f)
        }
    }

    private fun drawAttacheTextAndDivide(canvas: Canvas, parent: RecyclerView, parentStart: Float, parentEnd: Float){
        val yDivideTop = parent.bottom - parent.paddingBottom - mStockAttrs.contentPaddingBottom
        val yDivideBottom = parent.bottom - parent.paddingBottom - mStockAttrs.contentPaddingBottom + 25.dp
        mLineChartPaint.color = mStockAttrs.yAxisLineColor
        mLineChartPaint.strokeWidth = 0.75f
        canvas.drawLine(parentStart, yDivideBottom, parentEnd, yDivideBottom, mLineChartPaint)
        val volumeStr = "成交量:9399万股"
        val txtWidth = mHighLightDescPaint.measureText(volumeStr)
        val rectLeft = parent.left + 5.dpf
        val rectF = RectF(rectLeft, yDivideTop, rectLeft + txtWidth, yDivideBottom)
        val baseY = TextUtil.getTextBaseY(rectF, mHighLightDescPaint)
        mHighLightDescPaint.color = mStockAttrs.xAxisTxtColor
        mHighLightDescPaint.textSize = mStockAttrs.xAxisTxtSize
        canvas.drawText(volumeStr, rectLeft, baseY, mHighLightDescPaint)
    }


    private fun drawAvgLine(
        canvas: Canvas,
        parent: RecyclerView,
        mYAxis: YAxis,
        i: Int,
        parentStart: Float,
        parentEnd: Float,
        rectF:RectF,
        entryList: List<StockEntry>,
        stockEntry:StockEntry,
        childCount: Int, avgType: AvgType) {
        val child = parent.getChildAt(i)
        val adapterPosition = parent.getChildAdapterPosition(child)
        val viewWidth = child.width
        val avgValue = getAvgValue(avgType, stockEntry)
        if (DecimalUtil.smallOrEquals(avgValue, 0f)){
            return
        }
        val yPointF = getYPosition(avgValue, parent, mYAxis, mStockAttrs)
        val pointF2 = PointF(rectF.centerX(), yPointF)
        //pointF2 即为当前for 循环到的点，从左往右一共涉及到4个点 pointF0, pointF1, pointF2, pointF3; 其中 pointF1、pointF2为显示的。
        if (i < childCount - 1) {
            val pointF1Child = parent.getChildAt(i + 1)
            val barEntryLeft = pointF1Child.tag as StockEntry
            val avgValue2 = getAvgValue(avgType, barEntryLeft)
            if (DecimalUtil.smallOrEquals(avgValue, 0f)){
                return
            }
            val yPointFLeft = getYPosition(avgValue2, parent, mYAxis, mStockAttrs)
            val pointF1 = PointF(rectF.centerX() - viewWidth, yPointFLeft)

            if (pointF1.x >= parentStart && pointF2.x <= parentEnd) {
                val pointsOut = floatArrayOf(pointF1.x, pointF1.y, pointF2.x, pointF2.y)
                drawChartLine(canvas, pointsOut, avgType)
//                drawFill(parent, mStockAttrs, canvas, pointF1, pointF2, rectF.bottom)
                if (pointF1Child.left < parentEnd) { //左边界，处理pointF1值显示出来了的情况。
                    if (adapterPosition + 2 < entryList.size) {
                        val pointF0: PointF = createNearPoint(avgType, parent, mStockAttrs, entryList[adapterPosition + 2], pointF1, viewWidth, mYAxis, true)
//                        drawLineLeftBoundary(parent, canvas, pointF0, pointF1, parentEnd, avgType)
                    }
                } else if (child.right < parentStart && parentStart - child.right <= child.width) {
                    //右边界处理情况，pointF3显示出来跟没有显示出来。
                    if (adapterPosition - 1 > 0) {
                        val pointF3: PointF = createNearPoint(avgType, parent, mStockAttrs, entryList[adapterPosition - 1], pointF2,  viewWidth, mYAxis, false)
                        if (pointF3.x > parentStart) {
//                            drawLineRightBoundary(parent, canvas, pointF2, pointF3, parentStart, avgType)
                        } else if (pointF3.x < parentStart) {
                            if (adapterPosition - 2 > 0) {
                                val pointF4: PointF = createNearPoint(avgType, parent, mStockAttrs, entryList[adapterPosition - 2], pointF3,  viewWidth, mYAxis, false)
//                                drawLineRightBoundary(parent, canvas, pointF3, pointF4, parentStart, avgType)
                            }
                        }
                    }
                }
            } else if (pointF1.x < parentEnd && pointF1Child.right >= parentEnd) { //左边界，处理pointF1值没有显示出来
//                drawLineLeftBoundary(parent, canvas, pointF1, pointF2, parentEnd, avgType)
            }
        }
    }

    private fun drawLineLeftBoundary(
        parent: RecyclerView, canvas: Canvas, pointLeft: PointF,
        pointRight: PointF, crossX: Float, avgType: AvgType
    ) {
        val pointF = ChartComputeUtil.getInterceptPointF(pointLeft, pointRight, crossX)
        val points = floatArrayOf(pointF.x, pointF.y, pointRight.x, pointRight.y)
        drawChartLine(canvas, points, avgType)
//        drawFill(parent, mLineChartAttrs, canvas, pointF, pointRight, yZeroLine)
    }

    private fun drawLineRightBoundary(
        parent: RecyclerView, canvas: Canvas, pointLeft: PointF,
        pointRight: PointF, parentEnd: Float, avgType: AvgType
    ) {
        val pointFInterceptInner = ChartComputeUtil.getInterceptPointF(pointLeft, pointRight, parentEnd)
        val pointsInner = floatArrayOf(
            pointLeft.x, pointLeft.y, pointFInterceptInner.x,
            pointFInterceptInner.y
        )
        drawChartLine(canvas, pointsInner, avgType)
//        drawFill(parent, mLineChartAttrs, canvas, pointLeft, pointFInterceptInner, yZeroLine)
    }

    private fun drawChartLine(canvas: Canvas, points: FloatArray, avgType: AvgType) {
        val color: Int = mLineChartPaint.color
        mLineChartPaint.color = getAvgColor(avgType, mStockAttrs)
        mLineChartPaint.strokeWidth = dip2px(1f).toFloat()
        canvas.drawLines(points, mLineChartPaint)
        mLineChartPaint.color = color
    }

    //绘制上阴线、下阴线
    private fun drawTopLine(value:Float, canvas: Canvas, rectF: RectF, yAxis: YAxis,  parent: RecyclerView){
        canvas.save()
        val y = getYPosition(value, parent, yAxis, mStockAttrs)
        val x = rectF.centerX()
        if (rectF.left > parent.left.toFloat() && rectF.right < parent.right.toFloat()){
            canvas.drawLine(x, rectF.top, x, y, mHighLightLinePaint)
            canvas.restore()
        }
    }

    private fun drawDownLine(value:Float, canvas: Canvas, rectF: RectF, yAxis: YAxis,  parent: RecyclerView){
        canvas.save()
        val y = getYPosition(value, parent, yAxis, mStockAttrs)
        val x = rectF.centerX()
        if (rectF.left > parent.left.toFloat() && rectF.right < parent.right.toFloat()){
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
            canvas.drawPath(path, mBarChartPaint)
        }
    }

    //绘制选中时 highLight 标线及浮框。
    fun drawHighLight(canvas: Canvas, parent: RecyclerView, yAxis: YAxis) {
        if (mStockAttrs.enableValueMark) {
            val parentTop = parent.paddingTop.toFloat()
            val childCount = parent.childCount
            val contentRight = (parent.width - parent.paddingRight).toFloat()
            val contentBottom: Float = parent.height - parent.paddingBottom - mStockAttrs.contentPaddingBottom
            val attacheTop = parent.bottom - parent.paddingBottom - mStockAttrs.contentPaddingBottom + 25.dp
            val attacheBottom = parent.bottom - parent.paddingBottom - 18.dpf
            val contentLeft = parent.paddingLeft.toFloat()
            var child: View
            for (i in 0 until childCount) {
                child = parent.getChildAt(i)
                val barEntry = child.tag as StockEntry
                val width = child.width.toFloat()
                val childCenter = child.left + width / 2
                val valueStr = mHighLightValueFormatter.getBarLabel(barEntry)
                val points = floatArrayOf(childCenter, contentBottom, childCenter, parentTop)
                val pointsAttache = floatArrayOf(childCenter, attacheBottom, childCenter, attacheTop)
                if (barEntry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    val chartColor: Int = mStockAttrs.highLightColor
                    drawHighLightLine(canvas, points, barChartColor = chartColor)
                    drawHighLightLine(canvas, pointsAttache, barChartColor = chartColor)
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
        val fontMetrics: Paint.FontMetrics = mHighLightDescPaint.getFontMetrics()
        mHighLightDescPaint.color = mStockAttrs.highLightColor
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