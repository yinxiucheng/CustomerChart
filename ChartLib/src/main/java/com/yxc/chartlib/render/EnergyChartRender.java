package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.attrs.LineChartAttrs;
import com.yxc.chartlib.barchart.BaseBarChartAdapter;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.entrys.MaxMinEntry;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.utils.DisplayUtil;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/14
 */
final public class EnergyChartRender<E extends BaseYAxis> {

    private LineChartAttrs mLineAttrs;
    private Paint mBarChartPaint;
    private Paint mTextPaint;

    public EnergyChartRender(LineChartAttrs barChartAttrs) {
        this.mLineAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mLineAttrs.txtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mBarChartPaint.setColor(mLineAttrs.chartColor);
    }

    public void drawLineChart(Canvas canvas, RecyclerView parent, E mYAxis) {
        drawLineChartMax(canvas, parent, mYAxis);
        drawLineChartMin(canvas, parent, mYAxis);
    }

    //获取柱状体 RectF
    public static <T extends MaxMinEntry, V extends BaseYAxis> RectF getBarChartMaxRectF(View child, final RecyclerView parent, V mYAxis,
                                                                                         BaseChartAttrs barChartAttrs, float yValue) {
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - barChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - barChartAttrs.contentPaddingTop;
        float width = child.getWidth();
        float barSpaceWidth = width * barChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        float height = yValue / mYAxis.getAxisMaximum() * realYAxisLabelHeight;

        if (barChartAttrs.yAxisReverse && yValue > 0) {
            float valueTemp = mYAxis.getAxisMaximum() - yValue;
            height = valueTemp / mYAxis.getAxisMaximum() * realYAxisLabelHeight;
        }

        final float top = Math.max(contentBottom - height, parent.getPaddingTop());
        rectF.set(left, top, right, contentBottom);
        return rectF;
    }

