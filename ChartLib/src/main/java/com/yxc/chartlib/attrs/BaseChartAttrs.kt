package com.yxc.chartlib.attrs

/**
 * @author yxc
 * @date 2019-05-10
 */
open class BaseChartAttrs {
    @JvmField
    var contentPaddingBottom //底部内容绘制高度
            = 0f
    @JvmField
    var contentPaddingTop //顶部内容绘制
            = 0f
    @JvmField
    var displayNumbers //一屏显示多少个 chart
            = 0
    @JvmField
    var averageDisplay //画柱子时剩余的宽度分给部分柱子
            = false
    @JvmField
    var highLightRoundRectRadius = 0f
    @JvmField
    var barChartRadius = 0f
    @JvmField
    var ratioVelocity //recyclerView 惯性滑动的 加速度 比率。
            = 0.0
    @JvmField
    var ratioSpeed //LinearLayoutManager 速度的 比率。
            = 0.0
    @JvmField
    var layoutManagerOrientation //layout  orientation
            = 0
    @JvmField
    var layoutManagerReverseLayout //layout horizontal layout from right to left, default is true;
            = false
    @JvmField
    var yAxisZeroLineType //default == 0, from the left of the view to the right, else value == 1 from
            = 0
    @JvmField
    var enableYAxisLineDash //控制 Y轴用虚线
            = false
    @JvmField
    var enableYAxisZero // 控制是否显示 Y轴中的 0 刻度线
            = false
    @JvmField
    var enableYAxisGridLine // 控制是否显示 y轴对应的横轴网格线
            = false
    @JvmField
    var enableStartYAxisLabel //控制是否显示 Y轴右刻度
            = false
    @JvmField
    var enableEndYAxisLabel // 控制是否显示 Y轴左刻度
            = false
    @JvmField
    var enableXAxisGridLine //控制X对应的纵轴的网格线
            = false
    @JvmField
    var enableXAxisFirstGridLine //控制X对应的First_line网格线
            = false
    @JvmField
    var enableXAxisSecondGridLine //控制X对应的Second_line网格线
            = false
    @JvmField
    var enableXAxisThirdGridLine //控制X对应的Third_line网格线
            = false
    @JvmField
    var enableXAxisLabel //控制X轴刻度的绘制
            = false
    @JvmField
    var enableXAxisDisplayLabel //控制无规格的X轴刻度，供首页显示用。
            = false
    @JvmField
    var enableXAxisLineCircle //控制 折线图 value处的圈
            = false
    @JvmField
    var enableXAxisBg //允许 画底部X坐标处的背景颜色。
            = false
    @JvmField
    var enableBarBorder //控制是否显示边框
            = false
    @JvmField
    var enableCharValueDisplay //控制是否显示顶部的 value值
            = false
    @JvmField
    var enableScrollToScale //控制是否回溯到分界线处
            = false
    @JvmField
    var enableValueMark //控制柱状图顶部markView的显示
            = false
    @JvmField
    var enableEndDayXAxis //当天最后一个柱子是否显示XAxis
            = false
    @JvmField
    var yAxisMaximum //y轴刻度默认的最大刻度
            = 0f
    @JvmField
    var yAxisMinimum //y轴刻度默认的最小刻度
            = 0f
    @JvmField
    var yAxisLabelTxtSize //y轴刻度字体大小
            = 0f
    @JvmField
    var yAxisLabelTxtColor //y轴字体颜色
            = 0
    @JvmField
    var yAxisLabelSize //y轴刻度的格数
            = 0
    @JvmField
    var yAxisLineColor //y轴对应的网格线的颜色
            = 0
    @JvmField
    var yAxisLabelHorizontalPadding //刻度字跟边框的间距
            = 0f
    @JvmField
    var yAxisLabelVerticalPadding //刻度 字跟刻度线的位置对齐的调整
            = 0f
    @JvmField
    var yAxisReverse //y坐标 reverse，value从上到下增大。
            = false
    @JvmField
    var recyclerPaddingLeft //原始RecyclerView的 paddingLeft 值
            = 0f
    @JvmField
    var recyclerPaddingRight //原始RecyclerView的 paddingRight 值
            = 0f
    @JvmField
    var xAxisTxtSize //x轴刻度字体大小
            = 0f
    @JvmField
    var xAxisTxtColor //x轴刻度字体颜色
            = 0
    @JvmField
    var xAxisFirstDividerColor //x轴对应纵轴 第一种网格线颜色
            = 0
    @JvmField
    var xAxisSecondDividerColor //x轴对应纵轴 第二种网格线颜色
            = 0
    @JvmField
    var xAxisThirdDividerColor //x轴对应纵轴 第三种网格线颜色
            = 0
    @JvmField
    var xAxisLabelTxtPadding //x轴刻度跟 坐标线之间的间距（不居中的情况下)
            = 0f
    @JvmField
    var xAxisScaleDistance //x轴刻度文字的间距
            = 0
    @JvmField
    var xAxisBgRadius //X轴背景底部圆角半径
            = 0f
    @JvmField
    var xAxisLabelPosition //X轴坐标的位置，XAxis POSITION_CENTER = 0(default), POSITION_LEFT = 1, POSITION_RIGHT= 2;
            = 0
    @JvmField
    var xFirstLinePosition //X GrandLine 大周期线 POSITION_LEFT = 1, POSITION_RIGHT= 2; 默认靠左
            = 0
    @JvmField
    var xAxisLabelFont //x 周坐标字体
            = 0
    @JvmField
    var xAxisForbidDealEndBoundary = false
    @JvmField
    var barBorderColor //边框颜色
            = 0
    @JvmField
    var barBorderBgColor //背景颜色
            = 0
    @JvmField
    var barBorderWidth //边框的宽度
            = 0f
    @JvmField
    var barSpace //chart item中 space 占比，能够控制barchart的宽度
            = 0f
    @JvmField
    var barChartMaxWidth = 0f
    @JvmField
    var chartColor //图的基础颜色
            = 0
    @JvmField
    var chartEdgeColor //边界 chart 滑入时的过度颜色，这里以后需要用渐变动画来控制。
            = 0
    @JvmField
    var invalidChartColor = 0
    @JvmField
    var txtColor = 0
    @JvmField
    var txtSize = 0f

    //line ,Bezier曲线公用
    @JvmField
    var lineShaderBeginColor //fill底部的开始的深的颜色
            = 0
    @JvmField
    var lineShaderEndColor //fill底部的结束的浅的颜色
            = 0
    @JvmField
    var enableLineFill //底部fill
            = false
    @JvmField
    var fillAlpha //
            = 0
    @JvmField
    var lineSelectCircles //选中时绘制的 circles个数
            = 0
    @JvmField
    var yAxisHighStandardLine //高的 标准线
            = 0f
    @JvmField
    var yAxisMiddleStandardLine // 中间的 标准线
            = 0f
    @JvmField
    var yAxisLowStandardLine //低的 标准线
            = 0f
    @JvmField
    var isDisplay //区分数据首页显示的简易Chart还是 二级页面中的Chart
            = false
    @JvmField
    var highLightColor = 0
    @JvmField
    var dynamicAdjustItemFillWidth = false
    @JvmField
    var highLightBigTextSize = 0f
    @JvmField
    var highLightSmallTextSize = 0f
}