package com.xiaomi.fitness.chart.render

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.xiaomi.fitness.chart.attrs.BaseChartAttrs
import com.xiaomi.fitness.chart.component.BaseYAxis
import com.xiaomi.fitness.chart.component.YAxis
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry
import com.xiaomi.fitness.chart.util.ChartComputeUtil
import com.xiaomi.fitness.common.utils.AppUtil.isRTLDirection
import com.xiaomi.fitness.common.utils.DisplayUtil
import com.xiaomi.fitness.common.utils.TextUtil
import android.R.attr.dashWidth
import android.graphics.*


class YAxisRender<T : BaseYAxis?, V : BaseChartAttrs?>(protected var mBarChartAttrs: V) {
    private lateinit var mLinePaint: Paint
    private lateinit var mTextPaint: Paint
    private fun initTextPaint() {
        mTextPaint = Paint()
        mTextPaint.reset()
        mTextPaint.isAntiAlias = true
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.color = mBarChartAttrs!!.yAxisLabelTxtColor
        mTextPaint.textSize = mBarChartAttrs!!.yAxisLabelTxtSize
    }

    private fun initPaint() {
        mLinePaint = Paint()
        mLinePaint.reset()
        mLinePaint.isAntiAlias = true
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.strokeWidth = 0.75f
        mLinePaint.color = mBarChartAttrs!!.yAxisLineColor
    }

    //绘制 Y轴刻度线 横的网格线
    fun drawHorizontalLine(canvas: Canvas, parent: RecyclerView, yAxis: T) {
        if (yAxis==null)return
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val distance = bottom - mBarChartAttrs!!.contentPaddingBottom - mBarChartAttrs!!.contentPaddingTop - top
        val lineNums = yAxis.labelCount
        val lineDistance = distance / lineNums
        var gridLine = top + mBarChartAttrs!!.contentPaddingTop
        for (i in 0..lineNums) {
            if (i > 0) {
                gridLine += lineDistance
            }
            val path = Path()
            var enable = if (i == lineNums) {
                mBarChartAttrs!!.enableYAxisZero
            } else {
                mBarChartAttrs!!.enableYAxisGridLine //允许画 Y轴刻度
            }
            path.moveTo(left.toFloat(), gridLine)
            path.lineTo(right.toFloat(), gridLine)
            if (enable) {
                if(mBarChartAttrs!!.enableYAxisLineDash) mLinePaint.pathEffect = DashPathEffect(floatArrayOf(DisplayUtil.dip2pxF(4f), DisplayUtil.dip2pxF(1.5f)), 0f)
                canvas.drawPath(path, mLinePaint)
            }
        }
    }

    fun drawYAxisLabel(canvas: Canvas, parent: RecyclerView, yAxis: T) {
        if (isRTLDirection()) {
            if (mBarChartAttrs!!.enableStartYAxisLabel) {
                drawRightYAxisLabel(canvas, parent, yAxis)
            }
            if (mBarChartAttrs!!.enableEndYAxisLabel) {
                drawLeftYAxisLabel(canvas, parent, yAxis)
            }
        } else {
            if (mBarChartAttrs!!.enableStartYAxisLabel) {
                drawLeftYAxisLabel(canvas, parent, yAxis)
            }
            if (mBarChartAttrs!!.enableEndYAxisLabel) {
                drawRightYAxisLabel(canvas, parent, yAxis)
            }
        }
    }

