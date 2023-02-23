package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaomi.fitness.chart.attrs.BarChartAttrs;
import com.xiaomi.fitness.chart.component.BaseYAxis;
import com.xiaomi.fitness.chart.component.YAxis;
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.chart.entrys.SegmentBarEntry;
import com.xiaomi.fitness.chart.entrys.model.SegmentRectModel;
import com.xiaomi.fitness.chart.formatter.ValueFormatter;
import com.xiaomi.fitness.chart.transform.RecyclerTransform;
import com.xiaomi.fitness.chart.util.CanvasUtil;
import com.xiaomi.fitness.chart.util.ChartComputeUtil;
import com.xiaomi.fitness.chart.util.RoundRectType;
import com.xiaomi.fitness.common.utils.DecimalUtil;

import java.util.List;

/**
 * @author yxc
 * @since  2019/4/14
 */
public class BarChartRender extends BaseChartRender<RecyclerBarEntry, BarChartAttrs>{

    public BarChartRender(BarChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, highLightValueFormatter);
    }

    public BarChartRender(BarChartAttrs barChartAttrs, ValueFormatter barChartValueFormatter,
                          ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, barChartValueFormatter, highLightValueFormatter);
    }

    public BarChartRender(BarChartAttrs barChartAttrs) {
        super(barChartAttrs);
    }

    @Override
    protected int getChartColor(RecyclerBarEntry entry) {
        return mBarChartAttrs.chartColor;
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawBarChart(final Canvas canvas, @NonNull final RecyclerView parent, final YAxis mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerBarEntry barChart = (RecyclerBarEntry) child.getTag();
            RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mBarChartAttrs, barChart);
//            float radius = rectF.width() * mBarChartAttrs.barChartRoundRectRadiusRatio;
            float radius = 36;
            if (mBarChartAttrs.barChartSegment == BarChartAttrs.SEGMENT_Y) {
                drawSegmentChart(canvas, rectF, radius, parentLeft, parentRight, barChart, parent, mYAxis);
            } else {
                drawChart(canvas, rectF, radius, parentLeft, parentRight, barChart, mBarChartAttrs.chartColor , mBarChartAttrs.chartColor, RoundRectType.TYPE_TOP);
            }
        }
    }

    //绘制柱状图顶部value文字
    public <E extends BaseYAxis> void drawBarChartValue(Canvas canvas, @NonNull RecyclerView parent, E yAxis) {
        if (mBarChartAttrs.enableCharValueDisplay) {
            float parentRight = parent.getWidth() - parent.getPaddingRight();
            float parentLeft = parent.getPaddingLeft();
            int childCount = parent.getChildCount();

            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                RecyclerBarEntry barEntry = (RecyclerBarEntry) child.getTag();
                float width = child.getWidth();
                float childCenter = child.getLeft() + width / 2;
                float top = ChartComputeUtil.getYPosition(barEntry.getY(), parent, yAxis, mBarChartAttrs);
                String valueStr = mBarChartValueFormatter.getBarLabel(barEntry);
                float txtY = top - mBarChartAttrs.barChartValuePaddingBottom;
                drawText(canvas, parentLeft, parentRight, valueStr, childCenter, txtY, mTextPaint);
            }
        }
    }

    final public void drawBarChartDisplay(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerBarEntry barEntry = (RecyclerBarEntry) child.getTag();

            RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mBarChartAttrs, barEntry);
