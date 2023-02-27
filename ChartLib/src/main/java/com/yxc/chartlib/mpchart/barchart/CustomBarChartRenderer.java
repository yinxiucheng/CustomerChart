package com.yxc.chartlib.mpchart.barchart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.fitness.chart.entrys.SegmentBarEntry;
import com.yxc.chartlib.entrys.CurseEntry;
import com.yxc.chartlib.entrys.EcgEntry;
import com.yxc.chartlib.entrys.SleepEntry;
import com.yxc.chartlib.entrys.VO2MaxEntry;
import com.yxc.chartlib.entrys.model.SegmentRectModel;
import com.yxc.chartlib.mpchart.MPChartAttr;
import com.yxc.chartlib.util.CanvasUtil;
import com.yxc.chartlib.util.RoundRectType;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.AppUtil;
import com.yxc.chartlib.utils.ColorUtil;
import com.yxc.chartlib.utils.DisplayUtil;

public class CustomBarChartRenderer extends BarChartRenderer {
    private CustomBarChart mRoundRectBarChart;
    private CustomBarChartAttr attr;

    public CustomBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        mRoundRectBarChart = (CustomBarChart) chart;
        attr = mRoundRectBarChart.getAttribute();
    }

    @Override
    public void drawValues(Canvas c) {
        if (attr.barChartType != CustomBarChartAttr.TYPE_BAR_CHART_SEVEN) {
            super.drawValues(c);
        }
    }

    @Override
    public void initBuffers() {
        checkAttrs();
        BarData barData = mChart.getBarData();
        mBarBuffers = new BarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_SEVEN) {
                int bufferSize = computeBufferSize(set) * 4;//乘以4 表示矩形。
                mBarBuffers[i] = new SegmentBarChartBuffer(bufferSize, barData.getDataSetCount(), set.isStacked());
            } else {
                mBarBuffers[i] = new BarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                        barData.getDataSetCount(), set.isStacked());
            }
        }
    }

    private int computeBufferSize(IBarDataSet dataSet) {
        int rectListSizeSum = 0;
        int entryCount = dataSet.getEntryCount();
        for (int i = 0; i < entryCount; i++) {
            BarEntry entry = dataSet.getEntryForIndex(i);
            if (entry instanceof SegmentBarEntry) {
                SegmentBarEntry segmentBarEntry = (SegmentBarEntry) entry;
                List<SegmentRectModel> rectList = segmentBarEntry.rectValueModelList;
                rectListSizeSum += rectList.size();
            }
        }
        return rectListSizeSum;
    }

    @Override
    public void drawData(Canvas c) {
        BarData barData = mChart.getBarData();
        for (int i = 0; i < barData.getDataSetCount(); i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            if (set.isVisible()) {
                drawDataSet(c, set, i);
            }
        }
    }

    private void checkAttrs() {
        if (null == attr) {
            attr = mRoundRectBarChart.getAttribute();
        }
    }

    protected void drawDataSet(Canvas canvas, IBarDataSet dataSet, int index) {
        checkAttrs();
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();
        // initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());
        buffer.feed(dataSet);
        trans.pointValuesToPixel(buffer.buffer);
        if (attr.empty != MPChartAttr.EMPTY_N) {//空数据绘制
            drawEmpty(canvas, buffer, dataSet, attr);
            return;
        }
        if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_FIRST) {
            drawCommonEntryChart(canvas, buffer, dataSet, attr);
        } else if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_SECOND) {
            drawPaiEntryChart(canvas, buffer, dataSet, attr);
        } else if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_THIRD) {
            drawVO2MaxEntryChart(canvas, buffer, dataSet, attr);
        } else if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_FOUR) {
            drawCurseEntryChart(canvas, buffer, dataSet, attr);
        } else if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_SIX) {
            drawSleepEntryChart(canvas, buffer, dataSet, attr);
        } else if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_SEVEN) {
            drawSegmentEntryChart(canvas, buffer, dataSet, attr);
        } else if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_TRAINING_LOAD) {
            drawTrainingLoadEntryChart(canvas, buffer, dataSet, attr);
        } else if (attr.barChartType == CustomBarChartAttr.TYPE_BAR_CHART_RUNNING_INDICATOR) {
            drawRunningIndicatorEntryChart(canvas, buffer, dataSet, attr);
        } else {
            super.drawDataSet(canvas, dataSet, index);
        }
    }

    private void drawRunningIndicatorEntryChart(Canvas c, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        final boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }
        int entrySize = dataSet.getEntryCount();
        for (int j = 0, i = 0; j < buffer.size() && i < entrySize; j += 4, i++) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;
            /*为了底部能对齐，百分比显示正确，暂时不使用attr.mRectHeight*/
            float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
            float endX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
            RectF rectFBackground = new RectF(startX, 0, endX, mViewPortHandler.contentBottom());

            BarEntry entry = dataSet.getEntryForIndex(i);
            if (entry instanceof RecyclerBarEntry) {
                mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.running_indicator_semi_transparent_green_yellow));
                c.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
                if (entry.getY() > 0) {
                    mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.green_yellow));
                    RectF rectFTop = new RectF(startX, 0, endX, DisplayUtil.dip2px(1.7f));
                    c.drawRoundRect(rectFTop, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
                }
            }
        }
    }


    private List<SegmentRectModel> getAllSegmentRectModelList(IBarDataSet dataSet) {
        int entryCount = dataSet.getEntryCount();
        List<SegmentRectModel> rectModelList = new ArrayList<>();
        for (int i = 0; i < entryCount; i++) {
            BarEntry entry = dataSet.getEntryForIndex(i);
            if (entry instanceof SegmentBarEntry) {
                SegmentBarEntry segmentBarEntry = (SegmentBarEntry) entry;
                rectModelList.addAll(segmentBarEntry.rectValueModelList);
            }
        }
        return rectModelList;
    }


    private void drawSegmentEntryChart(Canvas c, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        final boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }
        List<SegmentRectModel> segmentRectModelList = getAllSegmentRectModelList(dataSet);
        int bufferSize = buffer.size();
        int rectModelListSize = segmentRectModelList.size();
        for (int j = 0, i = 0; j < bufferSize && i < rectModelListSize; j += 4, i++) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;
            /*为了底部能对齐，百分比显示正确，暂时不使用attr.mRectHeight*/
            float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
            float endX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
            RectF rectFBackground = new RectF(startX, buffer.buffer[j + 1], endX, buffer.buffer[j + 3]);
            float height = buffer.buffer[j + 3] - buffer.buffer[j + 1];
            float width = endX - startX;
            if (height < width) {
                float halfWidth = width / 2;
                rectFBackground.top = Math.max(buffer.buffer[j + 1] - halfWidth, mViewPortHandler.contentTop());
                rectFBackground.bottom = Math.min(buffer.buffer[j + 3] + halfWidth, mViewPortHandler.contentBottom());
            }
            SegmentRectModel segmentRectModel = segmentRectModelList.get(i);
            if (segmentRectModel.boardWidth > 0 && segmentRectModel.boardColor != -1) {
                mBarBorderPaint.setStrokeWidth(segmentRectModel.boardWidth);
                mBarBorderPaint.setColor(segmentRectModel.boardColor);
                c.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mBarBorderPaint);
            }
            if (segmentRectModel.rectColor != -1) {
                mRenderPaint.setColor(segmentRectModel.rectColor);
            }
            c.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
        }
    }

    protected void drawEmpty(Canvas c, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        if (attr.empty == MPChartAttr.EMPTY_TYPE_ONE) {
            drawEmptyView(c);
        } else if (attr.empty == MPChartAttr.EMPTY_TYPE_TWO) {
            drawEmptyEntryChart(c, buffer, dataSet, attr);
        }
    }

    protected void drawEmptyView(Canvas canvas) {
        mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_empty_color));
        RectF rectF = new RectF(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight(), mViewPortHandler.contentBottom());
        canvas.drawRoundRect(rectF, DisplayUtil.dip2px(1f), DisplayUtil.dip2px(1f), mRenderPaint);
    }

    private void drawEmptyEntryChart(Canvas c, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_empty_color));
        int entrySize = dataSet.getEntryCount();
        for (int j = 0, i = 0; j < buffer.size() && i < entrySize; j += 4, i++) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;
            RectF rectFBackground = new RectF(buffer.buffer[j], mViewPortHandler.contentTop(), buffer.buffer[j + 2],
                    mViewPortHandler.contentBottom());
            c.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
        }
    }

    private void drawTrainingLoadEntryChart(Canvas c, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        final boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }
        int entrySize = dataSet.getEntryCount();
        for (int j = 0, i = 0; j < buffer.size() && i < entrySize; j += 4, i++) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;
            /*为了底部能对齐，百分比显示正确，暂时不使用attr.mRectHeight*/
            float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
            float endX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
            RectF rectFBackground = new RectF(startX, 0, endX, mViewPortHandler.contentBottom());

            BarEntry entry = dataSet.getEntryForIndex(i);
            if (entry != null && entry instanceof RecyclerBarEntry) {
                mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_training_load_normal_bar));
                c.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);

                if (entry.getY() > 0) {
                    mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_training_load_top_bar));
                    RectF rectFTop = new RectF(startX, 0, endX, DisplayUtil.dip2px(1.7f));
                    c.drawRoundRect(rectFTop, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
                }
            }
        }
    }

    private void drawSleepEntryChart(Canvas c, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        final boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }
        int entrySize = dataSet.getEntryCount();
        for (int j = 0, i = 0; j < buffer.size() && i < entrySize; j += 4, i++) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;
            /*为了底部能对齐，百分比显示正确，暂时不使用attr.mRectHeight*/
            float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
            float endX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
            RectF rectFBackground = new RectF(startX, 0, endX, mViewPortHandler.contentBottom());

            BarEntry entry = dataSet.getEntryForIndex(i);
            if (entry != null && entry instanceof SleepEntry) {
                SleepEntry sleepEntry = (SleepEntry) entry;
                mRenderPaint.setColor(sleepEntry.barChartColor);
                c.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);

                if (sleepEntry.getY() > 0) {
                    mRenderPaint.setColor(sleepEntry.color);
                    RectF rectFTop = new RectF(startX, 0, endX, DisplayUtil.dip2px(1.7f));
                    c.drawRoundRect(rectFTop, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
                }
            }
        }
    }

    private void drawCommonEntryChart(Canvas c, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        final boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }
        int entrySize = dataSet.getEntryCount();
        for (int j = 0, i = 0; j < buffer.size() && i < entrySize; j += 4, i++) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;
            /*绘制背景*/
            /* RectF rectFBackground = new RectF(buffer.buffer[j], 0, buffer.buffer[j + 2],
                    attr.mRectHeight );*/
            /*为了底部能对齐，百分比显示正确，暂时不使用attr.mRectHeight*/
            float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
            float endX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
            RectF rectFBackground = new RectF(startX, 0, endX, buffer.buffer[j + 3]);

            mRenderPaint.setColor(attr.mNoneDoneColor);
            mRenderPaint.setAlpha((int) (attr.mNoneDoneAlpha * 255));
       /*     Log.d(TAG,"buffer = "+buffer.buffer[j]+" "+buffer.buffer[j+1]+" "+buffer.buffer[j+2]
            +" "+buffer.buffer[j+3]);
            Log.d(TAG,"mNoneDoneAlpha = "+attr.mNoneDoneAlpha +" mRectHeight "+attr.mRectHeight);*/
