package com.yxc.chartlib.mpchart;

import android.util.Log;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.xiaomi.fitness.chart.R;
import com.xiaomi.fitness.chart.entrys.SportRecordEntry;
import com.xiaomi.fitness.chart.mpchart.linechart.CustomLineChartAttr;
import com.xiaomi.fitness.chart.util.ChartUtil;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.utils.ColorUtil;
import com.xiaomi.fitness.common.utils.TimeDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @date 2020/12/22
 */
public class SportYAxis extends YAxis {

    //     TYPE_FIX_MIN_ZERO = 0; Y轴从固定的0开始 到 max；步频、起跳高度
//     TYPE_FIX_MIN_POSITIVE = 1; 从 entryList的 真实的 min（min不能小于0）开始，到max； 心率、速度、划频、Swolf
//     TYPE_FIX_COMMON = 2; 从entryList的最小值min开始到max的最大值，无论最大、最小是否为Positive， 例如海拔；
//     TYPE_FIX_RESTRICT_MAX = 3; 限制最大值，比如配速。Y轴 Invert，所以最小值min为大于等于 0 的Positive value； 配速
    public static final int TYPE_FIX_MIN_ZERO = 0;
    public static final int TYPE_FIX_MIN_POSITIVE = 1;
    public static final int TYPE_FIX_COMMON = 2;
    public static final int TYPE_FIX_RESTRICT_MAX = 3;

    public CustomLineChartAttr mLineChartAttr;

    public SportYAxis(AxisDependency dependency) {
        super(dependency);
    }

    public void setLineChartAttr(CustomLineChartAttr lineChartAttr) {
        this.mLineChartAttr = lineChartAttr;
    }

    @Override
    public int getLabelCount() {
        return super.getLabelCount();
    }

    public void setYAxisMaxMinimum(List<SportRecordEntry> values) {
        if (mLineChartAttr.restrictMax) {
            float yAxisMax = getYAxisMax2(values, getAxisMinimum());
            setAxisMaximum(yAxisMax);
        } else {
            if (mLineChartAttr.maxYAxisRatio > 0) {
                float yAxisMax = getYAxisMax(values, getAxisMinimum());
                setAxisMaximum(yAxisMax);
            }
        }
        if (!mLineChartAttr.minYAxisZero) {
            float yAxisMin = ChartUtil.getTheMinNumber(values);
            if (yAxisMin > 1) {
                float maximum = getAxisMaximum();
                float itemMin = (maximum - yAxisMin) / 10;
                setAxisMinimum(yAxisMin - itemMin);
            } else {
                setAxisMinimum(yAxisMin);
            }
        }
    }

    //限制最大值
    private float getYAxisMax2(List<SportRecordEntry> values, float yAxisMin) {
        int size = values.size();
        float yAxisMax = Integer.MIN_VALUE;
        float yAxisMinTemp = Integer.MAX_VALUE;
        float sum = 0;
        for (int i = 0; i < size; i++) {
            SportRecordEntry entry = values.get(i);
            yAxisMax = Math.max(entry.getY(), yAxisMax);
            yAxisMinTemp = Math.min(entry.getY(), yAxisMinTemp);
            sum += entry.getY();
        }
        float averageY = sum / (size * 1.0f);
        float distanceMin = averageY - yAxisMinTemp;
        float distanceMax = yAxisMax - averageY;
        int num = (int) (distanceMax / distanceMin);
        if (num > 5) {// 配速中 有 配速值很慢的点，坐标时不考虑它们了。
            yAxisMax = averageY + 2 * distanceMin; // 限制Y 轴坐标。
        }
        float distance = yAxisMax - yAxisMin;
        if (yAxisMax > 0 && distance <= 2) {
            return yAxisMax + 2;
        }
        return yAxisMax + distance * mLineChartAttr.maxYAxisRatio;
    }

