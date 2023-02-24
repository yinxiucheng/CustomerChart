package com.yxc.chartlib.component;


import com.github.mikephil.charting.components.AxisBase;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.attrs.BaseChartAttrs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/8
 */
public class BaseYAxis extends AxisBase {

    protected BaseChartAttrs mChartAttrs;
    public float labelHorizontalPadding;
    public float labelVerticalPadding;//刻度 字跟刻度线的位置对齐的调整
    public List<Float> scaleYLocationList;
    public HashMap<Float, Float> yAxisScaleMap;

    public BaseYAxis(BaseChartAttrs barChartAttrs) {
        this.mChartAttrs = barChartAttrs;
        setAxisMinimum(mChartAttrs.yAxisMinimum);
        setAxisMaximum(mChartAttrs.yAxisMaximum);
        setLabelCount(mChartAttrs.yAxisLabelSize);
        setTextSize(mChartAttrs.yAxisLabelTxtSize);
        setTextColor(mChartAttrs.yAxisLabelTxtColor);
        setGridColor(mChartAttrs.yAxisLineColor);
        this.labelHorizontalPadding = mChartAttrs.yAxisLabelHorizontalPadding;
        this.labelVerticalPadding = mChartAttrs.yAxisLabelVerticalPadding;
        scaleYLocationList = new ArrayList<>();
    }

    //yAxisMin not 0
    public void setLabelCount2(int count, int step) {
        super.setLabelCount(count);
        float diff = mAxisMaximum - mAxisMinimum;
        float itemRange = (float) diff / count;
        mEntries = new float[count+1];
//        if ((int)diff % count != 0) {
//            itemRange = step;
//        }
        for (int i = 0; i < count + 1; i++) {
            if (i == 0) {
                mEntries[i] = mAxisMaximum;
            } else if (i == count) {
                mEntries[i] = mAxisMinimum;
            } else {
                mEntries[i] = mAxisMaximum - i * itemRange;
            }
        }
    }

    //yAxisMin not 0
    //解决精度丢失
    public void setLabelCountWithFloatCalculate(int count, int scale) {
        super.setLabelCount(count);
        BigDecimal max = new BigDecimal(Float.toString(mAxisMaximum));
        BigDecimal min = new BigDecimal(Float.toString(mAxisMinimum));
        BigDecimal diff = max.subtract(min);
        BigDecimal itemRange = diff.divide(new BigDecimal(count));
        mEntries = new float[count+1];
        for (int i = 0; i < count + 1; i++) {
            if (i == 0) {
                mEntries[i] = max.setScale(scale,BigDecimal.ROUND_HALF_UP).floatValue();
            } else if (i == count) {
                mEntries[i] = min.setScale(scale,BigDecimal.ROUND_HALF_UP).floatValue();
            } else {
                mEntries[i] = max.subtract(new BigDecimal(i * itemRange.floatValue())).setScale(scale,BigDecimal.ROUND_HALF_UP).floatValue() ;
            }
        }
    }
    public void setLabelCount(int count, int step) {
        super.setLabelCount(count);
        float diff = mAxisMaximum - mAxisMinimum;
        float itemRange = diff / count;
        mEntries = new float[count];
        if ((int)diff % count != 0) {
            itemRange = step;
        }
        for (int i = 0; i < count; i++) {
            mEntries[i] = mAxisMaximum - i * itemRange;
        }
    }

    public void setLabelCount(int count) {
        super.setLabelCount(count);
        float label = mAxisMaximum - mAxisMinimum;
        float itemRange = label / count;
        mEntries = new float[count];
        float topValue = mAxisMaximum;

        for (int i = 0; i < count; i++) {
            if (i > 0) {
                topValue = topValue -  itemRange;
            }
            mEntries[i] = topValue;
        }
    }

