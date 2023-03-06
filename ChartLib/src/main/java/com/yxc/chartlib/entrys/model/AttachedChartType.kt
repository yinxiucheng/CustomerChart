package com.yxc.chartlib.entrys.model

/**
 * @author xiuchengyin
 *
 * @date 2023/3/3
 *
 */
sealed class AttachedChartType{

    object Volume : AttachedChartType()
    object MADC : AttachedChartType()
    object KDJ: AttachedChartType()

    companion object{
        fun proceedType(currentType: AttachedChartType): AttachedChartType {
            return when (currentType) {
                Volume -> MADC
                MADC -> KDJ
                KDJ -> Volume
            }
        }
    }
}
