package com.yxc.chartlib.entrys.model;

/**
 * @author yxc
 * @date 2019-11-05
 */
public class MaxMinModel {

    public float max;
    public float min;

    public MaxMinModel(float max, float min){
        this.max = max;
        this.min = min;
    }

    @Override
    public String toString() {
        return "MaxMinModel{" +
                "max=" + max +
                ", min=" + min +
                '}';
    }
}
