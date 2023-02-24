package com.yxc.chartlib.utils;

import android.content.Context;

import com.yxc.customerchart.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author yxc
 * @since 2019/4/10
 */
public class DecimalUtil {

    //一位小数
    public static final int ONE_LENGTH_DECIMAL = 1;
    //两位小数
    public static final int TWO_LENGTH_DECIMAL = 2;
    //三位小数
    public static final int THREE_LENGTH_DECIMAL = 3;

    //获取小数点后 type 小数的Float值。
    public static float getDecimalFloat(int type, float value) {
        float f;
        if (type == TWO_LENGTH_DECIMAL) {
            float temp = (value * 1000.0f) / 10.0f;
            f = Math.round(temp);
            f = f / 100.0f;
        } else if (type == THREE_LENGTH_DECIMAL) {
            float temp = (value * 1000.0f);
            f = Math.round(temp);
            f = f / 1000.0f;
        } else {
            float temp = (value * 10.0f);
            f = Math.round(temp);
            f = f / 10.0f;
        }
        return f;
    }

    public static Double getDecimalDouble(int type, double value) {
        double f;
        if (type == TWO_LENGTH_DECIMAL) {
            double temp = (value * 100.0f);
            f = Math.round(temp);
            f = f / 100.0f;
        } else if (type == THREE_LENGTH_DECIMAL) {
            double temp = (value * 1000.0f);
            f = Math.round(temp);
            f = f / 1000.0f;
        } else {
            double temp = (value * 10.0f);
            f = Math.round(temp);
            f = f / 10.0f;
        }
        return f;
    }

    public static Double getDoubleLen3(Double value){
        return getDecimalDouble(3, value);
    }

    public static String getDecimalFloatStr(int type, float value) {
        NumberFormat nf = NumberFormat.getInstance();
        DecimalFormat decimalFormat = (DecimalFormat) nf;
        return decimalFormat.format(getDecimalFloat(type, value));
    }

    public static String getDecimalFloatStr(float value){
        return getDecimalFloatStr(DecimalUtil.ONE_LENGTH_DECIMAL,value);
    }

    public static int getPluralsCount(float originalValue){
        float fValue = Math.abs(originalValue);
        int result = 0;
        if (0 <= fValue && fValue < 0.00001) {
            result = 0;
        } else if (0.9999 <= fValue && fValue < 1.00001) {
            result = 1;
        } else {
            result = Math.round(fValue + 0.5f);
        }
        return result;
    }

    public static String getPercentageStr(int dividend, int divider){
        return getPercentageStr(dividend, divider, 0);
    }

    public static String getPercentageStr(int dividend, int divider, int roundMode){
        if (divider == 0){
            return "";
        }
        if (roundMode == 1){
            int percentage = (dividend * 100) / divider;
            return getPercentageStr(percentage);//向下取整
        }else {
            float percentage = dividend * 1.0f / divider * 100;
            return getPercentageStr(Math.round(percentage));
        }
    }

    public static String getPercentageStr(int percentValue){
        Context context = AppUtil.getApp();
        return context.getString(R.string.common_percent_join_str, percentValue);
    }


    @SuppressWarnings("unused")
    public final static double DOUBLE_EPSILON = Double.longBitsToDouble(1);

    @SuppressWarnings("unused")
    public final static float FLOAT_EPSILON = Float.intBitsToFloat(1);


    public static final boolean equals(float a, float b) {
        return Math.abs(a - b) < 0.00001;
    }

    public static final boolean equals(int a, float b) {
        return Math.abs(a - b) < 0.00001;
    }


    public static final boolean equals(float a, int b) {
        return Math.abs(a - b) < 0.00001;
    }

    public static final boolean smallOrEquals(float a, float b) {
        boolean result = (a < b) || equals(a, b);
        return result;
    }

    public static final boolean bigOrEquals(float a, float b) {
        return (a > b) || equals(a, b);
    }

    @SuppressWarnings("unused")
    public static final boolean smallOrEquals(int a, float b) {
        boolean result = (a < b) || equals(a, b);
        return result;
    }

    public static final boolean smallOrEquals(float a, int b) {
        boolean result = (a < b) || equals(a, b);
        return result;
    }

    public static int getMaxInt(float yAxisMaximum) {
        float temp = yAxisMaximum + 10;
        return (int) temp;
    }
}
