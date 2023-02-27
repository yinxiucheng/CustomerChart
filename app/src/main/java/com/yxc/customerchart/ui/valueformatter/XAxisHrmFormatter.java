package com.yxc.customerchart.ui.valueformatter;


import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

/**
 * @author yxc
 * @date 2019/4/11
 */
public class XAxisHrmFormatter extends ValueFormatter {

    @Override
    public String getBarLabel(RecyclerBarEntry barEntry) {
        if (barEntry.type == BarEntry.TYPE_XAXIS_FIRST || barEntry.type == BarEntry.TYPE_XAXIS_SPECIAL) {
            int index = (int)barEntry.getX();
            return index/25 + "秒";
        } else {
            return "";
        }
    }

}
