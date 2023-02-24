package com.yxc.chartlib.entrys;

import android.graphics.Color;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.ColorUtil;

/**
 * @author yxc
 * @since 2019-05-15
 */
public class CurseEntry extends RecyclerBarEntry {

    public static final int CURSE_STATUE_NORMAL = 0;//无
    public static final int CURSE_STATUE_SIGN = 1;//经期
    public static final int CURSE_STATUE_FORECAST = 2;//预测经期
    public static final int CURSE_STATUE_DANGER = 3;//易孕期
    public static final int CURSE_STATUE_OVUM = 4;//排卵期

    public int curseStatue;
    public int fillColor;
    public int topColor;
    public int alphaValue;

    public CurseEntry(float x, float y, long timestamp, int type, int curseStatue) {
        super(x, y, timestamp, type);
        this.curseStatue = curseStatue;
        fillColor = getFillColor(curseStatue);
        topColor = getTopColor(curseStatue);
        alphaValue = getColorAlpha(curseStatue);
    }

    public int getFillColor(int curseStatue) {
        switch (curseStatue) {
            case CURSE_STATUE_SIGN:
                return ColorUtil.getResourcesColor(R.color.curse_color);
            case CURSE_STATUE_FORECAST:
                return ColorUtil.getResourcesColor(R.color.curse_fill_forecast);
            case CURSE_STATUE_NORMAL:
            default:
                return ColorUtil.getResourcesColor(R.color.chart_empty_color);
        }
    }

    public int getColorAlpha(int curseStatue) {
        switch (curseStatue) {
            case CURSE_STATUE_SIGN:
                return 0x60;
            case CURSE_STATUE_FORECAST:
                return 0x1A;
            case CURSE_STATUE_NORMAL:
            default:
                return 0x08;
        }
    }

    public int getTopColor(int curseStatue) {
        switch (curseStatue) {
            case CURSE_STATUE_SIGN:
            case CURSE_STATUE_FORECAST:
                return ColorUtil.getResourcesColor(R.color.curse_top_color);
            case CURSE_STATUE_OVUM:
            case CURSE_STATUE_DANGER:
            case CURSE_STATUE_NORMAL:
            default:
                return Color.TRANSPARENT;
        }
    }

    public static int getCurseResource(int curseStatue) {
        if (curseStatue == CurseEntry.CURSE_STATUE_FORECAST) {
            return R.drawable.data_cusre_forecast_item;
        } else if (curseStatue == CurseEntry.CURSE_STATUE_DANGER) {
            return R.drawable.data_cusre_danger_item;
        } else {
            return R.drawable.data_cusre_ovum_item;
        }
    }
}
