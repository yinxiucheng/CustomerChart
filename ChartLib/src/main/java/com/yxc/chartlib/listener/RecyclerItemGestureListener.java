package com.yxc.chartlib.listener;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.barchart.SpeedRatioLayoutManager;
import com.yxc.chartlib.view.BaseChartRecyclerView;

/**
 * @author yxc
 * @since 2019/4/23
 */
public class RecyclerItemGestureListener<T extends RecyclerBarEntry> implements RecyclerView.OnItemTouchListener {

    private OnItemGestureListener mListener;

    public boolean isLongPressing;

    private T selectBarEntry;

    private GestureDetector mGestureDetector;

    private SpeedRatioLayoutManager layoutManager;

    private RecyclerView.Adapter mAdapter;

    private int lastPosition;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public RecyclerItemGestureListener(Context context, final BaseChartRecyclerView recyclerView, final OnItemGestureListener listener) {
        mListener = listener;
        layoutManager = (SpeedRatioLayoutManager) recyclerView.getLayoutManager();
        mAdapter = recyclerView.getAdapter();
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                View child = recyclerView.findChildViewUnder(x, y);
                float parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
                if (child != null && mListener != null) {
//                    Logger.d("BarChart Render click begin time:" + System.currentTimeMillis());
                    float reservedWidth = child.getWidth() / 2.0f;
                    if ((x < recyclerView.getPaddingLeft() || x > parentRight)) {
                        return false;
                    }
                    final int position = recyclerView.getChildAdapterPosition(child);
                    if (position != RecyclerView.NO_POSITION) {
                        T barEntry = (T) child.getTag();
                        Log.d("MPChart", "BarChart Render notify change + SingleTapUp"+barEntry);
                        if (barEntry.getY() <= 0) {
                            if (null != selectBarEntry) {
                                selectBarEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);
                            }
                            selectBarEntry = null;
                            Log.i("MPChart","BarChart Render notify change + SingleTapUp null "+barEntry);
                            mListener.onItemSelected(null, position);
                            if (null != mAdapter) {
//                                Logger.d("BarChart Render notify change time1111:" + System.currentTimeMillis());
                                mAdapter.notifyItemChanged(position, false);
                            }
                            return false;
                        } else if (!barEntry.equals(selectBarEntry)) {
                            //重置原来的SelectBarEntry
                            if (null != selectBarEntry) {
                                selectBarEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);
                            }
                            selectBarEntry = barEntry;
                            barEntry.setSelected(RecyclerBarEntry.TYPE_SINGLE_TAP_UP_SELECTED);
                        } else {
                            selectBarEntry = null;
                            barEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);//再次被点击
                        }
                        mListener.onItemSelected(barEntry, position);
                        mListener.onItemClick(child, position);
                        if (null != mAdapter) {
//                            Logger.d("BarChart Render notify change time2222:" + System.currentTimeMillis());
                            mAdapter.notifyItemChanged(position, false);
                        }
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                View child = recyclerView.findChildViewUnder(x, y);
                float parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();

                isLongPressing = true;
                if (null != layoutManager) {
                    layoutManager.setRatioSpeed(0);
                }
                if (child != null && mListener != null) {
                    float reservedWidth = child.getWidth() / 2.0f;
                    if (x < recyclerView.getPaddingLeft() || x > parentRight) {
                        return;
                    }
                    final int position = recyclerView.getChildAdapterPosition(child);
                    if (position != RecyclerView.NO_POSITION) {
                        T barEntry = (T) child.getTag();
                        Log.i("MPChart","BarChart Render notify change + LongPress" + barEntry);
                        if (barEntry.getY() <= 0) {
                            return;
                        } else if (!barEntry.equals(selectBarEntry)) {
                            //重置原来的SelectBarEntry
                            if (null != selectBarEntry) {
                                selectBarEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);
                            }
                            selectBarEntry = barEntry;
                            barEntry.setSelected(RecyclerBarEntry.TYPE_LONG_PRESS_SELECTED);
                        } else {
                            selectBarEntry = null;
                            barEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);//再次被点击
                        }

