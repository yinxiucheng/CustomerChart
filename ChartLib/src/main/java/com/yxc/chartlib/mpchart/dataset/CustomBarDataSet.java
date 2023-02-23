package com.yxc.chartlib.mpchart.dataset;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * @author yxc
 * @date 2019-10-31
 */
public class CustomBarDataSet<T extends Entry> extends BarDataSet {

    public CustomBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }
}
