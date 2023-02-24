package com.yxc.chartlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yxc.chartlib.attrs.ChartAttrsUtil;
import com.yxc.chartlib.attrs.LineChartAttrs;
import com.yxc.chartlib.barchart.itemdecoration.LineChartItemDecoration;

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
