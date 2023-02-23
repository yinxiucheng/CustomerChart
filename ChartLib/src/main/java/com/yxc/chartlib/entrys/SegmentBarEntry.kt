package com.xiaomi.fitness.chart.entrys

import com.xiaomi.fitness.chart.entrys.model.SegmentRectModel
import com.xiaomi.fitness.common.utils.ArrayUtils.isEmpty
import java.util.*

open class SegmentBarEntry : RecyclerBarEntry {
    @JvmField
    var rectValueModelList: MutableList<SegmentRectModel>? = null
    private var segmentRange  = 0 // 每种业务数据的segmentRange不一样，心率、血氧、血压等。
    var maxValue = 0
    var minValue = 0

    //values 从小到大排序
    constructor(values: FloatArray, segmentRange: Int) {
        this.segmentRange = segmentRange
        val length = values.size
        rectValueModelList = mutableListOf()
        if (length <= 0) {
            return
        }
        var currentValue = values[0]
        var preValue = currentValue
        var segmentRectModel = SegmentRectModel(currentValue, preValue)
        rectValueModelList?.add(segmentRectModel)
        for (i in 1 until length) {
            currentValue = values[i]
            val range = currentValue - preValue
            if (range > segmentRange) {
                segmentRectModel = SegmentRectModel(currentValue, currentValue)
                rectValueModelList?.add(segmentRectModel)
            } else {
                segmentRectModel.topValue = currentValue
                segmentRectModel.bottomValue = preValue
            }
            preValue = currentValue
        }
    }

    /**
     * 针对自定义生成SegmentRectModel的构造函数
     */
    constructor(rectValueModelList: MutableList<SegmentRectModel>, segmentRange: Int) {
        this.rectValueModelList = rectValueModelList
        this.segmentRange = segmentRange
        setMaxMinValue(rectValueModelList)
    }

    private fun setMaxMinValue(rectModelList: List<SegmentRectModel>) {
        if (rectModelList.isNotEmpty()) {
            val valueList: MutableList<Float> = ArrayList()
            for (i in rectModelList.indices) {
                val rectModel = rectModelList[i]
                valueList.add(rectModel.topValue)
                valueList.add(rectModel.bottomValue)
            }
            valueList.sort()
            minValue = Math.round(valueList[0])
            maxValue = Math.round(valueList[valueList.size - 1])
        }
    }

    val rangeStr: String
        get() = if (maxValue > minValue) {
            "$minValue-$maxValue"
        } else maxValue.toString()

    constructor(i: Int, y: Int, startTime: Long, type: Int) : super(
        i.toFloat(),
        y.toFloat(),
        startTime,
        type
    )

    companion object {
        const val SEGMENT_RANGE_STRESS = 5
        const val SEGMENT_RANGE_HRM = 10
        const val SEGMENT_RANGE_BLOOD_PRESSURE = 5
        const val SEGMENT_RANGE_SPO = 10
        const val SEGMENT_RANGE_TEMPERATURE = 1
        @JvmStatic
        fun getRectModels(
            segmentRange: Int,
            values: List<Int>,
            rectColor: Int,
            boardColor: Int
        ): MutableList<SegmentRectModel> {
            val models: MutableList<SegmentRectModel> = mutableListOf()
            if (isEmpty(values)) {
                return models
            }
            if (values.size > 1) {
                Collections.sort(values)
            }
            var currentValue = values[0]
            var preValue = currentValue
            var segmentRectModel = SegmentRectModel(currentValue.toFloat(), preValue.toFloat())
            segmentRectModel.rectColor = rectColor
            segmentRectModel.boardColor = boardColor
            models.add(segmentRectModel)
            for (i in 1 until values.size) {
                currentValue = values[i]
                val range = (currentValue - preValue).toFloat()
                if (range > segmentRange) {
                    segmentRectModel = SegmentRectModel(currentValue.toFloat(), currentValue.toFloat())
                    segmentRectModel.rectColor = rectColor
                    segmentRectModel.boardColor = boardColor
                    models.add(segmentRectModel)
                } else {
                    segmentRectModel.topValue = currentValue.toFloat()
                }
                preValue = currentValue
            }
            return models
        }
    }
}