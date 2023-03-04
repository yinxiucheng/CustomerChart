package com.yxc.chartlib.component;

import com.github.mikephil.charting.components.AxisBase;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.formatter.ValueFormatter;

/**
 * @author yxc
 * @since  2019/4/8
 */
public class XAxis extends AxisBase {

    public static final int VIEW_DAY = 0;
    public static final int VIEW_WEEK = 1;
    public static final int VIEW_MONTH = 2;

    public static final int POSITION_CENTER = 0;
    public static final int POSITION_LEFT = 1;
    public static final int POSITION_RIGHT = 2;

    public int displayNumbers;
    public int firstDividerColor;
    public int secondDividerColor;
    public int thirdDividerColor;

    public float labelTxtPadding;

    public<V extends BaseChartAttrs> XAxis(V attrs, int displayNumbers, ValueFormatter valueFormatter) {
        this(attrs, displayNumbers);
        setValueFormatter(valueFormatter);
    }

    public<V extends BaseChartAttrs> XAxis(V attrs, int displayNumbers) {
        this.displayNumbers = displayNumbers;
        setTextColor(attrs.xAxisTxtColor);
        setTextSize(attrs.xAxisTxtSize);
        this.firstDividerColor = attrs.xAxisFirstDividerColor;
        this.secondDividerColor = attrs.xAxisSecondDividerColor;
        this.thirdDividerColor = attrs.xAxisThirdDividerColor;
        this.labelTxtPadding = attrs.xAxisLabelTxtPadding;
    }

    public void resetDisplayNumber(int displayNumbers){
        this.displayNumbers = displayNumbers;
    }

    public static boolean isSecondXAxis(int hourOfTheDay, BaseChartAttrs attrs) {
        if (hourOfTheDay == -1) {
            return false;
        }
        return hourOfTheDay % attrs.xAxisScaleDistance == 0;
    }
}
