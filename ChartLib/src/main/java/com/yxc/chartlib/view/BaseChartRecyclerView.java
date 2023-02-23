package com.yxc.chartlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaomi.fitness.chart.attrs.BaseChartAttrs;
import com.xiaomi.fitness.chart.barchart.itemdecoration.BaseChartItemDecoration;
import com.xiaomi.fitness.chart.component.BaseYAxis;
import com.xiaomi.fitness.chart.component.VarietyMaxYAxis;
import com.xiaomi.fitness.chart.component.YAxis;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.DisplayUtil;


/**
 * @author yxc
 * @since  2019-05-10
 *
 */
public abstract class BaseChartRecyclerView<T extends BaseChartAttrs, I extends BaseChartItemDecoration> extends RecyclerView {

    public OnChartTouchListener onChartTouchListener;
    public T mAttrs;
    public I mItemDecoration;

    @Override
    public void addItemDecoration(@NonNull ItemDecoration decor) {
        super.addItemDecoration(decor);
        if (decor instanceof  BaseChartItemDecoration){
            mItemDecoration = (I) decor;
        }
    }

    public <Y extends BaseYAxis>void setYAxis(Y mYAxis){
        mItemDecoration.setYAxis(mYAxis);
    }

    public I getItemDecoration(){
        return mItemDecoration;
    }

    public BaseChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = getAttrs(context, attrs);
//        setRecyclerViewDefaultPadding();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (onChartTouchListener != null){
                onChartTouchListener.onChartGestureStart(e);
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP
                || e.getAction() == MotionEvent.ACTION_CANCEL) {
            if (onChartTouchListener != null) {
                onChartTouchListener.onChartGestureEnd(e);
            }
        } else if (e.getActionMasked() == MotionEvent.ACTION_MOVE){
            if (onChartTouchListener != null) {
                onChartTouchListener.onChartGestureMovingOn(e);
            }
        }
        return super.onTouchEvent(e);
    }

    public void setOnChartTouchListener(OnChartTouchListener onChartTouchListener) {
        this.onChartTouchListener = onChartTouchListener;
    }

    private void setRecyclerViewDefaultPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (mAttrs.enableStartYAxisLabel) {
            paddingRight = DisplayUtil.dip2px(36);
        }
        if (mAttrs.enableEndYAxisLabel) {
            paddingLeft = DisplayUtil.dip2px(36);
        }
        int paddingStart = AppUtil.isRTLDirection() ? paddingRight : paddingLeft;
        int paddingEnd = AppUtil.isRTLDirection() ? paddingLeft : paddingStart;
        setPadding(paddingStart, getPaddingTop(), paddingEnd, getPaddingBottom());
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= mAttrs.ratioVelocity;
        velocityY *= mAttrs.ratioVelocity;
        return super.fling(velocityX, velocityY);
    }



    public interface OnChartTouchListener {

        void onChartGestureStart(MotionEvent e);

        void onChartGestureEnd(MotionEvent e);

        void onChartGestureMovingOn(MotionEvent e);
    }

    protected abstract T getAttrs(@NonNull Context context, @Nullable AttributeSet attrs);

}
