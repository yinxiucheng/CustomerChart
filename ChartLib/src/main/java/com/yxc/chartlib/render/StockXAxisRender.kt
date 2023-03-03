package com.yxc.chartlib.render

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.component.XAxis
import com.yxc.chartlib.formatter.ValueFormatter
import com.yxc.chartlib.utils.AppUtil.isRTLDirection
import com.yxc.chartlib.utils.DecimalUtil
import com.yxc.chartlib.utils.TextUtil.getTextBaseY
import com.yxc.chartlib.utils.TextUtil.getTxtHeight1
import com.yxc.chartlib.utils.TextUtil.setTypeface
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

/**
 * @author xiuchengyin
 *
 * @date 2023/3/3
 *
 */
class StockXAxisRender : XAxisRender<StockChartAttrs> {

    constructor(attrs: StockChartAttrs) : super(attrs)

    override fun drawXAxis(canvas: Canvas, parent: RecyclerView, xAxis: XAxis) {
        if (!mBarChartAttrs.enableXAxisLabel) {
            return
        }
        val txtHeight = getTxtHeight1(mTextPaint)
        val parentBottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        mTextPaint.color = xAxis.textColor
        val parentStart =
            if (isRTLDirection()) parent.width - parent.paddingLeft else parent.paddingLeft
        val parentEnd =
            if (isRTLDirection()) parent.paddingRight else parent.width - parent.paddingRight
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val xStart = if (isRTLDirection()) child.right else child.left
            val xEnd = if (isRTLDirection()) child.left else child.right
            val barEntry = child.tag as RecyclerBarEntry
            val formatter = xAxis.valueFormatter
            var dateStr = ""
            if (formatter is ValueFormatter) {
                dateStr = getXAxisLabel(barEntry, formatter)
            }
            if (!TextUtils.isEmpty(dateStr)) {
                val childWidth = child.width
                val txtWidth = mTextPaint.measureText(dateStr)
                val distance = childWidth - txtWidth
                var txtXStart = if (isRTLDirection()) xStart - distance / 2 else xStart + distance / 2
                var txtXEnd = if (isRTLDirection()) txtXStart - txtWidth else txtXStart + txtWidth

                //RTL 是从左到右 ， LTR是 右到左， 都是从坐标端开始。
                if (isRTLDirection()) {
                    if (mBarChartAttrs.xAxisLabelPosition == XAxis.POSITION_LEFT) {
                        txtXEnd = xEnd.toFloat()
                        txtXStart = xEnd - distance
                    } else if (mBarChartAttrs.xAxisLabelPosition == XAxis.POSITION_RIGHT) {
                        txtXStart = xStart.toFloat()
                        txtXEnd = txtXStart + txtWidth
                    }
                } else {
                    if (mBarChartAttrs.xAxisLabelPosition == XAxis.POSITION_LEFT) {
                        txtXStart = xStart.toFloat()
                        txtXEnd = txtXStart + txtWidth
                    } else if (mBarChartAttrs.xAxisLabelPosition == XAxis.POSITION_RIGHT) {
                        txtXEnd = xEnd.toFloat()
                        txtXStart = xEnd - distance
                    }
                }
                val txtTop = parentBottom - txtHeight
                val txtBottom = txtTop + txtHeight
                val length = dateStr.length
                val startRect = if (isRTLDirection()) txtXEnd else txtXStart
                val endRect = if (isRTLDirection()) txtXStart else txtXEnd
                val rectF = RectF(startRect, txtTop, endRect, txtBottom)
                val baseLineY = getTextBaseY(rectF, mTextPaint)
                //设置字体
                if (mBarChartAttrs.xAxisLabelFont != -1) {
                    setTypeface(mTextPaint, mBarChartAttrs.xAxisLabelFont)
                }
                if (isRTLDirection()) {
                    if (DecimalUtil.smallOrEquals(txtXEnd, parentStart) && DecimalUtil.bigOrEquals(
                            txtXStart,
                            parentEnd.toFloat()
                        )
                    ) { //中间位置
                        canvas.drawText(dateStr, rectF.left, baseLineY, mTextPaint)
                    } else if (txtXStart > parentStart && txtXEnd < parentStart) { //处理左边界
                        val displayLength =
                            ((parentStart - txtXEnd + 1) / txtWidth * length).toInt()
                        var endIndex = displayLength
                        if (endIndex < length) {
                            endIndex += 1
                        }
                        canvas.drawText(dateStr, 0, endIndex, txtXStart, baseLineY, mTextPaint)
                    } else if (txtXEnd < parentEnd && txtXStart > parentEnd) { //处理右边界
                        val displayLength = ((txtXStart - parentEnd) / txtWidth * length).toInt()
                        val index = length - displayLength
                        canvas.drawText(
                            dateStr,
                            index,
                            length,
                            parentStart.toFloat(),
                            baseLineY,
                            mTextPaint
                        )
                    }
                } else {
                    if (barEntry.type == RecyclerBarEntry.TYPE_XAXIS_END_TODAY || barEntry.type == RecyclerBarEntry.TYPE_XAXIS_END_DAY) {
                        canvas.drawText(
                            dateStr,
                            txtXStart - (txtXEnd - parentEnd) - 8,
                            baseLineY,
                            mTextPaint
                        ) //靠右
                    } else if (DecimalUtil.bigOrEquals(
                            txtXStart,
                            parentStart.toFloat()
                        ) && DecimalUtil.smallOrEquals(txtXEnd, parentEnd)
                    ) { //中间位置
                        canvas.drawText(dateStr, rectF.left, baseLineY, mTextPaint)
                    } else if (txtXStart < parentStart && txtXEnd > parentStart) { //处理左边界
//                        int displayLength = (int) ((txtXEnd - parentStart) / txtWidth * length);
//                        int index = length - displayLength;
//                        canvas.drawText(dateStr, index, length, parentStart, baseLineY, mTextPaint);
                        canvas.drawText(dateStr, parentStart.toFloat(), baseLineY, mTextPaint)
                    } else if (txtXEnd > parentEnd && txtXStart < parentEnd) { //处理右边界
                        if (!mBarChartAttrs.xAxisForbidDealEndBoundary) {
                            canvas.drawText(
                                dateStr,
                                txtXStart - (txtXEnd - parentEnd) - 8,
                                baseLineY,
                                mTextPaint
                            )
                        }
                    }
                }
            }
        }
    }


    override fun drawVerticalLine(canvas: Canvas, parent: RecyclerView, xAxis: XAxis) {
        if (!mBarChartAttrs.enableXAxisGridLine) {
            return
        }
        val parentTop = parent.paddingTop
        val parentBottom = parent.height - parent.paddingBottom
        val parentStart =
            if (isRTLDirection()) parent.width - parent.paddingRight else parent.paddingLeft
        val parentEnd =
            if (isRTLDirection()) parent.paddingLeft else parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (adapterPosition == RecyclerView.NO_POSITION) {
                continue
            }
            val type = parent.adapter!!.getItemViewType(adapterPosition)
            val xStart = if (isRTLDirection()) child.right.toFloat() else child.left.toFloat()
            //todo 大周期的线靠右画了，因为用的item的endTime 来计算的。
            val xEnd = if (isRTLDirection()) child.left.toFloat() else child.right.toFloat()
            if (isRTLDirection()) {
                if (xStart > parentStart || xStart < parentEnd) { //超出的时候就不要画了
                    continue
                }
            } else {
                if (xStart > parentEnd || xStart < parentStart) { //超出的时候就不要画了
                    continue
                }
            }
            var xFirstLine = xStart //大周期线靠左
            if (mBarChartAttrs.xFirstLinePosition == XAxis.POSITION_RIGHT) { //设置属性靠右
                xFirstLine = xEnd
            }
            if (type == RecyclerBarEntry.TYPE_XAXIS_FIRST || type == RecyclerBarEntry.TYPE_XAXIS_SPECIAL) {
                if (mBarChartAttrs.enableXAxisFirstGridLine) {
                    mLinePaint.color = xAxis.firstDividerColor
                    val path = Path()
                    path.moveTo(xFirstLine, parentBottom - mBarChartAttrs.contentPaddingBottom)
                    path.lineTo(xFirstLine, parentTop.toFloat())
                    canvas.drawPath(path, mLinePaint)
                }
            } else if (type == RecyclerBarEntry.TYPE_XAXIS_SECOND) {
                if (mBarChartAttrs.enableXAxisSecondGridLine) {
                    mLinePaint.color = xAxis.secondDividerColor
                    val path = Path()
                    path.moveTo(xStart, parentBottom - mBarChartAttrs.contentPaddingBottom)
                    path.lineTo(xStart, parentTop.toFloat())
                    canvas.drawPath(path, mLinePaint)
                }
            } else if (type == RecyclerBarEntry.TYPE_XAXIS_THIRD) {
                if (mBarChartAttrs.enableXAxisThirdGridLine) {
                    mLinePaint.color = xAxis.thirdDividerColor
                    val path = Path()
                    path.moveTo(xStart, parentBottom - mBarChartAttrs.contentPaddingBottom)
                    path.lineTo(xStart, parentTop.toFloat())
                    canvas.drawPath(path, mLinePaint)
                }
            }
        }
    }


}