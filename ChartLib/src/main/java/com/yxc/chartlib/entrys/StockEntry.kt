package com.yxc.chartlib.entrys

import com.yxc.chartlib.entrys.model.AttachedChartType
import com.yxc.chartlib.entrys.model.MaxMinModel
import com.yxc.chartlib.entrys.stock.KDJEntry
import com.yxc.chartlib.entrys.stock.MACDEntry
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class StockEntry : RecyclerBarEntry {
    constructor(x:Float, time:Long, shadowH: Float, shadowL:Float, open:Float, close:Float, volume:Float)
            :super(x, shadowH, time, type = TYPE_XAXIS_THIRD){
        mHigh = shadowH
        mLow = shadowL
        mOpen = open
        mClose = close
        this.volume = volume
    }
    /**
     * shadow-high value
     */
    var mHigh = 0f

    /**
     * shadow-low value
     */
    var mLow = 0f

    /**
     * close value
     */
    var mClose = 0f

    /**
     * open value
     */
    var mOpen = 0f

    var volume:Float = 0f

    //
    var isRise: Boolean = true

    var ma5 = 0f
    var ma10 = 0f
    var ma20 = 0f

    var macdEntry: MACDEntry? = null
    var kdjEntity: KDJEntry? = null

    companion object{
        fun getTheMaxMinModel(entries: List<StockEntry>): MaxMinModel {
            if (entries.isEmpty()) {
                return MaxMinModel(100f, 0f)
            }
            val maxMinEntry = entries[0]
            //todo 这里的最大最小值 得 考虑 ma5/ma10/ma20
            var max = maxMinEntry.mHigh
            var min = maxMinEntry.mLow
            for (i in entries.indices) {
                val entryTemp = entries[i]
                max = max.coerceAtLeast(entryTemp.mHigh)
                min = min.coerceAtMost(entryTemp.mLow)
            }
            return MaxMinModel(max, min)
        }

        fun getAttacheMaxMinModel(entries: List<StockEntry>, attachedChartType: AttachedChartType): MaxMinModel{
            return when (attachedChartType) {
                AttachedChartType.Volume -> getTheMaxMinModelVolume(entries)
                AttachedChartType.MADC -> MaxMinModel(100f, 0f)
                AttachedChartType.KDJ -> getTheMaxMinModelKDJ(entries)
            }
        }

        fun getTheMaxMinModelKDJ(entries: List<StockEntry>): MaxMinModel {
            if (entries.isEmpty()) {
                return MaxMinModel(100f, 0f)
            }
            var max = 100f
            var min = 0f
            for (i in entries.indices) {
                val kdjEntry = entries[i].kdjEntity as KDJEntry
                val maxTemp = kdjEntry.dVal.coerceAtMost(kdjEntry.jVal).coerceAtMost(kdjEntry.kVal)
                val minTemp = kdjEntry.dVal.coerceAtLeast(kdjEntry.jVal).coerceAtLeast(kdjEntry.kVal)
                max = max.coerceAtMost(maxTemp)
                min = min.coerceAtLeast(minTemp)
            }
            return MaxMinModel(max, min)
        }

        fun getTheMaxMinModelVolume(entries: List<StockEntry>): MaxMinModel {
            if (entries.isEmpty()) {
                return MaxMinModel(100f, 0f)
            }
            val maxMinEntry = entries[0]
            var max = maxMinEntry.volume
            var min = 0f
            for (i in entries.indices) {
                val entryTemp = entries[i]
                max = max.coerceAtLeast(entryTemp.volume)
            }
            return MaxMinModel(max, min)
        }
    }

    override fun toString(): String {
        return "StockEntry(mClose=$mClose, mOpen=$mOpen, ma5=$ma5, ma10=$ma10, ma20=$ma20)"
    }
}

