package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.render.BarChartRender;


/**
 * @author yxc
 * @since 2019/4/6
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class DisplayChartItemDecoration extends BaseChartItemDecoration<BarChartAttrs, YAxis> {

    private BarChartRender mBarChartRender;

    public DisplayChartItemDecoration(YAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        super(yAxis, xAxis, barChartAttrs);
        this.mBarChartRender = new BarChartRender(mBarChartAttrs);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        //横向 list 画竖线
        xAxisRenderer.drawXAxisDisplay(canvas, parent, mBarChartAttrs);//绘制数据首页中的供显示用的非数据关联的坐标。
        mBarChartRender.drawBarChartDisplay(canvas, parent, mYAxis);//draw BarChart
    }
}
