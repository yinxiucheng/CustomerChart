package com.yxc.fitness.chart.mpchart.linechart

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import com.github.mikephil.charting.utils.Utils
import com.yxc.chartlib.mpchart.dataset.CustomLineDataSet
import com.yxc.chartlib.mpchart.linechart.LineChartColorAttr
import com.yxc.customerchart.R
import java.util.*

/**
 * @author yxc
 * @date 2019-08-30
 */
open class DataCubicLineChart @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0)
    : CustomLineChart(context, attrs, defStyle) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        initLineChart() //初始化
    }
    /**
     * 初始化LineChart的一些设置
     */
    private fun initLineChart() {
        minOffset = 0f
        setExtraOffsets(0f, 0f, 0f, 0f) //图形绘制区域边界

        //不显示绘制区背景网格
        setDrawGridBackground(false)
        // no description text
        description.isEnabled = false
        // enable touch gestures
        setTouchEnabled(false)
        setDrawBorders(false)

        // enable scaling and dragging
        isDragEnabled = true
        setScaleEnabled(true)
        isScaleYEnabled = false
        // if disabled, scaling can be done on x- and y-axis separately
        setPinchZoom(true)
        setDrawMarkers(false) //点击时不显示数据
        setNoDataText(mContext.getString(R.string.common_data_empty)) //没有数据时显示内容
        /*禁止缩放*/
        val legend = legend
        legend.isEnabled = false //不显示图例,可以去掉new Dateset 时的字符串
        setXY()
    }

    private fun setXY() {
        mXAxis = xAxis
        mXAxis.axisMinimum = 0f
        mXAxis.setDrawAxisLine(false)
        mXAxis.isEnabled = false
        mAxisRight = axisRight
        mAxisRight.axisMinimum = 0f // this replaces setStartAtZero(true)
        mAxisRight.spaceBottom = 0f
        mAxisRight.isEnabled = false
        mAxisLeft = axisLeft
        mAxisLeft.axisMinimum = 0f
        mAxisLeft.isEnabled = false
    }

    fun <T : Entry?> bindData(values: List<T>?, colorAttr: LineChartColorAttr) {
        val set1: CustomLineDataSet<*>
        val data = data
        Collections.sort(values, EntryXComparator())
        if (data != null && data.dataSetCount > 0) {
            set1 = data.getDataSetByIndex(0) as CustomLineDataSet<*>
            set1.values = values
            set1.notifyDataSetChanged()
            set1.color = ContextCompat.getColor(mContext, colorAttr.lineColorResource) //设置绘制曲线颜色

            //填充区域显示渐变效果
            setFill(set1, colorAttr)
            set1.fillAlpha = 0XFF
            data.notifyDataChanged()
            notifyDataSetChanged()
        } else {
            set1 = CustomLineDataSet<Entry>(values, "DataSet 1")
            set1.cubicIntensity = 0.2f //曲线的平滑度，值越大越平滑
            set1.setDrawIcons(false) //坐标处不显示绘制图标
            set1.color = ContextCompat.getColor(mContext, colorAttr.lineColorResource) //设置绘制曲线颜色
            //填充区域显示渐变效果
            setFill(set1, colorAttr)
            set1.fillAlpha = 0XFF
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.lineWidth = 1f //设置绘制曲线宽度
            set1.valueTextSize = 4f //设置坐标处显示值的文字大小
            set1.setCircleColor(Color.GREEN) //设置坐标点处圆点的颜色。
            set1.circleRadius = 1f //设置坐标点处圆点的半径，单位DP
            set1.setDrawCircles(false) //数据坐标处不显示圆点
            set1.setDrawValues(false) //默认不显示数值
            set1.disableDashedLine()
            set1.disableDashedHighlightLine()
            set1.isHighlightEnabled = false //点击坐标点不显示高亮交叉线
            val dataSets = ArrayList<ILineDataSet>()
            set1.setDrawFilled(true)
            dataSets.add(set1) // add the data sets
            set1.mode = LineDataSet.Mode.LINEAR
            val l = LineData(dataSets)
            setData(l)
        }
        invalidate()
    }

    private fun setFill(set1: LineDataSet, colorAttr: LineChartColorAttr) {
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(mContext, colorAttr.drawableResource)
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = ContextCompat.getColor(mContext, colorAttr.fillColorResource) //设置数据范围下方的填充颜色
        }
    }

    companion object {
        private const val TAG = "RateCubicLineChart"
    }
}