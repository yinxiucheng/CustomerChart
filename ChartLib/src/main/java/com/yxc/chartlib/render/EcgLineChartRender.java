package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.LineChartAttrs;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.EcgEntry;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.utils.DisplayUtil;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/14
 */
final public class EcgLineChartRender extends LineChartRender {
    private LineChartAttrs mLineChartAttrs;
    private Paint mLineChartPaint;
    private Paint mTextPaint;
    private Paint mLineFillPaint;
    protected Paint mHighLightValuePaint;

    protected ValueFormatter mHighLightValueFormatter;

    public EcgLineChartRender(LineChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, highLightValueFormatter);
        this.mLineChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
        initLineFillPaint();
        initHighLightPaint();
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    protected void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mLineChartAttrs.txtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }


    protected void initBarChartPaint() {
        mLineChartPaint = new Paint();
        mLineChartPaint.reset();
        mLineChartPaint.setAntiAlias(true);
        mLineChartPaint.setStyle(Paint.Style.STROKE);
        mLineChartPaint.setStrokeCap(Paint.Cap.ROUND);
        mLineChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mLineChartPaint.setColor(mLineChartAttrs.chartColor);
    }

    private void initLineFillPaint() {
        mLineFillPaint = new Paint();
        mLineFillPaint.reset();
        mLineFillPaint.setAntiAlias(true);
        mLineFillPaint.setStyle(Paint.Style.FILL);
        mLineFillPaint.setColor(mLineChartAttrs.chartColor);
    }

    protected void initHighLightPaint() {
        mHighLightValuePaint = new Paint();
        mHighLightValuePaint.reset();
        mHighLightValuePaint.setAntiAlias(true);
        mHighLightValuePaint.setStyle(Paint.Style.FILL);
        mHighLightValuePaint.setStrokeWidth(1);
        mHighLightValuePaint.setColor(Color.WHITE);
        mHighLightValuePaint.setTextSize(DisplayUtil.dip2px(12));
    }

    public <T extends RecyclerBarEntry> void drawLineChart(Canvas canvas, RecyclerView parent, YAxis mYAxis) {
        final float parentRightBoard = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();
//        BaseBarChartAdapter adapter = (BaseBarChartAdapter) parent.getAdapter();
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            T barEntry = (T) child.getTag();
            float preValue = Integer.MIN_VALUE;
            if (i > 0){
                View pointF1Child = parent.getChildAt(i - 1);
                T barEntryLeft = (T) pointF1Child.getTag();
                if (barEntryLeft instanceof EcgEntry){
                    List<Float> values = ((EcgEntry) barEntryLeft).values;
                    if (values.size() > 0){
                        preValue =  values.get(values.size() - 1);
                    }
                }
            }
            if (barEntry instanceof EcgEntry){
                List<Float> values = ((EcgEntry) barEntry).values;
                RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mLineChartAttrs, barEntry);
                if (rectF.left < parentLeft || rectF.right > parentRightBoard){
                    continue;
                }
                float innerItemWidth = rectF.width()/values.size();
                float startX = rectF.left;
                preValue = preValue == Integer.MIN_VALUE?values.get(0): preValue;
                float firstPosition = ChartComputeUtil.getYPosition(preValue, parent, mYAxis, mLineChartAttrs);
                Path pathItem = new Path();
                pathItem.moveTo(startX, firstPosition);
                for (int j = 0; j < values.size(); j++) {
                    float yPosition = ChartComputeUtil.getYPosition(values.get(j), parent, mYAxis, mLineChartAttrs);
                    pathItem.lineTo(startX + j * innerItemWidth, yPosition );
                }
                canvas.drawPath(pathItem, mLineChartPaint);
            }
        }
    }
}
