package com.yxc.chartlib.barchart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.xiaomi.fitness.chart.attrs.BaseChartAttrs;
import com.xiaomi.fitness.chart.barchart.BaseBarChartAdapter;
import com.xiaomi.fitness.chart.component.BaseYAxis;
import com.xiaomi.fitness.chart.component.XAxis;
import com.xiaomi.fitness.chart.entrys.MaxMinEntry;
import com.xiaomi.fitness.chart.formatter.ValueFormatter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LineChartAdapter<T extends BaseYAxis> extends BaseBarChartAdapter<MaxMinEntry, T> {

    public LineChartAdapter(Context context, List<MaxMinEntry> entries,
                            RecyclerView recyclerView, XAxis xAxis,
                            BaseChartAttrs attrs, ValueFormatter valueFormatter) {
        super(context, entries, recyclerView, xAxis, attrs, valueFormatter);
    }

    public void setValueFormatter(ValueFormatter valueFormatter){
        this.valueFormatter = valueFormatter;
    }

    @Override
    public void onBindViewHolder(@NonNull BarChartViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        float contentWidth = (mRecyclerView.getWidth() - mRecyclerView.getPaddingRight()
                - mRecyclerView.getPaddingLeft());
        int itemWidth = (int) (contentWidth / mXAxis.displayNumbers);
        setLinearLayout(viewHolder.contentView, itemWidth);
        bindBarEntryToView(viewHolder, position);
    }


    private void bindBarEntryToView(BarChartViewHolder viewHolder, final int position) {
        final MaxMinEntry barEntry = mEntries.get(position);
        viewHolder.contentView.setTag(barEntry);
    }

    /**
     * * 设置每个色块宽度
     *
     * @param contentView 对应需要设置LinearLayout
     * @param itemWidth 对应的宽度
     */
    private void setLinearLayout(View contentView, int itemWidth) {
        ViewGroup.LayoutParams lp;
        lp = contentView.getLayoutParams();
        lp.width = itemWidth;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        contentView.setLayoutParams(lp);
    }
}
