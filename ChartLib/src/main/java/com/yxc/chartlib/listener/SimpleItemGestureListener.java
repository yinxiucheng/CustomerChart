package com.yxc.chartlib.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.BarEntry;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

/**
 * @author yxc
 * @since  2019/4/24
 */
public abstract class SimpleItemGestureListener implements OnItemGestureListener<RecyclerBarEntry> {
    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongItemClick(View view, int position) {

    }

    @Override
    public void onItemSelected(RecyclerBarEntry barEntry, int position) {

    }


    public abstract void onItemSelected(BarEntry barEntry, int position);

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

}
