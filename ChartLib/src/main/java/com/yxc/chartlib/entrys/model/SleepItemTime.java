package com.yxc.chartlib.entrys.model;

import android.content.Context;
import android.graphics.Color;

import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.attrs.SleepChartAttrs;
import com.xiaomi.fitness.common.utils.ColorUtil;


/**
 * @author yxc
 * @since 2019-05-09
 */
public class SleepItemTime {

    public static final int TYPE_DEEP_SLEEP = 3;//深睡

    public static final int TYPE_SLUMBER = 2;//潜睡

    public static final int TYPE_EYES_MOVE = 1;//快速眼动

    public static final int TYPE_WAKE = 0;//清醒

    public static final int TYPE_OTHER_SLEEP = 4;//其他睡眠：零星小睡 手动添加 睡眠检测等

    public static final int TYPE_NULL = -1;//空，没数据

    public float durationTime; //这里是 浮点型的小数。h为单位的。

    public int sleepTypeColor;

    public int sleepType = -1;

    public int chartHeightIndex = -1;

    public int durationTimeSed;//单位是秒

    public long startTimestamp;//入睡时间点

    public long endTimestamp;//醒来时间点

    public boolean isTop;

    public SleepItemTime() {

    }

    public static int getSleepTypeColor(int sleepType) {
        switch (sleepType) {
            case TYPE_DEEP_SLEEP:
                return ColorUtil.getResourcesColor(R.color.sleep_deep);
            case TYPE_SLUMBER:
                return ColorUtil.getResourcesColor(R.color.sleep_slumber);
            case TYPE_EYES_MOVE:
                return ColorUtil.getResourcesColor(R.color.sleep_eye_move);
            case TYPE_WAKE:
                return ColorUtil.getResourcesColor(R.color.sleep_wake);
            case TYPE_OTHER_SLEEP:
                return ColorUtil.getResourcesColor(R.color.sleep_other_color);
            default:
                return ColorUtil.getResourcesColor(R.color.common_transparent);
        }
    }

    public static int getSleepTypeColor(Context context, int sleepType) {
        switch (sleepType) {
            case TYPE_DEEP_SLEEP:
                return ColorUtil.getResourcesColor(R.color.sleep_deep);
            case TYPE_SLUMBER:
                return ColorUtil.getResourcesColor(R.color.sleep_slumber);
            case TYPE_EYES_MOVE:
                return ColorUtil.getResourcesColor(R.color.sleep_eye_move);
            case TYPE_WAKE:
                return ColorUtil.getResourcesColor(R.color.sleep_wake);
            case TYPE_OTHER_SLEEP:
                return ColorUtil.getResourcesColor(R.color.sleep_other_color);
            default:
                return ColorUtil.getResourcesColor(R.color.common_transparent);
        }
    }

    public long getSleepTime() {
        return endTimestamp - startTimestamp;
    }


    public int getChartColor(SleepChartAttrs mChartAttrs) {
        switch (sleepType) {
            case TYPE_SLUMBER:
                return mChartAttrs.slumberColor;
            case TYPE_EYES_MOVE:
                return mChartAttrs.eyeMoveColor;
            case TYPE_WAKE:
                return mChartAttrs.weakColor;
            case TYPE_OTHER_SLEEP:
                return mChartAttrs.otherColor;
            case TYPE_NULL:
                return Color.TRANSPARENT;
            case TYPE_DEEP_SLEEP:
            default:
                return mChartAttrs.deepSleepColor;
        }
    }
}
