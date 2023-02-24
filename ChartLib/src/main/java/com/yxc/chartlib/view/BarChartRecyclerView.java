package com.yxc.chartlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.attrs.ChartAttrsUtil;
import com.yxc.chartlib.barchart.itemdecoration.BarChartItemDecoration;

/**
 * @author yxc
 * @since 2019/4/10
 */
public class BarChartRecyclerView extends BaseChartRecyclerView<BarChartAttrs, BarChartItemDecoration> {

    public BarChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected BarChartAttrs getAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        return ChartAttrsUtil.getBarChartRecyclerAttrs(context, attrs);
    }

}
