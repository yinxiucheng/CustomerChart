package com.yxc.chartlib.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yxc.chartlib.attrs.ChartAttrsUtil;
import com.yxc.chartlib.attrs.SleepChartAttrs;
import com.yxc.chartlib.barchart.itemdecoration.BarChartItemDecoration;
import com.yxc.chartlib.utils.DisplayUtil;


/**
 * @author yxc
 * @since 2019/4/26
 */
public class SleepChartRecyclerView extends BaseChartRecyclerView<SleepChartAttrs, BarChartItemDecoration> {

    private static final String TAG = "SleepChartRecyclerView";
    private int mLastMotionX;
    private int mLastMotionY;
    boolean isLongPress;
    boolean isMoved;
    Runnable mLongPressRunnable;
    private Handler handler;

    public SleepChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                isLongPress = true;
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                Log.d(TAG, "receive Message and isLongPress true!");
            }
        };

        handler = new Handler();
    }

    @Override
    protected SleepChartAttrs getAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        return ChartAttrsUtil.getSleepChartAttrs(context, attrs , false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX(0);
        int y = (int) event.getY(0);
        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                isLongPress = false;
                isMoved = false;
                mLastMotionX = x;
                mLastMotionY = y;
                Log.d(TAG, "Down And send Message!!");
                handler.postDelayed(mLongPressRunnable, 400);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE,isLongPress = " + isLongPress);
                if (!isLongPress) {
                    Log.d(TAG, "parent get Action!");
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (isMoved)
                    break;
                if (Math.abs(mLastMotionX - x) > DisplayUtil.dip2px(3)
                        || Math.abs(mLastMotionY - y) > DisplayUtil.dip2px(3)) {
                    isMoved = true;
                    handler.removeCallbacks(mLongPressRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_UP, isLongPress = " + isLongPress);
                isLongPress = false;
                isMoved = false;
                handler.removeCallbacks(mLongPressRunnable);
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
