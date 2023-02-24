package com.yxc.chartlib.formatter;

import android.content.Context;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.entrys.SportRecordEntry;
import com.yxc.chartlib.utils.DecimalUtil;
import com.yxc.mylibrary.TimeDateUtil;


/**
 * @author yxc
 * @date 2019/4/11
 */
public class XAxisSportFormatter extends ValueFormatter {

    private Context mContext;

    public XAxisSportFormatter(Context context) {
        this.mContext = context;
    }

    @Override
    public String getBarLabel(RecyclerBarEntry barEntry) {
        SportRecordEntry entry = (SportRecordEntry) barEntry;
        int time = (int) entry.getX();

        float timeFloat = time * 1.0f / TimeDateUtil.TIME_MIN_INT;

        if (timeFloat > 0 && timeFloat < 1) {
            return DecimalUtil.getDecimalFloatStr(timeFloat);
        } else {
            return Integer.toString(time / TimeDateUtil.TIME_MIN_INT);
        }
    }

    @Override
    public String getFormattedValue(float value) {
        float timeFloat = value / TimeDateUtil.TIME_MIN_INT;
        return DecimalUtil.getDecimalFloatStr(timeFloat);
    }
}
