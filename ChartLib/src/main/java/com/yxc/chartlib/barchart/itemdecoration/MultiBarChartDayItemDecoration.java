package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.render.BarBoardRender;
import com.yxc.chartlib.render.MultiBarChartDayRender;


public class MultiBarChartDayItemDecoration extends BaseChartItemDecoration<BarChartAttrs, YAxis> {

    private MultiBarChartDayRender mBarChartRender;
    private BarBoardRender mBarBoardRender;
    private ValueFormatter mHighLightValueFormatter;

    public MultiBarChartDayItemDecoration(YAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        super(yAxis, xAxis, barChartAttrs);
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.mBarChartRender = new MultiBarChartDayRender(mBarChartAttrs, mHighLightValueFormatter);
        this.mBarBoardRender = new BarBoardRender(mBarChartAttrs);
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
        mBarChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
    }


    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        if (mXAxis == null || mYAxis == null) return;
        //横向 list 画竖线
        yAxisRenderer.drawYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
        yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

        xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
        xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度
        xAxisRenderer.drawXAxisDisplay(canvas, parent, mBarChartAttrs);//绘制数据首页中的供显示用的非数据关联的坐标。
        xAxisRenderer.drawBackground(canvas, parent);

        mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框

        mBarChartRender.drawChart(canvas, parent, mYAxis);//draw BarChart
        mBarChartRender.drawHighLight(canvas, parent, mYAxis);
    }
}
