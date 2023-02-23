package com.yxc.chartlib.barchart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.xiaomi.fitness.chart.attrs.BarChartAttrs;
import com.xiaomi.fitness.chart.barchart.BaseBarChartAdapter;
import com.xiaomi.fitness.chart.component.XAxis;
import com.xiaomi.fitness.chart.component.YAxis;
import com.xiaomi.fitness.chart.entrys.PaiEntry;
import com.xiaomi.fitness.chart.formatter.ValueFormatter;

import java.util.List;

public class PaiChartAdapter extends BaseBarChartAdapter<PaiEntry, YAxis> {

    public PaiChartAdapter(Context context, List<PaiEntry> entries,
                           RecyclerView recyclerView, XAxis xAxis,
                           BarChartAttrs attrs, ValueFormatter valueFormatter) {
        super(context, entries, recyclerView, xAxis, attrs, valueFormatter);
    }

    @Override
    public void onBindViewHolder(@NonNull BarChartViewHolder barChartViewHolder, int position) {
        super.onBindViewHolder(barChartViewHolder, position);
        float contentWidth = (mRecyclerView.getWidth() - mRecyclerView.getPaddingRight() - mRecyclerView.getPaddingLeft());
        int itemWidth = (int) (contentWidth / mXAxis.displayNumbers);
        setLinearLayout(barChartViewHolder.contentView, itemWidth);
        bindBarEntryToView(barChartViewHolder, position);
    }

    private void bindBarEntryToView(BarChartViewHolder viewHolder, final int position) {
        final PaiEntry barEntry = mEntries.get(position);
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
        lp.height = 1000;
        contentView.setLayoutParams(lp);
    }
}
