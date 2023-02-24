package com.yxc.chartlib.entrys;


import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.ColorUtil;

/**
 * @author yxc
 * @since 2019-05-15
 */
public class HearingEntry extends MaxMinEntry {
    public final static int MAX_Hearing = 100;

    public static final int TYPE_HEARING_LOW = 0;
    public static final int TYPE_HEARING_HIGH = 1;

    public int hearingType;
    public int color;

    public HearingEntry(float x, float y, long timestamp, int type) {
        super(x, y, timestamp, type);
        setPressType();
        color = getPressColor(hearingType);
    }

    private void setPressType() {
        if (getY() > 50) {
            hearingType = TYPE_HEARING_HIGH;
        } else {
            hearingType = TYPE_HEARING_HIGH;
        }
    }

    public int getPressColor(int hearingType) {
        switch (hearingType) {
            case TYPE_HEARING_HIGH:
                return ColorUtil.getResourcesColor(R.color.hearing_high);
            case TYPE_HEARING_LOW:
            default:
                return ColorUtil.getResourcesColor(R.color.hearing_low);
        }
    }


}
