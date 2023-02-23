package com.yxc.chartlib.entrys;

import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;

/**
 * @author yxc
 * @since  2019-05-23
 */
public class SportRecordEntry extends RecyclerBarEntry {

    public float minute;


    public SportRecordEntry(float i, float value, long timestamp, int type) {
        super(i, value, timestamp, type);
        this.minute = i;
    }
}
