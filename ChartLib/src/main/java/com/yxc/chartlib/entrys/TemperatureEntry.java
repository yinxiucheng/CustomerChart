package com.yxc.chartlib.entrys;

import com.yxc.fitness.chart.entrys.SegmentBarEntry;
import com.yxc.chartlib.entrys.model.SegmentRectModel;

import java.util.List;

public class TemperatureEntry extends SegmentBarEntry {

    public float maxBodyValue;

    public float minBodyValue;

    public float maxSkinValue;

    public float minSkinValue;

    public float getValidMinValue() {
        if (minBodyValue == 0 && minSkinValue == 0) {
            return 0f;
        } else if (minBodyValue == 0) {
            return minSkinValue;
        } else if (minSkinValue == 0) {
            return minBodyValue;
        } else {
            return Math.min(minBodyValue, minSkinValue);
        }
    }

    public TemperatureEntry(float[] values, int segmentRange) {
        super(values, segmentRange);
    }

    public TemperatureEntry(List<SegmentRectModel> rectValueModelList, int segmentRange) {
        super(rectValueModelList, segmentRange);
    }

    public TemperatureEntry(int i, int y, long startTime, int type) {
        super(i, y, startTime, type);
    }
}