                        if (null != mAdapter) {
//                            Logger.d("BarChart Render long press time333:" + System.currentTimeMillis());
                            mAdapter.notifyItemChanged(position, 0);
                        }
                        mListener.onLongItemClick(child, position);
                        mListener.onItemSelected(barEntry, position);
                    }
                }
            }

            @Override
            public boolean onDown(MotionEvent e) {
//                Log.d("OnItemTouch", " onDown: " + System.currentTimeMillis() / 1000);
                return super.onDown(e);
            }
        });

        BaseChartRecyclerView.OnChartTouchListener onChartTouchListener = new BaseChartRecyclerView.OnChartTouchListener() {
            @Override
            public void onChartGestureStart(MotionEvent e) {
            }

            @Override
            public void onChartGestureEnd(MotionEvent e) {
//                Log.d("OnItemTouch", " onChartGestureEnd： " + System.currentTimeMillis() / 1000);
                isLongPressing = false;
                if (null != layoutManager) {//控制RecyclerView的滑动
                    layoutManager.resetRatioSpeed();
                }
            }

            @Override
            public void onChartGestureMovingOn(MotionEvent e) {
//                Log.d("OnItemTouch", " onChartGestureMovingOn： " + System.currentTimeMillis() / 1000);
                float x = e.getX();
                float y = e.getY();
                View child;
                child = recyclerView.findChildViewUnder(x, y);
                float parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
                if (child != null && isLongPressing) {
                    // longPress not action end, then moving the item is touched should be set selected
                    float reservedWidth = child.getWidth() / 2.0f;
                    //deal with the condition of the edge
                    if (x < recyclerView.getPaddingLeft()  || x > parentRight) {
                        return;
                    }
                    int position = recyclerView.getChildAdapterPosition(child);
                    lastPosition = position;
                    if (position != RecyclerView.NO_POSITION) {
                        T barEntry = (T) child.getTag();
                        if (barEntry.getY() <= 0) {
                            return;
                        }

                        if (!barEntry.equals(selectBarEntry)) {
                            if (selectBarEntry != null) {
                                selectBarEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);
                            }
                            selectBarEntry = barEntry;
                            barEntry.setSelected(RecyclerBarEntry.TYPE_LONG_PRESS_SELECTED);
                            if (null != mAdapter) {
//                                Logger.d("BarChart Render move on time4444:" + System.currentTimeMillis());
                                mAdapter.notifyItemChanged(position, 0);
                            }
                        }
                        mListener.onItemSelected(barEntry, position);
                    }
                } else {
                    //when is not longPress, normal condition reset the selected BarEntry
                    if (null != selectBarEntry && selectBarEntry.getSelected() == RecyclerBarEntry.TYPE_LONG_PRESS_SELECTED && isLongPressing) {
                        selectBarEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);
                        selectBarEntry = null;
                        if (child == null) {
                            mAdapter.notifyItemChanged(lastPosition, 0);
                        }
                        Log.i("MPChart","BarChart Render notify change lastPosition" + lastPosition + "child" + child + "isLongPressing" + isLongPressing);
//                        Log.d("OnItemTouch", " onItemSelected 释放 在 onChartGestureMovingOn： " + System.currentTimeMillis() / 1000);
                        mListener.onItemSelected(null, -1);
                    }

                }
            }
        };
        recyclerView.setOnChartTouchListener(onChartTouchListener);


        OnScrollListener scrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (null != mListener) {
                    mListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != mListener) {
                    if (null != selectBarEntry && selectBarEntry.getSelected() != RecyclerBarEntry.TYPE_UNSELECTED && !isLongPressing) {
                        if (Math.abs(dx) > 4) {
                            selectBarEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);
                            selectBarEntry = null;
//                            Log.d("OnItemTouch", " onItemSelected 释放 在 onScrolled： " + System.currentTimeMillis() / 1000);
                            mListener.onItemSelected(null, -1);
                        }
                    }
                    mListener.onScrolled(recyclerView, dx, dy);
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView parent, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public void resetSelectedBarEntry() {
        if (null != selectBarEntry) {
            selectBarEntry.setSelected(RecyclerBarEntry.TYPE_UNSELECTED);
            selectBarEntry = null;
        }
        isLongPressing = false;
        if (null != layoutManager) {//控制RecyclerView的滑动
            layoutManager.resetRatioSpeed();
        }
    }

}
