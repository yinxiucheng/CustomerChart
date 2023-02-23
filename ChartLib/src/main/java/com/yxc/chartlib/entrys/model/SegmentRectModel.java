package com.yxc.chartlib.entrys.model;

public class SegmentRectModel {
    public float topValue;
    public float bottomValue;
    public int rectColor = -1;
    public int boardColor = -1;
    public int boardWidth = 0;//dp

    public SegmentRectModel(float topValue, float bottomValue) {
        this.topValue = topValue;
        this.bottomValue = bottomValue;
    }
}
