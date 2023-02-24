package com.yxc.chartlib.transform;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;

public class RecyclerTransform extends Transformer {

    public RecyclerTransform(ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
    }

    //value==1 时占多少个 px。
    public static <V extends BaseYAxis> float pxPerValue(final RecyclerView parent, V mYAxis, BaseChartAttrs barChartAttrs){
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - barChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - barChartAttrs.contentPaddingTop;
        float aAxisMaximum = mYAxis.getAxisMaximum();
        float aAxisMinimum = mYAxis.getAxisMinimum();
        float pxPerValue = realYAxisLabelHeight/(aAxisMaximum - aAxisMinimum);
        return pxPerValue;
    }

    //每个像素点对应 value的跨度
    public static <V extends BaseYAxis> float valuePerPx(final RecyclerView parent, V mYAxis, BaseChartAttrs barChartAttrs){
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - barChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - barChartAttrs.contentPaddingTop;
        float aAxisMaximum = mYAxis.getAxisMaximum();
        float aAxisMinimum = mYAxis.getAxisMinimum();
        float valuePerPx = realYAxisLabelHeight / (aAxisMaximum - aAxisMinimum);
        return valuePerPx;
    }

    public static <V extends BaseYAxis> float getPixelForValuesHeightBetweenBottom(final RecyclerView parent, V mYAxis,
                                                                BaseChartAttrs barChartAttrs,
                                                                float value) {
        float valuePerPx = valuePerPx(parent, mYAxis, barChartAttrs);
        float aAxisMinimum = mYAxis.getAxisMinimum();
        return (value - aAxisMinimum) * valuePerPx;
    }

}
