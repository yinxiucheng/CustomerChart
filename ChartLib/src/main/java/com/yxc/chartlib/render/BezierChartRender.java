package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

//import com.xiaomi.fitness.chart.attrs.BezierChartAttrs;
//import com.xiaomi.fitness.chart.bezier.ControlPoint;
//import com.xiaomi.fitness.chart.component.BaseYAxis;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.attrs.BezierChartAttrs;
import com.yxc.chartlib.bezier.ControlPoint;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.utils.DisplayUtil;
import com.yxc.chartlib.utils.DecimalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/14
 */
final public class BezierChartRender {
    private BezierChartAttrs mBarChartAttrs;
    private Paint mBarChartPaint;
    private Paint mTextPaint;
    private Paint mBezierFillPaint;
    private ValueFormatter mBarChartValueFormatter;

    public void setBarChartValueFormatter(ValueFormatter mBarChartValueFormatter) {
        this.mBarChartValueFormatter = mBarChartValueFormatter;
    }

    public BezierChartRender(BezierChartAttrs barChartAttrs,
                             ValueFormatter barChartValueFormatter) {
        this.mBarChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
        initBezierFillPaint();
        this.mBarChartValueFormatter = barChartValueFormatter;
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mBarChartAttrs.txtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.STROKE);
        mBarChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mBarChartPaint.setColor(mBarChartAttrs.chartColor);
    }

    private void initBezierFillPaint() {
        mBezierFillPaint = new Paint();
        mBezierFillPaint.reset();
        mBezierFillPaint.setAntiAlias(true);
        mBezierFillPaint.setStyle(Paint.Style.FILL);
        mBezierFillPaint.setColor(mBarChartAttrs.chartColor);
        mBezierFillPaint.setAlpha(mBarChartAttrs.fillAlpha);
    }

    //?????????????????????value??????
     public <T extends BaseYAxis>void drawBarChartValue(Canvas canvas, @NonNull RecyclerView parent, T yAxis) {
        if (mBarChartAttrs.enableCharValueDisplay) {
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            float parentRight = parent.getWidth() - parent.getPaddingRight();
            float parentLeft = parent.getPaddingLeft();
            float realYAxisLabelHeight = bottom - mBarChartAttrs.contentPaddingTop - parent.getPaddingTop();
            int childCount = parent.getChildCount();

            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                RecyclerBarEntry barEntry = (RecyclerBarEntry) child.getTag();
                float width = child.getWidth();
                float childCenter = child.getLeft() + width / 2;
                int height = (int) (barEntry.getY() / yAxis.getAxisMaximum() * realYAxisLabelHeight);
                float top = bottom - height;
                String valueStr = mBarChartValueFormatter.getBarLabel(barEntry);
                float txtY = top - DisplayUtil.dip2px(3);
                drawText(canvas, parentLeft, parentRight, valueStr, childCenter, txtY, mTextPaint);
            }
        }
    }


    private void drawText(Canvas canvas, float parentLeft, float parentRight,
                          String valueStr, float childCenter, float txtY, Paint paint) {
//        Log.d("BarChartRender", " valueStr:" + valueStr);
        float widthText = paint.measureText(valueStr);
        float txtXLeft = getTxtX(childCenter, valueStr);
        float txtXRight = txtXLeft + widthText;

        int txtStart = 0;
        int txtEnd = valueStr.length();

        if (DecimalUtil.smallOrEquals(txtXRight, parentLeft)) {//continue ??????????????????end == parentLeft ?????????????????????????????????????????????
        } else if (txtXLeft < parentLeft && txtXRight > parentLeft) {//???????????????????????????????????????????????????????????????
            int displaySize = (int) (valueStr.length() * (txtXRight - parentLeft) / widthText);//???????????????  "123456"???????????????????????? length - displaySize????????????????????????
            txtStart = valueStr.length() - displaySize;
            txtXLeft = Math.max(txtXLeft, parentLeft);
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        } else if (DecimalUtil.bigOrEquals(txtXLeft, parentLeft) && DecimalUtil.smallOrEquals(txtXRight, parentRight)) {//?????????
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        } else if (txtXLeft <= parentRight && txtXRight > parentRight) {//???????????????????????????????????????????????????????????????
            txtXLeft = getTxtX(childCenter, valueStr);
            txtEnd = (int) (valueStr.length() * (parentRight - txtXLeft) / widthText);
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        }
    }

    //??????????????????????????? X ??????
    private float getTxtX(float center, String valueStr) {
        return center - mTextPaint.measureText(valueStr) / 2;
    }

    private <T extends BaseYAxis>PointF getChildPointF(RecyclerView parent, View child, T mYAxis, BezierChartAttrs barChartAttrs) {
        RecyclerBarEntry barEntry = (RecyclerBarEntry) child.getTag();
        RectF rectF = ChartComputeUtil.getChartRectF(child, parent, mYAxis, barChartAttrs, barEntry);
        float pointX = (rectF.left + rectF.right) / 2;
        float pointY = rectF.top;
        PointF pointF = new PointF(pointX, pointY);
        return pointF;
    }

