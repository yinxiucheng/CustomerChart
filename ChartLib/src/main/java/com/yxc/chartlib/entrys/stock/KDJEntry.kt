package com.yxc.chartlib.entrys.stock

import com.yxc.chartlib.entrys.model.MaxMinModel
import com.yxc.chartlib.stockchart.model.entitys.KDJEntity
import com.yxc.chartlib.stockchart.model.entitys.MACDEntity
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
data class KDJEntry(var kVal: Float = 0f, var dVal:Float = 0f, var jVal:Float = 0f){

    fun getKDJVal(type:Int): Float{
        return when(type){
            1-> kVal
            2-> dVal
            else-> jVal
        }
    }
}
