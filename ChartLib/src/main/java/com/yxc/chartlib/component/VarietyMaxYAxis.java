package com.yxc.chartlib.component;


import com.xiaomi.fitness.chart.attrs.BaseChartAttrs;

/**
 * @author yxc
 * @date 2019-05-23
 */
public class VarietyMaxYAxis<V extends BaseChartAttrs> extends YAxis {

    public VarietyMaxYAxis(V barChartAttrs) {
        super(barChartAttrs);
    }

    public static VarietyMaxYAxis getFixMaxYAxis(BaseChartAttrs attrs, float max){
        VarietyMaxYAxis axis = new VarietyMaxYAxis(attrs);
        axis.mAxisMaximum = max;
        axis.setLabelCount(4);
        return axis;
    }

    public static VarietyMaxYAxis getFixMaxYAxisWithCount(BaseChartAttrs attrs, float max, float min, int labelCount){
        VarietyMaxYAxis axis = new VarietyMaxYAxis(attrs);
        axis.mAxisMaximum = max;
        axis.mAxisMinimum = min;
        axis.setLabelCount(labelCount);
        axis.labelCount = labelCount;
        return axis;
    }

    public static VarietyMaxYAxis getFixMaxYAxis(BaseChartAttrs attrs, float max, float min){
        VarietyMaxYAxis axis = new VarietyMaxYAxis(attrs);
        axis.mAxisMaximum = max;
        axis.mAxisMinimum = min;
        axis.setLabelCount(4);
        return axis;
    }

    public static VarietyMaxYAxis getFixMaxYAxis(BaseChartAttrs attrs, float max, float min, int step){
        VarietyMaxYAxis axis = new VarietyMaxYAxis(attrs);
        axis.mAxisMaximum = max;
        axis.mAxisMinimum = min;
        axis.setLabelCount(4, step);
        return axis;
    }

    //注意跟 getFixMaxYAxis(BaseChartAttrs attrs, float max, float min) 区分调用。
    public static VarietyMaxYAxis getFixMaxYAxis2(BaseChartAttrs attrs, float max, int count) {
        VarietyMaxYAxis axis = new VarietyMaxYAxis(attrs);
        axis.mAxisMaximum = max;
        axis.setLabelCount(count);
        axis.labelCount = count;
        return axis;
    }

    public static VarietyMaxYAxis getFixMaxYAxis3(BaseChartAttrs attrs, float max, float min, int count) {
        VarietyMaxYAxis axis = new VarietyMaxYAxis(attrs);
        axis.mAxisMaximum = max;
        axis.mAxisMinimum = min;
        axis.setLabelCount(count);
        axis.labelCount = count;
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public VarietyMaxYAxis resetFixMaxYAxis(VarietyMaxYAxis axis, float max) {
        float axisMaximum = max;
        int layoutCount = 4;
        axis.setAxisMaximum(axisMaximum);
        axis.setLabelCount(layoutCount);
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public VarietyMaxYAxis resetFixMaxYAxis(VarietyMaxYAxis axis, float max, float min, int step) {
        axis.setAxisMaximum(max);
        axis.setAxisMinimum(min);
        axis.setLabelCount(4, step);
        return axis;
    }

    public VarietyMaxYAxis resetFixMaxYAxis(VarietyMaxYAxis axis, float max, int count) {
        axis.setAxisMaximum(max);
        axis.setLabelCount(count);
        axis.labelCount = count;
        return axis;
    }

    //获取Y轴刻度值
    public static VarietyMaxYAxis getYAxisChild(BaseChartAttrs attrs, float max) {
        VarietyMaxYAxis axis = new VarietyMaxYAxis(attrs);
        if (max == 0) {
            max = 4;
        } else {
            max = max + max / 4;
        }
        axis.mAxisMaximum = max;
        axis.setLabelCount(4);
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public VarietyMaxYAxis resetYAxis(VarietyMaxYAxis axis, float max) {
        max = Math.max(max, 4);
        if (max != 4) {
            max = max + max / 4;
        }
        float axisMaximum = max;
        int layoutCount = 4;
        axis.setAxisMaximum(axisMaximum);
        axis.setLabelCount(layoutCount);
        return axis;
    }

}