//    LinearGradient mLinearGradient;
    public <T extends BaseYAxis>void drawBezierChart(Canvas canvas, RecyclerView parent, T mYAxis) {
        float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        final int childCount = parent.getChildCount();
        List<PointF> originPointFList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            PointF pointF = getChildPointF(parent, child, mYAxis, mBarChartAttrs);
            originPointFList.add(i, pointF);
        }

        List<ControlPoint> controlList = ControlPoint.getControlPointList(originPointFList, mBarChartAttrs.bezierIntensity);
        Path cubicPath = new Path();
        Path cubicFillPath = new Path();
        //??????????????????????????????
        for (int i = 0; i < controlList.size(); i++) {
            if (i == 0) {
                cubicPath.moveTo(originPointFList.get(i).x, originPointFList.get(i).y);
            }
            //????????????????????????
            cubicPath.cubicTo(
                    controlList.get(i).getConPoint1().x, controlList.get(i).getConPoint1().y,
                    controlList.get(i).getConPoint2().x, controlList.get(i).getConPoint2().y,
                    originPointFList.get(i + 1).x, originPointFList.get(i + 1).y);
        }
        if (mBarChartAttrs.enableBezierLineFill) {
            cubicFillPath.reset();
            cubicFillPath.moveTo(originPointFList.get(0).x, originPointFList.get(0).y);
            cubicFillPath.addPath(cubicPath);
            // create a new path, this is bad for performance
            drawCubicFill(parent, canvas,  originPointFList, cubicFillPath, bottom);
        }
        canvas.drawPath(cubicPath, mBarChartPaint);
        canvas.save();
    }

    private void drawCubicFill(RecyclerView parent, Canvas c, List<PointF> pointFList, Path spline, float bottom) {
        spline.lineTo(pointFList.get(pointFList.size() - 1).x, bottom);
        spline.lineTo(pointFList.get(0).x, bottom);
        spline.close();
        drawFilledPath(parent, c, spline);
    }

    /**
     * Draws the provided path in filled mode with the provided color and alpha.
     * Special thanks to Angelo Suzuki (https://github.com/tinsukE) for this.

     */
    private void drawFilledPath(RecyclerView parent, Canvas canvas, Path filledPath) {
        float yBottom = parent.getBottom() - parent.getPaddingBottom();
        float yTop = parent.getTop() + parent.getPaddingTop();
        Paint.Style previous = mBezierFillPaint.getStyle();
        mBezierFillPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(filledPath, mBezierFillPaint);
        LinearGradient mLinearGradient = new LinearGradient(
                0,
                yBottom,
                0,
                yTop,
                new int[]{mBarChartAttrs.lineShaderBeginColor, mBarChartAttrs.lineShaderEndColor},
                null,
                Shader.TileMode.CLAMP
        );
        mBezierFillPaint.setShader(mLinearGradient);
        mBezierFillPaint.setStyle(previous);
    }

}
