package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.xiaomi.fitness.chart.attrs.BaseChartAttrs;
//import com.xiaomi.fitness.chart.attrs.LineChartAttrs;
//import com.xiaomi.fitness.chart.barchart.BaseBarChartAdapter;
//import com.xiaomi.fitness.chart.barchart.itemdecoration.LineChartDrawable;
//import com.xiaomi.fitness.chart.component.BaseYAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.utils.DisplayUtil;
import com.yxc.chartlib.utils.TextUtil;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.attrs.LineChartAttrs;
import com.yxc.chartlib.barchart.BaseBarChartAdapter;
import com.yxc.chartlib.barchart.itemdecoration.LineChartDrawable;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.utils.AppUtil;

import java.util.List;

/**
 * @author yxc˙˙
 * @since 2019/4/14
 */
public class LineChartRender {
    private LineChartAttrs mLineChartAttrs;
    private Paint mLineFillPaint;

    private Paint mLineChartPaint;
    private Paint mTextPaint;

    protected Paint mHighLightValuePaint;

    private Paint mLineCirclePaint;

    private Paint mLinePaint;

    protected ValueFormatter mHighLightValueFormatter;

    public LineChartRender(LineChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {

        this.mLineChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
        initLineFillPaint();
        initHighLightPaint();
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    private void initHighLightPaint() {
        mHighLightValuePaint = new Paint();
        mHighLightValuePaint.reset();
        mHighLightValuePaint.setAntiAlias(true);
        mHighLightValuePaint.setStyle(Paint.Style.FILL);
        mHighLightValuePaint.setStrokeWidth(1);
        mHighLightValuePaint.setColor(Color.WHITE);
        mHighLightValuePaint.setTextSize(DisplayUtil.dip2px(12));
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mLineChartAttrs.txtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }

    private void initBarChartPaint() {
        mLineChartPaint = new Paint();
        mLineChartPaint.reset();
        mLineChartPaint.setAntiAlias(true);
        mLineChartPaint.setStyle(Paint.Style.FILL);
        mLineChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mLineChartPaint.setColor(mLineChartAttrs.chartColor);
    }

    private void initLineFillPaint() {
        mLineFillPaint = new Paint();
        mLineFillPaint.reset();
        mLineFillPaint.setAntiAlias(true);
        mLineFillPaint.setStyle(Paint.Style.FILL);
//        mLineFillPaint.setColor(mBarChartAttrs.lineColor);
        mLineCirclePaint = new Paint();
        mLineCirclePaint.reset();
        mLineCirclePaint.setAntiAlias(true);
        mLineCirclePaint.setStyle(Paint.Style.STROKE);
        mLineCirclePaint.setColor(mLineChartAttrs.lineColor);
        mLinePaint = new Paint();
        mLinePaint.reset();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mLineChartAttrs.lineStrokeWidth);
        mLinePaint.setColor(mLineChartAttrs.lineColor);
    }

    //绘制选中时 highLight 标线及浮框。
    public void drawHighLight(Canvas canvas, @NonNull RecyclerView parent, YAxis yAxis) {
        if (mLineChartAttrs.enableValueMark) {
            float parentTop = parent.getPaddingTop();
            float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mLineChartAttrs.contentPaddingBottom;
            int childCount = parent.getChildCount();
            float contentRight = parent.getWidth() - parent.getPaddingRight();
            float contentLeft = parent.getPaddingLeft();

            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                RecyclerBarEntry barEntry = (RecyclerBarEntry) child.getTag();
                float width = child.getWidth();
                float childCenter = child.getLeft() + width / 2;
                String valueStr = mHighLightValueFormatter.getBarLabel(barEntry);
                float[] points = new float[]{childCenter, contentBottom, childCenter, parentTop};
                if (mLineChartAttrs.lineHighLightType == LineChartAttrs.HIGH_LIGHT_LINE_Y) {
                    float yPosition = ChartComputeUtil.getYPosition(barEntry.getY(), parent, yAxis, mLineChartAttrs);
                    points = new float[]{childCenter, yPosition - mLineChartAttrs.linePointSelectRadius, childCenter, parentTop};
                }
                if (barEntry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    int chartColor = mLineChartAttrs.chartColor;
                    drawHighLightLine(canvas, points, chartColor);
                    drawHighLightValue(canvas, valueStr, childCenter, contentLeft, contentRight, parentTop, chartColor);
                }
            }
        }
    }

    //绘制柱状图顶部value文字
    private void drawHighLightValue(Canvas canvas, String valueStr, float childCenter,
                                    float contentLeft, float contentRight, float contentTop, int barChartColor) {
        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_STR);
        float leftEdgeDistance = Math.abs(childCenter - contentLeft);
        float rightEdgeDistance = Math.abs(contentRight - childCenter);

