package com.yxc.chartlib.mpchart.linechart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.chart.mpchart.MPChartAttr;
import com.xiaomi.fitness.chart.mpchart.linechart.CustomLineChart;
import com.xiaomi.fitness.chart.mpchart.linechart.CustomLineChartAttr;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.ColorUtil;
import com.xiaomi.fitness.common.utils.DisplayUtil;
import com.xiaomi.fitness.common.utils.TextUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author yxc
 * @since 2019-08-30
 */
public class CustomLineChartRenderer extends LineChartRenderer {
    private float[] mLineBuffer = new float[4];

    private YAxis mYAxis;
    private XAxis mXAxis;
    private ViewPortHandler mViewPortHandler;
    private CustomLineChartAttr mLineChartAttr;
    private String mMaxStr;

    public CustomLineChartRenderer(LineDataProvider chart, ChartAnimator animator,
                                   ViewPortHandler viewPortHandler,
                                   YAxis yAxis,
                                   XAxis xAxis,
                                   CustomLineChartAttr lineChartAttr) {
        super(chart, animator, viewPortHandler);
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mViewPortHandler = viewPortHandler;
        this.mLineChartAttr = lineChartAttr;
    }

    public void setLineChartAttr(CustomLineChartAttr lineChartAttr) {
        this.mLineChartAttr = lineChartAttr;
    }
    public void setMaxStr(String maxStr) {
        this.mMaxStr = maxStr;
    }

