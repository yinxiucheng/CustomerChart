package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

import com.xiaomi.fitness.chart.attrs.LineChartAttrs;
import com.xiaomi.fitness.chart.component.XAxis;
import com.xiaomi.fitness.chart.component.YAxis;
import com.xiaomi.fitness.chart.formatter.DefaultHighLightMarkValueFormatter;
import com.xiaomi.fitness.chart.formatter.ValueFormatter;
import com.xiaomi.fitness.chart.render.LineChartRender;

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

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter){
        this.mHighLightValueFormatter = highLightValueFormatter;
        this.mLineChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
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
