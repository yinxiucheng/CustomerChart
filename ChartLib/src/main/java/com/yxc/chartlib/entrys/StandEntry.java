package com.yxc.chartlib.entrys;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

/**
 * @author yxc
 * @date 2019-05-07
 */
public class StandEntry extends RecyclerBarEntry {

    public static final int STAND_TYPE_FIRST = 0;

    public static final int STAND_TYPE_SECOND = 1;

    public int standType = STAND_TYPE_FIRST;



    public StandEntry(int i, float value, long timestamp, int type) {
        super(i, value, timestamp, type);
    }


    public static int[] createDefaultValues(){
        int[] resultValues = new int[24];
        for (int i = 0; i < resultValues.length; i++) {
            resultValues[i] = 0;
        }
        return resultValues;
    }


}
