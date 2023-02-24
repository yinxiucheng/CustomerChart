package com.yxc.chartlib.utils

import androidx.collection.ArraySet
import java.util.*

object ArrayUtils {
    private const val TAG = "ArrayUtils"
    fun indexOf(dict: Array<Any?>?, key: Any?): Int {
        if (dict == null) {
            return -1
        }
        var i = 0
        val length = dict.size
        while (i < length) {
            if (dict[i] == key) {
                return i
            }
            i++
        }
        return -1
    }

    fun <T> filterNonNullSet(src: Array<T>): Set<T> {
        val ret: MutableSet<T> = ArraySet()
        for (t in src) {
            if (t == null) {
                continue
            }
            ret.add(t)
        }
        return ret
    }

    fun filterNonNullString(src: Array<String>): Array<String> {
        return filterNonNullList(src).toTypedArray()
    }

    fun <T> filterNonNullList(src: Array<T>): List<T> {
        val ret: MutableList<T> = LinkedList()
        for (t in src) {
            if (t == null) {
                continue
            }
            ret.add(t)
        }
        return ret
    }

    @JvmStatic
    fun isEmpty(collection: Collection<*>?): Boolean {
        return collection == null || collection.isEmpty()
    }

    fun <T> isEmpty(array: Array<T>?): Boolean {
        return array == null || array.size == 0
    }

    fun toIntArray(data: List<Int>): IntArray {
        val number = IntArray(data.size)
        var i = 0
        val length = data.size
        while (i < length) {
            number[i] = data[i]
            i++
        }
        return number
    }




    @JvmStatic
    fun getMaxValueInArray(srcList: List<Int>): Int {
        val sortedList = srcList.sortedWith(naturalOrder())
//        Collections.sort(srcList) targetSdkVersion <= 25 不判断数组类型 直接迭代排序 不可变数组将抛出UnsupportedOperationException异常
        val size = sortedList.size
        return if (size == 0) {
            0
        } else if (size == 1) {
            sortedList[0]
        } else {
            sortedList[size - 1]
        }
    }

    @JvmStatic
    fun getMinValueInArray(srcList: List<Int>): Int {
        val sortedList = srcList.sortedWith(naturalOrder())
        val size = sortedList.size
        return if (size == 0) {
            0
        } else {
            sortedList[0]
        }
    }

    @JvmStatic
    fun getMaxValueInFloatArray(srcList: List<Float>): Float {
        val sortedList = srcList.sortedWith(naturalOrder())
        return when (val size = sortedList.size) {
            0 -> 0f
            1 -> sortedList[0]
            else -> sortedList[size - 1]
        }
    }

    @JvmStatic
    fun getMinValueInFloatArray(srcList: List<Float>): Float {
        val sortedList = srcList.sortedWith(naturalOrder())
        val size = sortedList.size
        return if (size == 0) {
            0f
        } else {
            sortedList[0]
        }
    }

    @JvmStatic
    fun contains(array: IntArray, valueToFind: Int): Boolean {
        return Arrays.binarySearch(array, valueToFind) != -1
    }

}