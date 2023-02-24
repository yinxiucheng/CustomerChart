package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.formatter.DefaultBarChartValueFormatter;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.render.BarChartRender;


/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class BarChartItemDecoration extends BaseChartItemDecoration<BarChartAttrs, YAxis> {

    private BarChartRender mBarChartRender;

    private ValueFormatter mBarChartValueFormatter;
    private ValueFormatter mHighLightValueFormatter;

    public BarChartItemDecoration(YAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        super(yAxis, xAxis, barChartAttrs);
        this.mBarChartValueFormatter = new DefaultBarChartValueFormatter(0);
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.mBarChartRender = new BarChartRender(mBarChartAttrs, mBarChartValueFormatter, mHighLightValueFormatter);
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter){
        this.mHighLightValueFormatter = highLightValueFormatter;
        this.mBarChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
    }


    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        yAxisRenderer.drawYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
        yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

        xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
        xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度
        xAxisRenderer.drawXAxisDisplay(canvas, parent, mBarChartAttrs);//绘制数据首页中的供显示用的非数据关联的坐标。
        xAxisRenderer.drawBackground(canvas, parent);//绘制X 坐标背景。
        mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框

        mBarChartRender.drawHighLight(canvas, parent, mYAxis);
        Log.d("MPChart", "BarChart Render click draw off time:" + System.currentTimeMillis());
        mBarChartRender.drawBarChart(canvas, parent, mYAxis);//draw BarChart
        mBarChartRender.drawBarChartValue(canvas, parent, mYAxis);//draw BarChart value
    }
}
