package com.yxc.chartlib.formatter

import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.utils.DecimalUtil
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
import com.yxc.mylibrary.TimeDateUtil

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
class StockValueFormatter : ValueFormatter() {

    override fun getBarLabel(barEntry: RecyclerBarEntry?): String {
        if (barEntry is StockEntry){
//            return DecimalUtil.getDecimalFloatStr(DecimalUtil.TWO_LENGTH_DECIMAL, barEntry.mOpen) +
//                    ", " + DecimalUtil.getDecimalFloatStr(DecimalUtil.TWO_LENGTH_DECIMAL, barEntry.mClose) +
//                    ", " + DecimalUtil.getDecimalFloatStr(DecimalUtil.TWO_LENGTH_DECIMAL, barEntry.mShadowLow) +
//                    ", " + DecimalUtil.getDecimalFloatStr(DecimalUtil.TWO_LENGTH_DECIMAL, barEntry.mShadowHigh)
            return DecimalUtil.getDecimalFloatStr(DecimalUtil.TWO_LENGTH_DECIMAL, barEntry.mOpen) +
                    ", " + TimeDateUtil.getDateYYYYMMddSimple(barEntry.timestamp * 1000)
        }
        return ""
    }
}