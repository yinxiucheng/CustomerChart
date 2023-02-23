package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

import com.xiaomi.fitness.chart.attrs.BarChartAttrs;
import com.xiaomi.fitness.chart.component.VarietyMaxYAxis;
import com.xiaomi.fitness.chart.component.XAxis;
import com.xiaomi.fitness.chart.formatter.DefaultHighLightMarkValueFormatter;
import com.xiaomi.fitness.chart.formatter.ValueFormatter;
import com.xiaomi.fitness.chart.render.MaxMinBarChartRender;

/**
 * @author yxc
 * @since 2019/4/6
 * <p>
 */
public class MaxMinBarChartItemDecoration extends BaseChartItemDecoration<BarChartAttrs, VarietyMaxYAxis> {

    private MaxMinBarChartRender mBarChartRender;
    private ValueFormatter mHighLightValueFormatter;

    public MaxMinBarChartItemDecoration(VarietyMaxYAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        super(yAxis, xAxis, barChartAttrs);
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.mBarChartRender = new MaxMinBarChartRender(mBarChartAttrs, mHighLightValueFormatter);
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
        this.mBarChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        //横向 list 画竖线
        if (mBarChartAttrs.isDisplay) {
            mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框
            yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线
            xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            xAxisRenderer.drawXAxisDisplay(canvas, parent, mBarChartAttrs);//绘制数据首页中的供显示用的非数据关联的坐标。
            mBarChartRender.drawBarChartDisplay(canvas, parent, mYAxis);//draw BarChart
        } else {
            yAxisRenderer.drawYAxisLabel(canvas, parent, mYAxis);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
            yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线
            xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度
            xAxisRenderer.drawBackground(canvas, parent);
            mBarChartRender.drawBarChart(canvas, parent, mYAxis);//draw BarChart
            mBarChartRender.drawHighLight(canvas, parent, mYAxis);
            mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框
        }
    }
}
