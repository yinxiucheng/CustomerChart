package com.yxc.customerchart.ui.utils

import com.github.wangyiqian.stockchart.entities.IKEntity
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.entrys.stock.KDJEntry
import com.yxc.chartlib.entrys.stock.MACDEntry
import com.yxc.chartlib.utils.DecimalUtil
import com.yxc.customerchart.ui.kline.WindowCount
import com.yxc.customerchart.ui.kline.WindowCountManager
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
import com.yxc.mylibrary.TimeDateUtil
import java.lang.Math.abs

/**
 * @author xiuchengyin
 *
 * @date 2023/3/4
 *
 */
object DataHelper {

    fun createStockEntryList(kEntityList:List<IKEntity>, size:Int): WindowCountManager {
        val stockEntryList = mutableListOf<StockEntry>()
        var currentIndex = size
        var preEntry: StockEntry? = null
        val size = kEntityList.size
        val windowCount5 = WindowCount()
        val windowCount10 = WindowCount()
        val windowCount20 = WindowCount()
        for (i in size - 1 downTo 0){
            val entity = kEntityList[i]
            //todo Mock volume值
            val volume = (20 until 100).random()
            val stockEntry = StockEntry((currentIndex++).toFloat(), entity.getTime()/1000, entity.getHighPrice(),
                entity.getLowPrice(), entity.getOpenPrice(), entity.getClosePrice(), volume.toFloat())
            preEntry?.let {
                stockEntry.isRise = it.mClose < stockEntry.mClose
                val lastDate = TimeDateUtil.timestampToLocalDate(it.timestamp)
                val thisDate = TimeDateUtil.timestampToLocalDate(stockEntry.timestamp)
                if (!TimeDateUtil.isSameMonth(lastDate, thisDate)){
                    stockEntry.type = RecyclerBarEntry.TYPE_XAXIS_FIRST
                }
            }
            val avg5 = windowCount5.getAvg(5, stockEntry.mClose)
            if (!DecimalUtil.equals(avg5, -1f)){
                stockEntry.ma5 = avg5
            }
            val avg10 = windowCount10.getAvg(10, stockEntry.mClose)
            if (!DecimalUtil.equals(avg10, -1f)){
                stockEntry.ma10 = avg10
            }
            val avg20 = windowCount20.getAvg(20, stockEntry.mClose)
            if (!DecimalUtil.equals(avg20, -1f)){
                stockEntry.ma20 = avg20
            }

            //todo mock kdj 数据。
            stockEntry.kdjEntity = KDJEntry(stockEntry.ma5, stockEntry.ma10, stockEntry.ma20)

            val randomVal = (1..50).random()
            var macdVal = if(stockEntry.isRise) 50 + randomVal else 50 - randomVal
            val randomVal2 = (1..10).random()

            var dea = if (stockEntry.isRise) 100 - macdVal else 50 + macdVal

            val randomVal3 = (1..8).random()
            var dif = if (stockEntry.isRise) dea + randomVal3 else dea - randomVal3
            dif = dif.coerceAtLeast(0).coerceAtLeast(100)
            stockEntry.macdEntry = MACDEntry(dea.toFloat() , dif.toFloat() , macdVal.toFloat())
            preEntry = stockEntry
            stockEntryList.add(0, stockEntry)
        }
        return WindowCountManager(windowCount5, windowCount10, windowCount20, stockEntryList)
    }
}