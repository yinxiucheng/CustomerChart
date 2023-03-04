package com.yxc.chartlib.barchart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.customerchart.R;

import java.util.List;

/**
 * @author yxc
 * @since  2019/4/6
 */
public abstract class BaseBarChartAdapter<T extends RecyclerBarEntry, V extends BaseYAxis> extends
        RecyclerView.Adapter<BarChartViewHolder> {

    protected Context mContext;
    protected List<T> mEntries;
    protected RecyclerView mRecyclerView;
    protected XAxis mXAxis;
    protected V mYAxis;
    protected BaseChartAttrs mBarChartAttrs;
    protected ValueFormatter valueFormatter;

    public BaseBarChartAdapter(Context context, List<T> entries, RecyclerView recyclerView,
                               XAxis xAxis, BaseChartAttrs attrs) {
        this.mContext = context;
        this.mEntries = entries;
        this.mRecyclerView = recyclerView;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = attrs;
    }

    public BaseBarChartAdapter(Context context, List<T> entries, RecyclerView recyclerView,
                               XAxis xAxis, BaseChartAttrs attrs, ValueFormatter valueFormatter) {
        this(context, entries, recyclerView, xAxis, attrs);
        this.valueFormatter = valueFormatter;
    }

    public void setXAxis(XAxis mXAxis) {
        this.mXAxis = mXAxis;
        notifyDataSetChanged();
    }

    public void setEntries(List<T> mEntries) {
        this.mEntries = mEntries;
        notifyDataSetChanged();
    }

    public List<T> getEntries() {
        return mEntries;
    }

    @NonNull
    @Override
    public BarChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_barchart, parent, false);
        BarChartViewHolder viewHolder = new BarChartViewHolder(view);

        return viewHolder;
    }

    public void onBindViewHolder(@NonNull BarChartViewHolder viewHolder, int position){
        setItemContentDescription(viewHolder, position);
    }

    private void setItemContentDescription(BarChartViewHolder viewHolder, final int position){
        final RecyclerBarEntry barEntry = mEntries.get(position);
        String contentDescription = "";
        if (valueFormatter != null){
            contentDescription = valueFormatter.getBarLabel(barEntry);
        }
        viewHolder.contentView.setContentDescription(contentDescription);
    }

    @Override
    public int getItemViewType(int position) {
        if (mEntries != null && mEntries.size() > position){
            T barEntry = mEntries.get(position);
            if (null != barEntry) {
                return barEntry.type;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public void setYAxis(V mYAxis) {
        this.mYAxis = mYAxis;
        notifyDataSetChanged();
    }

    public void updateXAxis(XAxis xAxis){
        this.mXAxis = xAxis;
        notifyDataSetChanged();
    }
}
