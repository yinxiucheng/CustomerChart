package com.yxc.fitness.chart.entrys

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@IntDef(
    EnvironmentalNoiseLevel.INVALID,
    EnvironmentalNoiseLevel.PLEASANT,
    EnvironmentalNoiseLevel.HIGH,
    EnvironmentalNoiseLevel.EXTREME
)
@Retention(RetentionPolicy.SOURCE)
annotation class EnvironmentalNoiseLevel() {
    companion object {
        const val INVALID = -1
        const val PLEASANT = 0
        const val HIGH = 1
        const val EXTREME = 2
    }
}
