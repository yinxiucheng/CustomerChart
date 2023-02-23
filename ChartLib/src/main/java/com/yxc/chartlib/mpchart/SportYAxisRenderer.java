package com.yxc.chartlib.mpchart;

/**
 * @author yxc
 * @date 2019-08-30
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.core.graphics.ColorUtils;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.mpchart.linechart.CustomLineChartAttr;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.ColorUtil;
import com.xiaomi.fitness.common.utils.DisplayUtil;
import com.xiaomi.fitness.common.utils.TextUtil;

import java.util.List;

public class SportYAxisRenderer extends YAxisRenderer {

    ViewPortHandler mViewPortHandler;
    CustomLineChartAttr mLineChartAttr;

    public SportYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis,
                              Transformer trans,
                              CustomLineChartAttr lineChartAttr) {
        super(viewPortHandler, yAxis, trans);
        this.mViewPortHandler = viewPortHandler;
        this.mLineChartAttr = lineChartAttr;
    }

    public void setLineChartAttr(CustomLineChartAttr lineChartAttr) {
        this.mLineChartAttr = lineChartAttr;
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled() ? mYAxis.mEntryCount : (mYAxis.mEntryCount - 1);

        float itemDistance = 0;
        if (to > from) {
            itemDistance = Math.abs(Math.abs(mYAxis.mEntries[from + 1]) - Math.abs(mYAxis.mEntries[from]));
        }

        for (int i = from; i < to; i++) {
            String text = mYAxis.getFormattedLabel(i);
            boolean drawLabel = true;
            float times = 0;
            float distance = 0;
            if (i == to - 1) {
                float value = mYAxis.mEntries[i];
                distance = Math.abs(Math.abs(mYAxis.getAxisMaximum()) - Math.abs(value));
                if (distance > 0) {
                    times = itemDistance / distance;
                } else if (distance == 0) {
                    drawLabel = false;
                }
            }
            if (i == from) {
                float value = mYAxis.mEntries[i];
                distance = Math.abs(Math.abs(mYAxis.getAxisMinimum()) - Math.abs(value));
                if (distance > 0) {
                    times = itemDistance / distance;
                } else if (distance == 0) {
                    drawLabel = true;
                }
            }
            if (times > 5) {//最大刻度距离边界太近，不画。
                drawLabel = false;
            }
            if (drawLabel) {
                c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
            }
        }
    }

    @Override
    public void renderAxisLine(Canvas c) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled())
            return;

        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());

        if (mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
            c.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        } else {
            c.drawLine(mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    @Override
    public void renderGridLines(Canvas c) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled())
            return;

        if (mYAxis.isDrawGridLinesEnabled()) {

            int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());

            float[] positions = getTransformedPositions();

            mGridPaint.setColor(mYAxis.getGridColor());
            mGridPaint.setStrokeWidth(mYAxis.getGridLineWidth());
            mGridPaint.setPathEffect(mYAxis.getGridDashPathEffect());

            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();

            // draw the grid
            final int from = 0;
            final int to = mYAxis.mEntryCount;
            float itemDistance = 0;

            if (to > from) {
                itemDistance = Math.abs(Math.abs(mYAxis.mEntries[from + 1]) - Math.abs(mYAxis.mEntries[from]));
            }

            for (int i = 0, j = from; i < positions.length && j < to; i += 2, j++) {
                float distance = 0;
                float times = 0;
                boolean drawLine = true;

                if (j == from) {
                    float value = mYAxis.mEntries[j];
                    distance = Math.abs(Math.abs(mYAxis.getAxisMinimum()) - Math.abs(value));
                    if (distance > 0) {
                        times = itemDistance / distance;
                    } else if (distance == 0) {
                        drawLine = false;
                    }
                }

                if (j == to - 1) {
                    float value = mYAxis.mEntries[j];
                    distance = Math.abs(Math.abs(mYAxis.getAxisMaximum()) - Math.abs(value));
                    if (distance > 0) {
                        times = itemDistance / distance;
                    }
                }
                if (times > 5) {//最大刻度距离边界太近，不画。
                    drawLine = false;
                }
                // draw a path because lines don't support dashing on lower android versions
                if (drawLine) {
                    c.drawPath(linePath(gridLinePath, i, positions), mGridPaint);
                }
                gridLinePath.reset();
            }
            c.restoreToCount(clipRestoreCount);
        }

        if (mYAxis.isDrawZeroLineEnabled()) {
            drawZeroLine(c);
        }
    }

    /**
     * Sets up the axis values. Computes the desired number of labels between the two given extremes.
     *
     * @return
     */
    protected void computeAxisValues(float min, float max) {
        if (mAxis instanceof SportYAxis && mLineChartAttr.enableSportYAxisLabel) {
            float yMin = min;
            float yMax = max;
            int labelCount = mAxis.getLabelCount();
            double range = Math.abs(yMax - yMin);
            if (labelCount == 0 || range <= 0 || Double.isInfinite(range)) {
                mAxis.mEntries = new float[]{};
                mAxis.mCenteredEntries = new float[]{};
                mAxis.mEntryCount = 0;
                return;
            }
        } else {
            super.computeAxisValues(min, max);
        }
    }

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {

        List<LimitLine> limitLines = mYAxis.getLimitLines();

        if (limitLines == null || limitLines.size() <= 0)
            return;

        float[] pts = mRenderLimitLinesBuffer;
        pts[0] = 0;
        pts[1] = 0;
        Path limitLinePath = mRenderLimitLines;
        limitLinePath.reset();

        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);

            if (!l.isEnabled())
                continue;

            int clipRestoreCount = c.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(0.f, -l.getLineWidth());
            c.clipRect(mLimitLineClippingRect);

            mLimitLinePaint.setStyle(Paint.Style.STROKE);
            mLimitLinePaint.setColor(l.getLineColor());
            mLimitLinePaint.setStrokeWidth(l.getLineWidth());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());

            pts[1] = l.getLimit();

            mTrans.pointValuesToPixel(pts);

            limitLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1]);
            limitLinePath.lineTo(mViewPortHandler.contentRight(), pts[1]);

            c.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();
            // c.drawLines(pts, mLimitLinePaint);
            String label = l.getLabel();
            // if drawing the limit-value label is enabled
            if (label != null && !label.equals("")) {
                float textSize = DisplayUtil.sp2px(9);
                mLimitLinePaint.setStyle(l.getTextStyle());
                mLimitLinePaint.setPathEffect(null);
                mLimitLinePaint.setColor(l.getTextColor());
                mLimitLinePaint.setTypeface(l.getTypeface());
                mLimitLinePaint.setStrokeWidth(1.6f);
                mLimitLinePaint.setTextSize(textSize);

                final float labelLineHeight = TextUtil.getTxtHeight1(mLimitLinePaint);
                float xOffset = Utils.convertDpToPixel(4f) + l.getXOffset();
                float yOffset = l.getLineWidth() + labelLineHeight + l.getYOffset();

                final LimitLine.LimitLabelPosition position = l.getLabelPosition();
                if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    float bottom = pts[1] - DisplayUtil.dip2px(2);
                    float top = bottom - labelLineHeight - DisplayUtil.dip2px(2);
                    float[] copyPts = new float[]{top, bottom};
                    RectF rectF = drawBgRectF(copyPts, c, label);
                    float textBaseLine = TextUtil.getTextBaseY(rectF, mLimitLinePaint);
                    c.drawText(label, mViewPortHandler.contentRight() - xOffset, textBaseLine, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    float top = pts[1] + DisplayUtil.dip2px(2);
                    float bottom = top + labelLineHeight + DisplayUtil.dip2px(2);
                    float[] copyPts = new float[]{top, bottom};
                    RectF rectF = drawBgRectF(copyPts, c, label);
                    float textBaseLine = TextUtil.getTextBaseY(rectF, mLimitLinePaint);
                    c.drawText(label, mViewPortHandler.contentRight() - xOffset, textBaseLine, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.LEFT_BOTTOM) {
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    float top = pts[1] + DisplayUtil.dip2px(2);
                    float bottom = top + labelLineHeight + DisplayUtil.dip2px(2);
                    float[] copyPts = new float[]{top, bottom};
                    RectF rectF = drawBgRectF(copyPts, c, label);
                    float textBaseLine = TextUtil.getTextBaseY(rectF, mLimitLinePaint);
                    c.drawText(label, mViewPortHandler.offsetLeft() + xOffset, textBaseLine, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    float bottom = pts[1] - DisplayUtil.dip2px(2);
                    float top = bottom - labelLineHeight - DisplayUtil.dip2px(2);
                    float[] copyPts = new float[]{top, bottom};
                    RectF rectF = drawBgRectF(copyPts, c, label);
                    float textBaseLine = TextUtil.getTextBaseY(rectF, mLimitLinePaint);
                    c.drawText(label, mViewPortHandler.contentLeft() + xOffset, textBaseLine, mLimitLinePaint);
                }
            }
            c.restoreToCount(clipRestoreCount);
        }
    }

    private RectF drawBgRectF(float[] pts, Canvas canvas, String label) {
        Paint boardPaint = new Paint();
        //背景描边并填充全部
        boardPaint.setStyle(Paint.Style.FILL);
        boardPaint.setAntiAlias(true);
        //设置描边颜色
        boardPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_max_point_boarder));
        RectF rectF = new RectF();
        float top = pts[0];
        float bottom = pts[1];
        float startRectF = getStartBgRectF(label);
        float endRectF = getEndBgRectF(label);
        rectF.set(startRectF, top, endRectF, bottom);
        float radius = DisplayUtil.dip2px(8);
        canvas.drawRoundRect(rectF, radius, radius, boardPaint);
        return rectF;
    }

    private float getStartBgRectF(String label) {
        if (AppUtil.isRTLDirection()) {
            return mViewPortHandler.contentLeft() + DisplayUtil.dip2px(4);
        }
        return mViewPortHandler.contentRight() - mLimitLinePaint.measureText(label) - DisplayUtil.dip2px(6);
    }

    private float getEndBgRectF(String label) {
        if (AppUtil.isRTLDirection()) {
            return mViewPortHandler.contentLeft() + DisplayUtil.dip2px(4) + mLimitLinePaint.measureText(label) + DisplayUtil.dip2px(2);
        }
        return mViewPortHandler.contentRight() - DisplayUtil.dip2px(4);
    }
}
