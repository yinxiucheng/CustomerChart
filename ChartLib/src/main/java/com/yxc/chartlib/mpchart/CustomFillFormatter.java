package com.yxc.chartlib.mpchart;

import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

/**
 * @author yxc
 * @since  2019-12-02
 */
public class CustomFillFormatter extends DefaultFillFormatter {

    @Override
    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {

        float fillMin;
        float chartMinY = dataProvider.getYChartMin();

        if (dataSet.getYMax() > 0 && dataSet.getYMin() < 0) {
            fillMin = dataSet.getYMin();
        } else {
            fillMin = chartMinY;
        }
        return fillMin;
    }
}
