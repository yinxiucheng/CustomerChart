package com.yxc.chartlib.view;

import android.content.Context;

import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaomi.fitness.chart.component.DistanceCompare;


/**
 * @author yxc
 * @date 2019-11-12
 */
public class MyLinearSmoothScroller extends LinearSmoothScroller {

    public MyLinearSmoothScroller(Context context) {
        super(context);
    }

    protected void onTargetFound(DistanceCompare distanceCompare, RecyclerView.State state, Action action) {
        super.onTargetFound(distanceCompare.snapView, state, action);
    }
}