//            c.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
            /*绘制已完成*/
            RectF rectF = new RectF(startX, buffer.buffer[j + 1], endX, buffer.buffer[j + 3]);

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            } else {
                BarEntry entry = dataSet.getEntryForIndex(i);
                if (entry instanceof RecyclerBarEntry) {
                    RecyclerBarEntry barEntry = (RecyclerBarEntry) entry;
                    if (barEntry.validType == RecyclerBarEntry.TYPE_INVALID) {
                        mRenderPaint.setColor(attr.mNoneDoneColor);
                    } else {
                        mRenderPaint.setColor(attr.mDoneColor);
                        mRenderPaint.setAlpha((int) attr.mDoneAlpha * 255);
                    }
                }
            }
            c.drawRoundRect(rectF, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
        }
    }

    private void drawCurseEntryChart(Canvas c, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        final boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }
        int entrySize = dataSet.getEntryCount();
        for (int j = 0, i = 0; j < buffer.size() && i < entrySize; j += 4, i++) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;

            BarEntry entry = dataSet.getEntryForIndex(i);
            if (entry != null && entry instanceof CurseEntry) {
                CurseEntry curseEntry = (CurseEntry) entry;
                drawCurseItemEntry(c, buffer, attr, j, curseEntry);
            }
        }
    }

    private void drawCurseItemEntry(Canvas c, BarBuffer buffer, CustomBarChartAttr attr, int j, CurseEntry curseEntry) {
        float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
        float endX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
        if (curseEntry.curseStatue == CurseEntry.CURSE_STATUE_FORECAST
                || curseEntry.curseStatue == CurseEntry.CURSE_STATUE_DANGER
                || curseEntry.curseStatue == CurseEntry.CURSE_STATUE_OVUM) {
            int resource = CurseEntry.getCurseResource(curseEntry.curseStatue);
            RectF dstRectF = new RectF(Math.min(startX, endX), 0, Math.max(startX, endX), mViewPortHandler.contentBottom());
            Context context = AppUtil.getApp();
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource);
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            c.drawBitmap(bitmap, src, dstRectF, null);
        }

        if (curseEntry.curseStatue == CurseEntry.CURSE_STATUE_SIGN
                || curseEntry.curseStatue == CurseEntry.CURSE_STATUE_NORMAL) {
            RectF rectFBackground = new RectF(startX, 0, endX, mViewPortHandler.contentBottom());
            mRenderPaint.setColor(curseEntry.fillColor);
            mRenderPaint.setAlpha(curseEntry.alphaValue);
            c.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
        }

        /*绘制已完成*/
        if (curseEntry.curseStatue == CurseEntry.CURSE_STATUE_FORECAST
                || curseEntry.curseStatue == CurseEntry.CURSE_STATUE_SIGN) {
            RectF rectF = new RectF(startX, 0, endX, DisplayUtil.dip2px(1.7f));
            mRenderPaint.setColor(curseEntry.topColor);
            mRenderPaint.setAlpha(0xFF);
            c.drawRoundRect(rectF, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
        }
    }


    private void drawVO2MaxEntryChart(Canvas canvas, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        final boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }
        int entrySize = dataSet.getEntryCount();
        for (int j = 0, i = 0; j < buffer.size() && i < entrySize; j += 4, i++) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;
            BarEntry entry = dataSet.getEntryForIndex(i);
            if (null == entry) {
                continue;
            }
            float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
            float endX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
            if (entry instanceof VO2MaxEntry) {
                VO2MaxEntry vo2MaxEntry = (VO2MaxEntry) entry;
                RectF rectFBackground = new RectF(startX, 0, endX, mViewPortHandler.contentBottom());
                mRenderPaint.setColor(vo2MaxEntry.fillColor);
                canvas.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
                if (vo2MaxEntry.getY() > 0) {
                    RectF rectFTop = new RectF(startX, 0, endX, DisplayUtil.dip2px(1.7f));
                    mRenderPaint.setColor(vo2MaxEntry.vo2MaxColor);
                    canvas.drawRoundRect(rectFTop, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
                }
            } else {
                RectF rectFBackground = new RectF(startX, 0, endX, mViewPortHandler.contentBottom());
                mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_empty_color));
                canvas.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
            }
        }
    }

    private void drawPaiEntryChart(Canvas canvas, BarBuffer buffer, IBarDataSet dataSet, CustomBarChartAttr attr) {
        final boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }
        for (int j = 0; j < buffer.size(); j += 4) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;
            float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j] : buffer.buffer[j];
            float endX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - buffer.buffer[j + 2] : buffer.buffer[j + 2];
            RectF rectFBackground = new RectF(startX, buffer.buffer[j + 1], endX, mViewPortHandler.contentBottom());
            Path path = CanvasUtil.createRectRoundPath(rectFBackground, attr.mRectRadius, RoundRectType.TYPE_TOP);
            mRenderPaint.setColor(attr.mDoneColor);
            canvas.drawPath(path, mRenderPaint);
//            canvas.drawRoundRect(rectFBackground, attr.mRectRadius, attr.mRectRadius, mRenderPaint);
        }
    }
}
