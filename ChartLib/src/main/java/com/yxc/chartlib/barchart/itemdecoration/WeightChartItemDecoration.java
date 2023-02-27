package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.LineChartAttrs;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.render.LineChartRender;

/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class WeightChartItemDecoration extends BaseChartItemDecoration<LineChartAttrs, YAxis> {
    private LineChartRender mLineChartRender;
    private ValueFormatter mHighLightValueFormatter;

    public WeightChartItemDecoration(YAxis yAxis, XAxis xAxis, LineChartAttrs barChartAttrs) {
        super(yAxis, xAxis, barChartAttrs);
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.mLineChartRender = new LineChartRender(mBarChartAttrs, mHighLightValueFormatter);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        //横向 list 画竖线
        yAxisRenderer.drawStandardLines(canvas, parent, mYAxis);
        mLineChartRender.drawLineChart(canvas, parent, mYAxis);//draw BarChart
        mLineChartRender.drawHighLight(canvas, parent, mYAxis);
    }
}
