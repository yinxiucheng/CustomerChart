package com.yxc.chartlib.component;

import com.yxc.chartlib.attrs.BaseChartAttrs;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author yxc
 * @since 2019/4/8
 */
public class YAxis extends BaseYAxis {

    public int labelCount = 4; //Y轴刻度格数。

    public YAxis(BaseChartAttrs barChartAttrs) {
        super(barChartAttrs);
    }

    //获取Y轴刻度值
    public static YAxis getYAxis(BaseChartAttrs attrs, float axisMaximum, int layoutCount) {
        YAxis axis = new YAxis(attrs);
        axisMaximum = Math.max(axisMaximum, 4);
        axis.mAxisMaximum = axisMaximum;
        axis.setLabelCount(layoutCount);
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public YAxis resetYAxis(YAxis axis, float axisMaximum, int layoutCount) {
        axisMaximum = Math.max(axisMaximum, 4);
        axis.setAxisMaximum(axisMaximum);
        axis.setLabelCount(layoutCount);
        this.labelCount = layoutCount;
        return axis;
    }

    public HashMap<Float, Float> getYAxisScaleMap(float topLocation, float itemHeight, int count) {
        if (null == mEntries || mEntries.length == 0) {
            return new HashMap<>();
        }
        if (null == yAxisScaleMap) {
            yAxisScaleMap = new LinkedHashMap<>();
        } else {
            yAxisScaleMap.clear();
        }
        float location = topLocation;
        for (int i = 0; i <= count; i++) {
            if (i > 0) {
                location = location + itemHeight;
            }
            if (i < mEntries.length) {
                yAxisScaleMap.put(location, mEntries[i]);
            } else {
                //这里其实已经出错了，值的个数跟位置不匹配
                yAxisScaleMap.put(location, 0f);
            }
        }
        return yAxisScaleMap;
    }


    //获取Y轴刻度值
    public static YAxis getYAxisChild(BaseChartAttrs attrs, float max) {
        YAxis axis = new YAxis(attrs);
        if (max > 50000) {
            axis.mAxisMaximum = 80000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 30000) {
            axis.mAxisMaximum = 50000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 25000) {
            axis.mAxisMaximum = 30000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 20000) {
            axis.mAxisMaximum = 25000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 15000) {
            axis.mAxisMaximum = 20000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 10000) {
            axis.mAxisMaximum = 15000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 8000) {
            axis.mAxisMaximum = 10000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 6000) {
            axis.mAxisMaximum = 8000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 4000) {
            axis.mAxisMaximum = 6000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 3000) {
            axis.mAxisMaximum = 5000;
            axis.setLabelCount(5);
            axis.labelCount = 5;
        } else if (max > 2000) {
            axis.mAxisMaximum = 3000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 1500) {
            axis.mAxisMaximum = 2000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 1000) {
            axis.mAxisMaximum = 1500;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 800) {
            axis.mAxisMaximum = 1000;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 500) {
            axis.mAxisMaximum = 800;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 300) {
            axis.mAxisMaximum = 500;
            axis.labelCount = 4;
            axis.setLabelCount(4);
        } else if (max > 200) {
            axis.mAxisMaximum = 300;
            axis.labelCount = 4;
            axis.setLabelCount(4);
        } else if (max > 140) {
            axis.mAxisMaximum = 200;
            axis.labelCount = 4;
            axis.setLabelCount(4);
        } else if (max > 120) {
            axis.mAxisMaximum = 140;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 100) {
            axis.mAxisMaximum = 120;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 80) {
            axis.mAxisMaximum = 100;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 60) {
            axis.mAxisMaximum = 80;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 40) {
            axis.mAxisMaximum = 60;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 30) {
            axis.mAxisMaximum = 40;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 20) {
            axis.mAxisMaximum = 30;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 12) {
            axis.mAxisMaximum = 20;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 10) {
            axis.mAxisMaximum = 12;
            axis.labelCount = 4;
            axis.setLabelCount(4);
        } else if (max > 8) {
            axis.mAxisMaximum = 10;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else if (max > 5) {
            axis.mAxisMaximum = 8;
            axis.labelCount = 4;
            axis.setLabelCount(4);
        } else if (max > 3) {
            axis.mAxisMaximum = 5;
            axis.labelCount = 5;
            axis.setLabelCount(5);
        } else {
            axis.mAxisMaximum = 4;
            axis.labelCount = 4;
            axis.setLabelCount(4);
        }
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public YAxis resetYAxis(YAxis axis, float max) {
        float axisMaximum;
        if (max > 50000) {
            axisMaximum = 80000;
            labelCount = 5;
        } else if (max > 30000) {
            axisMaximum = 50000;
            labelCount = 5;
        } else if (max > 25000) {
            axisMaximum = 30000;
            labelCount = 5;
        } else if (max > 20000) {
            axisMaximum = 25000;
            labelCount = 5;
        } else if (max > 15000) {
            axisMaximum = 20000;
            labelCount = 5;
        } else if (max > 10000) {
            axisMaximum = 15000;
            labelCount = 5;
        } else if (max > 8000) {
            axisMaximum = 10000;
            labelCount = 5;
        } else if (max > 6000) {
            axisMaximum = 8000;
            labelCount = 5;
        } else if (max > 4000) {
            axisMaximum = 6000;
            labelCount = 5;
        } else if (max > 3000) {
            axisMaximum = 5000;
            labelCount = 5;
        } else if (max > 2000) {
            axisMaximum = 3000;
            labelCount = 5;
        } else if (max > 1500) {
            axisMaximum = 2000;
            labelCount = 5;
        } else if (max > 1000) {
            axisMaximum = 1500;
            labelCount = 5;
        } else if (max > 800) {
            axisMaximum = 1000;
            labelCount = 5;
        } else if (max > 500) {
            axisMaximum = 800;
            labelCount = 4;
        } else if (max > 300) {
            axisMaximum = 500;
            labelCount = 4;
        } else if (max > 200) {
            axisMaximum = 300;
            labelCount = 4;
        } else if (max > 140) {
            axisMaximum = 200;
            labelCount = 4;
        } else if (max > 120) {
            axisMaximum = 140;
            labelCount = 5;
        } else if (max > 100) {
            axisMaximum = 120;
            labelCount = 5;
        } else if (max > 80) {
            axisMaximum = 100;
            labelCount = 5;
        } else if (max > 60) {
            axisMaximum = 80;
            labelCount = 5;
        } else if (max > 40) {
            axisMaximum = 60;
            labelCount = 5;
        } else if (max > 30) {
            axisMaximum = 40;
            labelCount = 5;
        } else if (max > 20) {
            axisMaximum = 30;
            labelCount = 5;
        } else if (max > 12) {
            axisMaximum = 20;
            labelCount = 5;
        } else if (max > 10) {
            axisMaximum = 12;
            labelCount = 4;
        } else if (max > 8) {
            axisMaximum = 10;
            labelCount = 5;
        } else if (max > 5) {
            axisMaximum = 8;
            labelCount = 4;
        } else if (max > 3) {
            axisMaximum = 5;
            labelCount = 5;
        } else {
            axisMaximum = 4;
            labelCount = 4;
        }
        if (axisMaximum != mAxisMaximum) {
            axis.setAxisMaximum(axisMaximum);
            axis.setLabelCount(labelCount);
            return axis;
        }
        return axis;
    }


    @Override
    public int getLabelCount() {
        return labelCount;
    }

}