    //绘制左边的刻度
    @JvmOverloads
    fun drawLeftYAxisLabel(canvas: Canvas, parent: RecyclerView, yAxis: T, drawText: Boolean = true) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        //            String longestStr = yAxis.getLongestLabel();
//            float yAxisWidth = mTextPaint.measureText(longestStr) + mBarChartAttrs.recyclerPaddingLeft + 2 * yAxis.labelHorizontalPadding;
//            int paddingLeft = computeYAxisWidth(parent.getPaddingLeft(), yAxisWidth);
        val paddingLeft = parent.paddingLeft
//        //设置 recyclerView的 BarChart 内容区域
//        parent.setPadding(paddingLeft, parent.paddingTop, parent.paddingRight, parent.paddingBottom)
        val topLocation = top + mBarChartAttrs!!.contentPaddingTop
        val containerHeight = bottom - mBarChartAttrs!!.contentPaddingBottom - topLocation
        val itemHeight = containerHeight / yAxis!!.labelCount
        val yAxisScaleMap = yAxis.getYAxisScaleMap(topLocation, itemHeight, yAxis.labelCount)
        var i = 0
        for ((yAxisScaleLocation, yAxisScaleValue) in yAxisScaleMap) {
            i++
            val labelStr = yAxis.valueFormatter.getFormattedValue(yAxisScaleValue, yAxis)
            val txtY = yAxisScaleLocation + yAxis.labelVerticalPadding
            val txtX = paddingLeft - mTextPaint.measureText(labelStr) - yAxis.labelHorizontalPadding
            if (i <= yAxisScaleMap.size && drawText) {
                canvas.drawText(labelStr, txtX, txtY, mTextPaint)
            }
        }
    }

    @JvmOverloads
    fun drawRightYAxisLabel(canvas: Canvas, parent: RecyclerView, yAxis: T, drawText: Boolean = true) {
        val right = parent.width
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
//        val paddingRight = DisplayUtil.dip2px(40f)
//        val paddingLeft = DisplayUtil.dip2px(30f)
        //设置 recyclerView的 BarChart 内容区域
//        parent.setPadding(parent.paddingLeft, parent.paddingTop, paddingRight, parent.paddingBottom)
        val topLocation = top + mBarChartAttrs!!.contentPaddingTop
        val containerHeight = bottom - mBarChartAttrs!!.contentPaddingBottom - topLocation
        val itemHeight = containerHeight / yAxis!!.labelCount
        val yAxisScaleMap = yAxis.getYAxisScaleMap(topLocation, itemHeight, yAxis.labelCount)
        val txtX = right - parent.paddingRight + yAxis.labelHorizontalPadding
        var i = 0
        for ((yAxisScaleLocation, yAxisScaleValue) in yAxisScaleMap) {
            i++
            val labelStr = yAxis.valueFormatter.getFormattedValue(yAxisScaleValue, yAxis)
            val txtY = yAxisScaleLocation + yAxis.labelVerticalPadding
            if (i <= yAxisScaleMap.size && drawText) {
                canvas.drawText(labelStr, txtX, txtY, mTextPaint)
            }
        }
    }

    fun drawStandardLines(canvas: Canvas, parent: RecyclerView, yAxis: YAxis) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val highStandardLine = mBarChartAttrs!!.yAxisHighStandardLine
        val middleStandardLine = mBarChartAttrs!!.yAxisMiddleStandardLine
        val lowStandardLine = mBarChartAttrs!!.yAxisLowStandardLine
        val rightRectF = right - DisplayUtil.dip2px(8f).toFloat()
        if (highStandardLine != -1f) {
            val highPosition = ChartComputeUtil.getYPosition<RecyclerBarEntry>(highStandardLine, parent, yAxis, mBarChartAttrs)
            drawLine(canvas, left.toFloat(), right.toFloat(), highPosition)
            drawStandardLineLabel(canvas, yAxis, highStandardLine, highPosition, rightRectF)
        }
        if (middleStandardLine != -1f && middleStandardLine < yAxis.mAxisMaximum) {
            val middlePosition = ChartComputeUtil.getYPosition<RecyclerBarEntry>(middleStandardLine, parent, yAxis, mBarChartAttrs)
            drawLine(canvas, left.toFloat(), right.toFloat(), middlePosition)
            drawStandardLineLabel(canvas, yAxis, middleStandardLine, middlePosition, rightRectF)
        }
        if (lowStandardLine != -1f && lowStandardLine < yAxis.mAxisMaximum) {
            val lowPosition = ChartComputeUtil.getYPosition<RecyclerBarEntry>(lowStandardLine, parent, yAxis, mBarChartAttrs)
            drawLine(canvas, left.toFloat(), right.toFloat(), lowPosition)
            drawStandardLineLabel(canvas, yAxis, lowStandardLine, lowPosition, rightRectF)
        }
    }

    private fun drawStandardLineLabel(canvas: Canvas, yAxis: YAxis, standardLineValue: Float, standardYPosition: Float, rightRectF: Float) {
        val textHeight = TextUtil.getTxtHeight1(mTextPaint)
        val labelStr = yAxis.valueFormatter.getFormattedValue(standardLineValue, yAxis)
        val textWidth = mTextPaint.measureText(labelStr)
        val textRectF = RectF()
        val rectFBottom = standardYPosition - DisplayUtil.dip2px(10f)
        val rectFTop = rectFBottom - textHeight
        val rectLeft = rightRectF - textWidth
        textRectF[rectLeft, rectFTop, rightRectF] = rectFBottom
        val fontMetrics = mTextPaint.fontMetrics
        val baseYLine = textRectF.bottom - (fontMetrics.top + fontMetrics.bottom) / 2
        canvas.drawText(labelStr, textRectF.left, baseYLine, mTextPaint)
    }

    private fun drawLine(canvas: Canvas, left: Float, right: Float, yPosition: Float) {
        val path = Path()
        path.moveTo(left, yPosition)
        path.lineTo(right, yPosition)
        canvas.drawPath(path, mLinePaint)
    }

    private fun computeYAxisWidth(originPadding: Int, yAxisWidth: Float): Int {
        val resultPadding: Float
        Log.d("YAxis1", "originPadding:$originPadding yAxisWidth:$yAxisWidth")
        resultPadding = if (originPadding > yAxisWidth) {
            val distance = originPadding - yAxisWidth
            if (distance > DisplayUtil.dip2px(8f)) {
                Log.d("YAxis", "if control originPadding:$originPadding yAxisWidth:$yAxisWidth")
                yAxisWidth //实际需要的跟原来差8dp了就用，实际测量的，否则就用原来的
            } else {
                Log.d("YAxis", "else control originPadding:$originPadding yAxisWidth:$yAxisWidth")
                originPadding.toFloat()
            }
        } else { //原来设定的 padding 不够用
            Log.d("YAxis", "control originPadding:$originPadding yAxisWidth:$yAxisWidth")
            yAxisWidth
        }
        return resultPadding.toInt()
    }

    init {
        initPaint()
        initTextPaint()
    }
}