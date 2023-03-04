package com.yxc.chartlib.barchart.itemdecoration

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.component.StockYAxis
import com.yxc.chartlib.component.XAxis
import com.yxc.chartlib.component.YAxis
import com.yxc.chartlib.formatter.StockValueFormatter
import com.yxc.chartlib.render.StockChartRenderer
import com.yxc.chartlib.render.StockXAxisRender
import com.yxc.chartlib.render.StockYAxisRender

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class StockChartItemDecoration : BaseChartItemDecoration<StockChartAttrs, YAxis>{

    private  var mChartRender: StockChartRenderer<StockValueFormatter>
    private  var mAttacheYAxis: StockYAxis

    constructor(yAxis:YAxis, xAxis: XAxis, stockChartAttrs: StockChartAttrs, attacheYAxis:StockYAxis)
            :super(yAxis, xAxis, stockChartAttrs){
        mAttacheYAxis = attacheYAxis
        mChartRender = StockChartRenderer(stockChartAttrs, StockValueFormatter())
        yAxisRenderer = StockYAxisRender(stockChartAttrs)
        xAxisRenderer = StockXAxisRender(stockChartAttrs)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
        yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis) //画横的网格线
        xAxisRenderer.drawXAxis(canvas, parent, mXAxis)
        xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);
        mChartRender.drawStockChart(canvas, parent, mYAxis, mAttacheYAxis) //draw LineChart
        mChartRender.drawHighLight(canvas, parent, mYAxis)// highLight
        mBarBoardRender.drawBarBorder3(canvas, parent)
    }

    fun setYAxis(yAxis: YAxis, attacheYAxis:StockYAxis) {
        super.setYAxis(yAxis)
        mAttacheYAxis = attacheYAxis
    }
}