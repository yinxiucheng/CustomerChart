package com.yxc.chartlib.attrs;

/**
 * @author yxc
 * @date 2019-06-25
 */
public class LineChartAttrs extends BaseChartAttrs {
    public static final int HIGH_LIGHT_LINE_BOTTOM = 1;//高亮线从bottom处开始画
    public static final int HIGH_LIGHT_LINE_Y = 2;//高亮线从y值处开始画
    public static final int HIGH_LIGHT_LINE_BOTTOM_Y = 3;//高亮线从bottom处开始画,到Y值处
    public static final int HIGH_LIGHT_LINE_NO_CROSS_SELECT = 4;//不穿过Select Circle

    public float linePointRadius;//point的
    public float linePointSelectRadius;//选中的 SelectRadius
    public float linePointSelectStrokeWidth;//选中的点外层圈的 paint的strokeWidth。
    public int lineStrokeWidth;//line的width 宽度
    public int lineColor;//线的颜色
    public int lineHighLightType;//分从底部开始画，从y值处开始画。

    public int lineMaxColor;// Max线的颜色
    public int lineMinColor;// Min线的颜色

    public boolean linePointSelectCircleStroke;
}
