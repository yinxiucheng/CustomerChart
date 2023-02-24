package com.yxc.chartlib.entrys.model;

import android.content.Context;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.ColorUtil;

/**
 * @author yxc
 * @date 2019-07-11
 *
 * public @interface EnergyState {
 *         int NONE = 0;
 *         int ACTIVITY = 1;
 *         int STRESS = 2;
 *         int RECOVERY = 3;
 *     }
 */
public class EnergyEntry extends RecyclerBarEntry {
    public static final int MAX_ENERGY_VALUE = 100;
    public static final int MIN_ENERGY_VALUE = -100;

    public static final int ENERGY_TYPE_RECOVERY = 3;//恢复

    public static final int ENERGY_TYPE_PRESS = 2;//消耗&压力

    public static final int ENERGY_TYPE_SPORT = 1;//运动

    public static final int ENERGY_TYPE_NONE = 0;//运动

    public int energyType;//能量柱子的种类

    public EnergyEntry() {
    }

    public EnergyEntry(int i, float value, long timestamp, int type) {
        super(i, value, timestamp, type);
    }

    public static int getEnergyTypeColor(Context context, int pressType){
        switch (pressType) {
            case ENERGY_TYPE_RECOVERY:
                return ColorUtil.getResourcesColor(R.color.health_energy_recovery);
            case ENERGY_TYPE_PRESS:
                return ColorUtil.getResourcesColor(R.color.health_energy_press);
            case ENERGY_TYPE_SPORT:
                return ColorUtil.getResourcesColor(R.color.health_energy_sport);
            default:
                return ColorUtil.getResourcesColor(R.color.health_energy_recovery);
        }
    }


    @Override
    public String toString() {
        return "EnergyEntry{" +
                "energyType=" + energyType +
                ", timestamp=" + timestamp +
                ", type=" + type +
                ", value=" + getY() +
                '}';
    }
}
