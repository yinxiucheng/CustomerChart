package com.yxc.chartlib.entrys;

import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.common.utils.ColorUtil;

/**
 * @author yxc
 * @since 2019-05-15
 */
public class VO2MaxEntry extends RecyclerBarEntry {

    public int level;
    public int fillColor;
    public int vo2MaxColor;
    public float colorTransport;

    public VO2MaxEntry(float x, float y, long timestamp, int type, int level) {
        super(x, y, timestamp, type);
        this.level = level;
        fillColor = ColorUtil.getResourcesColor(R.color.vo2max_chart_color_15);
        vo2MaxColor = ColorUtil.getResourcesColor(R.color.vo2max_chart_color);
    }

}
