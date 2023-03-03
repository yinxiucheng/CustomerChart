package com.yxc.chartlib.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.util.CanvasUtil;
import com.yxc.chartlib.util.RoundRectType;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.AppUtil;
import com.yxc.chartlib.utils.ColorUtil;
import com.yxc.chartlib.utils.DisplayUtil;
import com.yxc.chartlib.utils.TextUtil;
import com.yxc.chartlib.utils.DecimalUtil;
 public class XAxisRender<V extends BaseChartAttrs> {

     protected Paint mTextPaint;
     protected Paint mBgPaint;
     protected Paint mLinePaint;

     protected V mBarChartAttrs;

    public XAxisRender(V barChartAttrs) {
        this.mBarChartAttrs = barChartAttrs;
        initTextPaint();
        initBgPaint();
        initPaint();
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.reset();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(0.75f);
        mLinePaint.setColor(Color.GRAY);
    }

    private void initBgPaint() {
        mBgPaint = new Paint();
        mBgPaint.reset();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(Color.GRAY);
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(mBarChartAttrs.xAxisTxtSize);
    }

    final public void drawBackground(Canvas canvas, RecyclerView parent) {
        if (!mBarChartAttrs.enableXAxisBg) {
            return;
        }
        Context context = parent.getContext();

        int color = mBgPaint.getColor();
        mBgPaint.setColor(ColorUtil.getResourcesColor(R.color.black_2));
        float left = parent.getLeft();
        float right = parent.getRight();
        float top = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float bottom = parent.getHeight();
        RectF rectF = new RectF(left, top, right, bottom);
        float radius = mBarChartAttrs.xAxisBgRadius;
        Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_BOTTOM);
//        canvas.drawPath(path, mBgPaint);
        mBgPaint.setColor(color);
    }

    //绘制网格 纵轴线
    public void drawVerticalLine(Canvas canvas, RecyclerView parent, XAxis xAxis) {
        if (!mBarChartAttrs.enableXAxisGridLine) {
            return;
        }
        int parentTop = parent.getPaddingTop();
        int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        int parentStart = AppUtil.isRTLDirection() ? parent.getWidth() - parent.getPaddingRight() : parent.getPaddingLeft();
        int parentEnd = AppUtil.isRTLDirection() ? parent.getPaddingLeft() : parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(child);
            if (adapterPosition == RecyclerView.NO_POSITION) {
                continue;
            }
            int type = parent.getAdapter().getItemViewType(adapterPosition);
            final float xStart = AppUtil.isRTLDirection() ? child.getRight() : child.getLeft();
            //todo 大周期的线靠右画了，因为用的item的endTime 来计算的。
            final float xEnd = AppUtil.isRTLDirection() ? child.getLeft() : child.getRight();
//            Log.d("XAxisRender", " xStart:" + xStart + " parentEnd:" + parentEnd + " parentStart:" + parentStart);
            if (AppUtil.isRTLDirection()) {
                if (xStart > parentStart || xStart < parentEnd) {//超出的时候就不要画了
                    continue;
                }
            } else {
                if (xStart > parentEnd || xStart < parentStart) {//超出的时候就不要画了
                    continue;
                }
            }
            float xFirstLine = xStart;//大周期线靠左

            if (mBarChartAttrs.xFirstLinePosition == XAxis.POSITION_RIGHT) {//设置属性靠右
                xFirstLine = xEnd;
            }

            if (type == RecyclerBarEntry.TYPE_XAXIS_FIRST || type == RecyclerBarEntry.TYPE_XAXIS_SPECIAL) {
                if (mBarChartAttrs.enableXAxisFirstGridLine) {
                    mLinePaint.setColor(xAxis.firstDividerColor);
                    Path path = new Path();
                    path.moveTo(xFirstLine, parentBottom - mBarChartAttrs.contentPaddingBottom);
                    path.lineTo(xFirstLine, parentTop);
                    canvas.drawPath(path, mLinePaint);
                }
            } else if (type == RecyclerBarEntry.TYPE_XAXIS_SECOND) {
                if (mBarChartAttrs.enableXAxisSecondGridLine) {
                    //拿到child 的布局信息
//                    PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
//                    mBgPaint.setPathEffect(pathEffect);
                    mLinePaint.setColor(xAxis.secondDividerColor);
                    Path path = new Path();
                    path.moveTo(xStart, parentBottom - mBarChartAttrs.contentPaddingBottom);
                    path.lineTo(xStart, parentTop);
                    canvas.drawPath(path, mLinePaint);
                }
            } else if (type == RecyclerBarEntry.TYPE_XAXIS_THIRD) {
                if (mBarChartAttrs.enableXAxisThirdGridLine) {
                    //拿到child 的布局信息
//                    PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
//                    mBgPaint.setPathEffect(pathEffect);
                    mLinePaint.setColor(xAxis.thirdDividerColor);
                    Path path = new Path();
                    path.moveTo(xStart, parentBottom - mBarChartAttrs.contentPaddingBottom);
                    path.lineTo(xStart, parentTop);
                    canvas.drawPath(path, mLinePaint);
                }
            }
        }
    }

    protected String getXAxisLabel(RecyclerBarEntry barEntry, ValueFormatter valueFormatter){
        String dateStr = valueFormatter.getBarLabel(barEntry);
        if (barEntry.type == RecyclerBarEntry.TYPE_XAXIS_END_DAY
                && !mBarChartAttrs.enableEndDayXAxis){
            dateStr = "";
        }
        return dateStr;
    }

    //绘制X坐标
    public void drawXAxis(Canvas canvas, RecyclerView parent, XAxis xAxis) {
        if (!mBarChartAttrs.enableXAxisLabel) {
            return;
        }
        float bottomHeight = parent.getPaddingBottom() + mBarChartAttrs.contentPaddingBottom;
        float txtHeight = TextUtil.getTxtHeight1(mTextPaint);
        float parentBottom = parent.getHeight() - bottomHeight;

        final int childCount = parent.getChildCount();
        mTextPaint.setColor(xAxis.getTextColor());

        int parentStart = AppUtil.isRTLDirection() ? parent.getWidth() - parent.getPaddingLeft() : parent.getPaddingLeft();
        int parentEnd = AppUtil.isRTLDirection() ? parent.getPaddingRight() : parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int xStart = AppUtil.isRTLDirection() ? child.getRight() : child.getLeft();
            final int xEnd = AppUtil.isRTLDirection() ? child.getLeft() : child.getRight();

            RecyclerBarEntry barEntry = (RecyclerBarEntry) child.getTag();

            IAxisValueFormatter formatter = xAxis.getValueFormatter();
            String dateStr = "";
            if (formatter instanceof ValueFormatter) {
                ValueFormatter valueFormatter = (ValueFormatter) formatter;
                dateStr = getXAxisLabel(barEntry, valueFormatter);
            }

            if (!TextUtils.isEmpty(dateStr)) {
                int childWidth = child.getWidth();
                float txtWidth = mTextPaint.measureText(dateStr);
                float distance = childWidth - txtWidth;
                float txtXStart = AppUtil.isRTLDirection() ? xStart - distance / 2 : xStart + distance / 2;
                float txtXEnd = AppUtil.isRTLDirection() ? txtXStart - txtWidth : txtXStart + txtWidth;

                //RTL 是从左到右 ， LTR是 右到左， 都是从坐标端开始。
                if (AppUtil.isRTLDirection()) {
                    if (mBarChartAttrs.xAxisLabelPosition == XAxis.POSITION_LEFT) {
                        txtXEnd = xEnd;
                        txtXStart = xEnd - distance;
                    } else if (mBarChartAttrs.xAxisLabelPosition == XAxis.POSITION_RIGHT) {
                        txtXStart = xStart;
                        txtXEnd = txtXStart + txtWidth;
                    }
                } else {
                    if (mBarChartAttrs.xAxisLabelPosition == XAxis.POSITION_LEFT) {
                        txtXStart = xStart;
                        txtXEnd = txtXStart + txtWidth;
                    } else if (mBarChartAttrs.xAxisLabelPosition == XAxis.POSITION_RIGHT) {
                        txtXEnd = xEnd;
                        txtXStart = xEnd - distance;
                    }
                }
                float txtTop = parentBottom + bottomHeight / 2 - txtHeight / 2;
                float txtBottom = txtTop + txtHeight;
                int length = dateStr.length();
                float startRect = AppUtil.isRTLDirection() ? txtXEnd : txtXStart;
                float endRect = AppUtil.isRTLDirection() ? txtXStart : txtXEnd;
                RectF rectF = new RectF(startRect, txtTop, endRect, txtBottom);
                float baseLineY = TextUtil.getTextBaseY(rectF, mTextPaint)-20;

                //设置字体
                if (mBarChartAttrs.xAxisLabelFont != -1) {
                    TextUtil.setTypeface(mTextPaint, mBarChartAttrs.xAxisLabelFont);
                }
                if (AppUtil.isRTLDirection()) {
                    if (DecimalUtil.smallOrEquals(txtXEnd, parentStart) && DecimalUtil.bigOrEquals(txtXStart, parentEnd)) {//中间位置
                        canvas.drawText(dateStr, rectF.left, baseLineY, mTextPaint);
                    } else if (txtXStart > parentStart && txtXEnd < parentStart) {//处理左边界
                        int displayLength = (int) ((parentStart - txtXEnd + 1) / txtWidth * length);
                        int endIndex = displayLength;
                        if (endIndex < length) {
                            endIndex += 1;
                        }
                        canvas.drawText(dateStr, 0, endIndex, txtXStart, baseLineY, mTextPaint);
                    } else if (txtXEnd < parentEnd && txtXStart > parentEnd) {//处理右边界
                        int displayLength = (int) ((txtXStart - parentEnd) / txtWidth * length);
                        int index = length - displayLength;
                        canvas.drawText(dateStr, index, length, parentStart, baseLineY, mTextPaint);
                    }
                } else {
                    if (barEntry.type == RecyclerBarEntry.TYPE_XAXIS_END_TODAY || barEntry.type == RecyclerBarEntry.TYPE_XAXIS_END_DAY){
                        canvas.drawText(dateStr, txtXStart - (txtXEnd - parentEnd) - 8, baseLineY, mTextPaint);//靠右
                    } else if (DecimalUtil.bigOrEquals(txtXStart, parentStart) && DecimalUtil.smallOrEquals(txtXEnd, parentEnd)) {//中间位置
                        canvas.drawText(dateStr, rectF.left, baseLineY, mTextPaint);
                    } else if (txtXStart < parentStart && txtXEnd > parentStart) {//处理左边界
//                        int displayLength = (int) ((txtXEnd - parentStart) / txtWidth * length);
//                        int index = length - displayLength;
//                        canvas.drawText(dateStr, index, length, parentStart, baseLineY, mTextPaint);
                        canvas.drawText(dateStr, parentStart, baseLineY, mTextPaint);
                    } else if (txtXEnd > parentEnd && txtXStart < parentEnd) {//处理右边界
                        if (!mBarChartAttrs.xAxisForbidDealEndBoundary) {
                            canvas.drawText(dateStr, txtXStart - (txtXEnd - parentEnd) - 8, baseLineY, mTextPaint);
                        }
                    }
                }
            }
        }
    }

    //绘制X坐标
    final public void drawXAxisDisplay(Canvas canvas, RecyclerView parent, V attrs) {
        if (!mBarChartAttrs.enableXAxisDisplayLabel) {
            return;
        }
        Context context = AppUtil.getApp();
        float parentBottom = parent.getHeight() - parent.getPaddingBottom()
                - mBarChartAttrs.contentPaddingBottom + DisplayUtil.dip2px(5);

        if (mBarChartAttrs.xAxisLabelFont != -1) {
            TextUtil.setTypeface(mTextPaint, mBarChartAttrs.xAxisLabelFont);
        }
        mTextPaint.setTextSize(attrs.xAxisTxtSize);
        mTextPaint.setColor(attrs.xAxisTxtColor);

        String[] strArray = new String[]{context.getString(R.string.data_xaxis_time_0), context.getString(R.string.data_xaxis_time_12), context.getString(R.string.data_xaxis_time_24)};

        float textWidth = mTextPaint.measureText(strArray[0]);
        float parentWidth = parent.getWidth() - parent.getPaddingStart() - parent.getPaddingEnd();
        float spaceWidth = (parentWidth - textWidth * strArray.length) / (strArray.length - 1);

        float spaceEntry = parent.getWidth() / mBarChartAttrs.displayNumbers + DisplayUtil.dip2px(3);

        float height = TextUtil.getTxtHeight1(mTextPaint);

        for (int i = 0; i < strArray.length; i++) {
            float rectFLeft = parent.getPaddingStart() + i * (spaceWidth + textWidth);
            if (i == 0) {
                rectFLeft = rectFLeft + spaceEntry / 2;
            }
            if (i == strArray.length - 1) {
                rectFLeft = rectFLeft - spaceEntry / 2 + DisplayUtil.dip2px(3);
            }
            float rectFRight = rectFLeft + textWidth;
            RectF rect = new RectF(rectFLeft, parentBottom, rectFRight, parentBottom + height);
            float baseLineY = TextUtil.getTextBaseY(rect, mTextPaint);
            canvas.drawText(strArray[i], rect.left, baseLineY, mTextPaint);
        }
    }
}
