package com.xiaomi.fitness.chart.entrys

import com.github.mikephil.charting.data.BarEntry
import com.xiaomi.fitness.chart.attrs.BaseChartAttrs
import com.xiaomi.fitness.chart.component.XAxis
import com.xiaomi.fitness.common.utils.TimeDateUtil
import com.xiaomi.fitness.common.utils.TimeDateUtil.getFirstDayOfMonth
import com.xiaomi.fitness.common.utils.TimeDateUtil.getHourOfTheDay
import com.xiaomi.fitness.common.utils.TimeDateUtil.isEndHourOfTheDay
import com.xiaomi.fitness.common.utils.TimeDateUtil.isLastDayOfMonth
import com.xiaomi.fitness.common.utils.TimeDateUtil.isSunday
import com.xiaomi.fitness.common.utils.TimeDateUtil.timestampToLocalDate
import org.joda.time.LocalDate
import java.util.*

/**
 * @author yxc
 * @since 2019/4/6
 */
open class RecyclerBarEntry : BarEntry, Comparable<RecyclerBarEntry> {
    var selected = TYPE_UNSELECTED

    @JvmField
    var isMinMax = TYPE_MINMAX_DEF //是否是最小、最大值。默认为非最小、最大值。

    @JvmField
    var timestamp: Long = 0

    @JvmField
    var localDate: LocalDate? = null

    @JvmField
    var type = 0

    @JvmField
    var validType = TYPE_VALID

    constructor() : super(0f, 0f) {}

    /**
     * @param x         坐标 X （第i个数据）
     * @param y         坐标 Y
     * @param timestamp 对应的时间戳，X轴的刻度
     * @param type      X轴刻度对应的 种类， 用于画X轴对应的三种刻度线，
     * TYPE_XAXIS_FIRST、TYPE_XAXIS_SPECIAL方便滑动时，ScrollToPosition的时候定位用的。
     */
    constructor(x: Float, y: Float, timestamp: Long, type: Int) : super(x, y) {
        this.timestamp = timestamp
        this.type = type
        validType = TYPE_VALID
        localDate = TimeDateUtil.timestampToLocalDate(timestamp)
    }

    constructor(x: Float, vals: FloatArray?) : super(x, vals) {}

    override fun compareTo(o: RecyclerBarEntry): Int {
        return (timestamp - o.timestamp).toInt()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val barEntry = o as RecyclerBarEntry
        return timestamp == barEntry.timestamp && type == barEntry.type && x == barEntry.x &&
                localDate == barEntry.localDate
    }

    override fun hashCode(): Int {
        return Objects.hash(timestamp, type, localDate)
    }

    fun isSelected(): Boolean {
        return selected == TYPE_SINGLE_TAP_UP_SELECTED || selected == TYPE_LONG_PRESS_SELECTED
    }

