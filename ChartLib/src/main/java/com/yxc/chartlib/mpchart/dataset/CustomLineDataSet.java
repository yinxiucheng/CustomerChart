package com.yxc.chartlib.mpchart.dataset;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.entrys.SportRecordEntry;
import com.xiaomi.fitness.common.utils.ColorUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomLineDataSet<T extends Entry> extends LineDataSet {

    public static final int TYPE_INVALID = -1;
    public static final int TYPE_LOW = 0;
    public static final int TYPE_MIDDLE = 1;
    public static final int TYPE_HIGH = 2;

    public static final String DATA_SET_TYPE_INVALID = "type_invalid";
    public static final String DATA_SET_TYPE_LOW = "type_low";
    public static final String DATA_SET_TYPE_MIDDLE = "type_middle";
    public static final String DATA_SET_TYPE_HIGH = "type_high";

    public int dataSetType = TYPE_INVALID;

    public CustomLineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
    }

    public CustomLineDataSet(List<Entry> yVals, String label, int dataSetType) {
        super(yVals, label);
        this.dataSetType = dataSetType;
    }

    //步频分类函数
    public static List<CustomLineDataSet> createDataSetList(List<SportRecordEntry> entryList) {
        List<SportRecordEntry> invalidList = new ArrayList<>();
        List<SportRecordEntry> lowList = new ArrayList<>();
        List<SportRecordEntry> middleList = new ArrayList<>();
        List<SportRecordEntry> highList = new ArrayList<>();

        int size = entryList.size();
        for (int i = 0; i < size; i++) {
            SportRecordEntry currentEntry = entryList.get(i);
            if (currentEntry.getY() <= 0) {
                invalidList.add(currentEntry);
            } else if (currentEntry.getY() < 160) {
                lowList.add(currentEntry);
            } else if (currentEntry.getY() <= 180) {
                middleList.add(currentEntry);
            } else {
                highList.add(currentEntry);
            }
        }
        List<CustomLineDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(createLineDataSet(invalidList, TYPE_INVALID));
        dataSetList.add(createLineDataSet(lowList, TYPE_LOW));
        dataSetList.add(createLineDataSet(middleList, TYPE_MIDDLE));
        dataSetList.add(createLineDataSet(highList, TYPE_HIGH));
        return dataSetList;
    }


    public static CustomLineDataSet createLineDataSet(List<SportRecordEntry> entryList, int dataSetType){
        switch (dataSetType){
            case TYPE_INVALID:
                CustomLineDataSet dataSetInvalid = new CustomLineDataSet(entryList, DATA_SET_TYPE_INVALID, TYPE_INVALID);
                setDataSetAttrList(dataSetInvalid, getCircleColorAsType(dataSetType));
                return dataSetInvalid;
            case TYPE_LOW:
                CustomLineDataSet dataSetLow = new CustomLineDataSet(entryList, DATA_SET_TYPE_LOW, TYPE_LOW);
                setDataSetAttrList(dataSetLow, getCircleColorAsType(dataSetType));
                return dataSetLow;
            case TYPE_MIDDLE:
                CustomLineDataSet dataSetMiddle = new CustomLineDataSet(entryList, DATA_SET_TYPE_MIDDLE, TYPE_MIDDLE);
                setDataSetAttrList(dataSetMiddle, getCircleColorAsType(dataSetType));
                return dataSetMiddle;
            case TYPE_HIGH:
            default:
                CustomLineDataSet dataSetHigh = new CustomLineDataSet(entryList, DATA_SET_TYPE_HIGH, TYPE_HIGH);
                setDataSetAttrList(dataSetHigh, getCircleColorAsType(dataSetType));
                return dataSetHigh;
        }
    }

    public static void setDataSetAttrList(CustomLineDataSet dataSet, int circleColor){
        dataSet.setCubicIntensity(0.2f);//曲线的平滑度，值越大越平滑
        dataSet.setDrawIcons(false);//坐标处不显示绘制图标
        dataSet.setColor(ColorUtil.getResourcesColor(R.color.common_transparent));//设置绘制曲线颜色
        //填充区域显示渐变效果
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setLineWidth(2f);//设置绘制曲线宽度
        dataSet.setValueTextSize(4f);//设置坐标处显示值的文字大小
        dataSet.setDrawCircles(true);//数据坐标处不显示圆点
        dataSet.setCircleColor(circleColor);
        dataSet.setDrawCircleHole(false);
        dataSet.setCircleRadius(2f);
        dataSet.setDrawValues(false);//默认不显示数值
        dataSet.setDrawFilled(false);
        dataSet.setHighlightEnabled(false);
        dataSet.setMode(Mode.LINEAR);
    }

    public static int getCircleColorAsType(int dataSetType){
        switch (dataSetType){
            case TYPE_HIGH:
                return ColorUtil.getResourcesColor(R.color.step_freq_high);
            case TYPE_LOW:
                return ColorUtil.getResourcesColor(R.color.step_freq_low);
            case TYPE_MIDDLE:
                return ColorUtil.getResourcesColor(R.color.step_freq_middle);
            case TYPE_INVALID:
            default:
                return ColorUtil.getResourcesColor(R.color.common_transparent);
        }
    }
}

