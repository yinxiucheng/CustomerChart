package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.fitness.chart.entrys.SegmentBarEntry;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.attrs.LineChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.entrys.MaxMinEntry;
import com.yxc.chartlib.entrys.model.SegmentRectModel;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.transform.RecyclerTransform;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.AppUtil;
import com.yxc.chartlib.utils.ColorUtil;
import com.yxc.chartlib.utils.DisplayUtil;
import com.yxc.chartlib.utils.TextUtil;
import com.yxc.chartlib.utils.DecimalUtil;
import com.yxc.chartlib.utils.LocalLanguageUtil;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/14
 */
public abstract class BaseChartRender<T extends RecyclerBarEntry, E extends BaseChartAttrs> {
    protected E mBarChartAttrs;
    protected Paint mBarChartPaint;
    protected Paint mTextPaint;
    protected Paint mBarChartStrokePaint;
    protected ValueFormatter mBarChartValueFormatter;
    protected ValueFormatter mHighLightValueFormatter;

    protected Paint mHighLightBigPaint;
    protected Paint mHighLightSmallPaint;
    protected Paint mHighLightDescPaint;
    protected Paint mHighLightLinePaint;

    public BaseChartRender(E barChartAttrs, ValueFormatter highLightValueFormatter) {
        this.mBarChartAttrs = barChartAttrs;
        this.mHighLightValueFormatter = highLightValueFormatter;
        initBarChartPaint();
        initTextPaint();
        initHighLightPaint();
    }

    public BaseChartRender(E barChartAttrs, ValueFormatter barChartValueFormatter,
                           ValueFormatter highLightValueFormatter) {
        this.mBarChartAttrs = barChartAttrs;
        this.mBarChartValueFormatter = barChartValueFormatter;
        this.mHighLightValueFormatter = highLightValueFormatter;
        initBarChartPaint();
        initTextPaint();
        initHighLightPaint();
    }

    public BaseChartRender(E barChartAttrs) {
        this.mBarChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
        initHighLightPaint();
    }

