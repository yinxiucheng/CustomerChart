package com.yxc.chartlib.formatter;


import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;

/**
 * @author yxc
 * @date 2019/4/13
 */
public class DefaultBarChartValueFormatter extends DefaultValueFormatter {
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     */
    public DefaultBarChartValueFormatter() {
        this(0);
    }

    public DefaultBarChartValueFormatter(int digits) {
        super(digits);
    }


    @Override
    public String getFormattedValue(float value) {
        return super.getFormattedValue(value);
    }

    public String getBarLabel(RecyclerBarEntry barEntry) {
        return barEntry.getY() > 0 ? getFormattedValue(barEntry.getY()) : "";
    }

}
