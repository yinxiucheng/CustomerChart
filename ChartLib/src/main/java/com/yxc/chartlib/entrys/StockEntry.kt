package com.yxc.chartlib.entrys

import com.yxc.fitness.chart.entrys.RecyclerBarEntry
/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class StockEntry : RecyclerBarEntry {
    constructor(x:Float, time:Long, shadowH: Float, shadowL:Float, open:Float, close:Float)
            :super(x, (shadowH + shadowL) / 2f, time, type = TYPE_XAXIS_THIRD){
        mShadowHigh = shadowH
        mShadowLow = shadowL
        mOpen = open
        mClose = close
    }
    /**
     * shadow-high value
     */
    var mShadowHigh = 0f

    /**
     * shadow-low value
     */
    var mShadowLow = 0f

    /**
     * close value
     */
    var mClose = 0f

    /**
     * open value
     */
    var mOpen = 0f

    //
    var isRise: Boolean = true

}
