package com.yxc.chartlib.mpchart.barchart;

import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.yxc.fitness.chart.entrys.SegmentBarEntry;
import com.yxc.chartlib.entrys.model.SegmentRectModel;

import java.util.List;


public class SegmentBarChartBuffer extends BarBuffer {

    public SegmentBarChartBuffer(int size, int dataSetCount, boolean containsStacks) {
        super(size, dataSetCount, containsStacks);
    }

    @Override
    public void feed(IBarDataSet data) {
        float size = data.getEntryCount() * phaseX;
        float barWidthHalf = mBarWidth / 2f;

        for (int i = 0; i < size; i++) {
            BarEntry entry = data.getEntryForIndex(i);
            if (entry == null)
                continue;

            SegmentBarEntry e;
            if (entry instanceof SegmentBarEntry) {
                e = (SegmentBarEntry) entry;
            } else {
                continue;
            }

            float x = e.getX();
            float rectLeft = x - barWidthHalf;
            float rectRight = x + barWidthHalf;

            List<SegmentRectModel> rectModelList = e.rectValueModelList;
            for (SegmentRectModel rectModel : rectModelList) {
                addBar(rectLeft, rectModel.topValue, rectRight, rectModel.bottomValue);
            }
        }
        reset();
    }
}