    protected void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mBarChartAttrs.txtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }

    protected void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setStrokeWidth(8);
        mBarChartPaint.setColor(mBarChartAttrs.chartColor);

        mBarChartStrokePaint=new Paint();
        mBarChartStrokePaint.reset();
        mBarChartStrokePaint.setAntiAlias(true);
        mBarChartStrokePaint.setStyle(Paint.Style.STROKE);
        mBarChartStrokePaint.setColor(mBarChartAttrs.chartColor);
    }

    protected void initHighLightPaint() {

        mHighLightBigPaint = new Paint();
        mHighLightBigPaint.reset();
        mHighLightBigPaint.setAntiAlias(true);
        mHighLightBigPaint.setColor(ColorUtil.getResourcesColor(R.color.text_color));

        if (mBarChartAttrs.highLightBigTextSize > 0) {
            mHighLightBigPaint.setTextSize(mBarChartAttrs.highLightBigTextSize);
        }else {
            int textSize = DisplayUtil.dip2px(24);
            if (LocalLanguageUtil.INSTANCE.languageGerman()
                    || LocalLanguageUtil.INSTANCE.languageGreek()
                    || LocalLanguageUtil.INSTANCE.languageGreek()){
                textSize = DisplayUtil.dip2px(18);
            }
            mHighLightBigPaint.setTextSize(textSize);
        }
        mHighLightBigPaint.setTypeface(Typeface.create("mipro-regular", Typeface.BOLD));

        mHighLightSmallPaint = new Paint();
        mHighLightSmallPaint.reset();
        mHighLightSmallPaint.setAntiAlias(true);
        mHighLightSmallPaint.setColor(ColorUtil.getResourcesColor(R.color.text_color));
        if (mBarChartAttrs.highLightSmallTextSize > 0) {
            mHighLightSmallPaint.setTextSize(mBarChartAttrs.highLightSmallTextSize);
        } else {
            mHighLightSmallPaint.setTextSize(DisplayUtil.dip2px(12));
        }
        mHighLightSmallPaint.setTextAlign(Paint.Align.LEFT);
        mHighLightSmallPaint.setTypeface(Typeface.create("mipro-regular", Typeface.BOLD));

        mHighLightDescPaint = new Paint();
        mHighLightDescPaint.reset();
        mHighLightDescPaint.setAntiAlias(true);
        mHighLightDescPaint.setColor(ColorUtil.getResourcesColor(R.color.text_color_40));
        mHighLightDescPaint.setTextSize(DisplayUtil.dip2px(12));
        mHighLightDescPaint.setTypeface(Typeface.create("mipro-regular", Typeface.NORMAL));

        mHighLightLinePaint = new Paint();
        mHighLightLinePaint.reset();
        mHighLightLinePaint.setAntiAlias(true);
        mHighLightLinePaint.setStyle(Paint.Style.STROKE);
        mHighLightLinePaint.setStrokeWidth(3);
        mHighLightLinePaint.setColor(mBarChartAttrs.highLightColor);
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    //绘制选中时 highLight 标线及浮框。
    public <E extends BaseYAxis> void drawHighLight(Canvas canvas, @NonNull RecyclerView parent, E yAxis) {
        if (mBarChartAttrs.enableValueMark) {
            int childCount = parent.getChildCount();
            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                T entry = (T) child.getTag();
                String valueStr = mHighLightValueFormatter.getBarLabel(entry);
                if (entry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    RectF rectF = ChartComputeUtil.getChartRectF(child, parent, yAxis, mBarChartAttrs, entry);
                    float width = child.getWidth();
                    float childCenter = child.getLeft() + width / 2;

                    //绘制浮框
                    RectF hRect = drawHighLightValue2(canvas, valueStr, childCenter, parent);
                    float[] points;
                    if (mBarChartAttrs instanceof LineChartAttrs
                            || entry instanceof SegmentBarEntry
                            || entry instanceof MaxMinEntry){
                        float contentBottom = child.getBottom() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
                        points = new float[]{childCenter, hRect.bottom, childCenter, contentBottom};
//                        SegmentBarEntry segmentBarEntry = (SegmentBarEntry) entry;
//                        points = getSegmentChartPoints(rectF, segmentBarEntry, parent, yAxis, childCenter);
                    } else {
                        points = new float[]{childCenter, rectF.top, childCenter, hRect.bottom};
                    }
                    canvas.drawLines(points, mHighLightLinePaint);
                }
            }
        }
    }

    private <E extends BaseYAxis> float[] getSegmentChartPoints(RectF rectF,
                                          SegmentBarEntry segmentBarEntry,
                                          RecyclerView parentView, E yAxis, float childCenter) {
        List<SegmentRectModel> rectModelList = segmentBarEntry.rectValueModelList;
        int size = rectModelList.size();
        float[] points = new float[size * 4];
        for (int i = 0; i < size; i++) {
            SegmentRectModel rectModel = rectModelList.get(i);
            float topValue = rectModel.topValue;
            float bottomValue = rectModel.bottomValue;
            float topRect = RecyclerTransform.getPixelForValuesHeightBetweenBottom(parentView, yAxis, mBarChartAttrs, topValue);
            float bottomRect = RecyclerTransform.getPixelForValuesHeightBetweenBottom(parentView, yAxis, mBarChartAttrs, bottomValue);
            points[4 * i] = childCenter;
            points[4 * i + 1] = rectF.bottom - topRect;
            points[4 * i + 2] = childCenter;
            points[4 * i + 3] = rectF.bottom - bottomRect;
        }
        return points;
    }

    private float computeValueStrWidth(String valueStr) {
        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_VALUE_STR);
        float sumWidth = 0;
        for (int i = 0; i < strings.length; i++) {
            if (i % 2 == 0) {
                sumWidth += mHighLightBigPaint.measureText(strings[i]);
            } else {
                sumWidth += mHighLightSmallPaint.measureText(strings[i]);
            }
        }
        return sumWidth;
    }

    protected RectF drawHighLightValue2(Canvas canvas, String valueFormatterStr, float childCenter, RecyclerView parent) {
        float parentTop = parent.getPaddingTop();
        float contentRight = parent.getWidth() - parent.getPaddingRight();
        float contentLeft = parent.getPaddingLeft();

        String[] strings = valueFormatterStr.split(DefaultHighLightMarkValueFormatter.CONNECT_STR);
        float leftEdgeDistance = Math.abs(childCenter - contentLeft);
        float rightEdgeDistance = Math.abs(contentRight - childCenter);

        float startPadding = DisplayUtil.dip2px(16);
        float endPadding = startPadding;
        float rectBottom = parentTop - DisplayUtil.dip2px(10);
        float txtTopPadding = DisplayUtil.dip2px(3);
        float rectMarginTop=DisplayUtil.dip2px(6);
        float rectMarginBottom=DisplayUtil.dip2px(8);

        String valueStr = strings[0];
        String descStr = strings[1];

        //计算宽度
        float txtValueWidth = computeValueStrWidth(valueStr);
        float txtDescWidth = mHighLightSmallPaint.measureText(descStr);
        float txtWidth = Math.max(txtValueWidth, txtDescWidth)+ startPadding + endPadding;
        float edgeDistance = txtWidth / 2.0f;

        float rectFHeight = DisplayUtil.dip2px(59);
        float rectTop = rectBottom - rectFHeight;

        //绘制RectF
        RectF rectF = new RectF();
        mBarChartPaint.setColor(ColorUtil.getResourcesColor(R.color.chart_selected_prompt_bg_color));
        float radius = mBarChartAttrs.highLightRoundRectRadius;
        if (leftEdgeDistance <= edgeDistance) {//矩形框靠左对齐
            rectF.set(contentLeft, rectTop, contentLeft + txtWidth, rectBottom);
        } else if (rightEdgeDistance <= edgeDistance) {//矩形框靠右对齐
            rectF.set(contentRight - txtWidth, rectTop, contentRight, rectBottom);
        } else {//居中对齐。
            rectF.set(childCenter - edgeDistance, rectTop, childCenter + edgeDistance, rectBottom);
        }
        canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);

