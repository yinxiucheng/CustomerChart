package com.yxc.chartlib.render

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.component.YAxis
import com.yxc.chartlib.formatter.ValueFormatter

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class  StockChartRender<T:ValueFormatter> constructor(var mStockAttrs: StockChartAttrs, var valueFormatter:T) {

    private val mTextPaint: Paint? = null

    private val mChartPaint: Paint? = null

    init {
        initTextPaint()
        initChartPaint()
    }

    fun initChartPaint(){

    }

    fun initTextPaint(){

    }

    fun <Y : YAxis> drawStockChart(canvas: Canvas, parent: RecyclerView, yAxis: Y) {

    }

    fun drawHighLight(canvas: Canvas, parent: RecyclerView, yAxis: YAxis){

    }

}