    /**
     * Draws a normal line.
     *
     * @param c
     * @param dataSet
     */
    @Override
    protected void drawLinear(Canvas c, ILineDataSet dataSet) {
        int entryCount = dataSet.getEntryCount();
        final boolean isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled();
        final int pointsPerEntryPair = isDrawSteppedEnabled ? 4 : 2;
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        float phaseY = mAnimator.getPhaseY();
        mRenderPaint.setStyle(Paint.Style.STROKE);
        Canvas canvas = null;
        // if the data-set is dashed, draw on bitmap-canvas
        if (dataSet.isDashedLineEnabled()) {
            canvas = mBitmapCanvas;
        } else {
            canvas = c;
        }

        mXBounds.set(mChart, dataSet);
        // if drawing filled is enabled
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            drawLinearFill(c, dataSet, trans, mXBounds);
        }
        // more than 1 color
        if (dataSet.getColors().size() > 1) {
            if (mLineBuffer.length <= pointsPerEntryPair * 2)
                mLineBuffer = new float[pointsPerEntryPair * 4];
            for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {
                Entry e = dataSet.getEntryForIndex(j);
                if (e == null) continue;
                mLineBuffer[0] = e.getX();
                mLineBuffer[1] = e.getY() * phaseY;
                if (j < mXBounds.max) {
                    e = dataSet.getEntryForIndex(j + 1);
                    if (e == null) break;
                    mLineBuffer[2] = e.getX();
                    mLineBuffer[3] = e.getY() * phaseY;
                } else {
                    mLineBuffer[2] = mLineBuffer[0];
                    mLineBuffer[3] = mLineBuffer[1];
                }
                trans.pointValuesToPixel(mLineBuffer);
                for (int i = 0; i < mLineBuffer.length; i++) {
                    if (i % 2 == 0) {
                        mLineBuffer[i] = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - mLineBuffer[i] : mLineBuffer[i];
                    }
                }
                float startX = mLineBuffer[0];
                float endX = mLineBuffer[2];
                if (!mViewPortHandler.isInBoundsRight(startX))
                    break;
                // make sure the lines don't do shitty things outside
                // bounds
                if (!mViewPortHandler.isInBoundsLeft(endX)
                        || (!mViewPortHandler.isInBoundsTop(mLineBuffer[1]) && !mViewPortHandler
                        .isInBoundsBottom(mLineBuffer[3])))
                    continue;
                // get the color that is set for this line-segment
                if (j < mXBounds.max) {
                    mRenderPaint.setShader(new LinearGradient(startX, mLineBuffer[1], endX, mLineBuffer[3],
                            dataSet.getColor(j), dataSet.getColor(j+1), Shader.TileMode.MIRROR));
                } else {
                    mRenderPaint.setColor(dataSet.getColor(j));
                }
                mRenderPaint.setStrokeCap(Paint.Cap.ROUND);
                if (mLineChartAttr.enableDrawLine){
                    canvas.drawLines(mLineBuffer, 0, pointsPerEntryPair * 2, mRenderPaint);
                }
            }
            drawMaxMinPoint(canvas, dataSet);
        } else { // only one color per dataset
            if (mLineBuffer.length < Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 2)
                mLineBuffer = new float[Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 4];
            Entry preEntry, currentEntry;
            preEntry = dataSet.getEntryForIndex(mXBounds.min);
            if (preEntry != null) {
                int j = 0;
                float yValueRange = mYAxis.getAxisMaximum() - mYAxis.getAxisMinimum();
                for (int x = mXBounds.min; x <= mXBounds.range + mXBounds.min; x++) {
                    preEntry = dataSet.getEntryForIndex(x == 0 ? 0 : (x - 1));
                    currentEntry = dataSet.getEntryForIndex(x);
                    if (preEntry == null || currentEntry == null) continue;
                    if (mLineChartAttr.enableCustomerStepped) {//调整游泳的阶梯线。
                        if (currentEntry.getX() - preEntry.getX() == 1) {
                            if (preEntry.getY() == 0) {
                                mLineBuffer[j++] = preEntry.getX();
                                mLineBuffer[j++] = -yValueRange / 100.0f;
                            } else {
                                mLineBuffer[j++] = preEntry.getX();
                                mLineBuffer[j++] = getYAsInverted(preEntry);
                            }
                            if (currentEntry.getY() == 0) {
                                mLineBuffer[j++] = preEntry.getX();
                                mLineBuffer[j++] = -yValueRange / 100.0f;
                            } else {
                                mLineBuffer[j++] = preEntry.getX();
                                mLineBuffer[j++] = getYAsInverted(currentEntry);
                            }
                        } else if (currentEntry.getY() == preEntry.getY() && currentEntry.getX() != 1) {
                            if (preEntry.getY() == 0) {
                                mLineBuffer[j++] = preEntry.getX() - 1 > 0 ? preEntry.getX() - 1 : preEntry.getX();
                                mLineBuffer[j++] = -yValueRange / 100.0f;
                            } else {
                                mLineBuffer[j++] = preEntry.getX() - 1 > 0 ? preEntry.getX() - 1 : preEntry.getX();
                                mLineBuffer[j++] = getYAsInverted(preEntry);
                            }
                            if (currentEntry.getY() == 0) {
                                mLineBuffer[j++] = currentEntry.getX();
                                mLineBuffer[j++] = -yValueRange / 100.0f;
                            } else {
                                mLineBuffer[j++] = currentEntry.getX();
                                mLineBuffer[j++] = getYAsInverted(currentEntry);
                            }
                        } else {
                            if (preEntry.getY() == 0) {
                                mLineBuffer[j++] = preEntry.getX();
                                mLineBuffer[j++] = -yValueRange / 100.0f;
                            } else {
                                mLineBuffer[j++] = preEntry.getX();
                                mLineBuffer[j++] = getYAsInverted(preEntry);
                            }
                            if (currentEntry.getY() == 0) {
                                mLineBuffer[j++] = currentEntry.getX();
                                mLineBuffer[j++] = -yValueRange / 100.0f;
                            } else {
                                mLineBuffer[j++] = currentEntry.getX();
                                mLineBuffer[j++] = getYAsInverted(currentEntry);
                            }
                        }
                    }else {
                        if (preEntry.getY() == 0) {
                            mLineBuffer[j++] = preEntry.getX();
                            mLineBuffer[j++] = -yValueRange / 100.0f;
                        } else {
                            mLineBuffer[j++] = preEntry.getX();
                            mLineBuffer[j++] = getYAsInverted(preEntry);
                        }
                        if (currentEntry.getY() == 0) {
                            mLineBuffer[j++] = currentEntry.getX();
                            mLineBuffer[j++] = -yValueRange / 100.0f;
                        } else {
                            mLineBuffer[j++] = currentEntry.getX();
                            mLineBuffer[j++] = getYAsInverted(currentEntry);
                        }
                    }
                }
                if (j > 0) {
                    trans.pointValuesToPixel(mLineBuffer);
                    for (int i = 0; i < mLineBuffer.length; i++) {
                        if (i % 2 == 0) {
                            mLineBuffer[i] = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - mLineBuffer[i] : mLineBuffer[i];
                        }
                    }
                    final int size = Math.max((mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2;
                    mRenderPaint.setColor(dataSet.getColor());
                    mRenderPaint.setStrokeWidth(DisplayUtil.dip2px(mLineChartAttr.lineStrokeWidth));
                    mRenderPaint.setStrokeCap(Paint.Cap.ROUND);
                    if (mLineChartAttr.enableDrawLine){
                        canvas.drawLines(mLineBuffer, 0, size, mRenderPaint);
                    }
                }
                drawMaxMinPoint(canvas, dataSet);
            }
        }
        mRenderPaint.setPathEffect(null);
    }

    protected void drawCubicBezier(Canvas canvas, ILineDataSet dataSet) {
        float phaseX = Math.max(0.f, Math.min(1.f, mAnimator.getPhaseX()));
        float phaseY = mAnimator.getPhaseY();
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        mXBounds.set(mChart, dataSet);
        float originalIntensity = dataSet.getCubicIntensity();
        cubicPath.reset();
        if (mXBounds.range >= 1) {
            float prevDx = 0f;
            float prevDy = 0f;
            float curDx = 0f;
            float curDy = 0f;
            // Take an extra point from the left, and an extra from the right.
            // That's because we need 4 points for a cubic bezier (cubic=4), otherwise we get lines moving and doing weird stuff on the edges of the chart.
            // So in the starting `prev` and `cur`, go -2, -1
            // And in the `lastIndex`, add +1
            final int firstIndex = mXBounds.min + 1;
            final int lastIndex = mXBounds.min + mXBounds.range;

            Entry prevPrev;
            Entry prev = dataSet.getEntryForIndex(Math.max(firstIndex - 2, 0));
            Entry cur = dataSet.getEntryForIndex(Math.max(firstIndex - 1, 0));
            Entry next = cur;
            int nextIndex = -1;

            if (cur == null) return;
            // let the spline start
            cubicPath.moveTo(cur.getX(), getYAsInverted(cur));
            float intensity = originalIntensity;

            for (int j = mXBounds.min + 1; j <= mXBounds.range + mXBounds.min; j++) {
                prevPrev = prev;
                prev = cur;
                cur = nextIndex == j ? next : dataSet.getEntryForIndex(j);
                nextIndex = j + 1 < dataSet.getEntryCount() ? j + 1 : j;
                next = dataSet.getEntryForIndex(nextIndex);
                if (cur instanceof RecyclerBarEntry) {
                    RecyclerBarEntry currentEntry = (RecyclerBarEntry) cur;
                    if (currentEntry.isMinMax == RecyclerBarEntry.TYPE_MINMAX_MAX
                            || currentEntry.isMinMax == RecyclerBarEntry.TYPE_MINMAX_MIN_NEAR
                            || currentEntry.isMinMax == RecyclerBarEntry.TYPE_MINMAX_MIN
                            || currentEntry.isMinMax == RecyclerBarEntry.TYPE_MINMAX_MAX_NEAR) {
                        intensity = 0.01f;//极值点附件降低 贝塞尔 曲线的强度，避免因为极值导致极值旁边的值受影响而被拉伸。（最好是找落差最大的相关联的两个点以及附近的点。）
//                      Log.d("CustomLineChart", "intensity:" + intensity + " j:" + j + " original Intensity:" + currentEntry.isMinMax);
                    } else {
                        intensity = originalIntensity;
                    }
                }
                prevDx = (cur.getX() - prevPrev.getX()) * intensity;
                prevDy = (getYAsInverted(cur) - getYAsInverted(prevPrev)) * intensity;
                curDx = (next.getX() - prev.getX()) * intensity;
                curDy = (getYAsInverted(next) - getYAsInverted(prev)) * intensity;
                cubicPath.cubicTo(prev.getX() + prevDx, (getYAsInverted(prev) + prevDy) * phaseY,
                        cur.getX() - curDx,
                        (getYAsInverted(cur) - curDy) * phaseY, cur.getX(), getYAsInverted(cur) * phaseY);
            }
        }
        // if filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled()) {
            cubicFillPath.reset();
            cubicFillPath.addPath(cubicPath);
            drawCubicFill(mBitmapCanvas, dataSet, cubicFillPath, trans, mXBounds);
        }
        mRenderPaint.setColor(dataSet.getColor());
        mRenderPaint.setStyle(Paint.Style.STROKE);
        mRenderPaint.setStrokeWidth(DisplayUtil.dip2px(mLineChartAttr.lineStrokeWidth));
        trans.pathValueToPixel(cubicPath);
        canvas.drawPath(cubicPath, mRenderPaint);
        mRenderPaint.setPathEffect(null);
        drawMaxMinPoint(canvas, dataSet);
    }

