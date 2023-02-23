package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author yxc
 * @since  2019/4/21
 */
public class LineChartDrawable extends Drawable {

    Paint mPaint;
    Path path;

    public LineChartDrawable(Paint paint, Path path){
        this.mPaint = paint;
        this.path = path;
        this.setAlpha(0x66);
    }


    @Override
    public void draw( @NonNull Canvas canvas) {
        canvas.drawPath(path, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


}
