package com.yxc.chartlib.component;

import android.view.View;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

/**
 * @author yxc
 * @since  2019/4/9
 *
 */
public class DistanceCompare<T extends RecyclerBarEntry> {

    public int distanceLeft;
    public int distanceRight;
    public int position;
    public T barEntry;
    public View snapView;

    public DistanceCompare(int distanceLeft, int distanceRight){
        this.distanceLeft = distanceLeft;
        this.distanceRight = distanceRight;
    }

    //月线靠近左边
    public boolean isNearLeft(){
        return distanceLeft < distanceRight;
    }

    //月线靠近左边
    public boolean isNearRight(){
        return distanceLeft > distanceRight;
    }

    @Override
    public String toString() {
        return "DistanceCompare{" +
                "distanceLeft=" + distanceLeft +
                ", distanceRight=" + distanceRight +
                ", position=" + position +
                '}';
    }


    public void setPosition(int position) {
        this.position = position;
    }

    public void setBarEntry(T barEntry) {
        this.barEntry = barEntry;
    }

}