    private void drawMaxMinPoint(Canvas canvas, ILineDataSet dataSet) {
        mXBounds.set(mChart, dataSet);
        if (mXBounds.range >= 1) {
            Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
            for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {
                Entry cur = dataSet.getEntryForIndex(j);
                if (cur instanceof RecyclerBarEntry) {
                    RecyclerBarEntry currentEntry = (RecyclerBarEntry) cur;
                    if (currentEntry.isMinMax == RecyclerBarEntry.TYPE_MINMAX_MAX || currentEntry.isMinMax == RecyclerBarEntry.TYPE_MINMAX_MIN) {
                        MPPointD pointD = trans.getPixelForValues(cur.getX(), getYAsInverted(cur));
                        int originalColor = mPaint.getColor();
                        float xLocation = (float) pointD.x;
                        xLocation = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - xLocation : xLocation;
                        float yLocation = (float) pointD.y;
                        Paint.Style originalStyle = mPaint.getStyle();
                        mPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_max_point_boarder));
                        mPaint.setStrokeWidth(10);
                        mPaint.setStyle(Paint.Style.STROKE);
                        int paintColor = dataSet.getColor();
                        if (dataSet.getColors().size() > 0){
                            paintColor = dataSet.getColor(j);
                        }
                        if (currentEntry.isMinMax == RecyclerBarEntry.TYPE_MINMAX_MAX) {
                            //边界点的处理
                            if (j == mXBounds.range + mXBounds.min) {
                                xLocation = xLocation - DisplayUtil.dip2px(5);
                            } else if (j == mXBounds.min) {
                                xLocation = xLocation + DisplayUtil.dip2px(5);
                            }
                            if (mLineChartAttr.enableMaxCircle) {
                                canvas.drawCircle(xLocation, yLocation, DisplayUtil.dip2px(5f), mPaint);
                                mPaint.setColor(paintColor);
                                mPaint.setStyle(Paint.Style.FILL);
                                canvas.drawCircle(xLocation, yLocation, DisplayUtil.dip2px(3.5f), mPaint);
                                drawMaxPopup(canvas, xLocation, yLocation, mPaint);
                            }
                        } else {
                            if (mLineChartAttr.enableMinCircle) {
                                canvas.drawCircle(xLocation, yLocation, DisplayUtil.dip2px(5f), mPaint);
                                mPaint.setColor(paintColor);
                                mPaint.setStyle(Paint.Style.FILL);
                                canvas.drawCircle(xLocation, yLocation, DisplayUtil.dip2px(3.5f), mPaint);
                                if (mYAxis.isInverted()) {//配速的最佳配速，其实是绘制最小
                                    drawMaxPopup(canvas, xLocation, yLocation, mPaint);
                                }
                            }
                        }
                        mPaint.setColor(originalColor);
                        mPaint.setStyle(originalStyle);
                    }
                }
            }
        }
    }

    private void drawMaxPopup(Canvas canvas, float xLocation, float yLocation, Paint mPaint) {
        if (TextUtils.isEmpty(mMaxStr) || !mLineChartAttr.enableMaxPoup) {
            return;
        }
        RectF rectF = new RectF();
        mPaint.setTextSize(DisplayUtil.sp2px(12f));
        int leftTextPadding = DisplayUtil.dip2px(12);
        int topTextPadding = DisplayUtil.dip2px(5);
        float width = mPaint.measureText(mMaxStr) + 2 * leftTextPadding;
        float height = TextUtil.getTxtHeight1(mPaint) + 2 * topTextPadding;
        float left = xLocation - width / 2;
        float right = xLocation + width / 2;
        if (left <= mViewPortHandler.contentLeft()) {
            left = mViewPortHandler.contentLeft() + DisplayUtil.dip2px(2);
            right = left + width;
        } else if (right >= mViewPortHandler.contentRight()) {
            right = mViewPortHandler.contentRight() - DisplayUtil.dip2px(2);
            left = right - width;
        }
        float bottom = yLocation - DisplayUtil.dip2px(8);
        float top = bottom - height;
        rectF.set(left, top, right, bottom);
        int roundRadius = DisplayUtil.dip2px(13);
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, mPaint);
        mPaint.setColor(ColorUtil.getResourcesColor(R.color.common_white));
        float baseY = TextUtil.getTextBaseY(rectF, mPaint);
        float textLeft = left + leftTextPadding;
        canvas.drawText(mMaxStr, textLeft, baseY, mPaint);
    }

    private float getYAsInverted(Entry entry) {
        final float phaseY = mAnimator.getPhaseY();
        float yValueRange = mYAxis.getAxisMaximum() - mYAxis.getAxisMinimum();
        if (mYAxis.isInverted()) {
            if (entry.getY() <= mYAxis.getAxisMinimum()) {
                return entry.getY() * phaseY;
            } else {
                return (yValueRange - entry.getY()) * phaseY;
            }
        } else {
            return entry.getY() * phaseY;
        }
    }

    /**
     * Draws a filled linear path on the canvas.
     *
     * @param c
     * @param dataSet
     * @param trans
     * @param bounds
     */
    @Override
    protected void drawLinearFill(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds) {
        if (AppUtil.isRTLDirection()) {
            drawLinearFillRTL(c, dataSet, trans, bounds);
        } else {
            drawLinearFillLTR(c, dataSet, trans, bounds);
        }
    }

    private void drawLinearFillLTR(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds){
        final Path filled = mGenerateFilledPathBuffer;
        final int startingIndex = bounds.min;
        final int endingIndex = bounds.range + bounds.min;
        final int indexInterval = 128;
        int currentStartIndex = 0;
        int currentEndIndex = indexInterval;
        int iterations = 0;
        // Doing this iteratively in order to avoid OutOfMemory errors that can happen on large bounds sets.
        do {
            currentStartIndex = startingIndex + (iterations * indexInterval);
            currentEndIndex = currentStartIndex + indexInterval;
            currentEndIndex = currentEndIndex > endingIndex ? endingIndex : currentEndIndex;
            if (currentStartIndex <= currentEndIndex) {
                generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled);
                trans.pathValueToPixel(filled);

                final Drawable drawable = dataSet.getFillDrawable();
                if (drawable != null) {
                    drawFilledPath(c, filled, drawable);
                } else {
                    drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
                }
            }
            iterations++;
        } while (currentStartIndex <= currentEndIndex);
    }

    private void drawLinearFillRTL(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds){
        final Path filled = mGenerateFilledPathBuffer;
        final int startingIndex = bounds.min;
        final int endingIndex = bounds.range + bounds.min;
        final int indexInterval = 128;
        int currentStartIndex = 0;
        int currentEndIndex = indexInterval;
        int iterations = 0;
        // Doing this iteratively in order to avoid OutOfMemory errors that can happen on large bounds sets.
        do {
            currentStartIndex = startingIndex + (iterations * indexInterval);
            currentEndIndex = currentStartIndex + indexInterval;
            currentEndIndex = currentEndIndex > endingIndex ? endingIndex : currentEndIndex;
            if (currentStartIndex <= currentEndIndex) {
                generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled);
                trans.pathValueToPixel(filled);
                final Drawable drawable = dataSet.getFillDrawable();
                if (drawable != null) {
                    drawFilledPath(c, filled, drawable);
                } else {
                    drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
                }
            }
            iterations++;
        } while (currentStartIndex <= currentEndIndex);
    }


    /**
     * Generates a path that is used for filled drawing.
     *
     * @param dataSet    The dataset from which to read the entries.
     * @param startIndex The index from which to start reading the dataset
     * @param endIndex   The index from which to stop reading the dataset
     * @param outputPath The path object that will be assigned the chart data.
     * @return
     */
    private void generateFilledPath(final ILineDataSet dataSet, final int startIndex, final int endIndex, final Path outputPath) {

        final float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, mChart);
        final float phaseY = mAnimator.getPhaseY();
        final boolean isDrawSteppedEnabled = dataSet.getMode() == LineDataSet.Mode.STEPPED;

        final Path filled = outputPath;
        filled.reset();
        final Entry entry = dataSet.getEntryForIndex(startIndex);
        float startX = AppUtil.isRTLDirection() ? mXAxis.getAxisMaximum() - entry.getX() : entry.getX();
        filled.moveTo(startX, fillMin);
        filled.lineTo(startX, getYAsInverted(entry));
        // create a new path
        Entry currentEntry = null;
        Entry previousEntry = entry;
        for (int x = startIndex + 1; x <= endIndex; x++) {
            currentEntry = dataSet.getEntryForIndex(x);
            float currentStartX = AppUtil.isRTLDirection() ? mXAxis.getAxisMaximum() - currentEntry.getX() : currentEntry.getX();
            if (mLineChartAttr.enableCustomerStepped && currentEntry.getX() - previousEntry.getX() == 1) {
                filled.lineTo(previousEntry.getX(), getYAsInverted(currentEntry));
            } else {
                filled.lineTo(currentStartX, getYAsInverted(currentEntry));
            }
            previousEntry = currentEntry;
        }
        // close up
        if (currentEntry != null) {
            float currentStartX = AppUtil.isRTLDirection() ? mXAxis.getAxisMaximum() - currentEntry.getX() : currentEntry.getX();
            filled.lineTo(currentStartX, fillMin);
        }
        filled.close();
    }

    protected void drawEmptyView(Canvas canvas) {
        mRenderPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_empty_color));
        RectF rectF = new RectF(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight(), mViewPortHandler.contentBottom());
        canvas.drawRoundRect(rectF, DisplayUtil.dip2px(1f), DisplayUtil.dip2px(1f), mRenderPaint);
    }

    @Override
    protected void drawDataSet(Canvas c, ILineDataSet dataSet) {
        if (mChart instanceof CustomLineChart) {
            CustomLineChart lineChart = (CustomLineChart) mChart;
            if (lineChart.getMLineChartAttr().empty != MPChartAttr.EMPTY_N) {
                drawEmptyView(c);
                return;
            }
        }

        if (dataSet.getEntryCount() < 1)
            return;
        mRenderPaint.setStrokeWidth(DisplayUtil.dip2px(mLineChartAttr.lineStrokeWidth));
        mRenderPaint.setPathEffect(dataSet.getDashPathEffect());
        switch (dataSet.getMode()) {
            default:
            case LINEAR:
            case STEPPED:
                drawLinear(c, dataSet);
                break;
            case CUBIC_BEZIER:
                drawCubicBezier(c, dataSet);
                break;
            case HORIZONTAL_BEZIER:
                drawHorizontalBezier(dataSet);
                break;
        }
        mRenderPaint.setPathEffect(null);
    }

    /**
     * cache for the circle bitmaps of all datasets
     */
    private HashMap<IDataSet, DataSetImageCache> mImageCaches = new HashMap<>();

    private float[] mCirclesBuffer = new float[2];

    protected void drawCircles(Canvas c) {

        mRenderPaint.setStyle(Paint.Style.FILL);

        float phaseY = mAnimator.getPhaseY();

        mCirclesBuffer[0] = 0;
        mCirclesBuffer[1] = 0;

        List<ILineDataSet> dataSets = mChart.getLineData().getDataSets();

        for (int i = 0; i < dataSets.size(); i++) {

            ILineDataSet dataSet = dataSets.get(i);

            if (!dataSet.isVisible() || !dataSet.isDrawCirclesEnabled() ||
                    dataSet.getEntryCount() == 0)
                continue;

            mCirclePaintInner.setColor(dataSet.getCircleHoleColor());

            Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

            mXBounds.set(mChart, dataSet);

            float circleRadius = dataSet.getCircleRadius();
            float circleHoleRadius = dataSet.getCircleHoleRadius();
            boolean drawCircleHole = dataSet.isDrawCircleHoleEnabled() &&
                    circleHoleRadius < circleRadius &&
                    circleHoleRadius > 0.f;
            boolean drawTransparentCircleHole = drawCircleHole &&
                    dataSet.getCircleHoleColor() == ColorTemplate.COLOR_NONE;

            DataSetImageCache imageCache;

            if (mImageCaches.containsKey(dataSet)) {
                imageCache = mImageCaches.get(dataSet);
            } else {
                imageCache = new DataSetImageCache();
                mImageCaches.put(dataSet, imageCache);
            }

            boolean changeRequired = imageCache.init(dataSet);

            // only fill the cache with new bitmaps if a change is required
            if (changeRequired) {
                imageCache.fill(dataSet, drawCircleHole, drawTransparentCircleHole);
            }

            int boundsRangeCount = mXBounds.range + mXBounds.min;

            for (int j = mXBounds.min; j <= boundsRangeCount; j++) {
                Entry e = dataSet.getEntryForIndex(j);
                if (e == null) break;

                mCirclesBuffer[0] = e.getX();
                mCirclesBuffer[1] = e.getY() * phaseY;

                trans.pointValuesToPixel(mCirclesBuffer);
                float startX = AppUtil.isRTLDirection() ? mViewPortHandler.contentRight() + mViewPortHandler.offsetLeft() - mCirclesBuffer[0] : mCirclesBuffer[0];

                if (AppUtil.isRTLDirection()) {
                    if (!mViewPortHandler.isInBoundsLeft(startX))
                        break;

                    if (!mViewPortHandler.isInBoundsRight(startX) ||
                            !mViewPortHandler.isInBoundsY(mCirclesBuffer[1]))
                        continue;
                } else {
                    if (!mViewPortHandler.isInBoundsRight(startX))
                        break;

                    if (!mViewPortHandler.isInBoundsLeft(startX) ||
                            !mViewPortHandler.isInBoundsY(mCirclesBuffer[1]))
                        continue;
                }
                Bitmap circleBitmap = imageCache.getBitmap(j);

                if (circleBitmap != null) {
                    c.drawBitmap(circleBitmap, startX - circleRadius, mCirclesBuffer[1] - circleRadius, null);
                }
            }
        }
    }


    private class DataSetImageCache {

        private Path mCirclePathBuffer = new Path();

        private Bitmap[] circleBitmaps;

        /**
         * Sets up the cache, returns true if a change of cache was required.
         *
         * @param set
         * @return
         */
        protected boolean init(ILineDataSet set) {

            int size = set.getCircleColorCount();
            boolean changeRequired = false;

            if (circleBitmaps == null) {
                circleBitmaps = new Bitmap[size];
                changeRequired = true;
            } else if (circleBitmaps.length != size) {
                circleBitmaps = new Bitmap[size];
                changeRequired = true;
            }

            return changeRequired;
        }

        /**
         * Fills the cache with bitmaps for the given dataset.
         *
         * @param set
         * @param drawCircleHole
         * @param drawTransparentCircleHole
         */
        protected void fill(ILineDataSet set, boolean drawCircleHole, boolean drawTransparentCircleHole) {

            int colorCount = set.getCircleColorCount();
            float circleRadius = set.getCircleRadius();
            float circleHoleRadius = set.getCircleHoleRadius();

            for (int i = 0; i < colorCount; i++) {

                Bitmap.Config conf = Bitmap.Config.ARGB_4444;
                Bitmap circleBitmap = Bitmap.createBitmap((int) (circleRadius * 2.1), (int) (circleRadius * 2.1), conf);

                Canvas canvas = new Canvas(circleBitmap);
                circleBitmaps[i] = circleBitmap;
                mRenderPaint.setColor(set.getCircleColor(i));

                if (drawTransparentCircleHole) {
                    // Begin path for circle with hole
                    mCirclePathBuffer.reset();

                    mCirclePathBuffer.addCircle(
                            circleRadius,
                            circleRadius,
                            circleRadius,
                            Path.Direction.CW);

                    // Cut hole in path
                    mCirclePathBuffer.addCircle(
                            circleRadius,
                            circleRadius,
                            circleHoleRadius,
                            Path.Direction.CCW);

                    // Fill in-between
                    canvas.drawPath(mCirclePathBuffer, mRenderPaint);
                } else {
                    canvas.drawCircle(
                            circleRadius,
                            circleRadius,
                            circleRadius,
                            mRenderPaint);

                    if (drawCircleHole) {
                        canvas.drawCircle(
                                circleRadius,
                                circleRadius,
                                circleHoleRadius,
                                mCirclePaintInner);
                    }
                }
            }
        }
        /**
         * Returns the cached Bitmap at the given index.
         *
         * @param index
         * @return
         */
        protected Bitmap getBitmap(int index) {
            return circleBitmaps[index % circleBitmaps.length];
        }
    }
}

