package com.yxc.chartlib.render

import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.component.StockYAxis
import com.yxc.chartlib.utils.DecimalUtil
import com.yxc.chartlib.utils.DisplayUtil
import com.yxc.customercomposeview.utils.dp
import com.yxc.fitness.chart.render.YAxisRender

class StockYAxisRender: YAxisRender<StockYAxis, StockChartAttrs>{

    constructor(mAttr:StockChartAttrs):super(mAttr)
    private lateinit var mDashPaint: Paint

    init {
        initDathPaint()
    }

    private fun initDathPaint() {
        mDashPaint = Paint()
        mDashPaint.reset()
        mDashPaint.isAntiAlias = true
        mDashPaint.style = Paint.Style.STROKE
        mDashPaint.strokeWidth = 1f
        val pathEffect: PathEffect = DashPathEffect(floatArrayOf(5f, 5f, 5f, 5f), 1f)
        mDashPaint.pathEffect = pathEffect
        mDashPaint.color = mBarChartAttrs.xAxisThirdDividerColor
    }

    //绘制 Y轴刻度线 横的网格线
    override fun drawHorizontalLine(canvas: Canvas, parent: RecyclerView, yAxis: StockYAxis) {
        val left = parent.left
        val right = parent.right
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val distance = bottom - mBarChartAttrs.contentPaddingBottom - mBarChartAttrs.contentPaddingTop - top
        val lineNums = yAxis.labelCount
        val lineDistance = distance / lineNums
        var gridLine = top + mBarChartAttrs.contentPaddingTop
        for (i in 0..lineNums) {
            if (i > 0) {
                gridLine += lineDistance
            }
            val path = Path()
            var enable = if (i == lineNums) {
                mBarChartAttrs.enableYAxisZero
            } else {
                mBarChartAttrs.enableYAxisGridLine //允许画 Y轴刻度
            }
            path.moveTo(left.toFloat(), gridLine)
            path.lineTo(right.toFloat(), gridLine)
            if (enable) {
                if(mBarChartAttrs.enableYAxisLineDash) mLinePaint.pathEffect = DashPathEffect(floatArrayOf(DisplayUtil.dip2pxF(4f), DisplayUtil.dip2pxF(1.5f)), 0f)
                canvas.drawPath(path, mLinePaint)
            }
        }
    }

    @JvmOverloads
    override fun drawLeftYAxisLabel(canvas: Canvas, parent: RecyclerView, yAxis: StockYAxis, drawText: Boolean) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val topLocation = top + mBarChartAttrs.contentPaddingTop
        val containerHeight = bottom - mBarChartAttrs.contentPaddingBottom - topLocation
        val itemHeight = containerHeight / yAxis.labelCount
        val yAxisScaleMap = yAxis.getYAxisScaleMap(topLocation, itemHeight, yAxis.labelCount)
        var i = 0
        for ((yAxisScaleLocation, yAxisScaleValue) in yAxisScaleMap) {
            i++
            val labelStr = DecimalUtil.getDecimalFloatStr(DecimalUtil.TWO_LENGTH_DECIMAL, yAxisScaleValue)
            val txtY = yAxisScaleLocation - 5.dp
            val txtX = parent.left +  yAxis.labelHorizontalPadding
            if (i <= yAxisScaleMap.size && drawText) {
                canvas.drawText(labelStr, txtX, txtY, mTextPaint)
            }
        }
    }

    //绘制右边的刻度
    //    public void drawRightYAxisLabel(Canvas canvas, RecyclerView parent, T yAxis) {
    //        if (mBarChartAttrs.enableEndYAxisLabel) {
    //            drawRightYAxisLabel(canvas, parent, yAxis, true);
    //            int right = parent.getWidth();
    //            int top = parent.getPaddingTop();
    //            int bottom = parent.getHeight() - parent.getPaddingBottom();
    //
    //            mTextPaint.setTextSize(yAxis.getTextSize());
    //            String longestStr = yAxis.getLongestLabel();
    //            float yAxisWidth = mTextPaint.measureText(longestStr) + mBarChartAttrs.recyclerPaddingRight;
    //
    //            int paddingRight = computeYAxisWidth(parent.getPaddingRight(), yAxisWidth);
    //            //设置 recyclerView的 BarChart 内容区域
    //            parent.setPadding(parent.getPaddingLeft(), parent.getPaddingTop(), paddingRight, parent.getPaddingBottom());
    //
    //            float topLocation = top + mBarChartAttrs.contentPaddingTop;
    //            float containerHeight = bottom - mBarChartAttrs.contentPaddingBottom - topLocation;
    //            float itemHeight = containerHeight / yAxis.getLabelCount();
    //            HashMap<Float, Float> yAxisScaleMap = yAxis.getYAxisScaleMap(topLocation, itemHeight, yAxis.getLabelCount());
    //
    //            float txtX = right - parent.getPaddingRight() + yAxis.labelHorizontalPadding;
    //
    //            for (Map.Entry<Float, Float> entry : yAxisScaleMap.entrySet()) {
    //                float yAxisScaleLocation = entry.getKey();
    //                float yAxisScaleValue = entry.getValue();
    //                ValueFormatter valueFormatter = (ValueFormatter) yAxis.getValueFormatter();
    //                String labelStr = valueFormatter.getFormattedValue(yAxisScaleValue);
    //                float txtY = yAxisScaleLocation + yAxis.labelVerticalPadding;
    //                canvas.drawText(labelStr, txtX, txtY, mTextPaint);
    //            }
    //        }
    //    }
}