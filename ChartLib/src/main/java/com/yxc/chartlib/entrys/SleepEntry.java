package com.yxc.chartlib.entrys;


import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.entrys.model.SleepTime;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.ColorUtil;

/**
 * @author yxc
 * @since 2019/4/6
 */
public class SleepEntry extends RecyclerBarEntry {

    public static final int STAGE_NONE = 0;// 无睡眠
    public static final int STAGE_BAD = 1;// 差
    public static final int STAGE_NORMAL = 2;// 一般
    public static final int STAGE_BETTER = 3;// 较好
    public static final int STAGE_BEST = 4;// 很好

    public SleepTime sleepTime;

    public int color;

    public int barChartColor;

    public int qualityType;

    public SleepEntry() {

    }

    public void setQualityType(int qualityType){
        this.qualityType = qualityType;
        this.barChartColor = ColorUtil.getResourcesColor(R.color.sleep_color_15);
        this.color = ColorUtil.getResourcesColor(R.color.sleep_color);
    }

    public SleepEntry(float x, float y, long timestamp, int type) {
        super(x, y, timestamp, type);
//        qualityType = ((int)x + 1) % 5;
    }

    public int getQualityTypeColor(int qualityType) {
        switch (qualityType) {
            case STAGE_BAD:
                return ColorUtil.getResourcesColor(R.color.sleep_quality_bad);
            case STAGE_NORMAL:
                return ColorUtil.getResourcesColor(R.color.sleep_quality_normal);
            case STAGE_BETTER:
                return ColorUtil.getResourcesColor(R.color.sleep_quality_better);
            case STAGE_BEST:
            default:
                return ColorUtil.getResourcesColor(R.color.sleep_quality_best);
        }
    }

}
