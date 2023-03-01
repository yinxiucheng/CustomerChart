package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.utils.AppUtil;
import com.yxc.chartlib.utils.DisplayUtil;


/**
 * @author yxc
 * @date 2019/4/14
 */
final public class BarBoardRender<V extends BaseChartAttrs> {

    V mBarChartAttrs;
    Paint mBarBorderPaint;
    Paint mTopBarBorderPaint;

    public BarBoardRender(V attrs) {
        this.mBarChartAttrs = attrs;
        initBarBorderPaint();
        initTopBarBorderPaint();
    }

    private void initBarBorderPaint() {
        mBarBorderPaint = new Paint();
        mBarBorderPaint.reset();
        mBarBorderPaint.setAntiAlias(true);
        mBarBorderPaint.setStyle(Paint.Style.STROKE);
        mBarBorderPaint.setStrokeWidth(1f);
        mBarBorderPaint.setColor(mBarChartAttrs.barBorderColor);
    }

    private void initTopBarBorderPaint() {
        mTopBarBorderPaint = new Paint();
        mTopBarBorderPaint.reset();
        mTopBarBorderPaint.setAntiAlias(true);
        mTopBarBorderPaint.setStyle(Paint.Style.STROKE);
        mTopBarBorderPaint.setStrokeWidth(1f);
        mTopBarBorderPaint.setColor(mBarChartAttrs.barBorderColor);
    }

    final public void drawBarBorder(@NonNull Canvas canvas, @NonNull RecyclerView parent) {
        if (mBarChartAttrs.enableBarBorder) {
            float top = parent.getPaddingTop();
            float start = AppUtil.isRTLDirection() ? parent.getWidth() - parent.getPaddingRight() : parent.getPaddingLeft();
            float end = AppUtil.isRTLDirection() ? parent.getPaddingLeft() : parent.getWidth() - parent.getPaddingRight();
            //底部有0的刻度是不是不用画，就画折线了。
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            Path startPath = new Path();
            startPath.moveTo(start, bottom);
            startPath.lineTo(start, top);
            canvas.drawPath(startPath, mBarBorderPaint);

            Path endPath = new Path();
            endPath.moveTo(end, bottom);
            endPath.lineTo(end, top);
            canvas.drawPath(endPath, mBarBorderPaint);

            Path topPath = new Path();
            topPath.moveTo(start, top);
            topPath.lineTo(end, top);
            if (mBarChartAttrs.enableYAxisLineDash){
                mTopBarBorderPaint.setPathEffect(new DashPathEffect(new float[] { DisplayUtil.dip2px(4), DisplayUtil.dip2px(1.5f) }, 0));
            }
            canvas.drawPath(topPath, mTopBarBorderPaint);

//            Path path
//
//            path.lineTo(end, top);
//            path.lineTo(end, bottom);

//            if (mBarChartAttrs.enableStartYAxisLabel){
//                float lineStart = start;
//                float LineEnd = AppUtil.isRTLDirection() ? parent.getRight() : parent.getLeft();
//                Path path1 = new Path();
//                path1.moveTo(lineStart, top);
//                path1.lineTo(LineEnd, top);
//                canvas.drawPath(path1, mBarBorderPaint);
//            }
//
//            if (mBarChartAttrs.enableEndYAxisLabel){
//                float lineStart = AppUtil.isRTLDirection() ? parent.getLeft() : parent.getRight();
//                float lineEnd = end;
//                Path path2 = new Path();
//                path2.moveTo(lineStart, top);
//                path2.lineTo(lineEnd, top);
//                canvas.drawPath(path2, mBarBorderPaint);
//            }
        }
    }

    final public void drawBarBorder1(@NonNull Canvas canvas, @NonNull RecyclerView parent) {
        if (mBarChartAttrs.enableBarBorder) {
            float top = parent.getPaddingTop();
            float start = AppUtil.isRTLDirection() ? parent.getRight() - DisplayUtil.dip2px(40) : parent.getPaddingLeft();
            float end = AppUtil.isRTLDirection() ? parent.getPaddingLeft() : parent.getRight() - DisplayUtil.dip2px(40);
            //底部有0的刻度是不是不用画，就画折线了。
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            Path path = new Path();
            path.moveTo(end, top);
            path.lineTo(end, bottom);
            canvas.drawPath(path, mBarBorderPaint);
        }
    }

    public void drawBarBorder3(@NonNull Canvas canvas, @NonNull RecyclerView parent) {
        if (mBarChartAttrs.enableBarBorder) {
            float top = parent.getPaddingTop();
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            float start = parent.getLeft();
            float end = parent.getRight();
            //底部有0的刻度是不是不用画，就画折线了。
            Path startPath = new Path();
            startPath.moveTo(start, bottom);
            startPath.lineTo(start, top);
            canvas.drawPath(startPath, mBarBorderPaint);
            Path endPath = new Path();
            endPath.moveTo(end, bottom);
            endPath.lineTo(end, top);
            canvas.drawPath(endPath, mBarBorderPaint);

            Path topPath = new Path();
            topPath.moveTo(start, top);
            topPath.lineTo(end, top);
            if (mBarChartAttrs.enableYAxisLineDash){
                mTopBarBorderPaint.setPathEffect(new DashPathEffect(new float[] { DisplayUtil.dip2px(4), DisplayUtil.dip2px(1.5f) }, 0));
            }
            canvas.drawPath(topPath, mTopBarBorderPaint);
        }
    }
}
