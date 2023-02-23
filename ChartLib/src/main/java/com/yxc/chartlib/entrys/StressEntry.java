package com.yxc.chartlib.entrys;

import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.entrys.MaxMinEntry;
import com.xiaomi.fitness.common.utils.ColorUtil;

/**
 * @author yxc
 * @since 2019-05-15
 */
public class StressEntry extends MaxMinEntry {
    public final static int MAX_STRESS = 100;

    public static final int TYPE_PRESS_SEVERE = 0;
    public static final int TYPE_PRESS_MODERATE = 1;
    public static final int TYPE_PRESS_MILD = 2;
    public static final int TYPE_PRESS_RELAX = 3;

    public int pressType;
    public int color;
    public int deviceType = DEVICE_DEFAULT_TYPE;
    public static final int DEVICE_HUAMI_TYPE = 1;
    public static final int DEVICE_DEFAULT_TYPE = 0;

    public StressEntry(float x, float y, long timestamp, int type, int deviceType) {
        super(x, y, timestamp, type);
        this.deviceType = deviceType;
        if (deviceType == DEVICE_HUAMI_TYPE) {
            setHuamiPressType();
        } else {
            setPressType();
        }
        color = getPressColor(pressType);
    }

    private void setPressType() {
        if (getY() > 80) {
            pressType = TYPE_PRESS_SEVERE;
        } else if (getY() > 50) {
            pressType = TYPE_PRESS_MODERATE;
        } else if (getY() > 25) {
            pressType = TYPE_PRESS_MILD;
        } else {
            pressType = TYPE_PRESS_RELAX;
        }
    }

    private void setHuamiPressType() {
        if (getY() > 79) {
            pressType = TYPE_PRESS_SEVERE;
        } else if (getY() > 59) {
            pressType = TYPE_PRESS_MODERATE;
        } else if (getY() > 39) {
            pressType = TYPE_PRESS_MILD;
        } else {
            pressType = TYPE_PRESS_RELAX;
        }
    }

    public static int getStressType(int stressValue, int deviceType) {
        if (deviceType == DEVICE_HUAMI_TYPE) {
            if (stressValue > 79) {
                return TYPE_PRESS_SEVERE;
            } else if (stressValue > 59) {
                return TYPE_PRESS_MODERATE;
            } else if (stressValue > 39) {
                return TYPE_PRESS_MILD;
            } else {
                return TYPE_PRESS_RELAX;
            }
        } else {
            if (stressValue > 80) {
                return TYPE_PRESS_SEVERE;
            } else if (stressValue > 50) {
                return TYPE_PRESS_MODERATE;
            } else if (stressValue > 25) {
                return TYPE_PRESS_MILD;
            } else {
                return TYPE_PRESS_RELAX;
            }
        }
    }

    public static int getPressTypeColor(int pressType) {
        switch (pressType) {
            case TYPE_PRESS_SEVERE:
                return ColorUtil.getResourcesColor(R.color.press_mild);
            case TYPE_PRESS_MODERATE:
                return ColorUtil.getResourcesColor(R.color.press_mild);
            case TYPE_PRESS_MILD:
                return ColorUtil.getResourcesColor(R.color.press_mild);
            case TYPE_PRESS_RELAX:
                return ColorUtil.getResourcesColor(R.color.press_mild);
            default:
                return ColorUtil.getResourcesColor(R.color.press_mild);
        }
    }

    public int getPressColor(int pressType) {
        switch (pressType) {
            case TYPE_PRESS_SEVERE:
                return ColorUtil.getResourcesColor(R.color.press_mild);
            case TYPE_PRESS_MODERATE:
                return ColorUtil.getResourcesColor(R.color.press_mild);
            case TYPE_PRESS_MILD:
                return ColorUtil.getResourcesColor(R.color.press_mild);
            case TYPE_PRESS_RELAX:
                return ColorUtil.getResourcesColor(R.color.press_mild);
            default:
                return ColorUtil.getResourcesColor(R.color.press_mild);
        }
    }


}
