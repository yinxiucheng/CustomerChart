package com.xiaomi.fitness.chart.barchart

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xiaomi.fitness.chart.component.BaseYAxis
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry
import com.xiaomi.fitness.chart.attrs.BaseChartAttrs
import com.xiaomi.fitness.chart.barchart.BaseBarChartAdapter
import com.xiaomi.fitness.chart.barchart.BarChartViewHolder
import com.xiaomi.fitness.chart.component.XAxis
import com.xiaomi.fitness.chart.formatter.ValueFormatter

/**
 * @author yxc
 * @since  2019/4/6
 */
class SpO2BarChartAdapter<T : BaseYAxis?>(
    context: Context?, entries: List<RecyclerBarEntry?>,
    recyclerView: RecyclerView?, xAxis: XAxis?,
    attrs: BaseChartAttrs?,
    valueFormatter: ValueFormatter
) : BaseBarChartAdapter<RecyclerBarEntry?, T>(context, entries, recyclerView, xAxis, attrs, valueFormatter) {

    override fun onBindViewHolder(viewHolder: BarChartViewHolder, position: Int) {
        super.onBindViewHolder(viewHolder, position)
        val contentWidth =
            (mRecyclerView.width - mRecyclerView.paddingRight - mRecyclerView.paddingLeft).toFloat()

//        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//        Log.d("Adapter", "firstVisibleItemPosition:" + firstVisibleItemPosition);
        val itemWidth = (contentWidth / mXAxis.displayNumbers).toInt()
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
        setLinearLayout(viewHolder.contentView, itemWidth)
        bindBarEntryToView(viewHolder, position)
    }

    private fun bindBarEntryToView(viewHolder: BarChartViewHolder, position: Int) {
        val barEntry = mEntries[position]!!
        viewHolder.contentView.tag = barEntry
    }

    /**
     * * 设置每个色块宽度
     *
     * @param contentView 对应需要设置LinearLayout
     * @param itemWidth   对应的宽度
     */
    private fun setLinearLayout(contentView: View, itemWidth: Int) {
        val lp: ViewGroup.LayoutParams
        lp = contentView.layoutParams
        lp.width = itemWidth
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT
        contentView.layoutParams = lp
    } //    @SuppressWarnings("unuesd")
    //    private void resetRecyclerPadding(int reminderWidth) {
    //        if (mBarChartAttrs.enableEndYAxisLabel && mBarChartAttrs.enableStartYAxisLabel) {
    //            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft() + reminderWidth / 2, mRecyclerView.getPaddingTop(),
    //                    mRecyclerView.getPaddingRight() + reminderWidth / 2, mRecyclerView.getPaddingBottom());
    //        } else if (mBarChartAttrs.enableStartYAxisLabel) {
    //            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), mRecyclerView.getPaddingTop(),
    //                    mRecyclerView.getPaddingRight() + reminderWidth, mRecyclerView.getPaddingBottom());
    //        } else if (mBarChartAttrs.enableEndYAxisLabel) {
    //            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft() + reminderWidth, mRecyclerView.getPaddingTop(),
    //                    mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
    //        }
    //    }
}