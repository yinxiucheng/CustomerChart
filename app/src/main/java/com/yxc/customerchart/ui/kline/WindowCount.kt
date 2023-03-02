package com.yxc.customerchart.ui.kline

import java.util.LinkedList
import java.util.Queue

/**
 * @author xiuchengyin
 *
 * @date 2023/3/2
 *
 */
class WindowCount {
    var queue:Queue<Float> = LinkedList()
    var sum:Float = 0f

    constructor()

    fun queueAddItem(value:Float){
        queue.offer(value)
        sum += value
    }

    fun queueRemoveItem(){
       val value = queue.poll()
        value?.let {
            sum -= value
        }
    }

    fun getAvg(type:Int, value: Float): Float{
        queueAddItem(value)
        val count = queue.size
        if (count < type){
            return value
        }else if (count == type){
            val avg = sum/count
            queueRemoveItem()
            return avg
        }
        return -1f
    }
}