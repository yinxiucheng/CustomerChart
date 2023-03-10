package com.yxc.chartlib.barchart.itemdecoration;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.fitness.chart.render.YAxisRender;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.render.BarBoardRender;
import com.yxc.chartlib.render.XAxisRender;


/**
 * @author yxc
 * @date 2019-11-16
 */
public class BaseChartItemDecoration<T extends BaseChartAttrs, Y extends BaseYAxis> extends RecyclerView.ItemDecoration {

    protected Y mYAxis;
    protected XAxis mXAxis;
    protected T mBarChartAttrs;
    protected YAxisRender yAxisRenderer;
    protected XAxisRender xAxisRenderer;
    protected BarBoardRender mBarBoardRender;

    public BaseChartItemDecoration(Y yAxis, XAxis xAxis, T barChartAttrs) {
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = barChartAttrs;
        this.yAxisRenderer = new YAxisRender(mBarChartAttrs);
        this.xAxisRenderer = new XAxisRender(mBarChartAttrs);
        this.mBarBoardRender = new BarBoardRender(mBarChartAttrs);
    }


    public void setYAxis(Y yAxis) {
        this.mYAxis = yAxis;
    }

    public void setXAxis(XAxis xAxis) {
        this.mXAxis = xAxis;
    }

}
