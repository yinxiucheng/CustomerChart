package com.yxc.chartlib.mpchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.common.utils.DisplayUtil;
import com.xiaomi.fitness.common.utils.TextUtil;


/**
 * @author yxc
 * @date 2019-10-11
 */
public class XAxisView extends View {
    String[] strArray = new String[]{"00:00", "12:00", "24:00"};
    Paint mTextPaint;
    int txtColor;
    float txtSize;

    public XAxisView(Context context) {
        super(context);
        initPaint();
    }

    public XAxisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public XAxisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XAxisView);
        txtColor = ta.getColor(R.styleable.XAxisView_XAxisTxtColor, getResources().getColor(R.color.text_color_20));
        txtSize = ta.getDimension(R.styleable.XAxisView_axisTxtSize, DisplayUtil.sp2px(8));
        initPaint();
        ta.recycle();
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(txtColor);
        mTextPaint.setTextSize(txtSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textWidth = mTextPaint.measureText(strArray[0]);
        float parentWidth = getWidth();
        float spaceWidth = (parentWidth - textWidth * strArray.length) / (strArray.length - 1);

        float parentBottom = getBottom();

        float height = TextUtil.getTxtHeight1(mTextPaint);
        for (int i = 0; i < strArray.length; i++) {
            float rectFLeft = getLeft() + i * (spaceWidth + textWidth);
            float rectFRight = rectFLeft + textWidth;
            RectF rect = new RectF(rectFLeft, getTop(), rectFRight, getBottom());
            float baseLineY = TextUtil.getTextBaseY(rect, mTextPaint);
            canvas.drawText(strArray[i], rect.left, height / 2, mTextPaint);
        }
    }

    public void setValues(String[] strArray) {
        this.strArray = strArray;
        invalidate();
    }

}
