package com.yxc.chartlib.component;

import com.xiaomi.fitness.chart.attrs.BarChartAttrs;
import com.xiaomi.fitness.chart.attrs.BaseChartAttrs;

/**
 * @author yxc
 * @since 2019/4/8
 */
public class SleepYAxis extends YAxis {

    public SleepYAxis(BaseChartAttrs barChartAttrs) {
        super(barChartAttrs);
    }

    //获取Y轴刻度值
    public static SleepYAxis getYAxis(BarChartAttrs attrs, float max) {
        SleepYAxis axis = new SleepYAxis(attrs);
        if (max < 12) {
            axis.mAxisMaximum = 12;
            axis.setLabelCount(6);
        } else if (max < 10) {
            axis.mAxisMaximum = 10;
            axis.setLabelCount(5);
        } else {
            int maxInt = Math.round(max);
            int distance = maxInt / 6;
            axis.mAxisMaximum = maxInt + distance;
            axis.setLabelCount(7);
        }
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public SleepYAxis resetYAxis(SleepYAxis axis, float max) {
        float axisMaximum;
        int layoutCount;
        if (max < 12) {
            axisMaximum = 12;
            layoutCount = 6;
        } else if (max < 10) {
            axisMaximum = 10;
            layoutCount = 5;
        } else {
            int maxInt = Math.round(max);
            int distance = maxInt / 6;
            axisMaximum = maxInt + distance;
            layoutCount = 7;
        }
        if (axisMaximum != mAxisMaximum) {
            axis.setAxisMaximum(axisMaximum);
            axis.setLabelCount(layoutCount);
            return axis;
        }
        return axis;
    }
}
