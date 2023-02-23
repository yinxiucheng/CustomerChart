package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xiaomi.fitness.chart.attrs.BarChartAttrs;
import com.xiaomi.fitness.chart.component.VarietyMaxYAxis;
import com.xiaomi.fitness.chart.component.YAxis;
import com.xiaomi.fitness.chart.entrys.MaxMinEntry;
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.chart.formatter.DefaultHighLightMarkValueFormatter;
import com.xiaomi.fitness.chart.formatter.ValueFormatter;
import com.xiaomi.fitness.chart.render.BaseChartRender;
import com.xiaomi.fitness.chart.util.CanvasUtil;
import com.xiaomi.fitness.chart.util.ChartComputeUtil;
import com.xiaomi.fitness.chart.util.RoundRectType;
import com.xiaomi.fitness.common.utils.DecimalUtil;
import com.xiaomi.fitness.common.utils.DisplayUtil;
import com.xiaomi.fitness.common.utils.TextUtil;


/**
 * @author yxc
 * @since 2019/4/14
 */
public class MaxMinBarChartRender<T extends YAxis> extends BaseChartRender<MaxMinEntry, BarChartAttrs> {

    private BarChartAttrs mBarChartAttrs;

    public MaxMinBarChartRender(BarChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, highLightValueFormatter);
        this.mBarChartAttrs = barChartAttrs;
    }

    @Override
    protected int getChartColor(RecyclerBarEntry entry) {
        return mBarChartAttrs.chartColor;
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawBarChart(final Canvas canvas, @NonNull final RecyclerView parent, final T mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            MaxMinEntry rateEntry = (MaxMinEntry) child.getTag();
            RectF rectFMinMax = getMinMaxRectF(child, parent, mYAxis, mBarChartAttrs, rateEntry);
            RectF rectFAverage = getAverageRectF(child, parent, mYAxis, mBarChartAttrs, rateEntry);
//            float radius = (rectFAverage.right - rectFAverage.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;
            float radius = 36f;
            drawChart(canvas, rectFMinMax, parentLeft, parentRight, radius, mBarChartAttrs.rateChartLightColor);

            if (rateEntry.getY() > 0 && mBarChartAttrs.enableAverageRectF){
                drawChart(canvas, rectFAverage, parentLeft, parentRight, radius, mBarChartAttrs.rateChartDarkColor);
            }
        }
    }

    private static <E extends YAxis> RectF getMinMaxRectF(View child, final RecyclerView parent, E yAxis,
                                                          BarChartAttrs mBarChartAttrs, MaxMinEntry rateEntry) {

        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - mBarChartAttrs.contentPaddingTop;
        float contentTop = parent.getPaddingTop() + mBarChartAttrs.contentPaddingTop;
        final float yMin= mBarChartAttrs.yAxisMinimum;
        float maxHeight = (rateEntry.maxY - yMin) / (yAxis.getAxisMaximum() - yMin) * realYAxisLabelHeight;
        float minHeight = (rateEntry.minY - yMin) / (yAxis.getAxisMaximum() - yMin) * realYAxisLabelHeight;
        float rectFTop = contentBottom - maxHeight;
        float rectFBottom = contentBottom - minHeight;
        if(rectFTop == rectFBottom && rectFTop != contentBottom)
            rectFBottom += DisplayUtil.dip2px(2);

        float width = child.getWidth();
        float barSpaceWidth = width * mBarChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        final float top = Math.max(rectFTop, contentTop);
        rectF.set(left, top, right, rectFBottom);
        return rectF;
    }


    /**
     * 静息心率
     */
    private static<E extends YAxis> RectF getAverageRectF(View child, final RecyclerView parent, E mYAxis,
                                                          BarChartAttrs mBarChartAttrs, MaxMinEntry rateEntry) {
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - mBarChartAttrs.contentPaddingTop;
        float contentTop = parent.getPaddingTop() + mBarChartAttrs.contentPaddingTop;
        final float yMin= mBarChartAttrs.yAxisMinimum;
        float height = (rateEntry.getY() - yMin)/ (mYAxis.getAxisMaximum() - yMin) * realYAxisLabelHeight;
        float top = contentBottom - height;
        final float topCenter = Math.max(top, contentTop);
        float rectFTop = topCenter - DisplayUtil.dip2px(2);
        float rectFBottom = topCenter + DisplayUtil.dip2px(2);
        float width = child.getWidth();
        float barSpaceWidth = width * mBarChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        rectF.set(left, rectFTop, right, rectFBottom);

        return rectF;
    }


    protected void drawChart(Canvas canvas, RectF rectF, float parentLeft, float parentRight, float radius, int paintColor) {
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft;
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_RIGHT);
            mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            mBarChartPaint.setColor(paintColor);
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_ALL);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            float distance = (parentRight - rectF.left);
            rectF.right = rectF.left + distance;
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_LEFT);
            mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        }
    }


    final public void drawBarChartDisplay(Canvas canvas, @NonNull RecyclerView parent, T mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            MaxMinEntry barEntry = (MaxMinEntry) child.getTag();
            RectF rectF = ChartComputeUtil.getChartRectF(child, parent, mYAxis, mBarChartAttrs, barEntry);
//            float radius = (rectF.right - rectF.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;
            float radius = 36;
            if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
                //中间的; 浮点数的 == 比较需要注意
                mBarChartPaint.setColor(mBarChartAttrs.chartColor);
                Path path = CanvasUtil.createRectRoundPath(rectF, radius);
                canvas.drawPath(path, mBarChartPaint);
            }
        }
    }


