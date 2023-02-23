package com.yxc.chartlib.mpchart.barchart;

import android.content.Context;
import android.util.AttributeSet;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.attrs.ChartAttrsUtil;
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.chart.entrys.SleepEntry;
import com.xiaomi.fitness.chart.mpchart.dataset.CustomBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class CustomBarChart<T extends RecyclerBarEntry> extends BarChart {
    protected CustomBarChartAttr mAttribute;

    public CustomBarChart(Context context) {
        super(context);
    }

    public CustomBarChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAttribute = ChartAttrsUtil.getMPBarChartAttr(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new CustomBarChartRenderer(this, mAnimator, mViewPortHandler);
    }

    public CustomBarChartAttr getAttribute() {
        return mAttribute;
    }

    private <K extends RecyclerBarEntry> void setYmaxValue(List<K> values) {
        float max;
        if ((values == null) || (values.size() == 0))
            return;
        max = values.get(0).getY();
        for (int i = 0; i < values.size(); i++) {
            max = Math.max(max, values.get(i).getY());
        }
        /*避免柱状图高度为0的情况*/
        if (max == 0)
            max = 1;
        getAxisLeft().setAxisMaximum(max);
        getAxisRight().setAxisMaximum(max);
    }

    public <K extends RecyclerBarEntry> void setEntryData(List<K> values) {
        CustomBarDataSet set1;
        setYmaxValue(values);
        getAxisLeft().setAxisMinimum(0);
        getAxisRight().setAxisMinimum(0);
        if (getData() != null && getData().getDataSetCount() > 0) {
            set1 = (CustomBarDataSet) getData().getDataSetByIndex(0);
            set1.setValues(values);
            getData().notifyDataChanged();
            notifyDataSetChanged();
        } else {
            set1 = new CustomBarDataSet(values, "Data Set");
            //  set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);
            set1.setColor(mAttribute.mDoneColor);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(mAttribute.mBarDutyCycle);//设置柱状图宽度占单位矩形的比例
            setData(data);
            setFitBars(true);
        }
        invalidate();
    }

    public <K extends RecyclerBarEntry> void setEntryData(List<K> values, float max, float min) {
        CustomBarDataSet set1;
        getAxisLeft().setAxisMaximum(max);
        getAxisRight().setAxisMaximum(max);
        getAxisLeft().setAxisMinimum(min);
        getAxisRight().setAxisMinimum(min);

        if (getData() != null && getData().getDataSetCount() > 0) {
            set1 = (CustomBarDataSet) getData().getDataSetByIndex(0);
            set1.setValues(values);
            getData().notifyDataChanged();
            notifyDataSetChanged();
        } else {
            set1 = new CustomBarDataSet(values, "Data Set");
            //  set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);
            set1.setColor(mAttribute.mDoneColor);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(mAttribute.mBarDutyCycle);//设置柱状图宽度占单位矩形的比例
            setData(data);
            setFitBars(true);
        }
        invalidate();
    }

    public void setSleepEntryData(List<SleepEntry> values) {
        CustomBarDataSet set1;
        if (getData() != null && getData().getDataSetCount() > 0) {
            set1 = (CustomBarDataSet) getData().getDataSetByIndex(0);
            set1.setValues(values);
            getData().notifyDataChanged();
            notifyDataSetChanged();
        } else {
            set1 = new CustomBarDataSet(values, "Data Set");
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(mAttribute.mBarDutyCycle);//设置柱状图宽度占单位矩形的比例
            setData(data);
            setFitBars(true);
        }
        invalidate();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initBarChart();
    }

    protected void setXY() {
        YAxis yAxis = getAxisLeft();
        YAxis yAxis2 = getAxisRight();
        XAxis xAxis = getXAxis();
        /*设置X轴*/
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(mAttribute.mMaxPoints + 0.5f);//解决第一根柱状图显示不全的问题.

        xAxis.setDrawLabels(false);//不显示刻度
        xAxis.setDrawAxisLine(false);//不显示X轴
        xAxis.setDrawGridLines(false);//不显示X轴对应的网格线
        /*设置 Y轴*/
        yAxis.setEnabled(false);
        yAxis.setDrawAxisLine(false);//不显示左边Y轴线
        yAxis.setDrawGridLines(false);//不显示左边Y轴网格线
        yAxis.setDrawLabels(false);//不显示左边Y轴刻度值
        yAxis2.setEnabled(false);//disable 右边Y轴
        yAxis2.setDrawAxisLine(false);//不显示 右边Y轴线
        yAxis2.setDrawGridLines(false);//不显示 右边Y轴网格线
        yAxis2.setDrawLabels(false);//不显示 右边Y轴刻度值
    }

    protected void setChartPadding() {
        //    setViewPortOffsets(0, 0, 0, 0);//图形绘制区域边界
        setMinOffset(0);
        setExtraOffsets(0, 0, 0, 0);
    }

    protected void initBarChart() {
        setChartPadding();
        setDrawMarkers(false);//点击后默认不显示数据值
//        setBackgroundColor(Color.BLACK);
        getDescription().setEnabled(false);// disable description text
        setTouchEnabled(false);//enable touch gestures
        setDrawGridBackground(false);//不显示绘制区背景网格
        setNoDataText(getContext().getString(R.string.common_data_empty));//没有数据时显示内容
        /*禁止缩放*/
        setDragEnabled(false);
        setScaleEnabled(false);
        setPinchZoom(false);
        setClickable(false);
        // no description text
        getDescription().setEnabled(false);
        getLegend().setEnabled(false);//不显示图例,可以去掉new Dateset 时的字符串
        setXY();
    }

    public void setBarChartColor(Context context, int doneColorRes, int noneDoneColorRes){
        mAttribute.mDoneColor = context.getColor(doneColorRes);
        mAttribute.mNoneDoneColor = context.getColor(noneDoneColorRes);
    }

    public void setBarChartType(int chartType){
        mAttribute.barChartType = chartType;
    }

    public void resetAttribute(int maxPoints, float rectRadius, int chartType, float dutyCycle){
        mAttribute.mMaxPoints = maxPoints;
        mAttribute.mRectRadius = rectRadius;
        mAttribute.barChartType = chartType;
        mAttribute.mBarDutyCycle = dutyCycle;
        //需要重新调用，不然mMaxPoints 不生效。
        initBarChart();
    }
}
