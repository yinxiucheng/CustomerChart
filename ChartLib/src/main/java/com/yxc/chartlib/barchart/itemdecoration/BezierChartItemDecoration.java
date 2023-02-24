package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BezierChartAttrs;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.formatter.DefaultBarChartValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.render.BezierChartRender;

/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 *
 */
public class BezierChartItemDecoration extends BaseChartItemDecoration<BezierChartAttrs, YAxis> {

    private BezierChartRender mBezierRender;
    private ValueFormatter mBarChartValueFormatter;

    public BezierChartItemDecoration(YAxis yAxis, XAxis xAxis, BezierChartAttrs barChartAttrs) {
        super(yAxis, xAxis, barChartAttrs);
        this.mBarChartValueFormatter = new DefaultBarChartValueFormatter(0);
        this.mBezierRender = new BezierChartRender(mBarChartAttrs, mBarChartValueFormatter);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        //横向 list 画竖线
        yAxisRenderer.drawYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
        yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

        xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
        xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度
        mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框

        mBezierRender.drawBezierChart(canvas, parent, mYAxis);
        mBezierRender.drawBarChartValue(canvas, parent, mYAxis);//draw LineChart value
    }
}
