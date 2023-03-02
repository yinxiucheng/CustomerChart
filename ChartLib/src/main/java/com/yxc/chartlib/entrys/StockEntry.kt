package com.yxc.chartlib.entrys

import com.yxc.chartlib.entrys.model.MaxMinModel
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class StockEntry : RecyclerBarEntry {
    constructor(x:Float, time:Long, shadowH: Float, shadowL:Float, open:Float, close:Float)
            :super(x, shadowH, time, type = TYPE_XAXIS_THIRD){
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

    var ma5 = 0f
    var ma10 = 0f
    var ma20 = 0f

    companion object{
        fun getTheMaxMinModel(entries: List<StockEntry>): MaxMinModel {
            if (entries == null || entries.isEmpty()) {
                return MaxMinModel(100f, 0f)
            }
            val maxMinEntry = entries[0]
            var max = maxMinEntry.mShadowHigh
            var min = maxMinEntry.mShadowLow
            for (i in entries.indices) {
                val entryTemp = entries[i]
                max = Math.max(max, entryTemp.mShadowHigh)
                min = Math.min(min, entryTemp.mShadowLow)
            }
            return MaxMinModel(max, min)
        }
    }

    override fun toString(): String {
        return "StockEntry(mClose=$mClose, mOpen=$mOpen, ma5=$ma5, ma10=$ma10, ma20=$ma20)"
    }
}

