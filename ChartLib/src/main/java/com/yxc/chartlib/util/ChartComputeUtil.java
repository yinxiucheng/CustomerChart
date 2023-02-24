package com.yxc.chartlib.util;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.xiaomi.fitness.chart.attrs.BaseChartAttrs;
//import com.xiaomi.fitness.chart.barchart.BaseBarChartAdapter;
//import com.xiaomi.fitness.chart.component.BaseYAxis;
//import com.xiaomi.fitness.chart.component.DistanceCompare;
//import com.xiaomi.fitness.chart.entrys.MultiBarEntry;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.barchart.BaseBarChartAdapter;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.component.DistanceCompare;
import com.yxc.chartlib.entrys.MultiBarEntry;
import com.yxc.chartlib.entrys.YAxisMaxEntries;
import com.yxc.chartlib.entrys.model.SleepItemTime;
import com.yxc.chartlib.transform.RecyclerTransform;
import com.yxc.chartlib.utils.AppUtil;
import com.yxc.chartlib.utils.ArrayUtils;
import com.yxc.mylibrary.TimeDateUtil;
//import com.xiaomi.fitness.chart.entrys.YAxisMaxEntries;
//import com.xiaomi.fitness.chart.entrys.model.SleepItemTime;
//import com.xiaomi.fitness.chart.transform.RecyclerTransform;
//import com.xiaomi.fitness.common.utils.AppUtil;
//import com.xiaomi.fitness.common.utils.ArrayUtils;
//import com.xiaomi.fitness.common.utils.TimeDateUtil;

//import org.joda.time.LocalDate;

//import java.time.LocalDate;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/12
 */
public class ChartComputeUtil {

    private static final int VIEW_DAY = 0;
    private static final int VIEW_WEEK = 1;
    private static final int VIEW_MONTH = 2;
    public static final int VIEW_YEAR = 3;

    //位置进行微调
    public static <T extends RecyclerBarEntry> YAxisMaxEntries getVisibleEntries(RecyclerView recyclerView, int displayNumbers) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        List<T> mEntries = adapter.getEntries();

        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();

        if (lastVisibleItemPosition == RecyclerView.NO_POSITION) {
            lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        }

        if (firstVisibleItemPosition == RecyclerView.NO_POSITION) {
            firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        }

