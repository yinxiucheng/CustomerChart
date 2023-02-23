package com.yxc.chartlib.barchart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaomi.fitness.chart.attrs.BarChartAttrs;
import com.xiaomi.fitness.chart.component.BaseYAxis;
import com.xiaomi.fitness.chart.component.XAxis;
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.chart.formatter.ValueFormatter;

import java.util.List;


/**
 * @author yxc
 * @since 2019/4/6
 */
public class BarChartAdapter extends BaseBarChartAdapter<RecyclerBarEntry, BaseYAxis> {

    protected ValueFormatter valueFormatter;

    public BarChartAdapter(Context context, List<RecyclerBarEntry> entries,
            RecyclerView recyclerView, XAxis xAxis, BarChartAttrs attrs, ValueFormatter valueFormatter) {
        super(context, entries, recyclerView, xAxis, attrs, valueFormatter);
    }

    @Override
    public void onBindViewHolder(@NonNull BarChartViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        float contentWidth = mRecyclerView.getWidth() - mRecyclerView.getPaddingEnd()
                - mRecyclerView.getPaddingStart();
        int itemWidth;
        if (mBarChartAttrs.dynamicAdjustItemFillWidth) {
            //需要-动态调整以fill width 最小误差
            int normalWidth = (int) Math.ceil(contentWidth / mXAxis.displayNumbers);
            int sumCount = mXAxis.displayNumbers - 2;
            int dividerWidth=(int) ((contentWidth - normalWidth * sumCount) / 2);
            dividerWidth = dividerWidth < 0 ? 1 : dividerWidth;
            if (position == 0 || position == mEntries.size() - 1) {
                itemWidth = dividerWidth;
            } else {
                itemWidth = normalWidth;
            }
            if ((position - 1) * normalWidth + dividerWidth > contentWidth) {
                itemWidth=0;
            }
        } else {
            itemWidth = (int) (contentWidth / mXAxis.displayNumbers);
        }
        setLinearLayout(viewHolder.contentView, itemWidth);
        bindBarEntryToView(viewHolder, position);
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
}
