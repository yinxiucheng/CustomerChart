package com.yxc.chartlib.mpchart.sleepchart;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * @author yxc
 * @date 2019-09-23
 */
public class SleepBarDataSet<T extends BarEntry> extends BarDataSet {

    public SleepBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }
}
