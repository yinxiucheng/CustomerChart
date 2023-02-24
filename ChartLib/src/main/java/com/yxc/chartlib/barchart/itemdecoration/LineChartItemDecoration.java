package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;
import android.util.Log;

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
public class LineChartItemDecoration extends BaseChartItemDecoration<LineChartAttrs, YAxis> {

    private LineChartRender mLineChartRender;
    private ValueFormatter mHighLightValueFormatter;

    public LineChartItemDecoration(YAxis yAxis, XAxis xAxis, LineChartAttrs barChartAttrs) {
        super(yAxis, xAxis, barChartAttrs);
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.mLineChartRender = new LineChartRender(mBarChartAttrs, mHighLightValueFormatter);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        //横向 list 画竖线
        yAxisRenderer.drawYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
        yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

        xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
        xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度
        xAxisRenderer.drawBackground(canvas, parent);

        mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框

        mLineChartRender.drawLineChart(canvas, parent, mYAxis);//draw LineChart
        mLineChartRender.drawHighLight(canvas, parent, mYAxis);//绘制选中高亮
    }


    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter){
        this.mHighLightValueFormatter = highLightValueFormatter;
        this.mLineChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
    }

}