        float leftPadding = DisplayUtil.dip2px(8);
        float rightPadding = DisplayUtil.dip2px(8);
        float centerPadding = DisplayUtil.dip2px(16);

        float rectBottom = contentTop;
        float txtTopPadding = DisplayUtil.dip2px(8);

        String leftStr = strings[0];
        String rightStr = strings[1];

        float txtLeftWidth = mHighLightValuePaint.measureText(leftStr);
        float txtRightWidth = mHighLightValuePaint.measureText(rightStr);

        float rectFHeight = TextUtil.getTxtHeight1(mHighLightValuePaint) + txtTopPadding * 2;
        float txtWidth = txtLeftWidth + txtRightWidth + leftPadding + rightPadding + centerPadding;

        float edgeDistance = txtWidth / 2.0f;
        float rectTop = contentTop - rectFHeight;

        //绘制RectF
        RectF rectF = new RectF();
        mLineChartPaint.setColor(barChartColor);
        if (leftEdgeDistance <= edgeDistance) {//矩形框靠左对齐
            rectF.set(contentLeft, rectTop, contentLeft + txtWidth, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mLineChartPaint);
        } else if (rightEdgeDistance <= edgeDistance) {//矩形框靠右对齐
            rectF.set(contentRight - txtWidth, rectTop, contentRight, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mLineChartPaint);
        } else {//居中对齐。
            rectF.set(childCenter - edgeDistance, rectTop, childCenter + edgeDistance, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mLineChartPaint);
        }

        //绘文字
        RectF leftRectF = new RectF(rectF.left + leftPadding, rectTop + txtTopPadding,
                rectF.left + leftPadding + txtLeftWidth, rectTop + txtTopPadding + rectFHeight);
        mHighLightValuePaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fontMetrics = mHighLightValuePaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (leftRectF.centerY() + (top + bottom) / 2);//基线中间点的y轴计算公式
        canvas.drawText(leftStr, rectF.left + leftPadding, baseLineY, mHighLightValuePaint);

        float dividerLineStartX = rectF.left + leftPadding + txtLeftWidth + centerPadding / 2.0f;
        float dividerLineStartY = rectTop + DisplayUtil.dip2px(10);
        float dividerLineEndX = dividerLineStartX;
        float dividerLineEndY = rectBottom - DisplayUtil.dip2px(10);
        float[] lines = new float[]{dividerLineStartX, dividerLineStartY, dividerLineEndX, dividerLineEndY};
        canvas.drawLines(lines, mHighLightValuePaint);