//        RectF bottomRectF = new RectF();
//        bottomRectF.set(rectF.left, rectF.bottom - rectBottomHeight , rectF.right, rectF.bottom);
//        RectF topRectF = new RectF();
//        topRectF.set(rectF.left, rectF.top, rectF.right, rectF.top + rectTopHeight);
//        RectF centerRectF = new RectF();
//        centerRectF.set(rectF.left, bottomRectF.top - rectBottomHeight,rectF.right,bottomRectF.top);
//
//        float baseLineYBottom = TextUtil.getTextBaseY(bottomRectF, mHighLightDescPaint);//基线中间点的y轴计算公式
//        float baseLineYTop = TextUtil.getTextBaseY(topRectF, mHighLightBigPaint);//基线中间点的y轴计算公式
//        float baseLineYCenter = TextUtil.getTextBaseY(centerRectF, mHighLightSmallPaint);//基线中间点的y轴计算公式
        float baseLineDescPaint = rectF.bottom - rectMarginBottom - 8;
        float baseLineBigPaint = baseLineDescPaint - TextUtil.getTxtHeight1(mHighLightDescPaint) - 8;
        float baseLineSmallPaint = baseLineBigPaint + 8;

        drawValueStr(canvas, valueStr, baseLineSmallPaint, baseLineBigPaint, rectF.left + startPadding);
        canvas.drawText(descStr, rectF.left + startPadding, baseLineDescPaint, mHighLightDescPaint);
        return rectF;
    }

    private void drawValueStr(Canvas canvas, String valueStr, float baseLineYCenter, float baseLineYTop, float valueStart){
        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_VALUE_STR);
        float currentValueEnd = valueStart;
        float txtWidth;
        for (int i = 0; i < strings.length; i++) {
            if (AppUtil.isRTLDirection()) {
                if (i % 2 == 0) {
                    txtWidth = mHighLightSmallPaint.measureText(strings[i]);
                    currentValueEnd += txtWidth + 6;
                    canvas.drawText(strings[i], currentValueEnd - txtWidth, baseLineYCenter - DisplayUtil.dip2px(2), mHighLightSmallPaint);
                } else {
                    txtWidth = mHighLightBigPaint.measureText(strings[i]);
                    currentValueEnd += txtWidth;
                    canvas.drawText(strings[i], currentValueEnd - txtWidth, baseLineYTop + DisplayUtil.dip2px(2), mHighLightBigPaint);
                }
            } else {
                if (i % 2 == 0) {
                    txtWidth = mHighLightBigPaint.measureText(strings[i]);
                    currentValueEnd += txtWidth;
                    canvas.drawText(strings[i], currentValueEnd - txtWidth, baseLineYTop + DisplayUtil.dip2px(2), mHighLightBigPaint);
                } else {
                    txtWidth = mHighLightSmallPaint.measureText(strings[i]);
                    currentValueEnd += txtWidth + 6;
                    canvas.drawText(strings[i], currentValueEnd - txtWidth, baseLineYCenter - DisplayUtil.dip2px(2), mHighLightSmallPaint);
                }
            }
        }
    }

    //绘制柱状图选中浮框
    protected RectF drawHighLightValue(Canvas canvas, String valueStr, float childCenter,
                                       RecyclerView parent, int barChartColor) {
        float parentTop = parent.getPaddingTop();
        float contentRight = parent.getWidth() - parent.getPaddingRight();
        float contentLeft = parent.getPaddingLeft();

        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_STR);
        float leftEdgeDistance = Math.abs(childCenter - contentLeft);
        float rightEdgeDistance = Math.abs(contentRight - childCenter);

        float startPadding = DisplayUtil.dip2px(10);
        float endPadding = DisplayUtil.dip2px(10);
        float centerPadding = DisplayUtil.dip2px(16);

        float rectBottom = parentTop;
        float txtTopPadding = DisplayUtil.dip2px(3);

        String startStr = AppUtil.isRTLDirection() ? strings[1] : strings[0];
        String endStr = AppUtil.isRTLDirection() ? strings[0] : strings[1];

        float txtStartWidth = mHighLightSmallPaint.measureText(startStr);
        float txtEndWidth = mHighLightSmallPaint.measureText(endStr);

        float rectFHeight = TextUtil.getTxtHeight1(mHighLightSmallPaint) + txtTopPadding * 2;
        float txtWidth = txtStartWidth + txtEndWidth + startPadding + endPadding + centerPadding;

        float edgeDistance = txtWidth / 2.0f;
        float rectTop = parentTop - rectFHeight;

        //绘制RectF
        RectF rectF = new RectF();
        mBarChartPaint.setColor(ColorUtil.getResourcesColor(R.color.black_5));

        float radius = mBarChartAttrs.highLightRoundRectRadius;
        if (leftEdgeDistance <= edgeDistance) {//矩形框靠左对齐
            rectF.set(contentLeft, rectTop, contentLeft + txtWidth, rectBottom);
        } else if (rightEdgeDistance <= edgeDistance) {//矩形框靠右对齐
            rectF.set(contentRight - txtWidth, rectTop, contentRight, rectBottom);
        } else {//居中对齐。
            rectF.set(childCenter - edgeDistance, rectTop, childCenter + edgeDistance, rectBottom);
        }
        canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);

        //绘文字
        float txtTop = rectTop + 3 * txtTopPadding;
        float leftStart = rectF.left + startPadding;
        float leftEnd = leftStart + txtStartWidth;
        RectF leftRectF = new RectF(leftStart, txtTop, leftEnd, txtTop + rectFHeight);
        mHighLightSmallPaint.setTextAlign(Paint.Align.LEFT);

        Paint.FontMetrics fontMetrics = mHighLightSmallPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (leftRectF.centerY() + (top + bottom) / 2);//基线中间点的y轴计算公式

        canvas.drawText(startStr, rectF.left + startPadding, baseLineY, mHighLightSmallPaint);

        float dividerLineStartX = leftEnd + centerPadding / 2.0f;
        float dividerLineStartY = rectTop + DisplayUtil.dip2px(6);
        float dividerLineEndX = dividerLineStartX;
        float dividerLineEndY = rectBottom - DisplayUtil.dip2px(6);
        float[] lines = new float[]{dividerLineStartX, dividerLineStartY, dividerLineEndX, dividerLineEndY};
        canvas.drawLines(lines, mHighLightSmallPaint);

        float rightStart = leftEnd + centerPadding;
        float rightEnd = rectF.right - endPadding;
        RectF rightRectF = new RectF(rightStart, txtTop, rightEnd, txtTop + rectFHeight);
        canvas.drawText(endStr, rightRectF.left, baseLineY, mHighLightSmallPaint);
        return rectF;
    }