    public HashMap<Float, Float> getYAxisScaleMap(float topLocation, float itemHeight, int count) {
        if (null == mEntries || mEntries.length == 0) {
            return new HashMap<>();
        }
        if (null == yAxisScaleMap) {
            yAxisScaleMap = new LinkedHashMap<>();
        } else {
            yAxisScaleMap.clear();
        }
        float location = topLocation;
        if (mChartAttrs.yAxisReverse) {//正序
            for (int i = count; i >= 0; i--) {
                if (i < count) {
                    location = location + itemHeight;
                }
                if (i < mEntries.length) {
                    yAxisScaleMap.put(location, mEntries[i]);
                } else {
                    //这里其实已经出错了，值的个数跟位置不匹配
                    yAxisScaleMap.put(location, 0f);
                }
            }
        } else {//倒序
            for (int i = 0; i <= count; i++) {
                if (i > 0) {
                    location = location + itemHeight;
                }
                if (i < mEntries.length) {
                    yAxisScaleMap.put(location, mEntries[i]);
                } else {
                    //这里其实已经出错了，值的个数跟位置不匹配
                    yAxisScaleMap.put(location, 0f);
                }
            }
        }
        return yAxisScaleMap;
    }


    //获取Y轴刻度值
    public static BaseYAxis getYAxis(BarChartAttrs attrs, float max) {
        BaseYAxis axis = new BaseYAxis(attrs);
        if (max > 50000) {
            axis.mAxisMaximum = 80000;
            axis.setLabelCount(5);
        } else if (max > 30000) {
            axis.mAxisMaximum = 50000;
            axis.setLabelCount(5);
        } else if (max > 25000) {
            axis.mAxisMaximum = 30000;
            axis.setLabelCount(5);
        } else if (max > 20000) {
            axis.mAxisMaximum = 25000;
            axis.setLabelCount(5);
        } else if (max > 15000) {
            axis.mAxisMaximum = 20000;
            axis.setLabelCount(5);
        } else if (max > 10000) {
            axis.mAxisMaximum = 15000;
            axis.setLabelCount(5);
        } else if (max > 8000) {
            axis.mAxisMaximum = 10000;
            axis.setLabelCount(5);
        } else if (max > 6000) {
            axis.mAxisMaximum = 8000;
            axis.setLabelCount(5);
        } else if (max > 4000) {
            axis.mAxisMaximum = 6000;
            axis.setLabelCount(5);
        } else if (max > 3000) {
            axis.mAxisMaximum = 5000;
            axis.setLabelCount(5);
        } else if (max > 2000) {
            axis.mAxisMaximum = 3000;
            axis.setLabelCount(5);
        } else if (max > 1500) {
            axis.mAxisMaximum = 2000;
            axis.setLabelCount(5);
        } else if (max > 1000) {
            axis.mAxisMaximum = 1500;
            axis.setLabelCount(5);
        } else if (max > 800) {
            axis.mAxisMaximum = 1000;
            axis.setLabelCount(5);
        } else if (max > 500) {
            axis.mAxisMaximum = 800;
            axis.setLabelCount(5);
        } else if (max > 300) {
            axis.mAxisMaximum = 500;
            axis.setLabelCount(4);
        } else if (max > 200) {
            axis.mAxisMaximum = 300;
            axis.setLabelCount(4);
        } else if (max > 140) {
            axis.mAxisMaximum = 200;
            axis.setLabelCount(4);
        } else if (max > 120) {
            axis.mAxisMaximum = 140;
            axis.setLabelCount(7);
        } else if (max < 120 && max > 100) {
            axis.mAxisMaximum = 120;
            axis.setLabelCount(6);
        } else if (max < 100 && max > 80) {
            axis.mAxisMaximum = 100;
            axis.setLabelCount(5);
        } else if (max < 80 && max > 60) {
            axis.mAxisMaximum = 80;
            axis.setLabelCount(5);
        } else if (max < 60 && max > 40) {
            axis.mAxisMaximum = 60;
            axis.setLabelCount(5);
        } else if (max < 40 && max > 30) {
            axis.mAxisMaximum = 40;
            axis.setLabelCount(5);
        } else if (max < 30 && max > 20) {
            axis.mAxisMaximum = 30;
            axis.setLabelCount(5);
        } else if (max < 20 && max > 12) {
            axis.mAxisMaximum = 20;
            axis.setLabelCount(5);
        } else if (max < 12 && max > 10) {
            axis.mAxisMaximum = 12;
            axis.setLabelCount(6);
        } else if (max < 10 && max > 8) {
            axis.mAxisMaximum = 10;
            axis.setLabelCount(5);
        } else if (max < 8 && max > 5) {
            axis.mAxisMaximum = 8;
            axis.setLabelCount(4);
        } else if (max < 5 && max > 3) {
            axis.mAxisMaximum = 5;
            axis.setLabelCount(5);
        } else {
            axis.mAxisMaximum = 4;
            axis.setLabelCount(4);
        }
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public BaseYAxis resetYAxis(BaseYAxis axis, float max) {
        float axisMaximum;
        int layoutCount;
        if (max > 50000) {
            axisMaximum = 80000;
            layoutCount = 5;
        } else if (max > 30000) {
            axisMaximum = 50000;
            layoutCount = 5;
        } else if (max > 25000) {
            axisMaximum = 30000;
            layoutCount = 5;
        } else if (max > 20000) {
            axisMaximum = 25000;
            layoutCount = 5;
        } else if (max > 15000) {
            axisMaximum = 20000;
            layoutCount = 5;
        } else if (max > 10000) {
            axisMaximum = 15000;
            layoutCount = 5;
        } else if (max > 8000) {
            axisMaximum = 10000;
            layoutCount = 5;
        } else if (max > 6000) {
            axisMaximum = 8000;
            layoutCount = 5;
        } else if (max > 4000) {
            axisMaximum = 6000;
            layoutCount = 5;
        } else if (max > 3000) {
            axisMaximum = 5000;
            layoutCount = 5;
        } else if (max > 2000) {
            axisMaximum = 3000;
            layoutCount = 5;
        } else if (max > 1500) {
            axisMaximum = 2000;
            layoutCount = 5;
        } else if (max > 1000) {
            axisMaximum = 1500;
            layoutCount = 5;
        } else if (max > 800) {
            axisMaximum = 1000;
            layoutCount = 5;
        } else if (max > 500) {
            axisMaximum = 800;
            layoutCount = 4;
        } else if (max > 300) {
            axisMaximum = 500;
            layoutCount = 4;
        } else if (max > 200) {
            axisMaximum = 300;
            layoutCount = 4;
        } else if (max > 140) {
            axisMaximum = 200;
            layoutCount = 4;
        } else if (max > 120) {
            axisMaximum = 140;
            layoutCount = 7;
        } else if (max < 120 && max > 100) {
            axisMaximum = 120;
            layoutCount = 6;
        } else if (max < 100 && max > 80) {
            axisMaximum = 100;
            layoutCount = 5;
        } else if (max < 80 && max > 60) {
            axisMaximum = 80;
            layoutCount = 5;
        } else if (max < 60 && max > 40) {
            axisMaximum = 60;
            layoutCount = 5;
        } else if (max < 40 && max > 30) {
            axisMaximum = 40;
            layoutCount = 5;
        } else if (max < 30 && max > 20) {
            axisMaximum = 30;
            layoutCount = 5;
        } else if (max < 20 && max > 12) {
            axisMaximum = 20;
            layoutCount = 5;
        } else if (max < 12 && max > 10) {
            axisMaximum = 12;
            layoutCount = 4;
        } else if (max < 10 && max > 8) {
            axisMaximum = 10;
            layoutCount = 5;
        } else if (max < 8 && max > 5) {
            axisMaximum = 8;
            layoutCount = 4;
        } else if (max < 5 && max > 3) {
            axisMaximum = 5;
            layoutCount = 5;
        } else {
            axisMaximum = 4;
            layoutCount = 4;
        }

        if (axisMaximum != mAxisMaximum) {
            axis.setAxisMaximum(axisMaximum);
            axis.setLabelCount(layoutCount);
            return axis;
        }
        return null;
    }
}