        float rightRectFStart = rectF.left + leftPadding + txtLeftWidth + centerPadding;
        RectF rightRectF = new RectF(rightRectFStart, rectTop + txtTopPadding, rectF.right - rightPadding, rectBottom - txtTopPadding);
        canvas.drawText(rightStr, rightRectF.left, baseLineY, mHighLightValuePaint);
    }


    private void drawHighLightLine(Canvas canvas, float[] floats, int barChartColor) {
        Paint.Style previous = mLineChartPaint.getStyle();
        float strokeWidth = mLineChartPaint.getStrokeWidth();
        int color = mLineChartPaint.getColor();
        // set
        mLineChartPaint.setStyle(Paint.Style.FILL);
        mLineChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mLineChartPaint.setColor(barChartColor);
        canvas.drawLines(floats, mLineChartPaint);
        // restore
        mLineChartPaint.setStyle(previous);
        mLineChartPaint.setStrokeWidth(strokeWidth);
        mLineChartPaint.setColor(color);
    }

    public <E extends BaseYAxis> void drawLineChart(Canvas canvas, RecyclerView parent, E mYAxis) {
        if (mLineChartAttrs.enableXAxisLineCircle) {
            if (AppUtil.isRTLDirection()) {
                drawLineChartWithPointRTL(canvas, parent, mYAxis);
            } else {
                drawLineChartWithPoint(canvas, parent, mYAxis);
            }

        } else {
            if (AppUtil.isRTLDirection()) {
                drawLineChartWithoutPointRTL(canvas, parent, mYAxis);
            } else {
                drawLineChartWithoutPoint(canvas, parent, mYAxis);
            }
        }
    }

    private <T extends RecyclerBarEntry, E extends BaseYAxis> void drawLineChartWithoutPointRTL(
            Canvas canvas, RecyclerView parent, E mYAxis) {
        final float parentStart = parent.getPaddingLeft();
        final float parentEnd = parent.getWidth() - parent.getPaddingRight();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) parent.getAdapter();
        List<T> entryList = adapter.getEntries();
        final int childCount = parent.getChildCount();
        int adapterPosition;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            T barEntry = (T) child.getTag();
            adapterPosition = parent.getChildAdapterPosition(child);
            int viewWidth = child.getWidth();
            RectF rectF = ChartComputeUtil
                    .getChartRectF(child, parent, mYAxis, mLineChartAttrs, barEntry);
            float yZeroLine = rectF.bottom;
            PointF pointF2 = new PointF((rectF.left + rectF.right) / 2, rectF.top);
            //pointF2 即为当前for 循环到的点，从左往右一共涉及到4个点 pointF0, pointF1, pointF2, pointF3; 其中 pointF1、pointF2为显示的。
            if (i >= 1) {
                View pointF1Child = parent.getChildAt(i - 1);
                T barEntryLeft = (T) pointF1Child.getTag();
                RectF rectFLeft = ChartComputeUtil
                        .getChartRectF(pointF1Child, parent, mYAxis, mLineChartAttrs, barEntryLeft);
                PointF pointF1 = new PointF((rectFLeft.left + rectFLeft.right) / 2, rectFLeft.top);
                if (pointF1.x >= parentStart && pointF2.x <= parentEnd) {
                    float[] pointsOut = new float[]{pointF1.x, pointF1.y, pointF2.x, pointF2.y};
                    drawChartLine(canvas, pointsOut, yZeroLine);
                    drawFill(parent, mLineChartAttrs, canvas, pointF1, pointF2, rectF.bottom);
                    if (pointF1Child.getLeft() < parentStart) {//左边界，处理pointF1值显示出来了的情况。
                        if (adapterPosition - 2 >= 0) {
                            PointF pointF0 = createNearPoint(parent, entryList, pointF1,
                                    adapterPosition - 2, viewWidth, mYAxis, true);
                            drawLineLeftBoundary(parent, canvas, pointF0, pointF1, parentStart,
                                    yZeroLine);
                            if (pointF0.x > parentStart && adapterPosition - 3 >= 0) {//处理左边界第2个点。
                                PointF pointFEx = createNearPoint(parent, entryList, pointF0,
                                        adapterPosition - 3, viewWidth, mYAxis, true);
                                drawLineLeftBoundary(parent, canvas, pointFEx, pointF0, parentStart,
                                        yZeroLine);
                            }
                        }
                    } else if (child.getRight() < parentEnd && parentEnd - child.getRight() <= child
                            .getWidth()) {
                        //右边界处理情况，pointF3显示出来跟没有显示出来。
                        if (adapterPosition + 1 < entryList.size()) {
                            PointF pointF3 = createNearPoint(parent, entryList, pointF2,
                                    adapterPosition + 1, viewWidth, mYAxis, false);
                            if (pointF3.x > parentEnd) {
                                drawLineRightBoundary(parent, canvas, pointF2, pointF3, parentEnd,
                                        yZeroLine);
                            } else if (pointF3.x < parentEnd) {
                                if (adapterPosition + 2 < entryList.size()) {
                                    PointF pointF4 = createNearPoint(parent, entryList, pointF3,
                                            adapterPosition + 2, viewWidth, mYAxis, false);
                                    drawLineRightBoundary(parent, canvas, pointF3, pointF4,
                                            parentEnd, yZeroLine);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentStart
                        && pointF1Child.getRight() >= parentStart) {//左边界，处理pointF1值没有显示出来
                    drawLineLeftBoundary(parent, canvas, pointF1, pointF2, parentStart, yZeroLine);
                }
            }
        }
    }

    private <T extends RecyclerBarEntry, E extends BaseYAxis> void drawLineChartWithoutPoint(
            Canvas canvas, RecyclerView parent, E mYAxis) {
        Log.i("test", "drawLineChartWithoutPoint");
        final float parentStart = parent.getWidth() - parent.getPaddingRight();
        final float parentEnd = parent.getPaddingLeft();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) parent.getAdapter();
        List<T> entryList = adapter.getEntries();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            drawLineChartInner(canvas, parent, mYAxis, i, parentStart, parentEnd, entryList, childCount);
        }
    }

    private <T extends RecyclerBarEntry, E extends BaseYAxis>
    void drawLineChartInner(Canvas canvas, RecyclerView parent, E mYAxis, int i, float parentStart, float parentEnd, List<T> entryList, int childCount){
        View child = parent.getChildAt(i);
        T barEntry = (T) child.getTag();
        int adapterPosition = parent.getChildAdapterPosition(child);
        int viewWidth = child.getWidth();
        RectF rectF = ChartComputeUtil
                .getChartRectF(child, parent, mYAxis, mLineChartAttrs, barEntry);
        float yZeroLine = rectF.bottom;
        PointF pointF2 = new PointF((rectF.left + rectF.right) / 2, rectF.top);
        //pointF2 即为当前for 循环到的点，从左往右一共涉及到4个点 pointF0, pointF1, pointF2, pointF3; 其中 pointF1、pointF2为显示的。
        if (i < childCount - 1) {
            View pointF1Child = parent.getChildAt(i + 1);
            T barEntryLeft = (T) pointF1Child.getTag();
            RectF rectFLeft = ChartComputeUtil
                    .getChartRectF(pointF1Child, parent, mYAxis, mLineChartAttrs, barEntryLeft);
            PointF pointF1 = new PointF((rectFLeft.left + rectFLeft.right) / 2, rectFLeft.top);
            if (pointF1.x >= parentEnd && pointF2.x <= parentStart) {
                float[] pointsOut = new float[]{pointF1.x, pointF1.y, pointF2.x, pointF2.y};
                drawChartLine(canvas, pointsOut, yZeroLine);
                drawFill(parent, mLineChartAttrs, canvas, pointF1, pointF2, rectF.bottom);
                if (pointF1Child.getLeft() < parentEnd) {//左边界，处理pointF1值显示出来了的情况。
                    if (adapterPosition + 2 < entryList.size()) {
                        PointF pointF0 = createNearPoint(parent, entryList, pointF1,
                                adapterPosition + 2, viewWidth, mYAxis, true);
                        drawLineLeftBoundary(parent, canvas, pointF0, pointF1, parentEnd,
                                yZeroLine);
                    }
                } else if (child.getRight() < parentStart
                        && parentStart - child.getRight() <= child.getWidth()) {
                    //右边界处理情况，pointF3显示出来跟没有显示出来。
                    if (adapterPosition - 1 > 0) {
                        PointF pointF3 = createNearPoint(parent, entryList, pointF2,
                                adapterPosition - 1, viewWidth, mYAxis, false);
                        if (pointF3.x > parentStart) {
                            drawLineRightBoundary(parent, canvas, pointF2, pointF3, parentStart,
                                    yZeroLine);
                        } else if (pointF3.x < parentStart) {
                            if (adapterPosition - 2 > 0) {
                                PointF pointF4 = createNearPoint(parent, entryList, pointF3,
                                        adapterPosition - 2, viewWidth, mYAxis, false);
                                drawLineRightBoundary(parent, canvas, pointF3, pointF4,
                                        parentStart, yZeroLine);
                            }
                        }
                    }
                }
            } else if (pointF1.x < parentEnd
                    && pointF1Child.getRight() >= parentEnd) {//左边界，处理pointF1值没有显示出来
                drawLineLeftBoundary(parent, canvas, pointF1, pointF2, parentEnd, yZeroLine);
            }
        }
    }

    private void drawLineRightBoundary(RecyclerView parent, Canvas canvas, PointF pointLeft,
            PointF pointRight, float parentEnd, float yZeroLine) {
        PointF pointFInterceptInner = ChartComputeUtil
                .getInterceptPointF(pointLeft, pointRight, parentEnd);
        float[] pointsInner = new float[]{pointLeft.x, pointLeft.y, pointFInterceptInner.x,
                pointFInterceptInner.y};
        drawChartLine(canvas, pointsInner, yZeroLine);
        drawFill(parent, mLineChartAttrs, canvas, pointLeft, pointFInterceptInner, yZeroLine);
    }

    private void drawLineLeftBoundary(RecyclerView parent, Canvas canvas, PointF pointLeft,
            PointF pointRight, float crossX, float yZeroLine) {
        PointF pointF = ChartComputeUtil.getInterceptPointF(pointLeft, pointRight, crossX);
        float[] points = new float[]{pointF.x, pointF.y, pointRight.x, pointRight.y};
        drawChartLine(canvas, points, yZeroLine);
        drawFill(parent, mLineChartAttrs, canvas, pointF, pointRight, yZeroLine);
    }

    /**
     * @param leftOrRight left == true, right == false
     */
    private <T extends RecyclerBarEntry, E extends BaseYAxis> PointF createNearPoint(
            RecyclerView parent, List<T> entryList, PointF currentPoint,
            int index, int viewWidth, E yAxis, boolean leftOrRight) {
        float xNear = leftOrRight ? currentPoint.x - viewWidth : currentPoint.x + viewWidth;
        T barEntryNear = entryList.get(index);
        float yNear = ChartComputeUtil
                .getYPosition(barEntryNear.getY(), parent, yAxis, mLineChartAttrs);
        PointF pointNext = new PointF(xNear, yNear);
        return pointNext;
    }

    private <T extends RecyclerBarEntry, E extends BaseYAxis> void drawLineChartWithPointRTL(
            Canvas canvas, RecyclerView parent, E mYAxis) {
        final float paddingLeft = parent.getPaddingLeft();
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) parent.getAdapter();
        List<T> entryList = adapter.getEntries();
        final int childCount = parent.getChildCount();
        int adapterPosition;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            T barEntry = (T) child.getTag();
            if (barEntry.getY() == 0) {
                continue;
            }
            adapterPosition = parent.getChildAdapterPosition(child);
            RectF rectF = ChartComputeUtil
                    .getChartRectF(child, parent, mYAxis, mLineChartAttrs, barEntry);
            float yZeroLine = rectF.bottom;
            int viewWidth = child.getWidth();
            PointF pointF2 = new PointF((rectF.left + rectF.right) / 2, rectF.top);
            //pointF2 即为当前for 循环到的点，从左往右一共涉及到4个点 pointF0, pointF1, pointF2, pointF3; 其中 pointF1、pointF2为显示的。
            if (i >= 1) {
                View pointF1Child = parent.getChildAt(i - 1);
                T barEntryLeft = (T) pointF1Child.getTag();
                if (barEntryLeft.getY() == 0) {
                    //前一个点为0，绘制当前点。
                    drawCircle(canvas, pointF2, barEntry, parent);
                    continue;
                }
                RectF rectFLeft = ChartComputeUtil
                        .getChartRectF(pointF1Child, parent, mYAxis, mLineChartAttrs, barEntryLeft);
                PointF pointF1 = new PointF((rectFLeft.left + rectFLeft.right) / 2, rectFLeft.top);
                if (pointF1.x >= paddingLeft && pointF2.x <= parentRight) {
                    drawChartLine(canvas, pointF1, pointF2, barEntryLeft, barEntry, yZeroLine);
                    drawFill(parent, mLineChartAttrs, canvas, pointF1, pointF2, rectF.bottom);
                    if (pointF1Child.getLeft() < paddingLeft) {
                        if (adapterPosition - 2 >= 0) {
                            PointF pointF0 = createNearPoint(parent, entryList, pointF1,
                                    adapterPosition - 2, viewWidth, mYAxis, true);
                            drawLineLeftBoundary(parent, canvas, pointF0, pointF1, paddingLeft,
                                    yZeroLine);
                            if (pointF0.x > paddingLeft && adapterPosition - 3 >= 0) {//处理左边界第2个点。
                                PointF pointFEx = createNearPoint(parent, entryList, pointF0,
                                        adapterPosition - 3, viewWidth, mYAxis, true);
                                drawLineCross(parent, canvas, pointFEx, pointF0, barEntryLeft,
                                        paddingLeft, yZeroLine, false);
                            }
                        }
                    } else if (child.getRight() < parentRight
                            && parentRight - child.getRight() <= child.getWidth()) {
                        //右边界处理情况，pointF3显示出来跟没有显示出来。
                        if (adapterPosition + 1 < entryList.size()) {
                            T barEntryRight = entryList.get(adapterPosition + 1);
                            if (barEntryRight.getY() == 0) {
                                drawCircle(canvas, pointF1, barEntryLeft, parent);
                                drawCircle(canvas, pointF2, barEntry, parent);
                                continue;
                            }
                            PointF pointF3 = createNearPoint(parent, entryList, pointF2,
                                    adapterPosition + 1, viewWidth, mYAxis, false);
                            if (pointF3.x >= parentRight) {
                                drawLineCross(parent, canvas, pointF2, pointF3, barEntryRight,
                                        parentRight, yZeroLine, true);
                            } else if (pointF3.x < parentRight) {
                                if (adapterPosition + 2 < entryList.size()) {
                                    T barEntry4 = entryList.get(adapterPosition + 2);
                                    if (barEntry4.getY() == 0) {
                                        drawCircle(canvas, pointF1, barEntryLeft, parent);
                                        drawCircle(canvas, pointF2, barEntry, parent);
                                        continue;
                                    }
                                    PointF pointF4 = createNearPoint(parent, entryList, pointF3,
                                            adapterPosition + 2, viewWidth, mYAxis, false);
                                    drawLineCross(parent, canvas, pointF3, pointF4, barEntryRight,
                                            parentRight, yZeroLine, true);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentRight
                        && pointF1Child.getRight() >= parentRight) {//右边界，处理pointF1值没有显示出来
                    drawLineCross(parent, canvas, pointF1, pointF2, barEntry, parentRight,
                            yZeroLine, false);
                }
                drawCircle(canvas, pointF1, barEntryLeft, parent);
                drawCircle(canvas, pointF2, barEntry, parent);
            } else {
                drawCircle(canvas, pointF2, barEntry, parent);
            }
        }
    }

    private <T extends RecyclerBarEntry, E extends BaseYAxis> void drawLineChartWithPoint(
            Canvas canvas, RecyclerView parent, E mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) parent.getAdapter();
        List<T> entryList = adapter.getEntries();
        final int childCount = parent.getChildCount();
        int adapterPosition;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            T barEntry = (T) child.getTag();
            if (barEntry.getY() == 0) {
                continue;
            }
            adapterPosition = parent.getChildAdapterPosition(child);
            RectF rectF = ChartComputeUtil
                    .getChartRectF(child, parent, mYAxis, mLineChartAttrs, barEntry);
            float yZero = rectF.bottom;
            PointF pointF2 = new PointF((rectF.left + rectF.right) / 2, rectF.top);
            //pointF2 即为当前for 循环到的点，从左往右一共涉及到4个点 pointF0, pointF1, pointF2, pointF3; 其中 pointF1、pointF2为显示的。
            if (i < childCount - 1) {
                View pointF1Child = parent.getChildAt(i + 1);
                T barEntryLeft = (T) pointF1Child.getTag();
                if (barEntryLeft.getY() == 0) {
                    //前一个点为0，绘制当前点。
                    drawCircle(canvas, pointF2, barEntry, parent);
                    continue;
                }
                RectF rectFLeft = ChartComputeUtil
                        .getChartRectF(pointF1Child, parent, mYAxis, mLineChartAttrs, barEntryLeft);
                PointF pointF1 = new PointF((rectFLeft.left + rectFLeft.right) / 2, rectFLeft.top);
                if (pointF1.x >= parentLeft && pointF2.x <= parentRight) {
                    drawChartLine(canvas, pointF1, pointF2, barEntryLeft, barEntry, yZero);
                    drawFill(parent, mLineChartAttrs, canvas, pointF1, pointF2, rectF.bottom);
                    if (pointF1Child.getLeft() < parentLeft) {//左边界，处理pointF1值显示出来了的情况。
                        if (adapterPosition + 2 < entryList.size()) {
                            float x = pointF1.x - pointF1Child.getWidth();
                            T barEntry0 = entryList.get(adapterPosition + 2);
                            float y = ChartComputeUtil
                                    .getYPosition(barEntry0.getY(), parent, mYAxis, mLineChartAttrs);
                            PointF pointF0 = new PointF(x, y);
                            drawLineCross(parent, canvas, pointF0, pointF1, barEntry, parentLeft,
                                    yZero, false);
                        }
                    } else if (child.getRight() < parentRight
                            && parentRight - child.getRight() <= child.getWidth()) {
                        //右边界处理情况，pointF3显示出来跟没有显示出来。
                        Log.d("test", "LineChartRender 111");
                        if (adapterPosition - 1 > 0) {
                            Log.d("test", "LineChartRender 222");
                            T barEntryRight = entryList.get(adapterPosition - 1);
                            if (barEntryRight.getY() == 0) {
                                Log.d("test", "LineChartRender 333");
                                drawCircle(canvas, pointF1, barEntryLeft, parent);
                                drawCircle(canvas, pointF2, barEntry, parent);
                                continue;
                            }
                            float x = pointF2.x + child.getWidth();
                            float y = ChartComputeUtil
                                    .getYPosition(barEntryRight.getY(), parent, mYAxis,
                                            mLineChartAttrs);
                            PointF pointF3 = new PointF(x, y);
                            if (pointF3.x >= parentRight) {
                                PointF dividerRightPoint = new PointF(parentRight, pointF3.y);
                                float[] points = new float[]{pointF2.x, pointF2.y,
                                        dividerRightPoint.x, dividerRightPoint.y};
                                drawChartLine(canvas, points, yZero);
                                drawFill(parent, mLineChartAttrs, canvas, pointF2, dividerRightPoint,
                                        rectF.bottom);
                                Log.d("test", "LineChartRender 444");
//                                drawLineCross(parent, canvas, pointF2, pointF3, barEntry, parentRight, yZero, true);
                            } else if (pointF3.x < parentRight) {
                                Log.d("test", "LineChartRender 555");
                                if (adapterPosition - 2 > 0) {
                                    Log.d("test", "LineChartRender 666");
                                    T barEntry4 = entryList.get(adapterPosition - 2);
                                    if (barEntry4.getY() == 0) {
                                        Log.d("test", "LineChartRender 777");
                                        drawCircle(canvas, pointF1, barEntryLeft, parent);
                                        drawCircle(canvas, pointF2, barEntry, parent);
                                        continue;
                                    }
                                    Log.d("test", "LineChartRender 888");
                                    float xInner = Math
                                            .min(pointF3.x + child.getWidth(), parentRight);
                                    float yInner = ChartComputeUtil
                                            .getYPosition(barEntry4.getY(), parent, mYAxis,
                                                    mLineChartAttrs);
                                    PointF pointF4 = new PointF(xInner, yInner);
                                    drawLineCross(parent, canvas, pointF3, pointF4, barEntryRight, parentRight, yZero, true);
//                                    float[] points = new float[]{pointF3.x, pointF3.y, pointF4.x,
//                                            pointF4.y};
//                                    drawChartLine(canvas, points, yZero);
//                                    drawFill(parent, mBarChartAttrs, canvas, pointF2, pointF4,
//                                            rectF.bottom);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentLeft
                        && pointF1Child.getRight() >= parentLeft) {//左边界，处理pointF1值没有显示出来
                    drawLineCross(parent, canvas, pointF1, pointF2, barEntry, parentLeft, yZero,
                            false);
                }
                drawCircle(canvas, pointF1, barEntryLeft, parent);
                drawCircle(canvas, pointF2, barEntry, parent);
            }else {
                drawCircle(canvas, pointF2, barEntry, parent);
            }
        }
    }

    private <T extends RecyclerBarEntry> void drawLineCross(RecyclerView parent, Canvas canvas,
            PointF pointFLeft, PointF pointFRight,
            T barEntry, float crossX, float yZeroLine, boolean crossLeft) {
        PointF pointF = ChartComputeUtil.getInterceptPointF(pointFLeft, pointFRight, crossX);
        drawChartLineCross(canvas, pointFLeft, pointFRight, pointF, barEntry, crossLeft, yZeroLine);
        if (crossLeft){
            drawFill(parent, mLineChartAttrs, canvas, pointF, pointFLeft, yZeroLine);
        }else {
            drawFill(parent, mLineChartAttrs, canvas, pointF, pointFRight, yZeroLine);
        }
    }

    private boolean checkoutIsInBoundary(PointF pointF, ViewGroup parent) {
        float boundaryLeft = parent.getPaddingLeft();
        float boundaryRight = parent.getWidth() - parent.getPaddingRight();
        if (pointF.x < boundaryRight && pointF.x > boundaryLeft) {
            return true;
        }
        return false;
    }

    private <T extends RecyclerBarEntry> void drawCircle(Canvas canvas, PointF pointF, T barEntry,
            RecyclerView parent) {
        if (checkoutIsInBoundary(pointF, parent)) {
            if (barEntry.isSelected()) {
                drawSelectCircle(canvas, pointF);
                if (mLineChartAttrs.lineSelectCircles == 2) {
                    canvas.drawCircle(pointF.x, pointF.y, mLineChartAttrs.linePointRadius,
                            mLineCirclePaint);
                }
            } else {
                Paint.Style style = mLineCirclePaint.getStyle();
                if (mLineChartAttrs.linePointSelectCircleStroke) {
                    mLineCirclePaint.setStyle(Paint.Style.STROKE);
                    mLineCirclePaint.setStrokeWidth(mLineChartAttrs.linePointSelectStrokeWidth);
                }
                canvas.drawCircle(pointF.x, pointF.y, mLineChartAttrs.linePointRadius,
                        mLineCirclePaint);
                mLineCirclePaint.setStyle(style);//restore
            }
        }
    }

    private <T extends RecyclerBarEntry> void drawChartLineCross(Canvas canvas, PointF pointF0,
            PointF pointF1,
            PointF pointIntercept, T barEntry, boolean crossLeft, float yZero) {
        float[] points;
        PointF pointF1Cross;
        float radius = mLineChartAttrs.linePointRadius;
        if (barEntry.isSelected()) {//选中时的圆半径
            radius = mLineChartAttrs.linePointSelectRadius
                    + mLineChartAttrs.linePointSelectStrokeWidth;
        }
        if (crossLeft) {
            pointF1Cross = getTheCrossPointFLeft(pointF0, pointF1, radius);
        } else {
            pointF1Cross = getTheCrossPointFRight(pointF0, pointF1, radius);
        }
        points = new float[]{pointIntercept.x, pointIntercept.y, pointF1Cross.x, pointF1Cross.y};
        drawChartLine(canvas, points, yZero);
    }


    private <T extends RecyclerBarEntry> void drawChartLine(Canvas canvas, PointF pointF1,
            PointF pointF2, T barEntryLeft, T barEntry, float yZero) {
        float[] points;
        PointF pointF1Cross;
        PointF pointF2Cross;
        if (barEntryLeft.isSelected()) {
            pointF1Cross = getTheCrossPointFLeft(pointF1, pointF2,
                    mLineChartAttrs.linePointSelectRadius);
            pointF2Cross = getTheCrossPointFRight(pointF1, pointF2, mLineChartAttrs.linePointRadius);
        } else if (barEntry.isSelected()) {
            pointF1Cross = getTheCrossPointFLeft(pointF1, pointF2, mLineChartAttrs.linePointRadius);
            pointF2Cross = getTheCrossPointFRight(pointF1, pointF2,
                    mLineChartAttrs.linePointSelectRadius);
        } else {
            pointF1Cross = getTheCrossPointFLeft(pointF1, pointF2, mLineChartAttrs.linePointRadius);
            pointF2Cross = getTheCrossPointFRight(pointF1, pointF2, mLineChartAttrs.linePointRadius);
        }
        points = new float[]{pointF1Cross.x, pointF1Cross.y, pointF2Cross.x, pointF2Cross.y};
        drawChartLine(canvas, points, yZero);
    }

    private PointF getTheCrossPointFLeft(PointF pointF1, PointF pointF2, float radius) {
        float xDistance = Math.abs(pointF1.x - pointF2.x);
        float yDistance = Math.abs(pointF1.y - pointF2.y);
        double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
        double yLength = yDistance * (radius * 1.0f / distance);
        double xLength = xDistance * (radius * 1.0f / distance);
        PointF pointF3 = new PointF();
        if (pointF1.y > pointF2.y) {
            pointF3.y = (float) (pointF1.y - yLength);
        } else {
            pointF3.y = (float) (pointF1.y + yLength);
        }
        if (pointF1.x > pointF2.x) {
            pointF3.x = (float) (pointF1.x - xLength);
        } else {
            pointF3.x = (float) (pointF1.x + xLength);
        }
        return pointF3;
    }

    private PointF getTheCrossPointFRight(PointF pointF1, PointF pointF2, float radius) {
        float xDistance = Math.abs(pointF1.x - pointF2.x);
        float yDistance = Math.abs(pointF1.y - pointF2.y);
        double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
        double yLength = yDistance * (radius * 1.0f / distance);
        double xLength = xDistance * (radius * 1.0f / distance);
        PointF pointF4 = new PointF();
        if (pointF1.y > pointF2.y) {
            pointF4.y = (float) (pointF2.y + yLength);
        } else {
            pointF4.y = (float) (pointF2.y - yLength);
        }
        if (pointF1.x > pointF2.x) {
            pointF4.x = (float) (pointF2.x + xLength);
        } else {
            pointF4.x = (float) (pointF2.x - xLength);
        }
        return pointF4;
    }

    private void drawChartLine(Canvas canvas, float[] points, float yZeroLine) {
        int color = mLinePaint.getColor();
        if (mLineChartAttrs.lineColor == -1) {
            mLinePaint.setColor(mLineChartAttrs.chartColor);
        } else {
            mLinePaint.setColor(mLineChartAttrs.lineColor);
        }
        if (points.length >= 4 && points[1] >= yZeroLine && points[3] >= yZeroLine) {
            mLinePaint.setColor(Color.TRANSPARENT);
        }
        canvas.drawLines(points, mLinePaint);
        mLinePaint.setColor(color);
    }

    private void drawSelectCircle(Canvas canvas, PointF pointF) {
        Paint.Style style = mLineCirclePaint.getStyle();
        float strokeWidth = mLineCirclePaint.getStrokeWidth();
        mLineCirclePaint.setStyle(Paint.Style.STROKE);
        mLineCirclePaint.setStrokeWidth(mLineChartAttrs.linePointSelectStrokeWidth);
        canvas.drawCircle(pointF.x, pointF.y, mLineChartAttrs.linePointSelectRadius,
                mLineCirclePaint);
        mLineCirclePaint.setStyle(style);
        mLineCirclePaint.setStrokeWidth(strokeWidth);
    }

    private void drawFill(RecyclerView parent, BaseChartAttrs chartAttrs, Canvas canvas,
                          PointF pointF, PointF pointF1, float bottom) {
        if (chartAttrs.enableLineFill) {
            float yBottom = parent.getBottom() - parent.getPaddingBottom();
            float yTop = parent.getTop() + parent.getPaddingTop();
            LinearGradient mLinearGradient = new LinearGradient(
                    0,
                    yBottom,
                    0,
                    yTop,
                    new int[]{chartAttrs.lineShaderBeginColor, chartAttrs.lineShaderEndColor},
                    null,
                    Shader.TileMode.CLAMP
            );
            mLineFillPaint.setShader(mLinearGradient);
            Path path = ChartComputeUtil.createColorRectPath(pointF, pointF1, bottom);
            LineChartDrawable drawable = new LineChartDrawable(mLineFillPaint, path);
            drawable.draw(canvas);
        }
    }
}
