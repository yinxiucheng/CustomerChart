package com.yxc.chartlib.formatter;

import android.content.Context;

import java.text.DecimalFormat;

/**
 * Created by philipp on 02/06/16.
 */
public class YAxisValueFormatter extends DefaultAxisValueFormatter {


    Context mContext;

    /**
     * decimalformat for formatting
     */
    protected DecimalFormat mFormat;

    /**
     * the number of decimal digits this formatter uses
     */
    protected int digits;


    public YAxisValueFormatter(Context context) {
       this(0);
       this.mContext = context;
    }

    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public YAxisValueFormatter(int digits) {
        super(digits);
        this.digits = digits;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }
        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value) {
        // avoid memory allocations here (for performance)
        return Integer.toString(Math.round(value));
    }

    /**
     * Returns the number of decimal digits this formatter uses or -1, if unspecified.
     *
     * @return
     */
    public int getDecimalDigits() {
        return digits;
    }
}
