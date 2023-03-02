package com.yxc.chartlib.entrys.model

/**
 * @author xiuchengyin
 *
 * @date 2023/3/3
 *
 */
sealed class AvgType(){
    class Avg5Type(typeValue: Int) : AvgType()
    class Avg10Type(typeValue: Int) : AvgType()
    class Avg20Type(typeValue: Int) : AvgType()

    companion object{
        val Avg5Type = Avg5Type(5)
        val Avg10Type = Avg10Type(10)
        val Avg20Type = Avg20Type(20)
    }
}
