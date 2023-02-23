package com.yxc.chartlib.mpchart;

/**
 * @author yxc
 * @date 2019-10-31
 */
public class MPChartAttr {
    public boolean minYAxisZero;
    public float maxYAxisRatio;
    public boolean restrictMax;//限制YAxis中的最大值，用在配速图表中，一个点的值很大把配速图表给拉下来的情况。

    public boolean enableYAxisGridLine;
    public boolean enableXAxisGridLine;
    public boolean enableTimeXAxisLabel;//允许自定义的整数倍处理 TimeXAxis的 Label
    public boolean enableSportYAxisLabel;//允许自定义的 SportYAxis的Label
    public int empty = EMPTY_N; //0非空，1整块空，2 多柱子的灰色空。

    public final static int EMPTY_N = 0;
    public final static int EMPTY_TYPE_ONE = 1;
    public final static int EMPTY_TYPE_TWO = 2;

//     TYPE_FIX_MIN_ZERO = 0; Y轴从固定的0开始 到 max；
//     TYPE_FIX_MIN_POSITIVE = 1; 从 entryList的 真实的 min（min不能小于0）开始，到max；
//     TYPE_FIX_COMMON = 2; 从entryList的最小值min开始到max的最大值，无论最大、最小是否为Positive，例如海拔；
//     TYPE_FIX_RESTRICT_MAX = 3; 限制最大值，比如配速。Y轴 Invert，所以最小值min为大于等于 0 的Positive value；
    public int sportYAxisLabelType;
    public int sportYAxisReminder;//极值向上、向下取整的除数。
}
