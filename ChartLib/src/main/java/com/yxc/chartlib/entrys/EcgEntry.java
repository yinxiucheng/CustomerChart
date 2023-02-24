package com.yxc.chartlib.entrys;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.ColorUtil;

/**
 * @author yxc
 * @since 2019-05-15
 */
public class EcgEntry extends RecyclerBarEntry {

    public static final int STATUE_ECG_LOW = 1;
    public static final int STATUE_ECG_MIDDLE = 2;
    public static final int STATUE_ECG_HIGH = 3;

    public int ecgStatue;
    public int fillColor;
    public int ecgColor;
    public float colorTransport;

    public EcgEntry(float x, float y, long timestamp, int type, int ecgStatue) {
        super(x, y, timestamp, type);
        this.ecgStatue = ecgStatue;
        fillColor = getFillColor(ecgStatue);
//        ecgColor = getEcgColor(ecgStatue);
    }

    public int getFillColor(int curseStatue) {
        switch (curseStatue) {
            case STATUE_ECG_LOW:
                return ColorUtil.getResourcesColor(R.color.data_ecg_low_fill_color);
            case STATUE_ECG_MIDDLE:
                return ColorUtil.getResourcesColor(R.color.data_ecg_middle_fill_color);
            case STATUE_ECG_HIGH:
                return ColorUtil.getResourcesColor(R.color.data_ecg_high_fill_color);
            default:
                return ColorUtil.getResourcesColor(R.color.data_ecg_low_fill_color);
        }
    }

    public static int getEcgColor(int curseStatue) {
        switch (curseStatue) {
            case STATUE_ECG_LOW:
                return ColorUtil.getResourcesColor(R.color.data_ecg_low_color);
            case STATUE_ECG_MIDDLE:
                return ColorUtil.getResourcesColor(R.color.data_ecg_middle_color);
            case STATUE_ECG_HIGH:
                return ColorUtil.getResourcesColor(R.color.data_ecg_high_color);
            default:
                return ColorUtil.getResourcesColor(R.color.data_ecg_low_color);
        }
    }

}