//            float radius = (rectF.right - rectF.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;
            float radius = 36;
            RectF rectFBg = ChartComputeUtil.getBarChartRectFBg(child, parent, mBarChartAttrs);

            if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
                //中间的; 浮点数的 == 比较需要注意
                mBarChartPaint.setColor(mBarChartAttrs.chartColor);
                Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_ALL);
                canvas.drawPath(path, mBarChartPaint);

                mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
                Path pathBg = CanvasUtil.createRectRoundPath(rectFBg, radius, RoundRectType.TYPE_ALL);
                canvas.drawPath(pathBg, mBarChartPaint);
            }
        }
    }

    private void drawChart(Canvas canvas, RectF rectF, float radius, float parentLeft, float parentRight, RecyclerBarEntry barEntry, int rectStrokeColor, int rectColor,int roundType) {
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft;
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_RIGHT_TOP);
            mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            // 中间部分的Item
            if (barEntry.validType == RecyclerBarEntry.TYPE_VALID) {
                mBarChartPaint.setColor(rectColor);
            } else {
                mBarChartPaint.setColor(mBarChartAttrs.invalidChartColor);
            }
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, roundType);
            canvas.drawPath(path, mBarChartPaint);
//            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
            if (rectStrokeColor != rectColor) {
                //需要额外画边框
                mBarChartStrokePaint.setColor(rectStrokeColor);
                canvas.drawPath(path, mBarChartStrokePaint);
//                canvas.drawRoundRect(rectF, radius, radius, mBarChartStrokePaint);
            }
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            float distance = (parentRight - rectF.left);
            rectF.right = rectF.left + distance;
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_LEFT_TOP);
            mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        }
    }

    private void drawSegmentChart(Canvas canvas, RectF rectF, float radius,
                                  float parentLeft, float parentRight,
                                  RecyclerBarEntry barEntry, RecyclerView parentView, YAxis yAxis){
        if (barEntry instanceof SegmentBarEntry){
            SegmentBarEntry segmentBarEntry = (SegmentBarEntry) barEntry;
            List<SegmentRectModel> rectModelList = segmentBarEntry.rectValueModelList;
            float x = rectF.centerX();
            float halfWidth = rectF.width() * 0.5f;
            float left = x - halfWidth;
            float right = x + halfWidth;
            int size = rectModelList.size();
            float contentBottom = parentView.getHeight() - parentView.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            float contentTop = parentView.getPaddingTop() + mBarChartAttrs.contentPaddingTop;
            for (int i = 0; i < size; i++) {
                SegmentRectModel rectModel = rectModelList.get(i);
                float topValue = rectModel.topValue;
                float bottomValue = rectModel.bottomValue;
                float topRect = RecyclerTransform.getPixelForValuesHeightBetweenBottom(parentView, yAxis, mBarChartAttrs, topValue);
                float bottomRect = RecyclerTransform.getPixelForValuesHeightBetweenBottom(parentView, yAxis, mBarChartAttrs, bottomValue);
                RectF rectFSegment = new RectF(left, topRect, right, bottomRect);
                rectFSegment.top = rectF.bottom - rectFSegment.top;
                rectFSegment.bottom = rectF.bottom - rectFSegment.bottom;
                if (rectFSegment.height() < rectFSegment.width()){
                    float reFixTop = rectFSegment.top - halfWidth;
                    float reFixBottom = rectFSegment.bottom + halfWidth;
                    rectFSegment.top = reFixTop;
                    rectFSegment.bottom = reFixBottom;
                    if (reFixTop <= contentTop) {
                        rectFSegment.top = contentTop;
                        rectFSegment.bottom = contentTop + rectF.width();
                    }
                    if (reFixBottom >= contentBottom) {
                        rectFSegment.bottom = contentBottom;
                        rectFSegment.top = contentBottom - rectF.width();
                    }
                }
                if (rectFSegment.top >= contentTop) {
                    drawChart(canvas, rectFSegment, radius, parentLeft, parentRight, barEntry, rectModel.boardColor, rectModel.rectColor, RoundRectType.TYPE_ALL);
                } else if (rectFSegment.bottom > contentTop) {
                    rectFSegment.top = contentTop;
                    drawChart(canvas, rectFSegment, radius, parentLeft, parentRight, barEntry, rectModel.boardColor, rectModel.rectColor,RoundRectType.TYPE_ALL);
                }
            }
        }
    }
}
