package com.yxc.chartlib.mpchart.linechart;


import com.yxc.chartlib.mpchart.MPChartAttr;

/**
 * @author yxc
 * @date 2019-12-02
 */
public class CustomLineChartAttr extends MPChartAttr {
    //line ,Bezier曲线公用
    public int lineColor;//线的颜色
    public int lineFillColor;
    public int lineFillRes;//Fill 渐变颜色
    public int limitTextColor;//limitLineColor
    public float lineStrokeWidth;//线的width
    public float pointRadius;//点的半径
    public boolean enableDrawLine;//线的绘制。
    public boolean enableLineFill;//底部fill
    public boolean enableMaxPoup;//最大值顶部的poup气泡
    public boolean enableMaxCircle;//最大值circle
    public boolean enableMinCircle;//最小值circle
    public boolean enableDrawCircle;//允许 画线的点。
    public int fillAlpha;//
    public boolean enableCustomerStepped;//游泳阶梯图

}
