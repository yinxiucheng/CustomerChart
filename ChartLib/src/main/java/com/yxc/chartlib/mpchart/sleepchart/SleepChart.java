package com.yxc.chartlib.mpchart.sleepchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.yxc.chartlib.entrys.SleepItemEntry;
import com.yxc.customerchart.R;
import com.yxc.chartlib.utils.AppUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yxc
 * @date 2019-09-10
 */
public class SleepChart extends BarChart {
    public SleepChartAttr mAttribute;

    public SleepChart(Context context) {
        this(context, null);
    }
    public SleepChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSleepChart);
        if (null == mAttribute) {
            mAttribute = new SleepChartAttr();
        }
        mAttribute.sleepChartType = ta.getInteger(R.styleable.CustomSleepChart_sleep_type, SleepChartAttr.SLEEP_CHART_TYPE_DEF);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLineChart(); //初始化
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new SleepChartRenderer(this, mAnimator, mViewPortHandler);
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart() {
        Context context = AppUtil.getApp();
        setMinOffset(0);
        setExtraOffsets(0, 0, 0, 0);//图形绘制区域边界

        //不显示绘制区背景网格
        setDrawGridBackground(false);
        // no description text
        getDescription().setEnabled(false);
        // enable touch gestures
        setTouchEnabled(true);
        setDrawBorders(false);

        // enable scaling and dragging
        setDragEnabled(false);
        setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        setPinchZoom(true);
        setDrawMarkers(false);//点击时不显示数据
        setNoDataText(context.getString(R.string.common_data_empty));//没有数据时显示内容
        /*禁止缩放*/
        Legend legend = getLegend();
        legend.setEnabled(false);//不显示图例,可以去掉new Dateset 时的字符串
        setXY();
    }

    private void setXY() {
        XAxis xl = getXAxis();
        xl.setAxisMinimum(0);
        xl.setEnabled(false);

        YAxis rightAxis = getAxisRight();
        rightAxis.setAxisMinimum(0);
        rightAxis.setAxisMaximum(4);
        rightAxis.setEnabled(false);

        YAxis leftAxis = getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(4);
        leftAxis.setEnabled(false);
    }

    @Override
    protected void calcMinMax() {
        IBarDataSet dataSet = mData.getDataSetByIndex(0);
        if (dataSet.getEntryCount() > 0) {
            SleepItemEntry entryEnd = (SleepItemEntry) dataSet.getEntryForIndex(dataSet.getEntryCount() - 1);
            mXAxis.calculate(mData.getXMin(), mData.getXMax() + entryEnd.sleepItemTime.durationTimeSed);
            mAxisLeft.calculate(mData.getYMin(YAxis.AxisDependency.LEFT), mData.getYMax(YAxis.AxisDependency.LEFT));
            mAxisRight.calculate(mData.getYMin(YAxis.AxisDependency.RIGHT), mData.getYMax(YAxis.AxisDependency.RIGHT));
        } else {
            super.calcMinMax();
        }
    }

    public void bindData(List<SleepItemEntry> values){
        bindData(values, true);
    }

    public void bindData(List<SleepItemEntry> values, boolean isConnected) {
        SleepBarDataSet set1;
        BarData data = getData();
        Collections.sort(values, new EntryXComparator());
        if (data != null && data.getDataSetCount() > 0) {
            set1 = (SleepBarDataSet) data.getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            data.notifyDataChanged();
            notifyDataSetChanged();
        } else {
            set1 = new SleepBarDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);//坐标处不显示绘制图标
            //填充区域显示渐变效果

            set1.setValueTextSize(4f);//设置坐标处显示值的文字大小
            set1.setDrawValues(false);//默认不显示数值

            //设置点击数据点高亮显示效果
            set1.setHighlightEnabled(false);//点击坐标点不显示高亮交叉线

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets
            BarData l = new BarData(dataSets);
            setData(l);
        }
        invalidate();
    }


}
