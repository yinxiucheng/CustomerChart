package com.yxc.chartlib.entrys;

import com.yxc.fitness.chart.entrys.SegmentBarEntry;
import com.yxc.chartlib.entrys.model.SegmentRectModel;

import java.util.List;

public class BloodPressureEntry extends SegmentBarEntry {

    public int maxHighPressure;

    public int minHighPressure;

    public int maxLowPressure;

    public int minLowPressure;

    public BloodPressureEntry(float[] values, int segmentRange) {
        super(values, segmentRange);
    }

    public BloodPressureEntry(List<SegmentRectModel> rectValueModelList, int segmentRange) {
        super(rectValueModelList, segmentRange);
    }

    public BloodPressureEntry(int i, int y, long startTime, int type) {
        super(i, y, startTime, type);
    }
}
