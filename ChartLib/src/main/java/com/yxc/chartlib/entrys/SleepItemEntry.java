package com.yxc.chartlib.entrys;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.entrys.model.SleepItemTime;

public class SleepItemEntry extends RecyclerBarEntry {


    public SleepItemTime sleepItemTime;


    public SleepItemEntry() {
    }

    public SleepItemEntry(float x, float y, long timestamp, int type) {
        super(x, y, timestamp, type);
    }

}
