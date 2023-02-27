package com.yxc.chartlib.entrys;



import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

import java.util.ArrayList;
import java.util.List;

public class EcgEntry extends RecyclerBarEntry {
    public List<Float> values = new ArrayList<>();

    public EcgEntry(int i, float value, long timestamp, int type) {
        super(i, value, timestamp, type);
    }
}
