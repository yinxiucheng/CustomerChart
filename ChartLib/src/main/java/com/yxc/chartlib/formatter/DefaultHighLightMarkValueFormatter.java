package com.yxc.chartlib.formatter;

import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.common.utils.DecimalUtil;
import com.xiaomi.fitness.common.utils.TimeDateUtil;

/**
 * @author yxc
 * @since 2019/4/24
 */
public class DefaultHighLightMarkValueFormatter extends DefaultValueFormatter {

    public static final String CONNECT_STR = "&";
    public static final String CONNECT_VALUE_STR = "@";
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public DefaultHighLightMarkValueFormatter(int digits) {
        super(digits);
    }

    @Override
    public String getFormattedValue(float value) {
        return super.getFormattedValue(value);
    }

    @Override
    public String getBarLabel(RecyclerBarEntry barEntry) {
        String str1 = DecimalUtil.getDecimalFloatStr(barEntry.getY());
        String str2 = TimeDateUtil.getDateSimpleLocalFormat(barEntry.timestamp);
        String resultStr = str1 + CONNECT_STR + str2;
        return barEntry.getY() > 0 ? resultStr : "";
    }


}