        List<T> visibleEntries = new ArrayList<>();
        if (firstVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition != RecyclerView.NO_POSITION) {
//            visibleEntries = mEntries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
            if (lastVisibleItemPosition < mEntries.size()) {
                visibleEntries = mEntries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
            }
        }
        float yAxisMaximum = 0;
        float yAxisMinimum = 0;
        if (visibleEntries.size() > 0) {
            yAxisMaximum = ChartUtil.getTheMaxNumber(visibleEntries);
            yAxisMinimum = ChartUtil.getTheMinNumber(visibleEntries);
        }
        return new YAxisMaxEntries(yAxisMaximum, yAxisMinimum, visibleEntries);
    }

    //对于月进行清洗数据
    public static <T extends RecyclerBarEntry> List<T> getMonthCleanEntries(List<T> visibleEntries) {
        int middle = visibleEntries.size() / 2;
        LocalDate middleDate = visibleEntries.get(middle).localDate;

        List<T> entryList = new ArrayList<>();
        for (int i = 0; i < visibleEntries.size(); i++) {
            T barEntry = visibleEntries.get(i);
            if (TimeDateUtil.isSameMonth(middleDate, barEntry.localDate)) {
                entryList.add(barEntry);
            }
        }
        return entryList;
    }

    //compute the scrollByDx, the left is large position, right is small position.
    public static <T extends RecyclerBarEntry> int computeScrollByXOffset(RecyclerView recyclerView, int displayNumbers, int type) {
        DistanceCompare distanceCompare = findDisplayFirstTypePosition(recyclerView, displayNumbers);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        if (null == adapter) {
            return 0;
        }
        List<T> entries = adapter.getEntries();
        int positionCompare = distanceCompare.position;
//        T entry = entries.get(positionCompare);

        View compareView = manager.findViewByPosition(positionCompare);
        if (null == compareView) {
            return 0;
        }
        int compareViewRight = compareView.getRight();
        int compareViewLeft = compareView.getLeft();

        int childWidth = compareView.getWidth();
        int parentLeft = recyclerView.getPaddingLeft();
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();

        int scrollByXOffset;
        if (distanceCompare.isNearLeft()) {//靠近左边，content左移，recyclerView右移，取正。
            //情况 1.
            int distance = AppUtil.isRTLDirection() ? compareViewLeft - parentLeft : compareViewRight - parentLeft;//原始调整距离
//            Log.d("ChartComputeUtil", "NearLeft:" + TimeDateUtil.getDateYYYYMMDDHHmmLocalFormat(entry.timestamp));
            if (positionCompare < displayNumbers + 1) {//防止 positionCompare过大，计算firstViewRight时，int越界
                int firstViewRight = compareViewRight + positionCompare * childWidth;
                int distanceRightBoundary = Math.abs(firstViewRight - parentRight);//右边界
                if (distanceRightBoundary < distance) { //content左移不够，顶到头，用 distanceRightBoundary
                    distance = distanceRightBoundary;
                }
            }
            scrollByXOffset = distance;
        } else {//靠近右边，content右移，recyclerView左移，取负。
            int distance = AppUtil.isRTLDirection() ? parentRight - compareViewLeft : parentRight - compareViewRight;//原始调整距离
//            Log.d("ChartComputeUtil", "NearRight:" + TimeDateUtil.getDateYYYYMMDDHHmmLocalFormat(entry.timestamp));
            if (entries.size() - positionCompare < displayNumbers) {
                //这个值会为 负的。
                int lastViewLeft = compareViewLeft - (entries.size() - 1 - positionCompare) * childWidth;
                int distanceLeftBoundary = Math.abs(parentLeft - lastViewLeft);//右边 - 左边，因为 lastViewLeft是负值，实际上是两值相加。
                if (distanceLeftBoundary < distance) {//content右移不够，顶到头，distanceLeftBoundary
                    distance = distanceLeftBoundary;
                }
            }
            //记得取负， scrollBy的话f
            scrollByXOffset = distance - 2 * distance;
        }
        return scrollByXOffset;
    }

    //find the largest divider position ( ItemDecoration ).
    private static <T extends RecyclerBarEntry> DistanceCompare findDisplayFirstTypePosition(RecyclerView recyclerView, int displayNumbers) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        if (null == manager || null == adapter) {
            return distanceCompare;
        }
        List<T> entries = adapter.getEntries();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        int position = firstVisibleItemPosition; //从右边的第一个View开始找
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
        int parentLeft = recyclerView.getPaddingLeft();

        for (int i = 0; i < displayNumbers; i++) {
            if (i > 0) {
                position++;
            }
            if (position >= 0 && position < entries.size()) {
                T barEntry = entries.get(position);
                if (barEntry.type == RecyclerBarEntry.TYPE_XAXIS_FIRST || barEntry.type == RecyclerBarEntry.TYPE_XAXIS_SPECIAL) {
                    distanceCompare.position = position;
                    View positionView = manager.findViewByPosition(position);
                    if (null != positionView) {
                        int viewLeft = positionView.getLeft();
                        int viewRight = positionView.getRight();
                        distanceCompare.distanceRight = parentRight - viewRight;
                        distanceCompare.distanceLeft = viewLeft - parentLeft;
                    }
                    distanceCompare.setBarEntry(barEntry);
                    break;
                }
            }
        }
        return distanceCompare;
    }


    private static int getNumbersUnitType(RecyclerBarEntry currentBarEntry, int type) {
        if (type == VIEW_DAY) {
            return TimeDateUtil.NUM_HOUR_OF_DAY;
        } else if (type == VIEW_WEEK) {
            return TimeDateUtil.NUM_DAY_OF_WEEK;
        } else if (type == VIEW_MONTH) {
            LocalDate localDate = currentBarEntry.localDate;
            LocalDate lastMonthEndLocalDate = TimeDateUtil.getFirstDayOfMonth(localDate).minusDays(1);//上个月末的最后一天
            int distance = TimeDateUtil.getIntervalDay(lastMonthEndLocalDate, localDate);
            return distance;
        } else if (type == VIEW_YEAR) {
            return TimeDateUtil.NUM_MONTH_OF_YEAR;
        }
        return TimeDateUtil.NUM_HOUR_OF_DAY;
    }


    /**
     * @return the scrollToPosition Just let this position item display,
     * but don't consume the location in the edge of screen. so Deprecated and
     * use computeScrollByXOffset replace
     */
    @Deprecated
    public static <T extends RecyclerBarEntry> DistanceCompare findScrollToPosition(RecyclerView recyclerView, int displayNumbers, int type) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        List<T> entries = adapter.getEntries();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        int position = firstVisibleItemPosition; //从右边的第一个View开始找
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
        int parentLeft = recyclerView.getPaddingLeft();
        DistanceCompare<T> distanceCompare = new DistanceCompare(0, 0);
        for (int i = 0; i < displayNumbers; i++) {
            if (i > 0) {
                position++;
            }
            if (position >= 0 && position < entries.size()) {
                T barEntry = entries.get(position);
                if (barEntry.type == RecyclerBarEntry.TYPE_XAXIS_FIRST || barEntry.type == RecyclerBarEntry.TYPE_XAXIS_SPECIAL) {
                    distanceCompare.position = position;

                    //这里最好不要去找这个view 容易 crash
                    View positionView = manager.findViewByPosition(position);
                    int viewLeft = positionView.getLeft();
                    distanceCompare.distanceRight = parentRight - viewLeft;
                    distanceCompare.distanceLeft = viewLeft - parentLeft;
                    distanceCompare.setBarEntry(barEntry);

                    if (distanceCompare.isNearLeft()) {//靠近左边回到上一个月。
                        int lastPosition = distanceCompare.position - getNumbersUnitType(distanceCompare.barEntry, type);
                        Log.d("ReLocation", "lastPosition:" + lastPosition + " entries' size" + entries.size());
                        if (lastPosition > 0) {
                            distanceCompare.position = lastPosition;
                            distanceCompare.barEntry = entries.get(lastPosition);
                        } else {
                            distanceCompare.position = 0;
                            distanceCompare.barEntry = entries.get(0);
                        }
                        distanceCompare.position = lastPosition;
                    } else {//靠近右边，直接返回当月的。

                    }
                    break;
                }
            }
        }
        return distanceCompare;
    }


    //获取柱状体 RectF
    public static <T extends RecyclerBarEntry, V extends BaseYAxis> RectF getChartRectF(View child, final RecyclerView parent, V mYAxis,
                                                                                        BaseChartAttrs barChartAttrs, T barEntry) {
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - barChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - barChartAttrs.contentPaddingTop;
        float contentTop = parent.getPaddingTop() + barChartAttrs.contentPaddingTop;

        float width = child.getWidth();
        float barSpaceWidth = width * barChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;

        float height = barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight;

        if (barChartAttrs.yAxisReverse && barEntry.getY() > 0) {//这里因为line， bezier图用到这个方法来确定 Chart图的位置。
            float valueTemp = mYAxis.getAxisMaximum() - barEntry.getY();
            height = valueTemp / mYAxis.getAxisMaximum() * realYAxisLabelHeight;
        }

        final float top = Math.max(contentBottom - height, contentTop);
        rectF.set(left, top, right, contentBottom);
        return rectF;
    }


    //获取柱状体 RectF 背景
    public static <T extends RecyclerBarEntry, V extends BaseYAxis> RectF getBarChartRectFBg(View child, final RecyclerView parent, BaseChartAttrs barChartAttrs) {
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - barChartAttrs.contentPaddingBottom;
        float contentTop = parent.getPaddingTop() + barChartAttrs.contentPaddingTop;

        float width = child.getWidth();
        float barSpaceWidth = width * barChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        rectF.set(left, contentTop, right, contentBottom);
        return rectF;
    }

    //获取柱状体 RectF，不支持反向
    public static <T extends RecyclerBarEntry, V extends BaseYAxis> RectF getBarChartRectF(View child, final RecyclerView parent, V mYAxis,
                                                                                           BaseChartAttrs barChartAttrs, T barEntry) {
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - barChartAttrs.contentPaddingBottom;
        float contentTop = parent.getPaddingTop() + barChartAttrs.contentPaddingTop;

        float pxPerValue = RecyclerTransform.pxPerValue(parent, mYAxis, barChartAttrs);
        float width = child.getWidth();
        float barSpaceWidth = width * barChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        float barChartMaxWidth = barChartAttrs.barChartMaxWidth;
        if (barChartMaxWidth > 0 && barChartWidth > barChartMaxWidth) {
            barChartWidth = barChartMaxWidth;
            barSpaceWidth = width - barChartWidth;
        }
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;

        float aAxisMaximum = mYAxis.getAxisMaximum();
        float aAxisMinimum = mYAxis.getAxisMinimum();
        float value = barEntry.getY();

        if (aAxisMinimum >= 0) {//a, /先不考虑 reverse
            float height = (value - aAxisMinimum) * pxPerValue;
            float top = Math.max(contentBottom - height, contentTop);
            rectF.set(left, top, right, contentBottom);
        } else {//
            float realPositiveHeight = aAxisMaximum * pxPerValue;
            float aAxisMinimumPositive = Math.abs(aAxisMaximum);
            float realNegativeHeight = aAxisMinimumPositive * pxPerValue;
            float zeroPosition = contentBottom - realNegativeHeight;// YAxis的 0 坐标。
            if (value >= 0) {
                float height = value / aAxisMaximum * realPositiveHeight;
                float top = Math.max(zeroPosition - height, contentTop);
                rectF.set(left, top, right, zeroPosition);
            } else {
                float height = value / aAxisMinimum * realNegativeHeight;
                float bottom = Math.min(zeroPosition + height, contentBottom);
                rectF.set(left, zeroPosition, right, bottom);
            }
        }
        return rectF;
    }


    public static <T extends BaseYAxis> List<RectFWrapper> getSleepChartRectFList(View child, final RecyclerView parent, T yAxis,
                                                                                  BaseChartAttrs mBarChartAttrs, List<SleepItemTime> sleepItemTimeList) {
        List<RectFWrapper> rectFList = new ArrayList<>();

        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - mBarChartAttrs.contentPaddingTop;
        float width = child.getWidth();
        float barSpaceWidth = width * mBarChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        float barChartMaxWidth = mBarChartAttrs.barChartMaxWidth;
        if (barChartMaxWidth > 0 && barChartWidth > barChartMaxWidth) {
            barChartWidth = barChartMaxWidth;
            barSpaceWidth = width - barChartWidth;
        }
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        float parentTop = parent.getPaddingTop();

        float rectFBottom = contentBottom;

        for (int i = 0; i < sleepItemTimeList.size(); i++) {
            SleepItemTime sleepItemTime = sleepItemTimeList.get(i);
            float yRectFTop = sleepItemTime.durationTime / yAxis.getAxisMaximum() * realYAxisLabelHeight;
            RectFWrapper rectF = createRectFWrapper(left, rectFBottom, right, yRectFTop, parentTop);
            rectF.isTop = sleepItemTime.isTop;
            rectF.color=   SleepItemTime.getSleepTypeColor(parent.getContext(), sleepItemTime.sleepType);
            rectFList.add(0, rectF);
            rectFBottom = rectF.top;
        }
        return rectFList;
    }

    public static <T extends BaseYAxis> List<RectFWrapper> getMultiChartRectFList(View child, final RecyclerView parent, T yAxis,
                                                                                  BaseChartAttrs mBarChartAttrs, List<MultiBarEntry.MultiItem> multiItems) {
        List<RectFWrapper> rectFList = new ArrayList<>();

        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - mBarChartAttrs.contentPaddingTop;
        float width = child.getWidth();
        float barSpaceWidth = width * mBarChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        float barChartMaxWidth = mBarChartAttrs.barChartMaxWidth;
        if (barChartMaxWidth > 0 && barChartWidth > barChartMaxWidth) {
            barChartWidth = barChartMaxWidth;
            barSpaceWidth = width - barChartWidth;
        }
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        float parentTop = parent.getPaddingTop();
        float rectFBottom = contentBottom;

        if (!ArrayUtils.isEmpty(multiItems)) {
            for (MultiBarEntry.MultiItem multiItem : multiItems) {
                float yRectFTop = multiItem.value/ yAxis.getAxisMaximum() * realYAxisLabelHeight;
                RectFWrapper rectF = createRectFWrapper(left, rectFBottom, right, yRectFTop, parentTop);
                rectF.isTop = multiItem.topStatus;
                rectF.color = multiItem.color;
                rectFList.add(rectF);
            }
        }
        return rectFList;
    }

    private static RectFWrapper createRectFWrapper(float left, float bottom, float right, float height, float parentTop) {
        RectFWrapper rectF = new RectFWrapper();
        rectF.set(left, bottom - height, right, bottom);
        final float top = Math.max(bottom - height, parentTop);
        rectF.set(left, top, right, bottom);
        return rectF;
    }


    private static RectF createRectF(float left, float bottom, float right, float height, float parentTop) {
        RectF rectF = new RectF();
        rectF.set(left, bottom - height, right, bottom);
        final float top = Math.max(bottom - height, parentTop);
        rectF.set(left, top, right, bottom);
        return rectF;
    }


    public static <T extends RecyclerBarEntry> float getYPosition(float yValue, RecyclerView parent, BaseYAxis mYAxis, BaseChartAttrs baseChartAttrs) {
        float contentBottom = parent.getHeight() - baseChartAttrs.contentPaddingBottom - parent.getPaddingBottom();
        float contentTop = baseChartAttrs.contentPaddingTop + parent.getPaddingTop();
        float contentHeight = contentBottom - contentTop;
        float height = yValue / mYAxis.getAxisMaximum() * contentHeight;
//        float top = Math.max(contentBottom - height, parent.getPaddingTop());
        return contentBottom - height;
    }

    public static Path createColorRectPath(PointF pointF1, PointF pointF2, float bottom) {
        Path path = new Path();
        path.moveTo(pointF1.x, pointF1.y);
        path.lineTo(pointF2.x, pointF2.y);
        path.lineTo(pointF2.x, bottom);
        path.lineTo(pointF1.x, bottom);
        path.close();
        return path;
    }

    public static PointF getInterceptPointF(PointF pointF1, PointF pointF2, float x) {
        float width = Math.abs(pointF1.x - pointF2.x);
        float height = Math.abs(pointF1.y - pointF2.y);
        float interceptWidth = Math.abs(pointF1.x - x);
        float interceptHeight = interceptWidth * 1.0f / width * height;
        float y;
        if (pointF2.y < pointF1.y) {
            y = pointF1.y - interceptHeight;
        } else {
            y = pointF1.y + interceptHeight;
        }
        return new PointF(x, y);
    }


}
