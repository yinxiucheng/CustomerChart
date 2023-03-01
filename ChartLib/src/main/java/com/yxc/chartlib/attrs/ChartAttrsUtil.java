package com.yxc.chartlib.attrs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.mpchart.barchart.CustomBarChartAttr;
import com.yxc.chartlib.mpchart.linechart.CustomLineChartAttr;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.ColorUtil;
import com.yxc.chartlib.utils.DisplayUtil;

public class ChartAttrsUtil {

    public static SleepChartAttrs getSleepChartAttrs(Context context, AttributeSet attributeSet, boolean isDaytimeSleep) {
        SleepChartAttrs attrs = new SleepChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.SleepChartRecyclerView);
        attrs.ratioSpeed = ta.getFloat(R.styleable.SleepChartRecyclerView_layoutManagerOrientation, 1f);
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.SleepChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.SleepChartRecyclerView_layoutManagerReverseLayout, true);

        attrs.deepSleepColor = ta.getColor(R.styleable.SleepChartRecyclerView_deepSleepColor, ColorUtil.getResourcesColor(R.color.sleep_deep));
        attrs.slumberColor = ta.getColor(R.styleable.SleepChartRecyclerView_slumberColor, ColorUtil.getResourcesColor(R.color.sleep_slumber));
        attrs.eyeMoveColor = ta.getColor(R.styleable.SleepChartRecyclerView_eyeMoveColor, ColorUtil.getResourcesColor(R.color.sleep_eye_move));
        attrs.weakColor = ta.getColor(R.styleable.SleepChartRecyclerView_weakColor, ColorUtil.getResourcesColor(R.color.sleep_wake));
        attrs.otherColor = ta.getColor(R.styleable.SleepChartRecyclerView_otherColor, ColorUtil.getResourcesColor(R.color.sleep_other_color));
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.SleepChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(80));

        attrs.contentPaddingTop = ta.getDimension(R.styleable.SleepChartRecyclerView_contentPaddingTop, DisplayUtil.dip2px(5));
        attrs.txtColor = ta.getColor(R.styleable.SleepChartRecyclerView_txtColor, ContextCompat.getColor(context, R.color.chart_common_txt_color));
        attrs.txtSize = ta.getDimension(R.styleable.SleepChartRecyclerView_txtSize, DisplayUtil.dip2px(10));
        attrs.sleepItemHeight = ta.getDimension(R.styleable.SleepChartRecyclerView_sleepItemHeight, DisplayUtil.dip2px(50));
        attrs.highLightRoundRectRadius = ta.getDimension(R.styleable.SleepChartRecyclerView_highLightRoundRectRadius, DisplayUtil.dip2px(14));
        attrs.barChartRadius = ta.getDimension(R.styleable.SleepChartRecyclerView_barChartRadius, DisplayUtil.dip2px(12));
        attrs.xAxisLabelFont = ta.getResourceId(R.styleable.SleepChartRecyclerView_xAxisLabelFont, -1);

        attrs.enableValueMark = ta.getBoolean(R.styleable.SleepChartRecyclerView_enableValueMark, true);
        attrs.enableEndDayXAxis = ta.getBoolean(R.styleable.SleepChartRecyclerView_enableEndDayXAxis, false);
        attrs.yAxisLineColor = ta.getColor(R.styleable.SleepChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.barChartMaxWidth = ta.getDimension(R.styleable.SleepChartRecyclerView_barChartMaxWidth,  0);

        ta.recycle();
        return attrs;
    }

    public static BarChartAttrs getBarChartRecyclerAttrs(Context context, AttributeSet attributeSet) {
        BarChartAttrs attrs = new BarChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BarChartRecyclerView);

        attrs.barBorderColor = ta.getColor(R.styleable.BarChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.chartColor = ta.getColor(R.styleable.BarChartRecyclerView_chartColor, ColorUtil.getResourcesColor(R.color.bar_chart_pink));
        attrs.invalidChartColor = ta.getColor(R.styleable.BarChartRecyclerView_invalidChartColor, ColorUtil.getResourcesColor(R.color.bar_chart_pink));
        attrs.highLightColor = ta.getColor(R.styleable.BarChartRecyclerView_highLightColor, ColorUtil.getResourcesColor(R.color.chart_highlight_line_color));
        attrs.chartEdgeColor = ta.getColor(R.styleable.BarChartRecyclerView_chartEdgeColor, ColorUtil.getResourcesColor(R.color.text_color_10));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.BarChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.BarChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));
        attrs.barSpace = ta.getFloat(R.styleable.BarChartRecyclerView_barSpace, 0.5f);
        attrs.barChartMaxWidth = ta.getDimension(R.styleable.BarChartRecyclerView_barChartMaxWidth, 0);
        attrs.highLightRoundRectRadius = ta.getDimension(R.styleable.BarChartRecyclerView_highLightRoundRectRadius, DisplayUtil.dip2px(14));
        attrs.barChartRadius = ta.getDimension(R.styleable.BarChartRecyclerView_barChartRadius, DisplayUtil.dip2px(12));

        //BarChart Value
        attrs.barChartValueTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartValueTxtColor, ColorUtil.getResourcesColor(R.color.text_color));
        attrs.barChartValueTxtSize = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValueTxtSize, DisplayUtil.sp2px(context, 12));
        attrs.barChartValueTxtMaskColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartValueTxtMaskColor, ColorUtil.getResourcesColor(R.color.common_white));
        attrs.barChartValueTxtMaskSize = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValueTxtMaskSize, DisplayUtil.sp2px(context, 13));
        attrs.barChartValuePaddingBottom = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValuePaddingBottom, DisplayUtil.dip2px(3));
        attrs.barChartValuePaddingLeft = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValuePaddingLeft, DisplayUtil.dip2px(2));
        attrs.barBorderWidth = ta.getDimension(R.styleable.BarChartRecyclerView_barBorderWidth, 0.75f);
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.BarChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(41));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.BarChartRecyclerView_contentPaddingTop, 0f);
        attrs.displayNumbers = ta.getInteger(R.styleable.BarChartRecyclerView_displayNumbers, 12);
        attrs.barChartRoundRectRadiusRatio = ta.getFloat(R.styleable.BarChartRecyclerView_barChartRoundRectRadiusRatio, 1.0f / 4);

        //Switch Button
        attrs.enableBarBorder = ta.getBoolean(R.styleable.BarChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_enableCharValueDisplay, false);
        attrs.enableEndYAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableEndYAxisLabel, true);
        attrs.enableStartYAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableStartYAxisLabel, true);
        attrs.enableYAxisGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisZero, true);
        attrs.enableYAxisLineDash = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisLineDash, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.BarChartRecyclerView_enableScrollToScale, true);
        attrs.enableValueMark = ta.getBoolean(R.styleable.BarChartRecyclerView_enableValueMark, true);
        attrs.enableEndDayXAxis = ta.getBoolean(R.styleable.BarChartRecyclerView_enableEndDayXAxis, false);
        attrs.enableXAxisDisplayLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisDisplayLabel, false);
        attrs.enableXAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableXAxisFirstGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisFirstGridLine, false);
        attrs.enableXAxisSecondGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisSecondGridLine, false);
        attrs.enableXAxisThirdGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisThirdGridLine, false);
        attrs.enableXAxisLineCircle = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisLineCircle, true);
        attrs.enableXAxisBg = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisBg, true);
        attrs.averageDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisLabel, false);

        attrs.yAxisZeroLineType = ta.getInteger(R.styleable.BarChartRecyclerView_yAxisZeroLineType, 0);
        attrs.ratioVelocity = ta.getFloat(R.styleable.BarChartRecyclerView_ratioVelocity, 0.5f);
        attrs.ratioSpeed = ta.getFloat(R.styleable.BarChartRecyclerView_ratioSpeed, 1f);
        //default is horizontal
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.BarChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.BarChartRecyclerView_layoutManagerReverseLayout, true);

        //YAxis
        attrs.yAxisMaximum = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisMaximum, 30000);
        attrs.yAxisMinimum = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisMinimum, 0);
        attrs.yAxisHighStandardLine = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisHighStandardLine, -1);
        attrs.yAxisMiddleStandardLine = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisMiddleStandardLine, -1);
        attrs.yAxisLowStandardLine = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisLowStandardLine, -1);
        attrs.yAxisLabelTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_yAxisLabelTxtColor, ColorUtil.getResourcesColor(R.color.chart_axis_text_color));
        attrs.yAxisLabelTxtSize = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelTxtSize, DisplayUtil.dip2px(10));
        attrs.yAxisLabelSize = ta.getInteger(R.styleable.BarChartRecyclerView_yAxisLabelSize, 5);
        attrs.yAxisLineColor = ta.getColor(R.styleable.BarChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.yAxisLabelHorizontalPadding = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelHorizontalPadding, DisplayUtil.dip2px(5));
        attrs.yAxisLabelVerticalPadding = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelVerticalPadding, DisplayUtil.dip2px(3));
        attrs.yAxisReverse = ta.getBoolean(R.styleable.BarChartRecyclerView_yAxisReverse, false);

        //XAxis
        attrs.xAxisFirstDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisFirstDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisSecondDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisSecondDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisThirdDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisThirdDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisTxtColor, ColorUtil.getResourcesColor(R.color.chart_axis_text_color));
        attrs.xAxisTxtSize = ta.getDimension(R.styleable.BarChartRecyclerView_xAxisTxtSize, DisplayUtil.INSTANCE.sp2px(context, 10));
        attrs.xAxisLabelTxtPadding = ta.getDimension(R.styleable.BarChartRecyclerView_xAxisLabelTxtPadding, DisplayUtil.dip2px(2));
        attrs.xAxisLabelPosition = ta.getInteger(R.styleable.BarChartRecyclerView_xAxisLabelPosition, XAxis.POSITION_CENTER);
        attrs.xFirstLinePosition = ta.getInteger(R.styleable.BarChartRecyclerView_xFirstLinePosition, XAxis.POSITION_LEFT);
        attrs.xAxisScaleDistance = ta.getInteger(R.styleable.BarChartRecyclerView_xAxisScaleDistance, 6);
        attrs.xAxisBgRadius = ta.getDimension(R.styleable.BarChartRecyclerView_xAxisBgRadius, DisplayUtil.dip2px(14));
        attrs.xAxisLabelFont = ta.getResourceId(R.styleable.BarChartRecyclerView_xAxisLabelFont, -1);

        //line fill color
        attrs.lineShaderBeginColor = ta.getColor(R.styleable.BarChartRecyclerView_lineShaderBeginColor, ColorUtil.getResourcesColor(R.color.rate_shader_begin));
        attrs.lineShaderEndColor = ta.getColor(R.styleable.BarChartRecyclerView_lineShaderEndColor, ColorUtil.getResourcesColor(R.color.rate_shader_end));
        attrs.enableLineFill = ta.getBoolean(R.styleable.BarChartRecyclerView_enableLineFill, true);
        attrs.fillAlpha = ta.getInteger(R.styleable.BarChartRecyclerView_fillAlpha, 0xA0);

        //highLight
        attrs.isDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_isDisplay, false);
        attrs.highLightBigTextSize = ta.getDimension(R.styleable.BarChartRecyclerView_highLightBigTextSize, DisplayUtil.dip2px(24));
        attrs.highLightSmallTextSize = ta.getDimension(R.styleable.BarChartRecyclerView_highLightSmallTextSize, DisplayUtil.dip2px(12));

        attrs.rateChartDarkColor = ta.getColor(R.styleable.BarChartRecyclerView_rateChartDarkColor, ColorUtil.getResourcesColor(R.color.rate_title_select_txt));
        attrs.rateChartLightColor = ta.getColor(R.styleable.BarChartRecyclerView_rateChartLightColor, ColorUtil.getResourcesColor(R.color.rate_chart_color_light));
        attrs.enableAverageRectF = ta.getBoolean(R.styleable.BarChartRecyclerView_enableAverageRectF, true);

        attrs.barChartSegment = ta.getInteger(R.styleable.BarChartRecyclerView_barChartSegment, BarChartAttrs.SEGMENT_N);
        attrs.dynamicAdjustItemFillWidth = ta.getBoolean(R.styleable.BarChartRecyclerView_dynamicAdjustItemFillWidth,false);

        ta.recycle();
        return attrs;
    }

    public static LineChartAttrs getLineChartRecyclerAttrs(Context context, AttributeSet attributeSet) {
        LineChartAttrs attrs = new LineChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.LineChartRecyclerView);

        attrs.barBorderColor = ta.getColor(R.styleable.LineChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.barBorderBgColor = ta.getColor(R.styleable.LineChartRecyclerView_barBorderBgColor, ColorUtil.getResourcesColor(R.color.rate_chart_bg));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.LineChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.LineChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));
        attrs.chartColor = ta.getColor(R.styleable.LineChartRecyclerView_chartColor, ColorUtil.getResourcesColor(R.color.bar_chart_pink));
        attrs.highLightColor = ta.getColor(R.styleable.LineChartRecyclerView_highLightColor, ColorUtil.getResourcesColor(R.color.chart_highlight_line_color));

        attrs.barSpace = ta.getFloat(R.styleable.LineChartRecyclerView_barSpace, 0.5f);
        attrs.highLightRoundRectRadius = ta.getDimension(R.styleable.LineChartRecyclerView_highLightRoundRectRadius, DisplayUtil.dip2px(14));
        attrs.barChartRadius = ta.getDimension(R.styleable.LineChartRecyclerView_barChartRadius, DisplayUtil.dip2px(12));

        //BarChart Value
        attrs.barBorderWidth = ta.getDimension(R.styleable.LineChartRecyclerView_barBorderWidth, 0.75f);
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.LineChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(41));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.LineChartRecyclerView_contentPaddingTop, 0f);
        attrs.displayNumbers = ta.getInteger(R.styleable.LineChartRecyclerView_displayNumbers, 12);

        //Switch Button
        attrs.enableBarBorder = ta.getBoolean(R.styleable.LineChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.LineChartRecyclerView_enableCharValueDisplay, true);
        attrs.enableEndYAxisLabel = ta.getBoolean(R.styleable.LineChartRecyclerView_enableEndYAxisLabel, true);
        attrs.enableStartYAxisLabel = ta.getBoolean(R.styleable.LineChartRecyclerView_enableStartYAxisLabel, true);
        attrs.enableYAxisGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableYAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.LineChartRecyclerView_enableYAxisZero, true);
        attrs.enableYAxisLineDash = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisLineDash, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.LineChartRecyclerView_enableScrollToScale, true);
        attrs.enableValueMark = ta.getBoolean(R.styleable.LineChartRecyclerView_enableValueMark, true);
        attrs.enableEndDayXAxis = ta.getBoolean(R.styleable.LineChartRecyclerView_enableEndDayXAxis, false);
        attrs.enableXAxisDisplayLabel = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisDisplayLabel, false);
        attrs.enableXAxisLabel = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableXAxisFirstGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisFirstGridLine, false);
        attrs.enableXAxisSecondGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisSecondGridLine, false);
        attrs.enableXAxisThirdGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisThirdGridLine, false);
        attrs.enableXAxisLineCircle = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisLineCircle, true);
        attrs.enableXAxisBg = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisBg, true);
        attrs.averageDisplay = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisLabel, false);

        attrs.ratioVelocity = ta.getFloat(R.styleable.LineChartRecyclerView_ratioVelocity, 0.5f);
        attrs.ratioSpeed = ta.getFloat(R.styleable.LineChartRecyclerView_ratioSpeed, 1f);
        //default is horizontal
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.LineChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.LineChartRecyclerView_layoutManagerReverseLayout, true);

        //YAxis
        attrs.yAxisMaximum = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisMaximum, 30000);
        attrs.yAxisMinimum = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisMinimum, 0);
        attrs.yAxisHighStandardLine = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisHighStandardLine, -1);
        attrs.yAxisMiddleStandardLine = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisMiddleStandardLine, -1);
        attrs.yAxisLowStandardLine = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisLowStandardLine, -1);
        attrs.yAxisLabelTxtColor = ta.getColor(R.styleable.LineChartRecyclerView_yAxisLabelTxtColor, ColorUtil.getResourcesColor(R.color.chart_axis_text_color));
        attrs.yAxisLabelTxtSize = ta.getDimension(R.styleable.LineChartRecyclerView_yAxisLabelTxtSize, DisplayUtil.sp2px(10));
        attrs.yAxisLabelSize = ta.getInteger(R.styleable.LineChartRecyclerView_yAxisLabelSize, 5);
        attrs.yAxisLineColor = ta.getColor(R.styleable.LineChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.yAxisLabelHorizontalPadding = ta.getDimension(R.styleable.LineChartRecyclerView_yAxisLabelHorizontalPadding, DisplayUtil.dip2px(5));
        attrs.yAxisLabelVerticalPadding = ta.getDimension(R.styleable.LineChartRecyclerView_yAxisLabelVerticalPadding, DisplayUtil.dip2px(3));
        attrs.yAxisReverse = ta.getBoolean(R.styleable.LineChartRecyclerView_yAxisReverse, false);

        //XAxis
        attrs.xAxisFirstDividerColor = ta.getColor(R.styleable.LineChartRecyclerView_xAxisFirstDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisSecondDividerColor = ta.getColor(R.styleable.LineChartRecyclerView_xAxisSecondDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisThirdDividerColor = ta.getColor(R.styleable.LineChartRecyclerView_xAxisThirdDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisTxtColor = ta.getColor(R.styleable.LineChartRecyclerView_xAxisTxtColor, ColorUtil.getResourcesColor(R.color.chart_axis_text_color));
        attrs.xAxisTxtSize = ta.getDimension(R.styleable.LineChartRecyclerView_xAxisTxtSize, DisplayUtil.sp2px(context, 10));
        attrs.xAxisLabelTxtPadding = ta.getDimension(R.styleable.LineChartRecyclerView_xAxisLabelTxtPadding, DisplayUtil.dip2px(2));
        attrs.xAxisLabelPosition = ta.getInteger(R.styleable.LineChartRecyclerView_xAxisLabelPosition, XAxis.POSITION_CENTER);
        attrs.xFirstLinePosition = ta.getInteger(R.styleable.LineChartRecyclerView_xFirstLinePosition, XAxis.POSITION_LEFT);
        attrs.xAxisScaleDistance = ta.getInteger(R.styleable.LineChartRecyclerView_xAxisScaleDistance, 6);
        attrs.xAxisBgRadius = ta.getDimension(R.styleable.LineChartRecyclerView_xAxisBgRadius, DisplayUtil.dip2px(14));
        attrs.xAxisLabelFont = ta.getResourceId(R.styleable.LineChartRecyclerView_xAxisLabelFont, -1);

        //line fill color
        attrs.lineShaderBeginColor = ta.getColor(R.styleable.LineChartRecyclerView_lineShaderBeginColor, ColorUtil.getResourcesColor(R.color.rate_shader_begin));
        attrs.lineShaderEndColor = ta.getColor(R.styleable.LineChartRecyclerView_lineShaderEndColor, ColorUtil.getResourcesColor(R.color.rate_shader_end));
        attrs.lineStrokeWidth = ta.getInteger(R.styleable.LineChartRecyclerView_lineStrokeWidth, 1);
        attrs.enableLineFill = ta.getBoolean(R.styleable.LineChartRecyclerView_enableLineFill, true);
        attrs.lineColor = ta.getColor(R.styleable.LineChartRecyclerView_lineColor, -1);
        attrs.fillAlpha = ta.getInteger(R.styleable.LineChartRecyclerView_fillAlpha, 0xA0);

        //highLight
        attrs.lineHighLightType = ta.getInt(R.styleable.LineChartRecyclerView_lineHighLightType, LineChartAttrs.HIGH_LIGHT_LINE_BOTTOM);
        attrs.linePointRadius = ta.getDimension(R.styleable.LineChartRecyclerView_linePointRadius, DisplayUtil.dip2px(3));
        attrs.linePointSelectRadius = ta.getDimension(R.styleable.LineChartRecyclerView_linePointSelectRadius, DisplayUtil.dip2px(5));
        attrs.linePointSelectStrokeWidth = ta.getDimension(R.styleable.LineChartRecyclerView_linePointSelectStrokeWidth, DisplayUtil.dip2px(1));
        attrs.lineSelectCircles = ta.getInteger(R.styleable.LineChartRecyclerView_lineSelectCircles, 2);//默认显示两个
        attrs.linePointSelectCircleStroke = ta.getBoolean(R.styleable.LineChartRecyclerView_linePointSelectCircleStroke, false);//是否空心

        //highLight
        attrs.isDisplay = ta.getBoolean(R.styleable.LineChartRecyclerView_isDisplay, false);
        attrs.lineMaxColor = ta.getColor(R.styleable.LineChartRecyclerView_lineMaxColor, -1);
        attrs.lineMinColor = ta.getColor(R.styleable.LineChartRecyclerView_lineMinColor, -1);

        attrs.dynamicAdjustItemFillWidth = ta.getBoolean(R.styleable.LineChartRecyclerView_dynamicAdjustItemFillWidth,false);
        ta.recycle();
        return attrs;
    }

    public static BezierChartAttrs getBezierChartAttrs(Context context, AttributeSet attributeSet) {
        BezierChartAttrs attrs = new BezierChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BezierChartRecyclerView);
        attrs.barBorderColor = ta.getColor(R.styleable.BezierChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.chartColor = ta.getColor(R.styleable.BezierChartRecyclerView_chartColor, ColorUtil.getResourcesColor(R.color.bar_chart_pink));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.BezierChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.BezierChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));
        attrs.barSpace = ta.getFloat(R.styleable.BezierChartRecyclerView_barSpace, 0.5f);
        attrs.highLightRoundRectRadius = ta.getDimension(R.styleable.BezierChartRecyclerView_highLightRoundRectRadius, DisplayUtil.dip2px(14));
        attrs.barChartRadius = ta.getDimension(R.styleable.BezierChartRecyclerView_barChartRadius, DisplayUtil.dip2px(12));

        //BarChart Value
        attrs.barBorderWidth = ta.getDimension(R.styleable.BezierChartRecyclerView_barBorderWidth, 0.75f);
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.BezierChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(41));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.BezierChartRecyclerView_contentPaddingTop, 0f);
        attrs.displayNumbers = ta.getInteger(R.styleable.BezierChartRecyclerView_displayNumbers, 12);

        //Switch Button
        attrs.enableBarBorder = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableCharValueDisplay, true);
        attrs.enableEndYAxisLabel = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableEndYAxisLabel, true);
        attrs.enableStartYAxisLabel = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableStartYAxisLabel, true);
        attrs.enableYAxisGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableYAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableYAxisZero, true);
        attrs.enableYAxisLineDash = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisLineDash, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableScrollToScale, true);
        attrs.enableValueMark = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableValueMark, true);
        attrs.enableEndDayXAxis = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableEndDayXAxis, false);
        attrs.enableXAxisDisplayLabel = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisDisplayLabel, false);
        attrs.enableXAxisLabel = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableXAxisFirstGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisFirstGridLine, true);
        attrs.enableXAxisSecondGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisSecondGridLine, true);
        attrs.enableXAxisThirdGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisThirdGridLine, false);
        attrs.enableXAxisLineCircle = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisLineCircle, true);
        attrs.enableXAxisBg = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisBg, true);
        attrs.averageDisplay = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisLabel, false);

        attrs.ratioVelocity = ta.getFloat(R.styleable.BezierChartRecyclerView_ratioVelocity, 0.5f);
        attrs.ratioSpeed = ta.getFloat(R.styleable.BezierChartRecyclerView_ratioSpeed, 1f);
        //default is horizontal
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.BezierChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.BezierChartRecyclerView_layoutManagerReverseLayout, true);

        //YAxis
        attrs.yAxisMaximum = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisMaximum, 30000);
        attrs.yAxisMinimum = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisMinimum, 0);
        attrs.yAxisHighStandardLine = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisHighStandardLine, -1);
        attrs.yAxisMiddleStandardLine = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisMiddleStandardLine, -1);
        attrs.yAxisLowStandardLine = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisLowStandardLine, -1);
        attrs.yAxisLabelTxtColor = ta.getColor(R.styleable.BezierChartRecyclerView_yAxisLabelTxtColor, ColorUtil.getResourcesColor(R.color.chart_axis_text_color));
        attrs.yAxisLabelTxtSize = ta.getDimension(R.styleable.BezierChartRecyclerView_yAxisLabelTxtSize, DisplayUtil.sp2px(10));
        attrs.yAxisLabelSize = ta.getInteger(R.styleable.BezierChartRecyclerView_yAxisLabelSize, 5);
        attrs.yAxisLineColor = ta.getColor(R.styleable.BezierChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.yAxisLabelHorizontalPadding = ta.getDimension(R.styleable.BezierChartRecyclerView_yAxisLabelHorizontalPadding, DisplayUtil.dip2px(5));
        attrs.yAxisLabelVerticalPadding = ta.getDimension(R.styleable.BezierChartRecyclerView_yAxisLabelVerticalPadding, DisplayUtil.dip2px(3));
        attrs.yAxisReverse = ta.getBoolean(R.styleable.BezierChartRecyclerView_yAxisReverse, false);

        //XAxis
        attrs.xAxisFirstDividerColor = ta.getColor(R.styleable.BezierChartRecyclerView_xAxisFirstDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisSecondDividerColor = ta.getColor(R.styleable.BezierChartRecyclerView_xAxisSecondDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisThirdDividerColor = ta.getColor(R.styleable.BezierChartRecyclerView_xAxisThirdDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisTxtColor = ta.getColor(R.styleable.BezierChartRecyclerView_xAxisTxtColor, ColorUtil.getResourcesColor(R.color.chart_axis_text_color));
        attrs.xAxisTxtSize = ta.getDimension(R.styleable.BezierChartRecyclerView_xAxisTxtSize, DisplayUtil.sp2px(context, 10));
        attrs.xAxisLabelTxtPadding = ta.getDimension(R.styleable.BezierChartRecyclerView_xAxisLabelTxtPadding, DisplayUtil.dip2px(2));
        attrs.xAxisLabelPosition = ta.getInteger(R.styleable.BezierChartRecyclerView_xAxisLabelPosition, XAxis.POSITION_CENTER);
        attrs.xFirstLinePosition = ta.getInteger(R.styleable.BezierChartRecyclerView_xFirstLinePosition, XAxis.POSITION_LEFT);
        attrs.xAxisScaleDistance = ta.getInteger(R.styleable.BezierChartRecyclerView_xAxisScaleDistance, 6);
        attrs.xAxisBgRadius = ta.getDimension(R.styleable.BezierChartRecyclerView_xAxisBgRadius, DisplayUtil.dip2px(14));
        attrs.xAxisLabelFont = ta.getResourceId(R.styleable.BezierChartRecyclerView_xAxisLabelFont, -1);

        //bezier curve
        attrs.bezierIntensity = ta.getFloat(R.styleable.BezierChartRecyclerView_bezierIntensity, 0.25f);
        attrs.enableBezierLineFill = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableBezierLineFill, true);
        attrs.bezierFillAlpha = ta.getInteger(R.styleable.BezierChartRecyclerView_bezierFillAlpha, 0x30);
        attrs.bezierFillColor = ta.getColor(R.styleable.BezierChartRecyclerView_bezierFillColor, ColorUtil.getResourcesColor(R.color.bar_chart_pink));
        attrs.bezierLinePaintColor = ta.getColor(R.styleable.BezierChartRecyclerView_bezierLinePaintColor, ColorUtil.getResourcesColor(R.color.bar_chart_pink));
        attrs.bezierLinePaintStrokeWidth = ta.getDimension(R.styleable.BezierChartRecyclerView_bezierLinePaintStrokeWidth, DisplayUtil.dip2px(3));

        //line fill color
        attrs.lineShaderBeginColor = ta.getColor(R.styleable.BezierChartRecyclerView_lineShaderBeginColor, ColorUtil.getResourcesColor(R.color.rate_shader_begin));
        attrs.lineShaderEndColor = ta.getColor(R.styleable.BezierChartRecyclerView_lineShaderEndColor, ColorUtil.getResourcesColor(R.color.rate_shader_end));
        attrs.enableLineFill = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableLineFill, true);
        attrs.fillAlpha = ta.getInteger(R.styleable.BezierChartRecyclerView_fillAlpha, 0xA0);

        //highLight
        attrs.isDisplay = ta.getBoolean(R.styleable.BezierChartRecyclerView_isDisplay, false);
        ta.recycle();
        return attrs;
    }

    public static CustomLineChartAttr getMPLineChartAttr(Context context, AttributeSet attributeSet) {
        CustomLineChartAttr mLineChartAttr = new CustomLineChartAttr();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.CustomLineChart);
        mLineChartAttr.minYAxisZero = ta.getBoolean(R.styleable.CustomLineChart_minYAxisZero, true);
        mLineChartAttr.maxYAxisRatio = ta.getFloat(R.styleable.CustomLineChart_maxYAxisRatio, 0);
        mLineChartAttr.restrictMax = ta.getBoolean(R.styleable.CustomLineChart_restrictMax, false);
        mLineChartAttr.enableYAxisGridLine = ta.getBoolean(R.styleable.CustomLineChart_enableYAxisGridLine, false);
        mLineChartAttr.enableXAxisGridLine = ta.getBoolean(R.styleable.CustomLineChart_enableXAxisGridLine, false);
        mLineChartAttr.enableLineFill = ta.getBoolean(R.styleable.CustomLineChart_enableLineFill, true);
        mLineChartAttr.enableDrawLine = ta.getBoolean(R.styleable.CustomLineChart_enableDrawLine, true);
        mLineChartAttr.fillAlpha = ta.getInt(R.styleable.CustomLineChart_fillAlpha, 0xA0);
        mLineChartAttr.lineColor = ta.getColor(R.styleable.CustomLineChart_lineColor, ColorUtil.getResourcesColor(R.color.rate_title_select_txt));
        mLineChartAttr.lineFillColor = ta.getColor(R.styleable.CustomLineChart_lineFillColor, ColorUtil.getResourcesColor(R.color.rate_chart_color_light));
        mLineChartAttr.lineFillRes = ta.getResourceId(R.styleable.CustomLineChart_lineFillRes, R.drawable.sport_fill_rate);
        mLineChartAttr.lineStrokeWidth = ta.getFloat(R.styleable.CustomLineChart_lineStrokeWidth, 5f);
        mLineChartAttr.pointRadius = ta.getFloat(R.styleable.CustomLineChart_pointRadius, 2.5f);
        mLineChartAttr.enableMaxCircle = ta.getBoolean(R.styleable.CustomLineChart_enableMaxCircle, false);
        mLineChartAttr.enableMinCircle = ta.getBoolean(R.styleable.CustomLineChart_enableMinCircle, false);
        mLineChartAttr.enableMaxPoup = ta.getBoolean(R.styleable.CustomLineChart_enableMaxPoup, false);
        mLineChartAttr.enableCustomerStepped = ta.getBoolean(R.styleable.CustomLineChart_enableCustomerStepped, false);
        mLineChartAttr.enableDrawCircle = ta.getBoolean(R.styleable.CustomLineChart_enableDrawCircle, false);
        mLineChartAttr.limitTextColor = ta.getColor(R.styleable.CustomLineChart_limitTextColor, -1);
        mLineChartAttr.enableTimeXAxisLabel = ta.getBoolean(R.styleable.CustomLineChart_enableTimeXAxisLabel, false);
        mLineChartAttr.enableSportYAxisLabel = ta.getBoolean(R.styleable.CustomLineChart_enableSportYAxisLabel, false);
        mLineChartAttr.sportYAxisLabelType = ta.getInt(R.styleable.CustomLineChart_sportYAxisLabelType, 0);
        mLineChartAttr.sportYAxisReminder = ta.getInt(R.styleable.CustomLineChart_sportYAxisReminder, 4);
        ta.recycle();
        return mLineChartAttr;
    }

    public static CustomBarChartAttr getMPBarChartAttr(Context context, AttributeSet attributeSet) {
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.CustomBarChart);
        CustomBarChartAttr mAttribute = new CustomBarChartAttr();
        mAttribute.mRectHeight = ta.getDimension(R.styleable.CustomBarChart_rect_height, 100);
        mAttribute.fillEndColor = ta.getColor(R.styleable.CustomBarChart_fill_end_color, Color.GREEN);
        mAttribute.mDoneColor = ta.getColor(R.styleable.CustomBarChart_done_color, Color.GREEN);
        mAttribute.mNoneDoneColor = ta.getColor(R.styleable.CustomBarChart_none_done_color, mAttribute.mDoneColor);
        mAttribute.mBarDutyCycle = ta.getFloat(R.styleable.CustomBarChart_duty_cycle, 5);
        mAttribute.mDoneAlpha = ta.getFloat(R.styleable.CustomBarChart_done_alpha, 1.0f);
        mAttribute.mNoneDoneAlpha = ta.getFloat(R.styleable.CustomBarChart_none_done_alpha, 0.2f);
        mAttribute.mRectRadius = ta.getDimension(R.styleable.CustomBarChart_rect_bar_radius, 0f);
        mAttribute.mMaxPoints = ta.getInteger(R.styleable.CustomBarChart_max_points, 24);
        mAttribute.barChartType = ta.getInteger(R.styleable.CustomBarChart_bar_chart_type, CustomBarChartAttr.TYPE_BAR_CHART_FIRST);
        mAttribute.enableTimeXAxisLabel = ta.getBoolean(R.styleable.CustomLineChart_enableTimeXAxisLabel, false);
        mAttribute.enableSportYAxisLabel = ta.getBoolean(R.styleable.CustomLineChart_enableSportYAxisLabel, false);
        mAttribute.sportYAxisLabelType = ta.getInt(R.styleable.CustomLineChart_sportYAxisLabelType, 0);
        mAttribute.sportYAxisReminder = ta.getInt(R.styleable.CustomLineChart_sportYAxisReminder, 4);
        ta.recycle();
        return mAttribute;
    }

    public static StockChartAttrs getStockChartAttrs(Context context, AttributeSet attributeSet) {
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.StockChartRecyclerView);
        StockChartAttrs attrs = new StockChartAttrs();
        //StockChartAttrs special
        attrs.setRiseColor(ta.getColor(R.styleable.StockChartRecyclerView_riseColor, Color.parseColor("#E36245")));
        attrs.setDownColor(ta.getColor(R.styleable.StockChartRecyclerView_downColor, Color.parseColor("#3FC08E")));
        attrs.setChartItemFill(ta.getBoolean(R.styleable.StockChartRecyclerView_isChartItemFill, true));

        attrs.barBorderColor = ta.getColor(R.styleable.StockChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.barBorderBgColor = ta.getColor(R.styleable.StockChartRecyclerView_barBorderBgColor, ColorUtil.getResourcesColor(R.color.rate_chart_bg));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.StockChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.StockChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));
        attrs.chartColor = ta.getColor(R.styleable.StockChartRecyclerView_chartColor, ColorUtil.getResourcesColor(R.color.bar_chart_pink));
        attrs.highLightColor = ta.getColor(R.styleable.StockChartRecyclerView_highLightColor, ColorUtil.getResourcesColor(R.color.chart_highlight_line_color));

        attrs.barSpace = ta.getFloat(R.styleable.StockChartRecyclerView_barSpace, 0.5f);
        attrs.highLightRoundRectRadius = ta.getDimension(R.styleable.StockChartRecyclerView_highLightRoundRectRadius, DisplayUtil.dip2px(14));
        attrs.barChartRadius = ta.getDimension(R.styleable.StockChartRecyclerView_barChartRadius, DisplayUtil.dip2px(12));

        //BarChart Value
        attrs.barBorderWidth = ta.getDimension(R.styleable.StockChartRecyclerView_barBorderWidth, 0.75f);
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.StockChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(41));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.StockChartRecyclerView_contentPaddingTop, 0f);
        attrs.displayNumbers = ta.getInteger(R.styleable.StockChartRecyclerView_displayNumbers, 12);

        //Switch Button
        attrs.enableBarBorder = ta.getBoolean(R.styleable.StockChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.StockChartRecyclerView_enableCharValueDisplay, true);
        attrs.enableEndYAxisLabel = ta.getBoolean(R.styleable.StockChartRecyclerView_enableEndYAxisLabel, true);
        attrs.enableStartYAxisLabel = ta.getBoolean(R.styleable.StockChartRecyclerView_enableStartYAxisLabel, true);
        attrs.enableYAxisGridLine = ta.getBoolean(R.styleable.StockChartRecyclerView_enableYAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.StockChartRecyclerView_enableYAxisZero, true);
        attrs.enableYAxisLineDash = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisLineDash, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.StockChartRecyclerView_enableScrollToScale, true);
        attrs.enableValueMark = ta.getBoolean(R.styleable.StockChartRecyclerView_enableValueMark, true);
        attrs.enableEndDayXAxis = ta.getBoolean(R.styleable.StockChartRecyclerView_enableEndDayXAxis, false);
        attrs.enableXAxisDisplayLabel = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisDisplayLabel, false);
        attrs.enableXAxisLabel = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableXAxisFirstGridLine = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisFirstGridLine, false);
        attrs.enableXAxisSecondGridLine = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisSecondGridLine, false);
        attrs.enableXAxisThirdGridLine = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisThirdGridLine, false);
        attrs.enableXAxisLineCircle = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisLineCircle, true);
        attrs.enableXAxisBg = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisBg, true);
        attrs.averageDisplay = ta.getBoolean(R.styleable.StockChartRecyclerView_enableXAxisLabel, false);

        attrs.ratioVelocity = ta.getFloat(R.styleable.StockChartRecyclerView_ratioVelocity, 0.5f);
        attrs.ratioSpeed = ta.getFloat(R.styleable.StockChartRecyclerView_ratioSpeed, 1f);
        //default is horizontal
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.StockChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.StockChartRecyclerView_layoutManagerReverseLayout, true);

        //YAxis
        attrs.yAxisMaximum = ta.getFloat(R.styleable.StockChartRecyclerView_yAxisMaximum, 30000);
        attrs.yAxisMinimum = ta.getFloat(R.styleable.StockChartRecyclerView_yAxisMinimum, 0);
        attrs.yAxisHighStandardLine = ta.getFloat(R.styleable.StockChartRecyclerView_yAxisHighStandardLine, -1);
        attrs.yAxisMiddleStandardLine = ta.getFloat(R.styleable.StockChartRecyclerView_yAxisMiddleStandardLine, -1);
        attrs.yAxisLowStandardLine = ta.getFloat(R.styleable.StockChartRecyclerView_yAxisLowStandardLine, -1);
        attrs.yAxisLabelTxtColor = ta.getColor(R.styleable.StockChartRecyclerView_yAxisLabelTxtColor, ColorUtil.getResourcesColor(R.color.chart_axis_text_color));
        attrs.yAxisLabelTxtSize = ta.getDimension(R.styleable.StockChartRecyclerView_yAxisLabelTxtSize, DisplayUtil.sp2px(10));
        attrs.yAxisLabelSize = ta.getInteger(R.styleable.StockChartRecyclerView_yAxisLabelSize, 5);
        attrs.yAxisLineColor = ta.getColor(R.styleable.StockChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.yAxisLabelHorizontalPadding = ta.getDimension(R.styleable.StockChartRecyclerView_yAxisLabelHorizontalPadding, DisplayUtil.dip2px(5));
        attrs.yAxisLabelVerticalPadding = ta.getDimension(R.styleable.StockChartRecyclerView_yAxisLabelVerticalPadding, DisplayUtil.dip2px(3));
        attrs.yAxisReverse = ta.getBoolean(R.styleable.StockChartRecyclerView_yAxisReverse, false);

        //XAxis
        attrs.xAxisFirstDividerColor = ta.getColor(R.styleable.StockChartRecyclerView_xAxisFirstDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisSecondDividerColor = ta.getColor(R.styleable.StockChartRecyclerView_xAxisSecondDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisThirdDividerColor = ta.getColor(R.styleable.StockChartRecyclerView_xAxisThirdDividerColor, ColorUtil.getResourcesColor(R.color.chart_bar_border_color));
        attrs.xAxisTxtColor = ta.getColor(R.styleable.StockChartRecyclerView_xAxisTxtColor, ColorUtil.getResourcesColor(R.color.chart_axis_text_color));
        attrs.xAxisTxtSize = ta.getDimension(R.styleable.StockChartRecyclerView_xAxisTxtSize, DisplayUtil.sp2px(context, 10));
        attrs.xAxisLabelTxtPadding = ta.getDimension(R.styleable.StockChartRecyclerView_xAxisLabelTxtPadding, DisplayUtil.dip2px(2));
        attrs.xAxisLabelPosition = ta.getInteger(R.styleable.StockChartRecyclerView_xAxisLabelPosition, XAxis.POSITION_CENTER);
        attrs.xFirstLinePosition = ta.getInteger(R.styleable.StockChartRecyclerView_xFirstLinePosition, XAxis.POSITION_LEFT);
        attrs.xAxisScaleDistance = ta.getInteger(R.styleable.StockChartRecyclerView_xAxisScaleDistance, 6);
        attrs.xAxisBgRadius = ta.getDimension(R.styleable.StockChartRecyclerView_xAxisBgRadius, DisplayUtil.dip2px(14));
        attrs.xAxisLabelFont = ta.getResourceId(R.styleable.StockChartRecyclerView_xAxisLabelFont, -1);
        //highLight
        attrs.isDisplay = ta.getBoolean(R.styleable.StockChartRecyclerView_isDisplay, false);
        ta.recycle();
        return attrs;
    }

}