//    //绘制选中时 highLight 标线及浮框。
//    public void drawHighLightRate(Canvas canvas, @NonNull RecyclerView parent, VarietyMaxYAxis yAxis) {
//        if (mBarChartAttrs.enableValueMark) {
//            float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
//            int childCount = parent.getChildCount();
//            View child;
//            for (int i = 0; i < childCount; i++) {
//                child = parent.getChildAt(i);
//                MaxMinEntry rateEntry = (MaxMinEntry) child.getTag();
//                float width = child.getWidth();
//                float childCenter = child.getLeft() + width / 2;
//                String valueStr = mHighLightValueFormatter.getBarLabel(rateEntry);
//                if (rateEntry.isSelected() && !TextUtils.isEmpty(valueStr)) {
//                    int chartColor = mBarChartAttrs.highLightColor;
//                    RectF rectF  = drawHighLightValue(canvas, valueStr, childCenter,
//                            parent, chartColor);
//                    // 上端点、下端点
//                    float[] points = new float[]{childCenter, contentBottom,
//                            childCenter, rectF.bottom};
//                    drawHighLightLine(canvas, points, chartColor);
//                }
//            }
//        }
//    }
//
//    //绘制柱状图选中浮框
//    protected RectF drawHighLightValue(Canvas canvas, String valueStr, float childCenter,
//                                    RecyclerView parent, int barChartColor) {
//        float contentRight = parent.getWidth() - parent.getPaddingRight();
//        float contentLeft = parent.getPaddingLeft();
//        float parentTop = parent.getPaddingTop() + mBarChartAttrs.contentPaddingTop - DisplayUtil.dip2px(4);
//
//        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_STR);
//        float leftEdgeDistance = Math.abs(childCenter - contentLeft);
//        float rightEdgeDistance = Math.abs(contentRight - childCenter);
//
//        float leftPadding = DisplayUtil.dip2px(10);
//        float rightPadding = DisplayUtil.dip2px(10);
//
//
//        float rectBottom = parentTop;
//        float txtTopPadding = DisplayUtil.dip2px(3);
//
//
//        String leftStr = strings[0];
//        float txtRightWidth = 0;
//
//        String rightStr = "";
//        float centerPadding = 0;
//        if (strings.length > 1){
//            rightStr = strings[1];
//            txtRightWidth = mHighLightSmallPaint.measureText(rightStr);
//            centerPadding = DisplayUtil.dip2px(16);
//        }
//
//        float txtLeftWidth = mHighLightSmallPaint.measureText(leftStr);
//        float rectFHeight = TextUtil.getTxtHeight1(mHighLightSmallPaint) + txtTopPadding * 2;
//        float txtWidth = txtLeftWidth + txtRightWidth + leftPadding + rightPadding + centerPadding;
//        float edgeDistance = txtWidth / 2.0f;
//        float rectTop = parentTop - rectFHeight;
//
//        //绘制RectF
//        RectF rectF = new RectF();
//        float radius = mBarChartAttrs.highLightRoundRectRadius;
//        mBarChartPaint.setColor(barChartColor);
//        if (leftEdgeDistance <= edgeDistance) {//矩形框靠左对齐
//            rectF.set(contentLeft, rectTop, contentLeft + txtWidth, rectBottom);
//            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
//        } else if (rightEdgeDistance <= edgeDistance) {//矩形框靠右对齐
//            rectF.set(contentRight - txtWidth, rectTop, contentRight, rectBottom);
//            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
//        } else {//居中对齐。
//            rectF.set(childCenter - edgeDistance, rectTop, childCenter + edgeDistance, rectBottom);
//            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
//        }
//
//        //绘文字
//        float txtTop = rectTop + 3 * txtTopPadding;
//        RectF leftRectF = new RectF(rectF.left + leftPadding, txtTop,
//                rectF.left + leftPadding + txtLeftWidth, txtTop + rectFHeight);
//        mHighLightSmallPaint.setTextAlign(Paint.Align.LEFT);
//        Paint.FontMetrics fontMetrics = mHighLightSmallPaint.getFontMetrics();
//        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
//        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
//        int baseLineY = (int) (leftRectF.centerY() + (top + bottom) / 2);//基线中间点的y轴计算公式
//        canvas.drawText(leftStr, rectF.left + leftPadding, baseLineY, mHighLightSmallPaint);
//
//        if (strings.length > 1){
//            float dividerLineStartX = rectF.left + leftPadding + txtLeftWidth + centerPadding / 2.0f;
//            float dividerLineStartY = rectTop + DisplayUtil.dip2px(6);
//            float dividerLineEndX = dividerLineStartX;
//            float dividerLineEndY = rectBottom - DisplayUtil.dip2px(6);
//            float[] lines = new float[]{dividerLineStartX, dividerLineStartY, dividerLineEndX, dividerLineEndY};
//            canvas.drawLines(lines, mHighLightSmallPaint);
//
//            float rightRectFStart = rectF.left + leftPadding + txtLeftWidth + centerPadding;
//            RectF rightRectF = new RectF(rightRectFStart, txtTop, rectF.right - rightPadding, txtTop + rectFHeight);
//            canvas.drawText(rightStr, rightRectF.left, baseLineY, mHighLightSmallPaint);
//        }
//        return rectF;
//    }
//
//    protected void drawHighLightLine(Canvas canvas, float[] floats, int barChartColor) {
//        Paint.Style previous = mBarChartPaint.getStyle();
//        float strokeWidth = mBarChartPaint.getStrokeWidth();
//        int color = mBarChartPaint.getColor();
//        // set
//        mBarChartPaint.setStyle(Paint.Style.FILL);
//        mBarChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
//        mBarChartPaint.setColor(barChartColor);
//        canvas.drawLines(floats, mBarChartPaint);
//        // restore
//        mBarChartPaint.setStyle(previous);
//        mBarChartPaint.setStrokeWidth(strokeWidth);
//        mBarChartPaint.setColor(color);
//    }

}
