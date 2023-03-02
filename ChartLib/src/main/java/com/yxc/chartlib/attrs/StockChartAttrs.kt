package com.yxc.chartlib.attrs

import android.graphics.Color

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class StockChartAttrs : BaseChartAttrs() {
    /**
     * 涨色值
     */
    var riseColor: Int = DEFAULT_RISE_COLOR

    /**
     * 跌色值
     */
    var downColor: Int = DEFAULT_DOWN_COLOR

    /**
     * 柱子是否空心
     */
    var isChartItemFill: Boolean = true

    //5日均线
    var avg5Color: Int = DEFAULT_DOWN_COLOR

    //10日均线
    var avg10Color: Int = DEFAULT_DOWN_COLOR

    //20日均线
    var avg20Color: Int = DEFAULT_DOWN_COLOR

}

val DEFAULT_RISE_COLOR = Color.parseColor("#E36245")

val DEFAULT_DOWN_COLOR = Color.parseColor("#3FC08E")
