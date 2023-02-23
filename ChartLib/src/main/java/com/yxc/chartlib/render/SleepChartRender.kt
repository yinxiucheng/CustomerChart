package com.xiaomi.fitness.chart.render

import android.graphics.*
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xiaomi.fitness.chart.R
import com.xiaomi.fitness.chart.attrs.SleepChartAttrs
import com.xiaomi.fitness.chart.entrys.SleepItemEntry
import com.xiaomi.fitness.chart.entrys.model.SleepItemTime
import com.xiaomi.fitness.chart.formatter.DefaultHighLightMarkValueFormatter
import com.xiaomi.fitness.chart.formatter.ValueFormatter
import com.xiaomi.fitness.common.utils.*
import com.xiaomi.fitness.common.utils.DisplayUtil.dip2px

class SleepChartRender(
    private val mChartAttrs: SleepChartAttrs,
    highLightValueFormatter: ValueFormatter
) {
    private lateinit var mBarChartPaint: Paint
    private lateinit var mTextPaint: Paint
    private lateinit var mHighLightValueBigPaint: Paint
    private lateinit var mHighLightDescPaint: Paint
    private lateinit var mLinePaint: Paint//竖线 横线
    private lateinit var mHighLightLinePaint: Paint
    private var mHighLightValueFormatter: ValueFormatter
    private var mSleepStageSize: Int = 4

    init {
        initBarChartPaint()
        initTextPaint()
        initLinePaint()
        initHighLightPaint()
        mHighLightValueFormatter = highLightValueFormatter
    }

    private fun initLinePaint() {
        mLinePaint = Paint()
        mLinePaint.reset()
        mLinePaint.isAntiAlias = true
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.strokeWidth = 1f
        mLinePaint.color = mChartAttrs!!.yAxisLineColor
    }

    private fun initTextPaint() {
        mTextPaint = Paint()
        mTextPaint.reset()
        mTextPaint.isAntiAlias = true
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = mChartAttrs.txtSize
        mTextPaint.color = mChartAttrs.txtColor
    }

    private fun initBarChartPaint() {
        mBarChartPaint = Paint()
        mBarChartPaint.reset()
        mBarChartPaint.isAntiAlias = true
        mBarChartPaint.style = Paint.Style.FILL
        mBarChartPaint.color = mChartAttrs.deepSleepColor
    }

    private fun initHighLightPaint() {
        //duration
        mHighLightValueBigPaint = Paint()
        mHighLightValueBigPaint.reset()
        mHighLightValueBigPaint.isAntiAlias = true
        mHighLightValueBigPaint.color = ColorUtil.getResourcesColor(R.color.text_color)
        mHighLightValueBigPaint.textSize = DisplayUtil.sp2px(24f).toFloat()
        mHighLightValueBigPaint.setTypeface(Typeface.create("mipro-regular", Typeface.BOLD))

        //time desc
        mHighLightDescPaint = Paint()
        mHighLightDescPaint.reset()
        mHighLightDescPaint.isAntiAlias = true
        mHighLightDescPaint.color = ColorUtil.getResourcesColor(R.color.text_color_40)
        mHighLightDescPaint.textSize = DisplayUtil.dip2px(12f).toFloat()
        mHighLightDescPaint.typeface = Typeface.create("mipro-regular", Typeface.NORMAL)

        //line
        mHighLightLinePaint = Paint()
        mHighLightLinePaint.reset()
        mHighLightLinePaint.isAntiAlias = true
        mHighLightLinePaint.style = Paint.Style.STROKE
        mHighLightLinePaint.strokeWidth = 3f
        mHighLightLinePaint.color = mChartAttrs!!.yAxisLineColor
    }

    fun setHighLightValueFormatter(highLightValueFormatter: ValueFormatter) {
        mHighLightValueFormatter = highLightValueFormatter
    }

    fun drawLines(canvas: Canvas, parent: RecyclerView) {
        drawVerticalLines(canvas, parent)
        drawHorizontalDashLines(canvas, parent)
    }

    private fun drawVerticalLines(canvas: Canvas, parent: RecyclerView) {
        var top = parent.paddingTop
        var bottom = parent.height - parent.paddingBottom - mChartAttrs.contentPaddingBottom
        var start = parent.paddingLeft
        var end = parent.right - parent.paddingRight
        mLinePaint.pathEffect = null

        val startPath = Path()
        startPath.moveTo(start.toFloat(), bottom)
        startPath.lineTo(start.toFloat(), top.toFloat())
        canvas.drawPath(startPath, mLinePaint)

        val endPath = Path()
        endPath.moveTo(end.toFloat(), bottom)
        endPath.lineTo(end.toFloat(), top.toFloat())
        canvas.drawPath(endPath, mLinePaint)
    }

    private fun drawHorizontalDashLines(canvas: Canvas, parent: RecyclerView) {
        var bottom = parent.height - parent.paddingBottom - mChartAttrs.contentPaddingBottom
        val distanceHeight = mChartAttrs.sleepItemHeight
        var start = parent.paddingLeft
        var end = parent.right - parent.paddingRight
        var itemHeight: Float
        mLinePaint.pathEffect =
            DashPathEffect(floatArrayOf(DisplayUtil.dip2pxF(4f), DisplayUtil.dip2pxF(1.5f)), 0f)
        for (i in 0..mSleepStageSize) {
            itemHeight = bottom - distanceHeight * i
            val path = Path()
            path.moveTo(start.toFloat(), itemHeight)
            path.lineTo(end.toFloat(), itemHeight)
            canvas.drawPath(path, mLinePaint)
        }
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    fun drawSleepChart(canvas: Canvas, parent: RecyclerView) {
        val parentEnd = parent.width - parent.paddingEnd.toFloat()
        val parentStart = parent.paddingStart.toFloat()
        val contentWidth = parent.width - parent.paddingStart - parent.paddingEnd.toFloat()
        val chartTop = parent.top + parent.paddingTop.toFloat()
        val childCount = parent.childCount
        var sumWidth = 0f
        var latestSleepEntry: SleepItemEntry? = null
        var longestSleepEntry: SleepItemEntry? = null
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val sleepEntry = child.tag as SleepItemEntry
            val sleepTime = sleepEntry.sleepItemTime
            if (i == 0) {
                latestSleepEntry = sleepEntry
            }
            if (i == childCount - 1) {
                longestSleepEntry = sleepEntry
            }
            var end = parentEnd - sumWidth
            var start = end - child.width
            if (AppUtil.isRTLDirection()) {
                start = parentStart + sumWidth
                end = start + child.width
            }
            sumWidth += child.width
            drawSleepChartItem(canvas, parent, sleepTime, start, end)
        }
        if (sumWidth < contentWidth) { //补一段填满。
            val colorOrigin = mBarChartPaint.color
            val end = parentStart + (contentWidth - sumWidth)
            val rectFBottom = getSleepRectBottom(SleepItemTime.TYPE_NULL, chartTop)
            mBarChartPaint.color = mChartAttrs.weakColor
            drawRectF(canvas, mBarChartPaint, parentStart, rectFBottom, end, rectFBottom)
            mBarChartPaint.color = colorOrigin
        }
        drawXAxisLabel(parent, longestSleepEntry, canvas, latestSleepEntry)
    }

    private fun drawXAxisLabel(
        parent: RecyclerView,
        longestSleepEntry: SleepItemEntry?,
        canvas: Canvas,
        latestSleepEntry: SleepItemEntry?,
    ) {
        val parentEnd = parent.width - parent.paddingEnd.toFloat()
        val parentStart = parent.paddingStart.toFloat()
        //xAxis
        var bottom = parent.height - parent.paddingBottom - mChartAttrs.contentPaddingBottom
        var textBottomBaseLineY = bottom + 64
        if (!AppUtil.isRTLDirection()) {
            if (longestSleepEntry?.sleepItemTime != null) {
                val leftStr = TimeDateUtil.getDateStr(
                    longestSleepEntry.sleepItemTime.startTimestamp,
                    "HH:mm"
                )
                canvas.drawText(leftStr, parentStart, textBottomBaseLineY, mTextPaint)
            }
            if (latestSleepEntry?.sleepItemTime != null) {
                val rightStr = TimeDateUtil.getDateStr(
                    latestSleepEntry.sleepItemTime.endTimestamp,
                    "HH:mm"
                )
                canvas.drawText(
                    rightStr,
                    parentEnd - mTextPaint.measureText(rightStr),
                    textBottomBaseLineY,
                    mTextPaint
                )
            }
        }else{
            if (latestSleepEntry?.sleepItemTime != null) {
                val leftStr = TimeDateUtil.getDateStr(
                    latestSleepEntry.sleepItemTime.endTimestamp,
                    "HH:mm"
                )
                canvas.drawText(leftStr, parentStart, textBottomBaseLineY, mTextPaint)
            }
            if (longestSleepEntry?.sleepItemTime != null) {
                val rightStr = TimeDateUtil.getDateStr(
                    longestSleepEntry.sleepItemTime.startTimestamp,
                    "HH:mm"
                )
                canvas.drawText(
                    rightStr,
                    parentEnd - mTextPaint.measureText(rightStr),
                    textBottomBaseLineY,
                    mTextPaint
                )
            }
        }
    }

    private fun drawSleepChartItem(
        canvas: Canvas, parent: RecyclerView,
        sleepItemTime: SleepItemTime,
        start: Float,
        end: Float
    ) {
        val bottom = parent.height - parent.paddingBottom - mChartAttrs.contentPaddingBottom
        val distanceHeight = mChartAttrs.sleepItemHeight
        val color = sleepItemTime.getChartColor(mChartAttrs)
        val colorOrigin = mBarChartPaint.color
        mBarChartPaint.color = color
        val rectFBottom = getSleepRectBottom(sleepItemTime.chartHeightIndex, bottom)
        if (sleepItemTime.sleepType != SleepItemTime.TYPE_NULL) {
            var rectTop = rectFBottom - distanceHeight
            drawRectF(canvas, mBarChartPaint, start, rectFBottom, end, rectTop)
        }
        mBarChartPaint.color = colorOrigin
    }

    private fun getSleepRectBottom(sleepHeightIndex: Int, parentBottom: Float): Float {
        val distanceHeight = mChartAttrs.sleepItemHeight
        return if (sleepHeightIndex == SleepItemTime.TYPE_NULL) {
            parentBottom
        } else parentBottom - (mSleepStageSize - sleepHeightIndex - 1) * distanceHeight
    }

    private fun drawRectF(
        canvas: Canvas,
        paint: Paint?,
        start: Float,
        bottom: Float,
        right: Float,
        top: Float
    ) {
        val rectF = RectF(start, top, right, bottom)
        canvas.drawRect(rectF, paint!!)
    }

    //绘制选中时 highLight 标线及浮框。
    fun drawHighLight(canvas: Canvas, parent: RecyclerView) {
        if (mChartAttrs.enableValueMark) {
            val bottom = parent.height - parent.paddingBottom - mChartAttrs.contentPaddingBottom
            val childCount = parent.childCount
            var child: View
            for (i in 0 until childCount) {
                child = parent.getChildAt(i)
                val barEntry = child.tag as SleepItemEntry
                val sleepItemTime = barEntry.sleepItemTime
                val width = child.width.toFloat()
                val selectItemBottom = getSleepRectBottom(sleepItemTime.chartHeightIndex, bottom)
                val selectItemTop = selectItemBottom - mChartAttrs.sleepItemHeight
                val childCenter = child.left + width / 2
                val valueStr = mHighLightValueFormatter.getBarLabel(barEntry)
                if (barEntry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    drawHighLightRect(canvas, valueStr, childCenter, selectItemTop, parent)
                }
            }
        }
    }

    private fun drawHighLightRect(
        canvas: Canvas, valueStr: String, childCenter: Float, selectItemTop: Float,
        parent: RecyclerView
    ) {
        val parentBottom = parent.height - parent.paddingBottom - mChartAttrs.contentPaddingBottom
        val contentRight = parent.width - parent.paddingRight.toFloat()
        val contentLeft = parent.paddingLeft.toFloat()
        val leftEdgeDistance = Math.abs(childCenter - contentLeft)
        val rightEdgeDistance = Math.abs(contentRight - childCenter)
        val startPadding = dip2px(16f)
        val endPadding = startPadding
        val strings: List<String> =
            valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_VALUE_STR)
        if (strings.isEmpty() || strings.size < 2)
            return

        val finalValueStr = strings[0] //10时48分
        val finalDescStr = strings[1] //00:000-1:00

        //计算宽度和高度
        val measureValueText: Float = mHighLightValueBigPaint.measureText(finalValueStr)
        val measureDescText: Float = mHighLightDescPaint.measureText(finalDescStr)
        val txtWidth = Math.max(measureValueText, measureDescText) + startPadding + endPadding
        val halfWidth = txtWidth / 2.0f

        val rectFHeight = dip2px(59f)
        val rectBottom: Float = parentBottom - 30 - mSleepStageSize * mChartAttrs.sleepItemHeight
        val rectTop = rectBottom - rectFHeight

        //绘制RectF
        val rectF = RectF()
        mBarChartPaint.color = ColorUtil.getResourcesColor(R.color.chart_selected_prompt_bg_color)
        val radius = mChartAttrs.highLightRoundRectRadius
        if (leftEdgeDistance <= halfWidth) { //矩形框靠左对齐
            rectF.set(contentLeft, rectTop, contentLeft + txtWidth, rectBottom)
        } else if (rightEdgeDistance <= halfWidth) { //矩形框靠右对齐
            rectF.set(contentRight - txtWidth, rectTop, contentRight, rectBottom)
        } else { //居中对齐。
            rectF.set(childCenter - halfWidth, rectTop, childCenter + halfWidth, rectBottom)
        }
        canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint)

        //绘文字
        mHighLightValueBigPaint.textAlign = Paint.Align.LEFT
        mHighLightDescPaint.textAlign = Paint.Align.LEFT
        val rectMarginBottom = dip2px(8f).toFloat()

        val baseLineDescPaint = rectF.bottom - rectMarginBottom - 8
        val baseLineBigPaint: Float =
            baseLineDescPaint - TextUtil.getTxtHeight1(mHighLightDescPaint) - 8
        canvas.drawText(
            finalValueStr,
            rectF.left + startPadding,
            baseLineBigPaint,
            mHighLightValueBigPaint
        )
        canvas.drawText(
            finalDescStr,
            rectF.left + startPadding,
            baseLineDescPaint,
            mHighLightDescPaint
        )
        val points = floatArrayOf(childCenter, selectItemTop, childCenter, rectBottom)
        canvas.drawLines(points, mHighLightLinePaint)
    }

    fun updateStageSize(size: Int) {
        mSleepStageSize = size
    }
}