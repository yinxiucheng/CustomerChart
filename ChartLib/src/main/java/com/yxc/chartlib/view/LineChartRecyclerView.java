package com.yxc.chartlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiaomi.fitness.chart.attrs.ChartAttrsUtil;
import com.xiaomi.fitness.chart.attrs.LineChartAttrs;
import com.xiaomi.fitness.chart.barchart.itemdecoration.LineChartItemDecoration;


/**
 * @author yxc
 * @since 2019/4/10
 */
public class LineChartRecyclerView extends BaseChartRecyclerView<LineChartAttrs, LineChartItemDecoration> {


    public LineChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected LineChartAttrs getAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        return ChartAttrsUtil.getLineChartRecyclerAttrs(context, attrs);
    }

}
