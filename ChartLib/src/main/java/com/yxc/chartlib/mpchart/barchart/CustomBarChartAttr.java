package com.yxc.chartlib.mpchart.barchart;


import com.yxc.chartlib.mpchart.MPChartAttr;

/**
 * @author yxc
 * @date 2019-10-31
 */
public class CustomBarChartAttr extends MPChartAttr {
    public final static int TYPE_BAR_CHART_FIRST = 0;
    public final static int TYPE_BAR_CHART_SECOND = 1;//PAI
    public final static int TYPE_BAR_CHART_THIRD = 2;//VOMax
    public final static int TYPE_BAR_CHART_FOUR = 3;//curse
    public final static int TYPE_BAR_CHART_FIVE = 4;//energy
    public final static int TYPE_BAR_CHART_SIX = 5;//睡眠
    public final static int TYPE_BAR_CHART_SEVEN = 6;//segment Chart, 修改Buffer、ChartEntry
    public final static int TYPE_BAR_CHART_TRAINING_LOAD = 7;//Training load
    public final static int TYPE_BAR_CHART_RUNNING_INDICATOR = 8;//Running Indicator

    public float mRectHeight;
    public float mBarDutyCycle;
    public float mRectRadius;
    public int fillEndColor;
    public int mDoneColor;
    public float mDoneAlpha;
    public int mNoneDoneColor;
    public float mNoneDoneAlpha;
    public int mMaxPoints;
    public int barChartType;
}
