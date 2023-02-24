package com.yxc.customerchart.rxutils;


import com.github.mikephil.charting.components.YAxis;
import com.yxc.chartlib.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

public class VolFormatter extends YAxisValueFormatter {
    private final int unit;
    private DecimalFormat mFormat;
    private String u;

    public VolFormatter(int unit) {
        super(unit);
        if (unit == 1) {
            mFormat = new DecimalFormat("#0");
        } else {
            mFormat = new DecimalFormat("#0.00");
        }
        this.unit = unit;
        this.u=MyUtils.getVolUnit(unit);
    }

    @Override
    public String getFormattedValue(float value) {
        value = value / unit;
        if(value==0){
            return u;
        }
        return mFormat.format(value);
    }
}
