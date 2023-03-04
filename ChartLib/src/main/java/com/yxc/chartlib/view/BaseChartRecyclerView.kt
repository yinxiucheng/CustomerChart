package com.yxc.chartlib.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.barchart.itemdecoration.BaseChartItemDecoration;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.utils.AppUtil;
import com.yxc.chartlib.utils.DisplayUtil;

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

    public BaseChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = getAttrs(context, attrs);
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


    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= mAttrs.ratioVelocity;
        velocityY *= mAttrs.ratioVelocity;
        return super.fling(velocityX, velocityY);
    }

    public void addOnChartTouchListener(OnChartTouchListener onChartTouchListener) {
        this.onChartTouchListener = onChartTouchListener;
    }

    public interface OnChartTouchListener {

        void onChartGestureStart(MotionEvent e);

        void onChartGestureEnd(MotionEvent e);

        void onChartGestureMovingOn(MotionEvent e);
    }

    protected abstract T getAttrs(@NonNull Context context, @Nullable AttributeSet attrs);

}
