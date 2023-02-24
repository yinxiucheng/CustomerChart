package com.yxc.chartlib.mpchart.sleepchart;

import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.yxc.chartlib.entrys.SleepItemEntry;
import com.yxc.chartlib.entrys.model.SleepItemTime;

/**
 * @author yxc
 * @date 2019-09-23
 */
public class SleepBuffer extends BarBuffer {

    float mYChartMax;
    int mSleepChartType;

    public SleepBuffer(int size, int dataSetCount, float yChartMax, int sleepChartType) {
        super(size, dataSetCount, false);
        mYChartMax = yChartMax;
        this.mSleepChartType = sleepChartType;
    }

    @Override
    public void feed(IBarDataSet data) {

        float size = data.getEntryCount() * phaseX;

        for (int i = 0; i < size; i++) {
            BarEntry entry = data.getEntryForIndex(i);
            if (entry == null)
                continue;

            SleepItemEntry e;
            if (entry instanceof SleepItemEntry) {
                e = (SleepItemEntry) entry;
            } else {
                continue;
            }

            float x = e.getX();
            float y = e.getY();

            SleepItemTime sleepItemTime = e.sleepItemTime;
            int barWidth = sleepItemTime.durationTimeSed;
            if (barWidth == 0) {
                continue;
            }

            int left = (int) x;
            int right = (int) (x + barWidth);

            SleepChartValue chartValue = getSleepChartValue(left, right, sleepItemTime.sleepType, 4);
            if (mSleepChartType == SleepChartAttr.SLEEP_CHART_TYPE_FIRST){
                chartValue = getSleepChartValue1(left, right, sleepItemTime.sleepType, 4);
            }
            int bottom = (int) chartValue.bottom;
            int top = (int) chartValue.top;

            // multiply the height of the rect with the phase
            if (top > 0)
                top *= phaseY;
            else
                bottom *= phaseY;

            addBar(left, top, right, bottom);
        }
        reset();
    }

    private SleepChartValue getSleepChartValue1(float start, float end, int sleepType, int mYChartMax) {
        SleepChartValue sleepChartValue = new SleepChartValue();
        sleepChartValue.start = start;
        sleepChartValue.end = end;
        float top;
        float bottom;
        if (sleepType == SleepItemTime.TYPE_WAKE
                || sleepType == SleepItemTime.TYPE_EYES_MOVE
                || sleepType == SleepItemTime.TYPE_SLUMBER
                || sleepType == SleepItemTime.TYPE_DEEP_SLEEP) {
            top = mYChartMax;
            bottom = 0;
        } else {
            top = 0;
            bottom = 0;
        }
        sleepChartValue.top = top;
        sleepChartValue.bottom = bottom;
        return sleepChartValue;
    }

    private SleepChartValue getSleepChartValue(float start, float end, int sleepType, int mYChartMax) {
        SleepChartValue sleepChartValue = new SleepChartValue();
        sleepChartValue.start = start;
        sleepChartValue.end = end;

        float top;
        float bottom;
        if (sleepType == SleepItemTime.TYPE_WAKE) {
            top = mYChartMax;
            bottom = mYChartMax * 3 / 4;
        } else if (sleepType == SleepItemTime.TYPE_EYES_MOVE) {
            top = mYChartMax * 3 / 4;
            bottom = mYChartMax / 2;
        } else if (sleepType == SleepItemTime.TYPE_SLUMBER) {
            top = mYChartMax / 2;
            bottom = mYChartMax / 4;
        } else if (sleepType == SleepItemTime.TYPE_DEEP_SLEEP) {
            top = mYChartMax / 4;
            bottom = 0;
        } else {
            top = 0;
            bottom = 0;
        }
        sleepChartValue.top = top;
        sleepChartValue.bottom = bottom;
        return sleepChartValue;
    }
}
