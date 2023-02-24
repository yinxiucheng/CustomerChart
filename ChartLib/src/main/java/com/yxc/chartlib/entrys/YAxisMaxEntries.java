package com.yxc.chartlib.entrys;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since 2019-05-13
 */
public class YAxisMaxEntries<T extends RecyclerBarEntry> {

    public final static float DEFAULT_YAXIS_MAX = 100.0f;

    public YAxisMaxEntries(){}

    public void bind(YAxisMaxEntries yAxisMaxEntries){
        this.yAxisMaximum = yAxisMaxEntries.yAxisMaximum;
        if (visibleEntries == null){
            visibleEntries = new ArrayList<>();
        }else {
            visibleEntries.clear();
        }
        visibleEntries.addAll(yAxisMaxEntries.visibleEntries);
    }

    public YAxisMaxEntries(float yAxisMaximum, List<T> visibleEntries) {
        this.yAxisMaximum = yAxisMaximum;
        this.visibleEntries = visibleEntries;
    }


    public YAxisMaxEntries(float yAxisMaximum, float yAxisMinimum, List<T> visibleEntries) {
        this.yAxisMaximum = yAxisMaximum;
        this.yAxisMinimum = yAxisMinimum;
        this.visibleEntries = visibleEntries;
    }
    public float yAxisMaximum;
    public float yAxisMinimum;
    public List<T> visibleEntries;

}
