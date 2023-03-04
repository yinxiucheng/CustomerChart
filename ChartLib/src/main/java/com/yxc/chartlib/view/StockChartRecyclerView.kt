package com.yxc.chartlib.view

import android.content.Context
import android.util.AttributeSet
import com.yxc.chartlib.attrs.ChartAttrsUtil
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.barchart.itemdecoration.StockChartItemDecoration

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class StockChartRecyclerView : BaseChartRecyclerView<StockChartAttrs, StockChartItemDecoration> {

    constructor(context: Context, attrs:AttributeSet):super(context, attrs)

    override fun getAttrs(context: Context, attrs: AttributeSet): StockChartAttrs {
       return ChartAttrsUtil.getStockChartAttrs(context, attrs)

    }
}