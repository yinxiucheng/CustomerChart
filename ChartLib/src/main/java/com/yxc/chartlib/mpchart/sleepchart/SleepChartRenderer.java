package com.yxc.chartlib.mpchart.sleepchart;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.entrys.SleepItemEntry;
import com.xiaomi.fitness.chart.entrys.model.SleepItemTime;
import com.xiaomi.fitness.chart.mpchart.MPChartAttr;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.ColorUtil;

/**
 * @author yxc
 * @since 2019-09-23
 */
public class SleepChartRenderer extends BarChartRenderer {

    public SleepChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    public void initBuffers() {
        BarData barData = mChart.getBarData();
        float yChartMax = mChart.getYChartMax();
        mBarBuffers = new SleepBuffer[barData.getDataSetCount()];
        int sleepChartType = SleepChartAttr.SLEEP_CHART_TYPE_DEF;
        if (mChart instanceof SleepChart) {
            sleepChartType = ((SleepChart) mChart).mAttribute.sleepChartType;
        }
        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            int size = set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1);
            mBarBuffers[i] = new SleepBuffer(size, barData.getDataSetCount(), yChartMax, sleepChartType);
        }
    }

    @Override
    public void drawData(Canvas c) {

        BarData barData = mChart.getBarData();

        if (mChart instanceof SleepChart) {
            int empty = ((SleepChart) mChart).mAttribute.empty;
            if (empty != MPChartAttr.EMPTY_N) {
                drawEmptyView(c);
            } else {
                for (int i = 0; i < barData.getDataSetCount(); i++) {
                    IBarDataSet set = barData.getDataSetByIndex(i);
                    if (set.isVisible()) {
                        drawDataSet(c, set, i);
                    }
                }
            }
        }
    }

    protected void drawEmptyView(Canvas canvas) {
        mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.black_3));
        RectF rectF = new RectF(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(),
                mViewPortHandler.contentRight(), mViewPortHandler.contentBottom());
        canvas.drawRect(rectF, mRenderPaint);
    }

    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);

        buffer.feed(dataSet);
        trans.pointValuesToPixel(buffer.buffer);
        mRenderPaint.setColor(dataSet.getColor());

        int count = Math.min((int) (Math.ceil((float) (dataSet.getEntryCount()) * phaseX)), dataSet.getEntryCount());

        for (int j = 0, i = 0; j < buffer.size() && i < count; j += 4, i++) {
            BarEntry entry = dataSet.getEntryForIndex(i);
            SleepItemEntry e;
            int paintColor;
            if (entry instanceof SleepItemEntry) {
                e = (SleepItemEntry) entry;
                paintColor = SleepItemTime.getSleepTypeColor(e.sleepItemTime.sleepType);
            } else {
                continue;
            }
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;

            mRenderPaint.setColor(paintColor);
            float startF = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
            float endF = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
            float width = Math.abs(endF - startF);
            RectF rectF;
            if (width < 1) {
                endF = AppUtil.isRTLDirection() ? startF - 1 : startF + 1;
                rectF = new RectF(startF, buffer.buffer[j + 1], endF, buffer.buffer[j + 3]);
            } else {
                rectF = new RectF(startF, buffer.buffer[j + 1], endF, buffer.buffer[j + 3]);
            }
            c.drawRect(rectF, mRenderPaint);
        }
    }

    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {
        float left = x - barWidthHalf;
        float right = x + barWidthHalf;
        float top = y1;
        float bottom = y2;
        mBarRect.set(left, top, right, bottom);
        trans.rectToPixelPhase(mBarRect, mAnimator.getPhaseY());
    }

}