//    protected void drawHighLightLine(Canvas canvas, float[] floats, int barChartColor) {
//        Paint.Style previous = mBarChartPaint.getStyle();
//        float strokeWidth = mBarChartPaint.getStrokeWidth();
//        int color = mBarChartPaint.getColor();
//        // set
//        mBarChartPaint.setStyle(Paint.Style.FILL);
//        mBarChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
//        mBarChartPaint.setColor(ColorUtil.getResourcesColor(R.color.black_10));
//        canvas.drawLines(floats, mBarChartPaint);
//        // restore
//        mBarChartPaint.setStyle(previous);
//        mBarChartPaint.setStrokeWidth(strokeWidth);
//        mBarChartPaint.setColor(color);
//    }


    protected boolean drawText(Canvas canvas, float parentLeft, float parentRight,
                             String valueStr, float childCenter, float txtY, Paint paint) {
//        Log.d("BarChartRender", " valueStr:" + valueStr);
        float widthText = paint.measureText(valueStr);
        float txtXLeft = getTxtX(childCenter, valueStr);
        float txtXRight = txtXLeft + widthText;

        int txtStart = 0;
        int txtEnd = valueStr.length();

        if (DecimalUtil.smallOrEquals(txtXRight, parentLeft)) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
            return true;
        } else if (txtXLeft < parentLeft && txtXRight > parentLeft) {//左边部分滑入的时候，处理柱状图、文字的显示
            int displaySize = (int) (valueStr.length() * (txtXRight - parentLeft) / widthText);//比如要显示  "123456"的末两位，需要从 length - displaySize的位置开始显示。
            txtStart = valueStr.length() - displaySize;
            txtXLeft = Math.max(txtXLeft, parentLeft);
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        } else if (DecimalUtil.bigOrEquals(txtXLeft, parentLeft) && DecimalUtil.smallOrEquals(txtXRight, parentRight)) {//中间的
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        } else if (txtXLeft <= parentRight && txtXRight > parentRight) {//右边部分滑出的时候，处理柱状图，文字的显示
            txtXLeft = getTxtX(childCenter, valueStr);
            txtEnd = (int) (valueStr.length() * (parentRight - txtXLeft) / widthText);
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        }
        return false;
    }

    //获取文字显示的起始 X 坐标
    private float getTxtX(float center, String valueStr) {
        float txtX = center - mTextPaint.measureText(valueStr) / 2;
        return txtX;
    }
    protected abstract <E extends RecyclerBarEntry> int getChartColor(E entry);
}
