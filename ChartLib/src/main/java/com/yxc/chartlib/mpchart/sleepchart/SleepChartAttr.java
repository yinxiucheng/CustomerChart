package com.yxc.chartlib.mpchart.sleepchart;


import com.yxc.chartlib.mpchart.MPChartAttr;

/**
 * @author yxc
 * @date 2019-12-02
 */
public class SleepChartAttr extends MPChartAttr {
    public final static int SLEEP_CHART_TYPE_DEF = 0;
    public final static int SLEEP_CHART_TYPE_FIRST = 1;// 睡眠图仅仅颜色区分 sleep item type，不同的item 都是同一 y高度。

    public int sleepChartType = SLEEP_CHART_TYPE_DEF;
}
