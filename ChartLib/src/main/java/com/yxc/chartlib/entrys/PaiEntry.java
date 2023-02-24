package com.yxc.chartlib.entrys;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

public class PaiEntry extends RecyclerBarEntry {

    public PaiEntry(float x, float y, long timestamp, int type) {
        super(x, y, timestamp, type);
    }

    public float dailyPai;
    public float totalPai;
    public float highZonePai;
    public float mediumZonePai;
    public float lowZonePai;

}
