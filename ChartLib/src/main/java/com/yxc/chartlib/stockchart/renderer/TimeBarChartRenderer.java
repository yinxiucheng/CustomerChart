package com.yxc.chartlib.stockchart.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yxc.chartlib.utils.ColorUtil;
import com.yxc.customerchart.R;

/**
 * Created by ly on 2016/9/24.
 */
public class TimeBarChartRenderer extends BarChartRenderer {

    public TimeBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    private RectF mBarShadowRectBuffer = new RectF();

    @Override
    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0.f;

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // draw the bar shadow before the values
        if (mChart.isDrawBarShadowEnabled()) {
            mShadowPaint.setColor(dataSet.getBarShadowColor());

            BarData barData = mChart.getBarData();

            final float barWidth = barData.getBarWidth();
            final float barWidthHalf = barWidth / 2.0f;
            float x;

            for (int i = 0, count = Math.min((int) (Math.ceil((float) (dataSet.getEntryCount()) * phaseX)), dataSet.getEntryCount());
                 i < count;
                 i++) {

                BarEntry e = dataSet.getEntryForIndex(i);

                x = e.getX();

                mBarShadowRectBuffer.left = x - barWidthHalf;
                mBarShadowRectBuffer.right = x + barWidthHalf;

                trans.rectValueToPixel(mBarShadowRectBuffer);

                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    continue;
                }

                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) {
                    break;
                }

                mBarShadowRectBuffer.top = mViewPortHandler.contentTop();
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom();

                c.drawRect(mBarShadowRectBuffer, mShadowPaint);
            }
        }

        // initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        for (int j = 0; j < buffer.size(); j += 4) {

            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                continue;
            }

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                break;
            }


            // Set the color for the currently drawn value. If the index
            // is out of bounds, reuse colors.
            mRenderPaint.setColor(dataSet.getColor(j / 4));


            int i = j / 4;
            Object openClose = dataSet.getEntryForIndex(i).getData();
            if (openClose != null) {//根据开平判断柱状图的颜色填充
                float value = (Float) openClose;
                if (value > 0) {//表示增加
                    mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.up_color));
                    mRenderPaint.setStyle(Paint.Style.FILL);
                } else if (value < 0) {
                    mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.down_color));
                    mRenderPaint.setStyle(Paint.Style.FILL);
                }else if (value == 0) {
                    mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.equal_color));
                    mRenderPaint.setStyle(Paint.Style.FILL);
                }
            }


            c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint);

            if (drawBorder) {
                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mBarBorderPaint);
            }
        }
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BarData barData = mChart.getBarData();

        for (Highlight high : indices) {

            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            BarEntry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set)) {
                continue;
            }

            Transformer trans = mChart.getTransformer(set.getAxisDependency());
            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setStrokeWidth(0.6f);
            boolean isStack = (high.getStackIndex() >= 0 && e.isStacked()) ? true : false;

            final float y1;
            final float y2;

            if (isStack) {

                if (mChart.isHighlightFullBarEnabled()) {

                    y1 = e.getPositiveSum();
                    y2 = -e.getNegativeSum();

                } else {

                    Range range = e.getRanges()[high.getStackIndex()];

                    y1 = range.from;
                    y2 = range.to;
                }
            } else {
                y1 = e.getY();
                y2 = 0.f;
            }
            prepareBarHighlight(e.getX(), y1, y2, barData.getBarWidth() / 2f, trans);
            setHighlightDrawPos(high, mBarRect);
            c.drawLine(mBarRect.centerX(), mViewPortHandler.getContentRect().bottom, mBarRect.centerX(), 0, mHighlightPaint);
        }
    }
}
