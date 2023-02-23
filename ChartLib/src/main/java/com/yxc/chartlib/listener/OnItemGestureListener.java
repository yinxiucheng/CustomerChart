package com.yxc.chartlib.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;


/**
 * @author yxc
 * @date 2019-09-17
 */
public interface OnItemGestureListener<T extends RecyclerBarEntry> {

    void onItemClick(View view, int position);

    void onLongItemClick(View view, int position);

    void onItemSelected(T barEntry, int position);

    void onScrollStateChanged(RecyclerView recyclerView, int newState);

    void onScrolled(RecyclerView recyclerView, int dx, int dy);
}
