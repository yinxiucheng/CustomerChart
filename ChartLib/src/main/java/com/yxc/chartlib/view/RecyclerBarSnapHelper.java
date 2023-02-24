package com.yxc.chartlib.view;

import android.graphics.PointF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.barchart.BaseBarChartAdapter;
import com.yxc.chartlib.component.DistanceCompare;

import java.util.List;


/**
 * @author yxc
 * @since 2019-11-12
 */
public class RecyclerBarSnapHelper extends SnapHelper {

    private static final float INVALID_DISTANCE = 1f;

    @Nullable
    private OrientationHelper mHorizontalHelper;

    private int displayNumbers;

    public RecyclerBarSnapHelper(int displayNumbers){
        this.displayNumbers = displayNumbers;
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull DistanceCompare distanceCompare) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = computeScrollByXOffset(mRecyclerView, distanceCompare);
        } else {
            out[0] = 0;
        }
        return out;
    }


    //compute the scrollByDx, the left is large position, right is small position.
    public int computeScrollByXOffset(RecyclerView recyclerView, DistanceCompare distanceCompare) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        if (null == adapter) {
            return 0;
        }
        List<RecyclerBarEntry> entries = adapter.getEntries();
        int positionCompare = distanceCompare.position;

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
            int distance = compareViewRight - parentLeft;//原始调整距离
            if (positionCompare < displayNumbers + 1) {//防止 positionCompare过大，计算firstViewRight时，int越界
                int firstViewRight = compareViewRight + positionCompare * childWidth;
                int distanceRightBoundary = Math.abs(firstViewRight - parentRight);//右边界
                if (distanceRightBoundary < distance) { //content左移不够，顶到头，用 distanceRightBoundary
                    distance = distanceRightBoundary;
                }
            }
            scrollByXOffset = distance;
        } else {//靠近右边，content右移，recyclerView左移，取负。
            int distance = parentRight - compareViewRight;//原始调整距离
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


    @Nullable
    @Override
    public DistanceCompare findSnapView(RecyclerView.LayoutManager layoutManager) {
        return findDisplayFirstTypePosition(mRecyclerView, displayNumbers);
    }

    //find the largest divider position ( ItemDecoration ).
    private DistanceCompare findDisplayFirstTypePosition(RecyclerView recyclerView, int displayNumbers) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        if (null == manager || null == adapter) {
            return distanceCompare;
        }
        List<RecyclerBarEntry> entries = adapter.getEntries();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        int position = firstVisibleItemPosition; //从右边的第一个View开始找
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
        int parentLeft = recyclerView.getPaddingLeft();

        for (int i = 0; i < displayNumbers; i++) {
            if (i > 0) {
                position++;
            }
            if (position >= 0 && position < entries.size()) {
                RecyclerBarEntry barEntry = entries.get(position);
                if (barEntry.type == RecyclerBarEntry.TYPE_XAXIS_FIRST || barEntry.type == RecyclerBarEntry.TYPE_XAXIS_SPECIAL) {
                    distanceCompare.position = position;
                    View positionView = manager.findViewByPosition(position);
                    if (null != positionView) {
                        int viewLeft = positionView.getLeft();
                        distanceCompare.distanceRight = parentRight - viewLeft;
                        distanceCompare.distanceLeft = viewLeft - parentLeft;
                        distanceCompare.snapView = positionView;
                    }
                    distanceCompare.setBarEntry(barEntry);
                    break;
                }
            }
        }
        return distanceCompare;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
                                      int velocityY) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return RecyclerView.NO_POSITION;
        }

        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION;
        }

        final DistanceCompare distanceCompare = findSnapView(layoutManager);
        final View currentView = distanceCompare.snapView;
        if (currentView == null) {
            return RecyclerView.NO_POSITION;
        }

        final int currentPosition = layoutManager.getPosition(currentView);
        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider =
                (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
        // deltaJumps sign comes from the velocity which may not match the order of children in
        // the LayoutManager. To overcome this, we ask for a vector from the LayoutManager to
        // get the direction.
        PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
        if (vectorForEnd == null) {
            // cannot get a vector for the given position.
            return RecyclerView.NO_POSITION;
        }

        int hDeltaJump;
        if (layoutManager.canScrollHorizontally()) {
            hDeltaJump = estimateNextPositionDiffForFling(layoutManager,
                    getHorizontalHelper(layoutManager), velocityX, 0);
            if (vectorForEnd.x < 0) {
                hDeltaJump = -hDeltaJump;
            }
        } else {
            hDeltaJump = 0;
        }
        int deltaJump = hDeltaJump;
        if (deltaJump == 0) {
            return RecyclerView.NO_POSITION;
        }

        int targetPos = currentPosition + deltaJump;
        if (targetPos < 0) {
            targetPos = 0;
        }
        if (targetPos >= itemCount) {
            targetPos = itemCount - 1;
        }
        return targetPos;
    }

    /**
     * Estimates a position to which SnapHelper will try to scroll to in response to a fling.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @param helper        The {@link OrientationHelper} that is created from the LayoutManager.
     * @param velocityX     The velocity on the x axis.
     * @param velocityY     The velocity on the y axis.
     * @return The diff between the target scroll position and the current position.
     */
    private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager layoutManager,
                                                 OrientationHelper helper, int velocityX, int velocityY) {
        int[] distances = calculateScrollDistance(velocityX, velocityY);
        float distancePerChild = computeDistancePerChild(layoutManager, helper);
        if (distancePerChild <= 0) {
            return 0;
        }
        int distance = Math.abs(distances[0]) > Math.abs(distances[1]) ? distances[0] : distances[1];
        return Math.round(distance / distancePerChild);
    }

    /**
     * Computes an average pixel value to pass a single child.
     * <p>
     * Returns a negative value if it cannot be calculated.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @param helper        The relevant {@link OrientationHelper} for the attached
     *                      {@link RecyclerView.LayoutManager}.
     * @return A float value that is the average number of pixels needed to scroll by one view in
     * the relevant direction.
     */
    private float computeDistancePerChild(RecyclerView.LayoutManager layoutManager,
                                          OrientationHelper helper) {
        View minPosView = null;
        View maxPosView = null;
        int minPos = Integer.MAX_VALUE;
        int maxPos = Integer.MIN_VALUE;
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return INVALID_DISTANCE;
        }

        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            final int pos = layoutManager.getPosition(child);
            if (pos == RecyclerView.NO_POSITION) {
                continue;
            }
            if (pos < minPos) {
                minPos = pos;
                minPosView = child;
            }
            if (pos > maxPos) {
                maxPos = pos;
                maxPosView = child;
            }
        }
        if (minPosView == null || maxPosView == null) {
            return INVALID_DISTANCE;
        }
        int start = Math.min(helper.getDecoratedStart(minPosView),
                helper.getDecoratedStart(maxPosView));
        int end = Math.max(helper.getDecoratedEnd(minPosView),
                helper.getDecoratedEnd(maxPosView));
        int distance = end - start;
        if (distance == 0) {
            return INVALID_DISTANCE;
        }
        return 1f * distance / ((maxPos - minPos) + 1);
    }


    @NonNull
    private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null || mHorizontalHelper.getLayoutManager() != layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }


}