    private <T extends MaxMinEntry> void drawLineChartMax(Canvas canvas, RecyclerView parent, E mYAxis) {
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
            RectF rectF = getBarChartMaxRectF(child, parent, mYAxis, mLineAttrs, barEntry.getMaxY());
            PointF pointF2 = new PointF((rectF.left + rectF.right) / 2, rectF.top);
            //pointF2 即为当前for 循环到的点，从左往右一共涉及到4个点 pointF0, pointF1, pointF2, pointF3; 其中 pointF1、pointF2为显示的。
            if (i < childCount - 1) {
                View pointF1Child = parent.getChildAt(i + 1);
                T barEntryLeft = (T) pointF1Child.getTag();
                RectF rectFLeft = getBarChartMaxRectF(pointF1Child, parent, mYAxis, mLineAttrs, barEntryLeft.getMaxY());
                PointF pointF1 = new PointF((rectFLeft.left + rectFLeft.right) / 2, rectFLeft.top);

                if (pointF1.x >= parentLeft && pointF2.x <= parentRight) {
                    drawChartLine(canvas, pointF1, pointF2, mLineAttrs.lineMaxColor);

                    if (pointF1Child.getLeft() < parentLeft) {//左边界，处理pointF1值显示出来了的情况。
                        if (adapterPosition + 2 < entryList.size()) {
                            float x = pointF1.x - pointF1Child.getWidth();
                            T barEntry0 = entryList.get(adapterPosition + 2);
                            float y = ChartComputeUtil.getYPosition(barEntry0.getMaxY(), parent, mYAxis, mLineAttrs);
                            PointF pointF0 = new PointF(x, y);
                            PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF0, pointF1, parentLeft);
                            drawChartLineCross(canvas, pointF0, pointF1, pointFIntercept, false, mLineAttrs.lineMaxColor);
                        }
                    } else if (child.getRight() < parentRight && parentRight - child.getRight() <= child.getWidth()) {
                        //右边界处理情况，pointF3显示出来跟没有显示出来。
                        if (adapterPosition - 1 > 0) {
                            T barEntryRight = entryList.get(adapterPosition - 1);
                            if (barEntryRight.getMaxY() == 0) {
                                drawCircle(canvas, pointF1, barEntryLeft, parentRight, parentLeft, mLineAttrs.lineMaxColor);
                                drawCircle(canvas, pointF2, barEntry, parentRight, parentLeft, mLineAttrs.lineMaxColor);
                                continue;
                            }
                            float x = pointF2.x + child.getWidth();
                            float y = ChartComputeUtil.getYPosition(barEntryRight.getMaxY(), parent, mYAxis, mLineAttrs);
                            PointF pointF3 = new PointF(x, y);

                            if (pointF3.x >= parentRight) {
                                PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF2, pointF3, parentRight);
                                drawChartLineCross(canvas, pointF2, pointF3, pointFIntercept, true, mLineAttrs.lineMaxColor);
                            } else if (pointF3.x < parentRight) {
                                if (adapterPosition - 2 > 0) {
                                    float xInner = pointF3.x + child.getWidth();
                                    T barEntry4 = entryList.get(adapterPosition - 2);
                                    if (barEntry4.getMaxY() == 0) {
                                        drawCircle(canvas, pointF1, barEntryLeft, parentRight, parentLeft, mLineAttrs.lineMaxColor);
                                        drawCircle(canvas, pointF2, barEntry, parentRight, parentLeft, mLineAttrs.lineMaxColor);
                                        continue;
                                    }
                                    float yInner = ChartComputeUtil.getYPosition(barEntry4.getMaxY(), parent, mYAxis, mLineAttrs);
                                    PointF pointF4 = new PointF(xInner, yInner);

                                    PointF pointFInterceptInner = ChartComputeUtil.getInterceptPointF(pointF3, pointF4, parentRight);
                                    drawChartLineCross(canvas, pointF3, pointF4, pointFInterceptInner, true, mLineAttrs.lineMaxColor);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentLeft && pointF1Child.getRight() >= parentLeft) {//左边界，处理pointF1值没有显示出来
                    PointF pointF = ChartComputeUtil.getInterceptPointF(pointF1, pointF2, parentLeft);
                    drawChartLineCross(canvas, pointF1, pointF2, pointF, false, mLineAttrs.lineMaxColor);
                }
                drawCircle(canvas, pointF1, barEntryLeft, parentRight, parentLeft, mLineAttrs.lineMaxColor);
                drawCircle(canvas, pointF2, barEntry, parentRight, parentLeft, mLineAttrs.lineMaxColor);
            }
        }
    }

    private <T extends MaxMinEntry> void drawLineChartMin(Canvas canvas, RecyclerView parent, E mYAxis) {
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
            RectF rectF = getBarChartMaxRectF(child, parent, mYAxis, mLineAttrs, barEntry.getMinY());
            PointF pointF2 = new PointF((rectF.left + rectF.right) / 2, rectF.top);
            //pointF2 即为当前for 循环到的点，从左往右一共涉及到4个点 pointF0, pointF1, pointF2, pointF3; 其中 pointF1、pointF2为显示的。
            if (i < childCount - 1) {
                View pointF1Child = parent.getChildAt(i + 1);
                T barEntryLeft = (T) pointF1Child.getTag();
                RectF rectFLeft = getBarChartMaxRectF(pointF1Child, parent, mYAxis, mLineAttrs, barEntryLeft.getMinY());
                PointF pointF1 = new PointF((rectFLeft.left + rectFLeft.right) / 2, rectFLeft.top);

                if (pointF1.x >= parentLeft && pointF2.x <= parentRight) {
                    drawChartLine(canvas, pointF1, pointF2, mLineAttrs.lineMinColor);

                    if (pointF1Child.getLeft() < parentLeft) {//左边界，处理pointF1值显示出来了的情况。
                        if (adapterPosition + 2 < entryList.size()) {
                            float x = pointF1.x - pointF1Child.getWidth();
                            T barEntry0 = entryList.get(adapterPosition + 2);
                            float y = ChartComputeUtil.getYPosition(barEntry0.getMinY(), parent, mYAxis, mLineAttrs);
                            PointF pointF0 = new PointF(x, y);
                            PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF0, pointF1, parentLeft);
                            drawChartLineCross(canvas, pointF0, pointF1, pointFIntercept, false, mLineAttrs.lineMinColor);
                        }
                    } else if (child.getRight() < parentRight && parentRight - child.getRight() <= child.getWidth()) {
                        //右边界处理情况，pointF3显示出来跟没有显示出来。
                        if (adapterPosition - 1 > 0) {
                            T barEntryRight = entryList.get(adapterPosition - 1);
                            if (barEntryRight.getMinY() == 0) {
                                drawCircle(canvas, pointF1, barEntryLeft, parentRight, parentLeft, mLineAttrs.lineMinColor);
                                drawCircle(canvas, pointF2, barEntry, parentRight, parentLeft, mLineAttrs.lineMinColor);
                                continue;
                            }
                            float x = pointF2.x + child.getWidth();
                            float y = ChartComputeUtil.getYPosition(barEntryRight.getMinY(), parent, mYAxis, mLineAttrs);

                            PointF pointF3 = new PointF(x, y);

                            if (pointF3.x >= parentRight) {
                                PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF2, pointF3, parentRight);
                                drawChartLineCross(canvas, pointF2, pointF3, pointFIntercept, true, mLineAttrs.lineMinColor);
                            } else if (pointF3.x < parentRight) {
                                if (adapterPosition - 2 > 0) {
                                    float xInner = pointF3.x + child.getWidth();
                                    T barEntry4 = entryList.get(adapterPosition - 2);
                                    if (barEntry4.getMinY() == 0) {
                                        drawCircle(canvas, pointF1, barEntryLeft, parentRight, parentLeft, mLineAttrs.lineMinColor);
                                        drawCircle(canvas, pointF2, barEntry, parentRight, parentLeft, mLineAttrs.lineMinColor);
                                        continue;
                                    }
                                    float yInner = ChartComputeUtil.getYPosition(barEntry4.getMinY(), parent, mYAxis, mLineAttrs);
                                    PointF pointF4 = new PointF(xInner, yInner);

                                    PointF pointFInterceptInner = ChartComputeUtil.getInterceptPointF(pointF3, pointF4, parentRight);
                                    drawChartLineCross(canvas, pointF3, pointF4, pointFInterceptInner, true, mLineAttrs.lineMinColor);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentLeft && pointF1Child.getRight() >= parentLeft) {//左边界，处理pointF1值没有显示出来
                    PointF pointF = ChartComputeUtil.getInterceptPointF(pointF1, pointF2, parentLeft);
                    drawChartLineCross(canvas, pointF1, pointF2, pointF, false, mLineAttrs.lineMinColor);
                }
                drawCircle(canvas, pointF1, barEntryLeft, parentRight, parentLeft, mLineAttrs.lineMinColor);
                drawCircle(canvas, pointF2, barEntry, parentRight, parentLeft, mLineAttrs.lineMinColor);
            }
        }
    }

    private <T extends RecyclerBarEntry> void drawCircle(Canvas canvas, PointF pointF, T barEntry, float parentRight, float parentLeft, int color) {
        if (pointF.x < parentRight && pointF.x > parentLeft) {
            int colorOriginal = mBarChartPaint.getColor();
            mBarChartPaint.setColor(color);
            if (barEntry.isSelected()) {
                if (mLineAttrs.lineSelectCircles == 2) {
                    canvas.drawCircle(pointF.x, pointF.y, mLineAttrs.linePointRadius, mBarChartPaint);
                }
            } else {
                canvas.drawCircle(pointF.x, pointF.y, mLineAttrs.linePointRadius, mBarChartPaint);
            }
            mBarChartPaint.setColor(colorOriginal);
        }
    }

    private void drawChartLineCross(Canvas canvas, PointF pointF0, PointF pointF1,
                                    PointF pointIntercept, boolean crossLeft, int color) {
        float[] points;
        PointF pointF1Cross;
        float radius = mLineAttrs.linePointRadius;
        if (crossLeft) {
            pointF1Cross = getTheCrossPointFLeft(pointF0, pointF1, radius);
        } else {
            pointF1Cross = getTheCrossPointFRight(pointF0, pointF1, radius);
        }
        points = new float[]{pointIntercept.x, pointIntercept.y, pointF1Cross.x, pointF1Cross.y};
        drawChartLine(canvas, points, color);
    }

    private void drawChartLine(Canvas canvas, PointF pointF1, PointF pointF2, int color) {
        float[] points;
        PointF pointF1Cross = getTheCrossPointFLeft(pointF1, pointF2, mLineAttrs.linePointRadius);
        PointF pointF2Cross = getTheCrossPointFRight(pointF1, pointF2, mLineAttrs.linePointRadius);
        points = new float[]{pointF1Cross.x, pointF1Cross.y, pointF2Cross.x, pointF2Cross.y};
        drawChartLine(canvas, points, color);
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

    private void drawChartLine(Canvas canvas, float[] points, int color) {
        int colorOriginal = mBarChartPaint.getColor();
        mBarChartPaint.setColor(color);
        canvas.drawLines(points, mBarChartPaint);
        mBarChartPaint.setColor(colorOriginal);
    }

}
