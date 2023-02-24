package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.VarietyMaxYAxis;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.render.EnergyBarChartRender;

/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class EnergyBarChartItemDecoration extends BaseChartItemDecoration<BarChartAttrs, VarietyMaxYAxis> {

    private EnergyBarChartRender mBarChartRender;

    public EnergyBarChartItemDecoration(VarietyMaxYAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        super(yAxis, xAxis, barChartAttrs);
        this.mBarChartRender = new EnergyBarChartRender(mBarChartAttrs);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
//        yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
//        if (AppUtil.isRTLDirection()) {
//            yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis, false);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
//        } else {
//            yAxisRenderer.drawRightYAxisLabel(canvas, parent, mYAxis, false);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
//        }
        yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线
        xAxisRenderer.drawXAxis(canvas, parent, mXAxis);
//        xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);
//        xAxisRenderer.drawBackground(canvas, parent);
        mBarChartRender.drawBarChart(canvas, parent, mYAxis);//draw LineChart
//        mBarBoardRender.drawBarBorder1(canvas, parent);
    }
}
