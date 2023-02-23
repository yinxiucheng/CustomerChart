package com.yxc.chartlib.mpchart;

import com.github.mikephil.charting.components.XAxis;
import com.xiaomi.fitness.common.utils.TimeDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @date 2020/12/18
 */
public class TimeXAxis extends XAxis {

    public int interval;

    public void updateAxisMaximum(float maximum){
        setAxisMaximum(maximum);
    }

    @Override
    public int getLabelCount() {
        float max = getAxisMaximum();
        float min = getAxisMinimum();
        int labelCount;
        if (max > 6000 * TimeDateUtil.TIME_MIN_INT){
            interval = 2000 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 4800 * TimeDateUtil.TIME_MIN_INT) {// 80个小时
            interval = 1920 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 2400 * TimeDateUtil.TIME_MIN_INT) {// 40个小时
            interval = 960 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 1200 * TimeDateUtil.TIME_MIN_INT) {
            interval = 480 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 600 * TimeDateUtil.TIME_MIN_INT) {
            interval = 240 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 300 * TimeDateUtil.TIME_MIN_INT) {
            interval = 120 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 150 * TimeDateUtil.TIME_MIN_INT) {
            interval = 60 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 100 * TimeDateUtil.TIME_MIN_INT) {
            interval = 30 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 50 * TimeDateUtil.TIME_MIN_INT) {
            interval = 20 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 25 * TimeDateUtil.TIME_MIN_INT) {
            interval = 10 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 20 * TimeDateUtil.TIME_MIN_INT) {
            interval = 5 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 15 * TimeDateUtil.TIME_MIN_INT) {
            interval = 4 * TimeDateUtil.TIME_MIN_INT;
        } else if (max > 5 * TimeDateUtil.TIME_MIN_INT) {
            interval = 2 * TimeDateUtil.TIME_MIN_INT;
        } else {
            interval = TimeDateUtil.TIME_MIN_INT;
        }
        float currentEntry = min;
        List<Float> entryList = new ArrayList<>();
        do {
            entryList.add(currentEntry);
            currentEntry += interval;
//            Log.d("TimeXAxis", "currentEntry:" + currentEntry + " max:" + max);
        } while (currentEntry <= max);

        labelCount = entryList.size();
//        Log.d("TimeXAxis", "getLabelCount() invoke labelCount:" + labelCount + " interval:" + interval / TimeDateUtil.TIME_MIN_INT + " max:" + max / TimeDateUtil.TIME_MIN_INT);
        mEntryCount = labelCount;
        if (mEntries.length < labelCount) {
            // Ensure stops contains at least numStops elements.
            mEntries = new float[labelCount];
        }
        for (int i = 0; i < labelCount; i++) {
            mEntries[i] = entryList.get(i);
//            Log.d("TimeXAxis", "getLabelCount() labelCount i:" + i + " label:" + mEntries[i] / TimeDateUtil.TIME_MIN_INT);
        }
        return labelCount;
    }

    public int getInterval() {
        getLabelCount();
        return interval;
    }
}
