package com.yxc.chartlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yxc.chartlib.attrs.BezierChartAttrs;
import com.yxc.chartlib.attrs.ChartAttrsUtil;
import com.yxc.chartlib.barchart.itemdecoration.BezierChartItemDecoration;

/**
 * @author yxc
 * @since 2019/4/10
 */
public class BezierChartRecyclerView extends BaseChartRecyclerView<BezierChartAttrs, BezierChartItemDecoration> {


    public BezierChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected BezierChartAttrs getAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        return ChartAttrsUtil.getBezierChartAttrs(context, attrs);
    }

}