    companion object {
        const val TYPE_XAXIS_FIRST = 1 //一个月
        const val TYPE_XAXIS_SECOND = 2 //7天的线，需要drawText
        const val TYPE_XAXIS_THIRD = 3 //最小刻度的线
        const val TYPE_XAXIS_SPECIAL = 4 //同时是月线以及7日分隔线
        const val TYPE_XAXIS_END_DAY = 5//一天最后的一个柱子
        const val TYPE_XAXIS_END_TODAY = 6//今天最后的一个柱子

        const val TYPE_UNSELECTED = 0 //没有选中
        const val TYPE_SINGLE_TAP_UP_SELECTED = 1 //单击选中
        const val TYPE_LONG_PRESS_SELECTED = 2 //长按选中
        const val TYPE_VALID = 0
        const val TYPE_INVALID = 1
        const val TYPE_MINMAX_DEF = 0 //默认非最小、最大值。
        const val TYPE_MINMAX_MIN = -1 //图表中的极小值的Flag 标志位 -1，在设置贝塞尔曲线强度的时候取最小值。
        const val TYPE_MINMAX_MIN_NEAR = -2 //图表中的极小值附近的点的标记 -2
        const val TYPE_MINMAX_MAX = 1 //图表中的极大值的Flag 标志位 1，在设置贝塞尔曲线强度的时候取最小值。
        const val TYPE_MINMAX_MAX_NEAR = 2 //图表中的极大值附近的点的标记 2

        @JvmStatic
        fun getWeekBarEntryType(localDate: LocalDate?): Int {
            var type = TYPE_XAXIS_SECOND
            val isSunday = isSunday(localDate!!)
            if (isSunday) {
                type = TYPE_XAXIS_FIRST
            }
            return type
        }

        @JvmStatic
        fun getMonthBarEntryType(attrs: BaseChartAttrs, localDate: LocalDate): Int {
            var type = TYPE_XAXIS_THIRD
            val isLastDayOfMonth = isLastDayOfMonth(localDate)
            val dayOfYear = localDate.dayOfYear
            if (isLastDayOfMonth && (dayOfYear + 1) % attrs.xAxisScaleDistance == 0) {
                type = TYPE_XAXIS_SPECIAL
            } else if (isLastDayOfMonth) {
                type = TYPE_XAXIS_FIRST
            } else if ((dayOfYear + 1) % attrs.xAxisScaleDistance == 0) {
                type = TYPE_XAXIS_SECOND
            }
            return type
        }

        @JvmStatic
        fun getMonthBarEntryType2(attrs: BaseChartAttrs, localDate: LocalDate): Int {
            //1 7 14 21 28
            var type = TYPE_XAXIS_THIRD
            val firstDayOfMonth: LocalDate = getFirstDayOfMonth(localDate)
            val dayOfMonth = localDate.dayOfMonth
            if (firstDayOfMonth == localDate || dayOfMonth % 7 == 0) {
                type = TYPE_XAXIS_SECOND
            }
            return type
        }

        @JvmStatic
        fun getDayBarEntryType(attrs: BaseChartAttrs?, startTime: Long, endTime: Long): Int {
            var type = TYPE_XAXIS_THIRD
            if (attrs == null) {
                return type
            }
            val itemDistance = (endTime - startTime).toInt()
//            Logger.d("XAxis Entry Type itemDistance:" + itemDistance);
            val isLastHourOfTheDay = isEndHourOfTheDay(endTime)
            val hourOfTheDay = getHourOfTheDay(startTime)
            if (isLastHourOfTheDay && XAxis.isSecondXAxis(hourOfTheDay, attrs)) {
                return TYPE_XAXIS_SPECIAL
            } else if (isLastHourOfTheDay) {
                type = TYPE_XAXIS_FIRST
            } else if (XAxis.isSecondXAxis(hourOfTheDay, attrs)) {
                type = TYPE_XAXIS_SECOND
            }

            if (isLastEntryOfTheToday(endTime, itemDistance)){
                type = TYPE_XAXIS_END_TODAY
            } else if (isLastEntryOfTheDay(endTime, itemDistance)){
                type = TYPE_XAXIS_END_DAY
            }
            return type
        }

        private fun isLastEntryOfTheDay(endTime:Long, itemDistance:Int):Boolean{
            val timestampTemp = endTime + (1.5f * itemDistance).toInt() //加上1.5个间距
            val localDate1 = timestampToLocalDate(endTime)
            val localDate2 = timestampToLocalDate(timestampTemp)
//            Logger.d("XAxis Entry Type endTime:$endTime distance: $itemDistance localDate1 $localDate1 localDate2 $localDate2")
            return localDate1.dayOfWeek != localDate2.dayOfWeek
        }

        private fun isLastEntryOfTheToday(endTime:Long, itemDistance:Int):Boolean{
            val bool1 =  isLastEntryOfTheDay(endTime, itemDistance)
            val bool2 = TimeDateUtil.isToday(timestampToLocalDate(endTime))
//            Logger.d("XAxis Entry Type endTime:$endTime distance: $itemDistance isLastEntryOfTheToday $bool1 $bool2")
            return bool1 && bool2
        }

        @JvmStatic
        fun getDayBarEntryType2(attrs: BaseChartAttrs?, startTime: Long, endTime: Long): Int {
            var type = TYPE_XAXIS_THIRD
            if (attrs == null) {
                return type
            }
            val hourOfTheDay = getHourOfTheDay(startTime)
            if (XAxis.isSecondXAxis(hourOfTheDay, attrs))
                type = TYPE_XAXIS_SECOND
            return type
        }

        fun <T : RecyclerBarEntry?> getValidEntryReverse(barEntries: List<T>): Int {
            val size = barEntries.size
            for (i in 0 until size) {
                val entry = barEntries[i]
                if (entry!!.y > 0) {
                    return entry.y.toInt()
                }
            }
            return 0
        }

        //获取最大值
        fun <T : RecyclerBarEntry?> getTheMaxNumber(entries: List<T>?): Float {
            if (entries == null || entries.isEmpty()) {
                return 0f
            }
            val maxMinEntry = entries[0]
            var max = maxMinEntry!!.y
            for (i in entries.indices) {
                val entryTemp = entries[i]
                max = Math.max(max, entryTemp!!.y)
                max = Math.max(max, entryTemp.y)
            }
            return max
        }

        @JvmStatic
        fun <T : RecyclerBarEntry?> getValidEntry(barEntries: List<T>): Int {
            val size = barEntries.size
            for (i in size - 1 downTo 0) {
                val rateEntry = barEntries[i]
                if (rateEntry!!.y > 0) {
                    return rateEntry.y.toInt()
                }
            }
            return 0
        }

        @JvmStatic
        fun <T : RecyclerBarEntry?> getValidEntryFloat(barEntries: List<T>): Float {
            val size = barEntries.size
            for (i in size - 1 downTo 0) {
                val rateEntry = barEntries[i]
                if (rateEntry!!.y > 0) {
                    return rateEntry.y
                }
            }
            return 0f
        }
    }
}