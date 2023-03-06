package com.yxc.chartlib.entrys.stock

/**
 * @author xiuchengyin
 *
 * @date 2023/2/28
 *
 */
data class MACDEntry(var dea: Float = 0f,
                     var dif: Float = 0f,
                     var macd: Float = 0f){

    fun getLineValue(type:Int):Float{
       return when(type){
            1 -> dea
            else-> dif
        }
    }
}

