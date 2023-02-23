package com.yxc.chartlib.entrys;

import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;

import java.util.List;

/**
 * @author yxc
 * @date 2019-05-13
 */
public class MaxMinEntry extends RecyclerBarEntry {

    public float minY;
    public float maxY;
    public float averageY;

    public MaxMinEntry(float i, float value, long timestamp, int type) {
        super(i, value, timestamp, type);
    }

    public MaxMinEntry(float x, float[] vals) {
        super(x, vals);
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getAverageY() {
        return averageY;
    }

    public void setAverageY(float averageY) {
        this.averageY = averageY;
    }

    //获取最大值
    public static float getTheMinNumber(List<MaxMinEntry> entries) {
        if(entries == null || entries.size() == 0){
            return 0;
        }
        float min = 80;
        for (int i = 0; i < entries.size(); i++) {
            MaxMinEntry entryTemp = entries.get(i);
            float maxVal = entryTemp.maxY;
            if (maxVal > 0) {
                min = Math.min(min, maxVal);
            }
            float minVal = entryTemp.minY;
            if (minVal > 0) {
                min = Math.min(min, minVal);
            }
            float yVal = entryTemp.getY();
            if (yVal > 0) {
                min = Math.min(min, yVal);
            }
        }
        return min;
    }
}
