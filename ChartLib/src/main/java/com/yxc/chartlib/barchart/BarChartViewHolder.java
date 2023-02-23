package com.yxc.chartlib.barchart;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BarChartViewHolder extends RecyclerView.ViewHolder {
    public View contentView;

    BarChartViewHolder(@NonNull View itemView) {
        super(itemView);
        contentView = itemView;
    }
}
