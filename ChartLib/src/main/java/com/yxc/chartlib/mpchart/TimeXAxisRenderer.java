package com.yxc.chartlib.mpchart;

/**
 * @author yxc
 * @date 2019-08-30
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.mpchart.linechart.CustomLineChartAttr;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.DisplayUtil;

public class TimeXAxisRenderer extends XAxisRenderer {

    ViewPortHandler mViewPortHandler;
    String unitStr;
    CustomLineChartAttr mLineChartAttr;

    public TimeXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis,
                             Transformer trans, String unitStr, CustomLineChartAttr lineChartAttr) {
        super(viewPortHandler, xAxis, trans);
        this.mViewPortHandler = viewPortHandler;
        this.unitStr = unitStr;
        this.mLineChartAttr = lineChartAttr;
    }

    public void setLineChartAttr(CustomLineChartAttr lineChartAttr){
        this.mLineChartAttr = lineChartAttr;
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        float yOffset = mXAxis.getYOffset();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        MPPointF pointF = MPPointF.getInstance(0, 0);
        if (mXAxis.getPosition() == XAxisPosition.TOP) {
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mViewPortHandler.contentTop() - yOffset, pointF);

        } else if (mXAxis.getPosition() == XAxisPosition.TOP_INSIDE) {
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mViewPortHandler.contentTop() + yOffset + mXAxis.mLabelRotatedHeight, pointF);

        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM) {
            pointF.x = AppUtil.isRTLDirection() ? 0.05f : 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mViewPortHandler.contentBottom() + yOffset * 2, pointF);

        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mViewPortHandler.contentBottom() - yOffset - mXAxis.mLabelRotatedHeight, pointF);

        } else { // BOTH SIDED
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mViewPortHandler.contentTop() - yOffset, pointF);
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mViewPortHandler.contentBottom() + yOffset, pointF);
        }

        int color = mAxisLabelPaint.getColor();
        int alpha = mAxisLabelPaint.getAlpha();
        mAxisLabelPaint.setColor(Color.WHITE);
        mAxisLabelPaint.setAlpha(0x03);

        RectF rectF = new RectF();
        rectF.set(0, mViewPortHandler.contentBottom() + DisplayUtil.dip2px(0.5f),
                DisplayUtil.getScreenWidth(), mViewPortHandler.contentBottom() + mViewPortHandler.offsetBottom() + DisplayUtil.dip2px(1));
        c.drawRect(rectF, mAxisLabelPaint);
        mAxisLabelPaint.setColor(color);
        mAxisLabelPaint.setAlpha(alpha);

//        float textSize = mAxisLabelPaint.getTextSize();
//        mAxisLabelPaint.setTextSize(DisplayUtil.sp2px(8));
//        float width = mAxisLabelPaint.measureText(unitStr.trim());
//        float txtLeft = mViewPortHandler.contentRight() + DisplayUtil.dip2px(10);
//        float txtRight = txtLeft + width;

//        RectF rectFText = new RectF();
//        rectFText.set(txtLeft, rectF.top, txtRight, rectF.bottom);
//        float baseY = TextUtil.getTextBaseY(rectF, mAxisLabelPaint);
//        c.drawText(unitStr, txtLeft, baseY, mAxisLabelPaint);
//        mAxisLabelPaint.setTextSize(textSize);
        MPPointF.recycleInstance(pointF);
    }

    @Override
    public void renderAxisLine(Canvas c) {
        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled())
            return;

        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());
        mAxisLinePaint.setPathEffect(mXAxis.getAxisLineDashPathEffect());

        if (mXAxis.getPosition() == XAxisPosition.TOP
                || mXAxis.getPosition() == XAxisPosition.TOP_INSIDE
                || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentTop(), mAxisLinePaint);
        }

        if (mXAxis.getPosition() == XAxisPosition.BOTTOM
                || mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE
                || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    @Override
    public void renderGridLines(Canvas c) {
        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;

        int clipRestoreCount = c.save();
        c.clipRect(getGridClippingRect());

        if (mRenderGridLinesBuffer.length != mAxis.mEntryCount * 2) {
            mRenderGridLinesBuffer = new float[mXAxis.mEntryCount * 2];
        }
        float[] positions = mRenderGridLinesBuffer;

        for (int i = 0; i < positions.length; i += 2) {
            positions[i] = mXAxis.mEntries[i / 2];
            positions[i + 1] = mXAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);
        setupGridPaint();
        Path gridLinePath = mRenderGridLinesPath;
        gridLinePath.reset();

        for (int i = 0; i < positions.length; i += 2) {

            drawGridLine(c, positions[i], positions[i + 1], gridLinePath);
        }
        c.restoreToCount(clipRestoreCount);
    }


    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

        float[] positions = new float[mXAxis.mEntryCount * 2];

        for (int i = 0; i < positions.length; i += 2) {
            // only fill x values
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = mXAxis.mEntries[i / 2];
            }
        }
        mTrans.pointValuesToPixel(positions);
        for (int i = 0; i < positions.length; i += 2) {
            float x = positions[i];
            float bound = x;

            if (mViewPortHandler.isInBoundsX(bound)) {
                String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);
                if (i == 0) {
                    Context context = AppUtil.getApp();
                    label = context.getString(R.string.common_join_str, label, unitStr);
                }
                if (mXAxis.isAvoidFirstLastClippingEnabled()) {
                    // avoid clipping of the last
                    boolean isEndIndex = AppUtil.isRTLDirection() ? i == 0 : i == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1;
                    boolean isStartIndex = AppUtil.isRTLDirection() ? i == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1 : i == 0;
                    if (isEndIndex) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                        if (width > mViewPortHandler.offsetRight() * 2 && x + width > mViewPortHandler.getChartWidth()) {
                            x -= width / 2;
                        }
                        // avoid clipping of the first
                    } else if (isStartIndex) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                        x += width / 2;
                    }
                }
                float startLabel = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() - x : x;
                drawLabel(c, label, startLabel, pos, anchor, labelRotationAngleDegrees);
            }
        }
    }

    /**
     * Sets up the axis values. Computes the desired number of labels between the two given extremes.
     * @return
     */
    protected void computeAxisValues(float min, float max) {
        if (mAxis instanceof TimeXAxis && mLineChartAttr.enableTimeXAxisLabel) {
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
        computeSize();
    }
}
