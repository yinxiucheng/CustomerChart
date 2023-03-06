package com.yxc.chartlib.barchart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.entrys.model.EnergyEntry;


/**
 * @author yxc
 * @since 2019/4/6
 */
public class EnergyChartAdapter extends BaseBarChartAdapter<EnergyEntry, BaseYAxis> {

    public EnergyChartAdapter(Context context, List<EnergyEntry> entries,
                              RecyclerView recyclerView, XAxis xAxis, BarChartAttrs attrs) {
        super(context, entries, recyclerView, xAxis, attrs);
    }

    @Override
    public void onBindViewHolder(@NonNull BarChartViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        float contentWidth = (mRecyclerView.getWidth() - mRecyclerView.getPaddingRight()
                - mRecyclerView.getPaddingLeft());
        int itemWidth;
        if (mBarChartAttrs.dynamicAdjustItemFillWidth) {
            //需要-动态调整以fill width 最小误差
            int normalWidth = (int) Math.ceil(contentWidth / mXAxis.displayNumbers);
            int sumCount = mXAxis.displayNumbers - 2;
            if (position == 0 || position == mEntries.size() - 1) {
                itemWidth = (int) ((contentWidth - normalWidth * sumCount) / 2);
            } else {
                itemWidth = normalWidth;
            }
        } else {
            itemWidth = (int) (contentWidth / mXAxis.displayNumbers);
        }
        setLinearLayout(viewHolder.contentView, itemWidth);
        bindBarEntryToView(viewHolder, position);
    }

    private void bindBarEntryToView(BarChartViewHolder viewHolder, final int position) {
        final EnergyEntry barEntry = mEntries.get(position);
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

    @SuppressWarnings("unuesd")
    private void resetRecyclerPadding(int reminderWidth) {
        if (mBarChartAttrs.enableEndYAxisLabel && mBarChartAttrs.enableStartYAxisLabel) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft() + reminderWidth / 2,
                    mRecyclerView.getPaddingTop(),
                    mRecyclerView.getPaddingRight() + reminderWidth / 2,
                    mRecyclerView.getPaddingBottom());
        } else if (mBarChartAttrs.enableStartYAxisLabel) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), mRecyclerView.getPaddingTop(),
                    mRecyclerView.getPaddingRight() + reminderWidth,
                    mRecyclerView.getPaddingBottom());
        } else if (mBarChartAttrs.enableEndYAxisLabel) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft() + reminderWidth,
                    mRecyclerView.getPaddingTop(),
                    mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
        }
    }

}