    private float getYAxisMax(List<SportRecordEntry> values, float yAxisMin) {
        int size = values.size();
        float yAxisMax = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            SportRecordEntry entry = values.get(i);
            yAxisMax = Math.max(entry.getY(), yAxisMax);
        }
        float distance = yAxisMax - yAxisMin;
        if (yAxisMax > 0 && distance <= 2) {
            return yAxisMax + 2;
        }
        return yAxisMax + distance * mLineChartAttr.maxYAxisRatio;
    }

    public void setYAxisParam(List<SportRecordEntry> values, float restrictMax) {
        int yAxisType = mLineChartAttr.sportYAxisLabelType;
        int reminder = mLineChartAttr.sportYAxisReminder;
        if (yAxisType == TYPE_FIX_RESTRICT_MAX) {
            setYAxisParam4(values, restrictMax);
        } else if (yAxisType == TYPE_FIX_COMMON) {
            setYAxisParam3(values);
        } else if (yAxisType == TYPE_FIX_MIN_ZERO) {
            setYAxisParam1(values, reminder);
        } else {//TYPE_FIX_MIN_Positive
            setYAxisParam2(values, reminder);
        }
    }

    private void setEntriesLabel(List<Integer> entryList){
        int labelCount = entryList.size();
        if (labelCount <= 2) {
        }
        setLabelCount(labelCount, true);
//        Log.d("TimeXAxis", "getLabelCount() invoke labelCount:" + labelCount + " interval:" + interval / TimeDateUtil.TIME_MIN_INT + " max:" + max / TimeDateUtil.TIME_MIN_INT);
        mEntryCount = labelCount;
        if (mEntries.length < labelCount) {
            // Ensure stops contains at least numStops elements.
            mEntries = new float[labelCount];
        }
        for (int i = 0; i < labelCount; i++) {
            mEntries[i] = entryList.get(i);
//            Log.d("TimeXAxis", "getLabelCount() labelCount i:" + i + " label:" + mEntries[i] / TimeDateUtil.TIME_MIN_INT);
        }
        if (entryList.size() > 0){
            float minYAxis = entryList.get(0);
            float maxYAxis = entryList.get(entryList.size() - 1);
            setAxisMinimum(minYAxis);
            setAxisMaximum(maxYAxis);
        }
    }

    private void setYAxisParam1(List<SportRecordEntry> values, int reminder) {
        float max = ChartUtil.getTheMaxNumber(values);
        float itemFloat = max / 4.0f;
        int itemValueTemp = fetchTopRound(itemFloat, reminder);
        int maxLevel = fetchTopRound(max, itemValueTemp);
        int itemValue = maxLevel / 4;
        List<Integer> entryList = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            entryList.add(i * itemValue);
        }
        setEntriesLabel(entryList);
    }

    private void setYAxisParam2(List<SportRecordEntry> values, int reminder) {
        float min = ChartUtil.getTheMinNumber(values);
        float max = ChartUtil.getTheMaxNumber(values);

        int maxInt = Math.round(max);
        int minInt = Math.round(min);
        int maxLevel = fetchTopRound(maxInt, reminder);
        int minLevel = fetchBottomRound(minInt, reminder, false);
        int itemValue = (maxLevel - minLevel)/3;//C
        List<Integer> entryList = new ArrayList<>();
        entryList.add(Math.max(0, minLevel - itemValue));
        //当最大值偏大，itemValue偏小时会导致最大值超出绘制区域
        if (max > (min + 3.1f * itemValue)) {
            itemValue = itemValue + 1;
        }
        for (int i = 0; i < 5; i++) {
            entryList.add(minLevel + i * itemValue);
        }
        setEntriesLabel(entryList);
    }


    private void setYAxisParam3(List<SportRecordEntry> values) {
        float min = ChartUtil.getTheMinNumber(values);
        float max = ChartUtil.getTheMaxNumber(values);
        float itemTempFloat = (max - min)/3.0f;
        List<Integer> entryList = new ArrayList<>();
        int minLevel = 0;
        int maxLevel = 0;
        if (itemTempFloat == 0){
            int itemValue = 4 * 2;
            maxLevel = fetchTopRound(max, 4);
            minLevel = fetchBottomRound(min, 4, true);
            int minLabel = minLevel - itemValue * 2;
            for (int i = 0; i <= 5 ; i++) {
                entryList.add(minLabel + itemValue * i);
            }
        } else {
            int itemValueTemp = fetchTopRound(itemTempFloat, 3);
            maxLevel = fetchTopRound(max, itemValueTemp);
            minLevel = fetchBottomRound(min, itemValueTemp, true);
            int axisRange = maxLevel - minLevel;
            if (minLevel != 0) {
                int itemValue = axisRange / 3;
                entryList.add(minLevel - itemValue);
                for (int i = 0; i < 5; i++) {
                    entryList.add(minLevel + i * itemValue);
                }
            } else {
                maxLevel = fetchTopRound(maxLevel, 4);
                int itemValue = maxLevel / 4;
                for (int i = 0; i <= 5; i++) {
                    entryList.add(itemValue * i);
                }
            }
        }
        setEntriesLabel(entryList);
    }

    private int fetchTopRound(float original, int reminder){
        int temp = Math.round(original + 0.5f);//防止气泡靠边。
        if (temp == 0){
            return reminder;
        }
        for (int i = 0; i <= reminder; i++){
            if (temp % reminder == 0){
                return temp;
            }
            temp++;
        }
        return temp;
    }

    //向上取 reminder的公倍数
    private int fetchTopRound(int original, int reminder){
        if (original == 0){
            return reminder;
        }
        for (int i = 0; i<= reminder; i++){
            if (original % reminder == 0){
                return original;
            }
            original++;
        }
        return original;
    }

    //向下取 reminder的公倍数
    private int fetchBottomRound(float original, int reminder, boolean ignorePositive) {
        int result = 0;
        int temp = Math.round(original - 0.5f);
        for (int i = 0; i <= reminder; i++) {
            if (temp % reminder == 0) {
                result = ignorePositive ? temp : Math.max(0, temp);
                return result;
            }
            temp --;
        }
        return ignorePositive ? result : Math.max(0, result);
    }

    //向下取 reminder的公倍数
    private int fetchBottomRound(int original, int reminder, boolean ignorePositive) {
        int result = 0;
        for (int i = 0; i <= reminder; i++) {
            if (original % reminder == 0) {
                result = original;
                return ignorePositive ? result : Math.max(0, result);
            }
            original--;
        }
        return ignorePositive ? result : Math.max(0, result);
    }



    private void setYAxisParam4(List<SportRecordEntry> values, float restrictMax) {
        setYAxisMaxMinimum(values);
    }

    //配速的绘制方案(需要完善)
    private void setYAxisParam5(List<SportRecordEntry> values, float restrictMax) {
        float yAxisMax = ChartUtil.getTheMaxNumber(values);
        float yAxisMin = ChartUtil.getTheMinNumber(values);
        int itemValue = (int) ((yAxisMax - yAxisMin) / 4);
        if (itemValue < 60) {
            itemValue = 60;
        } else if (itemValue < 120) {
            itemValue = 120;
        } else if (itemValue < 180) {
            itemValue = 180;
        } else if (itemValue < 240) {
            itemValue = 240;
        } else if (itemValue < 300) {
            itemValue = 300;
        } else if (itemValue < 600) {
            itemValue = 600;
        } else if (itemValue < 1200) {
            itemValue = 1200;
        } else {
            itemValue = 2400;
        }
        float maxLabel = yAxisMax;
        for (int i = 0; i <= itemValue; i++) { // 向下找到 minLabel；
            maxLabel++;
            if (Math.round(maxLabel) % itemValue == 0) {//配速，考虑显示整数
                maxLabel = Math.round(maxLabel);
                break;
            }
        }
        if (restrictMax != -1) {
            maxLabel = Math.min(maxLabel, restrictMax);
        }

        float currentEntry = maxLabel;
        List<Float> entryList = new ArrayList<>();
        do {
            entryList.add(currentEntry);
            currentEntry -= itemValue;
//            Log.d("SportYAxis", " do while currentEntry:" + currentEntry);
//            Log.d("TimeXAxis", "currentEntry:" + currentEntry + " max:" + max);
        } while (currentEntry > yAxisMin);
        float minLabel;
        if (currentEntry <= 0) {
            minLabel = Math.max(0, currentEntry);
            entryList.add(minLabel);
        } else {
            minLabel = currentEntry - itemValue;
            minLabel = Math.max(0, minLabel);
            entryList.add(minLabel);
            if (minLabel > 0) {
                minLabel -= itemValue;
                minLabel = Math.max(0, minLabel);
                entryList.add(minLabel);
            }
        }
        int labelCount = entryList.size();
        setLabelCount(labelCount, true);
//        Log.d("TimeXAxis", "getLabelCount() invoke labelCount:" + labelCount + " interval:" + interval / TimeDateUtil.TIME_MIN_INT + " max:" + max / TimeDateUtil.TIME_MIN_INT);
        mEntryCount = labelCount;
        if (mEntries.length < labelCount) {
            // Ensure stops contains at least numStops elements.
            mEntries = new float[labelCount];
        }

        for (int i = 0; i < labelCount; i++) {
            mEntries[i] = entryList.get(i);
            Log.d("SportYAxis", "getLabelCount() labelCount i:" + i + " label:" + mEntries[i] / TimeDateUtil.TIME_MIN_INT);
        }
        float minYAxis = minLabel - itemValue / 2;
        float maxYAxis = maxLabel;
        setAxisMinimum(minLabel);
        setAxisMaximum(maxLabel);
    }

    private int getItemValue(float yAxisMax, float yAxisMin) {
        int itemValue = (int) ((yAxisMax - yAxisMin) / 4);
        if (itemValue < 10) {

        } else if (itemValue < 100) {
            int reminder = itemValue % 10;
            if (reminder <= 2) {
                itemValue = itemValue / 10 * 10;
            } else {
                itemValue = (itemValue / 10 + 1) * 10;//
            }
        }
        itemValue = Math.max(2, itemValue);
        return itemValue;
    }

    private void setEntriesMaxMin(float min, float max, int itemValue) {
        Log.d("SportYAxis", " min:" + min + " max:" + max + " itemValue:" + itemValue);
        float currentEntry = min;
        List<Float> entryList = new ArrayList<>();
        do {
            entryList.add(currentEntry);
            currentEntry += itemValue;
            Log.d("SportYAxis", " do while currentEntry:" + currentEntry);
//            Log.d("TimeXAxis", "currentEntry:" + currentEntry + " max:" + max);
        } while (currentEntry <= max);
        entryList.add(currentEntry);

        int labelCount = entryList.size();
        if (labelCount <= 2) {

        }

        setLabelCount(labelCount, true);
//        Log.d("TimeXAxis", "getLabelCount() invoke labelCount:" + labelCount + " interval:" + interval / TimeDateUtil.TIME_MIN_INT + " max:" + max / TimeDateUtil.TIME_MIN_INT);
        mEntryCount = labelCount;
        if (mEntries.length < labelCount) {
            // Ensure stops contains at least numStops elements.
            mEntries = new float[labelCount];
        }
        for (int i = 0; i < labelCount; i++) {
            mEntries[i] = entryList.get(i);
//            Log.d("TimeXAxis", "getLabelCount() labelCount i:" + i + " label:" + mEntries[i] / TimeDateUtil.TIME_MIN_INT);
        }
        float minYAxis = min;
        float maxYAxis = max + itemValue;
        setAxisMinimum(minYAxis);
        setAxisMaximum(maxYAxis);
    }

    public void setLimitLine(float limitValue, String limitStr, LimitLine.LimitLabelPosition position) {
        int textColor = mLineChartAttr.limitTextColor == -1 ? mLineChartAttr.lineColor : mLineChartAttr.limitTextColor;
        LimitLine limitLine = new LimitLine(limitValue, limitStr);
        limitLine.setLineWidth(1.5f);
        limitLine.setLineColor(ColorUtil.getResourcesColor(R.color.text_color_20));
        limitLine.enableDashedLine(10f, 10f, 0f);
        limitLine.setLabelPosition(position);
        limitLine.setTextSize(10f);
        limitLine.setTextColor(textColor);
        addLimitLine(limitLine);
    }

    public void setLimitLine(float limitValue, String limitStr) {
        setLimitLine(limitValue, limitStr, LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
    }
}
