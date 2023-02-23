package com.yxc.chartlib.entrys;

import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.chart.entrys.model.SleepItemTime;

public class SleepItemEntry extends RecyclerBarEntry {


    public SleepItemTime sleepItemTime;


    public SleepItemEntry() {
    }

    public SleepItemEntry(float x, float y, long timestamp, int type) {
        super(x, y, timestamp, type);
    }

}
