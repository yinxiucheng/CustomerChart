package com.yxc.chartlib.barchart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.formatter.ValueFormatter;

import java.util.List;


/**
 * @author yxc
 * @date 2019-05-15
 */
public class StandChartAdapter extends BaseBarChartAdapter<RecyclerBarEntry, YAxis> {

    public StandChartAdapter(Context context, List<RecyclerBarEntry> entries,
                             RecyclerView recyclerView, XAxis xAxis,
                             BarChartAttrs attrs, ValueFormatter valueFormatter) {
        super(context, entries, recyclerView, xAxis, attrs, valueFormatter);
    }

    @Override
    public void onBindViewHolder(@NonNull BarChartViewHolder barChartViewHolder, int position) {
        super.onBindViewHolder(barChartViewHolder, position);
        float contentWidth = (mRecyclerView.getWidth() - mRecyclerView.getPaddingRight() - mRecyclerView.getPaddingLeft());

//        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//        Log.d("Adapter", "firstVisibleItemPosition:" + firstVisibleItemPosition);

        int itemWidth = (int) (contentWidth / mXAxis.displayNumbers);
//        int reminderWidth = (int) (contentWidth % mXAxis.displayNumbers);
//
//        //todo 这里只画右边，所以调整多余的只加在右边。 会造成重绘时，月的抖动，因为来回变动,
//        // todo没有限定一定要显示一周，一天、一年、一月的数据是，不用重新设定这个padding，多显示一点没事。
//        if (position > mEntries.size() - 2 && mBarChartAttrs.enableScrollToScale) {
//            //加载更多的时候，最后一次绘制item时 再改RecyclerView的padding
//            resetRecyclerPadding(reminderWidth);
//        }
//
//        if (mBarChartAttrs.averageDisplay && position <= reminderWidth){
//            //把多余的px宽度分给部分item position
//            itemWidth = itemWidth + 1;
//        }

        setLinearLayout(barChartViewHolder.contentView, itemWidth);
        bindBarEntryToView(barChartViewHolder, position);
    }

    private void bindBarEntryToView(BarChartViewHolder viewHolder, final int position) {
        final RecyclerBarEntry barEntry = mEntries.get(position);
        viewHolder.contentView.setTag(barEntry);
    }

    /**
     * * 设置每个色块宽度
     *
     * @param contentView 对应需要设置LinearLayout
     * @param itemWidth   对应的宽度
     */
    private void setLinearLayout(View contentView, int itemWidth) {
        ViewGroup.LayoutParams lp;
        lp = contentView.getLayoutParams();
        lp.width = itemWidth;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        contentView.setLayoutParams(lp);
    }

//    @SuppressWarnings("unused")
//    private void resetRecyclerPadding(int reminderWidth) {
//        if (mBarChartAttrs.enableEndYAxisLabel && mBarChartAttrs.enableStartYAxisLabel) {
//            mRecyclerView.setPaddingRelative(mRecyclerView.getPaddingLeft() + reminderWidth / 2, mRecyclerView.getPaddingTop(),
//                    mRecyclerView.getPaddingRight() + reminderWidth / 2, mRecyclerView.getPaddingBottom());
//        } else if (mBarChartAttrs.enableStartYAxisLabel) {
//            mRecyclerView.setPaddingRelative(mRecyclerView.getPaddingLeft(), mRecyclerView.getPaddingTop(),
//                    mRecyclerView.getPaddingRight() + reminderWidth, mRecyclerView.getPaddingBottom());
//        } else if (mBarChartAttrs.enableEndYAxisLabel) {
//            mRecyclerView.setPaddingRelative(mRecyclerView.getPaddingLeft() + reminderWidth, mRecyclerView.getPaddingTop(),
//                    mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
//        }
//    }
}
