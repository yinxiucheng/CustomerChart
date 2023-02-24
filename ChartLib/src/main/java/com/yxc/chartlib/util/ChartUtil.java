package com.yxc.chartlib.util;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/10
 */
public class ChartUtil {

    //获取最大值
    public static float getTheMaxNumber(List<? extends RecyclerBarEntry> entries) {
        if (null == entries || entries.size() == 0){
            return 0;
        }
        RecyclerBarEntry barEntry = entries.get(0);
        float max = barEntry.getY();
        int size = entries.size();
        for (int i = 0; i < size; i++) {
            RecyclerBarEntry entryTemp = entries.get(i);
            max = Math.max(max, entryTemp.getY());
        }
        return max;
    }

    public static float getTheMinNumber(List<? extends RecyclerBarEntry> entries) {
        if (null == entries || entries.size() == 0){
            return 0;
        }
        RecyclerBarEntry barEntry = entries.get(0);
        float min = barEntry.getY();
        int size = entries.size();
        for (int i = 0; i < size; i++) {
            RecyclerBarEntry entryTemp = entries.get(i);
            min = Math.min(min, entryTemp.getY());
        }
        return min;
    }

    public static float getTheAvgNumber(List<? extends RecyclerBarEntry> entries) {
        if (null == entries || entries.size() == 0){
            return 0;
        }
        int size = entries.size();
        float sum = 0;
        for (int i = 0; i < size; i++) {
            RecyclerBarEntry entryTemp = entries.get(i);
            sum += entryTemp.getY();
        }
        float averageY = sum / (size * 1.0f);
        return averageY;
    }

    public static int getMaxInt(float yAxisMaximum) {
        float temp = yAxisMaximum + 10;
        return (int) temp;
    }
}
