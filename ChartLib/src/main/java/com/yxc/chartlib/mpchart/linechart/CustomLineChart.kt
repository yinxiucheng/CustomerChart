package com.yxc.fitness.chart.mpchart.linechart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import com.github.mikephil.charting.utils.Utils
//import com.xiaomi.fitness.chart.R
//import com.xiaomi.fitness.chart.attrs.ChartAttrsUtil
//import com.xiaomi.fitness.chart.entrys.SportRecordEntry
//import com.xiaomi.fitness.chart.formatter.XAxisSportFormatter
import com.yxc.chartlib.attrs.ChartAttrsUtil
import com.yxc.chartlib.entrys.SportRecordEntry
import com.yxc.chartlib.formatter.XAxisSportFormatter
import com.yxc.chartlib.mpchart.*
import com.yxc.chartlib.mpchart.dataset.CustomLineDataSet
import com.yxc.chartlib.mpchart.linechart.CustomLineChartAttr
import com.yxc.chartlib.mpchart.linechart.CustomLineChartRenderer
import com.yxc.customerchart.R
import com.yxc.chartlib.utils.AppUtil.app
import com.yxc.chartlib.utils.AppUtil.isRTLDirection
import com.yxc.chartlib.utils.ColorUtil
//import com.xiaomi.fitness.chart.mpchart.dataset.CustomLineDataSet
//import com.xiaomi.fitness.common.extensions.application
//import com.xiaomi.fitness.common.utils.AppUtil.isRTLDirection
//import com.xiaomi.fitness.common.utils.ColorUtil
//import com.xiaomi.fitness.common.utils.DecimalUtil
import java.util.*

/**
 * @author yxc
 * @since 2019-08-30
 */
open class CustomLineChart : LineChart {
    var mLineChartAttr: CustomLineChartAttr
    protected lateinit var mContext:Context

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : super(context, attrs, defStyle) {
        mContext = context
        mLineChartAttr = ChartAttrsUtil.getMPLineChartAttr(context, attrs)
    }


    private val yAxisMin = 0.0f
    var mMaxStr: String? = null
    override fun onFinishInflate() {
        super.onFinishInflate()
        setLineChartAttr()
        initLineChart() //?????????
        //  getAxisPoints(); //???????????????
//        initHourDivLimitLine();
//        checkShowLimit();
    }

    private fun setLineChartAttr() {
        (mAxisRendererRight as SportYAxisRenderer).setLineChartAttr(mLineChartAttr)
        (mAxisRendererLeft as SportYAxisRenderer).setLineChartAttr(mLineChartAttr)
        (mRenderer as CustomLineChartRenderer).setLineChartAttr(mLineChartAttr)
        (mRenderer as CustomLineChartRenderer).setMaxStr(mMaxStr)
        (mXAxisRenderer as TimeXAxisRenderer).setLineChartAttr(mLineChartAttr)
        (mAxisRight as SportYAxis).setLineChartAttr(mLineChartAttr)
        (mAxisLeft as SportYAxis).setLineChartAttr(mLineChartAttr)
    }

    override fun init() {
        super.init()
        val context: Context = app
        mXAxis = TimeXAxis()
        mAxisRight = SportYAxis(YAxis.AxisDependency.RIGHT)
        mAxisLeft = SportYAxis(YAxis.AxisDependency.LEFT)

        //????????????mLineChartAttr ???????????????????????????????????? onFinishInflate??? ????????????
        val axisDependencyYAxis = if (isRTLDirection()) mAxisLeft else mAxisRight
        mRenderer = CustomLineChartRenderer(this, mAnimator, mViewPortHandler,
                axisDependencyYAxis,
                mXAxis,
                mLineChartAttr
            )
        mAxisRendererRight = SportYAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer, mLineChartAttr)
        mAxisRendererLeft = SportYAxisRenderer(mViewPortHandler, mAxisLeft, mRightAxisTransformer, mLineChartAttr)
        mXAxisRenderer = TimeXAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer,
                context.getString(R.string.widget_line_chart_unit_desc), mLineChartAttr)
    }

    /**
     * ?????????LineChart???????????????
     */
    private fun initLineChart() {
        /*
        setViewPortOffsets(38, 28, 2, 30);
        ????????????????????????,???recyvleview????????????????????????????????????setMinOffset???setExtraOffsets
        */
        val padding = 12f //????????????
        minOffset = 0f
        setExtraOffsets(0f, 0.5f, 0f, padding) //????????????????????????

        //??????????????????????????????
        setDrawGridBackground(false)
        // no description text
        description.isEnabled = false

        // enable touch gestures
        setTouchEnabled(true)
        val lineColor = ColorUtil.getResourcesColor(R.color.text_color_10)
        val txtColor = ColorUtil.getResourcesColor(R.color.text_color_30)
        setDrawBorders(true)
        setBorderColor(lineColor)
        setBorderWidth(0.5f)

        // enable scaling and dragging
        isDragEnabled = false
        setScaleEnabled(false)
        isScaleYEnabled = false
        // if disabled, scaling can be done on x- and y-axis separately
        setPinchZoom(true)
        setDrawMarkers(false) //????????????????????????
        setNoDataText(mContext.getString(R.string.common_data_empty)) //???????????????????????????
        /*????????????*/
        val legend = legend
        legend.isEnabled = false //???????????????,????????????new Dateset ???????????????
        setXY(lineColor, txtColor)
    }

    private fun setXY(lineColor: Int, txtColor: Int) {
        mXAxis = xAxis
        mXAxis.setAvoidFirstLastClipping(true)
        mXAxis.position = XAxis.XAxisPosition.BOTTOM
        mXAxis.axisMinimum = 0f
        mXAxis.gridColor = lineColor
        mXAxis.axisLineColor = lineColor
        mXAxis.textColor = txtColor
        mXAxis.setDrawAxisLine(mLineChartAttr.enableXAxisGridLine)
        mXAxis.setDrawGridLines(mLineChartAttr.enableXAxisGridLine)
        mXAxis.textSize = 9.3f
        mXAxis.valueFormatter = XAxisSportFormatter(mContext)
        if (isRTLDirection()) {
            mAxisLeft = axisLeft
            mAxisLeft.gridColor = lineColor
            mAxisLeft.axisLineColor = lineColor
            mAxisLeft.setDrawAxisLine(mLineChartAttr.enableYAxisGridLine)
            mAxisLeft.setDrawZeroLine(false)
            mAxisLeft.textColor = txtColor
            mAxisLeft.textSize = 8.7f
            mAxisLeft.minWidth = 30f
            if (mLineChartAttr.minYAxisZero) {
                mAxisLeft.axisMinimum = yAxisMin // this replaces setStartAtZero(true)
            }
            mAxisLeft.spaceBottom = 0f
            mAxisRight = axisRight
            if (mLineChartAttr.minYAxisZero) {
                mAxisRight.axisMinimum = yAxisMin
            }
            mAxisRight.setDrawZeroLine(false)
            mAxisRight.isEnabled = false
        } else {
            mAxisRight = axisRight
            mAxisRight.gridColor = lineColor
            mAxisRight.axisLineColor = lineColor
            mAxisRight.setDrawAxisLine(mLineChartAttr.enableYAxisGridLine)
            mAxisRight.setDrawZeroLine(false)
            mAxisRight.textColor = txtColor
            mAxisRight.textSize = 8.7f
            mAxisRight.minWidth = 30f
            if (mLineChartAttr.minYAxisZero) {
                mAxisRight.axisMinimum = yAxisMin // this replaces setStartAtZero(true)
            }
            mAxisRight.spaceBottom = 0f
            mAxisLeft = axisLeft
            if (mLineChartAttr.minYAxisZero) {
                mAxisLeft.axisMinimum = yAxisMin
            }
            mAxisLeft.setDrawZeroLine(false)
            mAxisLeft.isEnabled = false
        }
    }

    fun setMaxStr(maxStr: String?) {
        mMaxStr = maxStr
        (mRenderer as CustomLineChartRenderer).setMaxStr(mMaxStr)
    }

    fun setLimitLineTextColor(limitTextColor: Int) {
        mLineChartAttr.limitTextColor = limitTextColor
    }

    private fun setLimitLineInner(limitValue: Float, limitStr: String?, position: LimitLine.LimitLabelPosition?) {
        if (isRTLDirection()) {
            (mAxisLeft as SportYAxis).setLimitLine(limitValue, limitStr, position)
        } else {
            (mAxisRight as SportYAxis).setLimitLine(limitValue, limitStr, position)
        }
    }

    fun setLimitLine(limitValue: Float, limitStr: String?, valueRange:Float, minValue:Float = 0f) {
        var limitRange = if (minValue < 0) limitValue - minValue else limitValue
        val limitPosition = getLimitLabelPosition(limitRange, valueRange)
        setLimitLineInner(limitValue, limitStr, limitPosition)
    }

    private fun getLimitLabelPosition(limitValue: Float, range: Float): LimitLine.LimitLabelPosition {
        if (isRTLDirection()) {
            if (range == 0.0f){
                return LimitLine.LimitLabelPosition.LEFT_TOP
            } else if (limitValue * 100 / range >= 10) {
                return LimitLine.LimitLabelPosition.LEFT_BOTTOM
            }
            return LimitLine.LimitLabelPosition.LEFT_TOP
        } else {
            if (range == 0.0f){
                return LimitLine.LimitLabelPosition.RIGHT_TOP
            } else if (limitValue * 100 / range >= 10) {
                return LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            }
            return LimitLine.LimitLabelPosition.RIGHT_TOP
        }
    }

    fun bindStepRepData(values: List<SportRecordEntry>) {
        setYAxisMaxMinimum(values, -1f)
        val dataSetList = CustomLineDataSet.createDataSetList(values)
        val setSize = dataSetList.size
        val dataSets = ArrayList<ILineDataSet>()
        for (i in 0 until setSize) {
            val customLineDataSet = dataSetList[i]
            dataSets.add(customLineDataSet)
        }
        val l = LineData(dataSets)
        setData(l)
        invalidate()
    }

    private fun setYAxisMaxMinimum(values: List<SportRecordEntry>, restrictMax: Float) {
        (mAxisLeft as SportYAxis).setYAxisParam(values, restrictMax)
        (mAxisRight as SportYAxis).setYAxisParam(values, restrictMax)
    }

    @JvmOverloads
    fun bindData(values: List<SportRecordEntry>, model: LineDataSet.Mode?, restrictMax: Float = -1f, colorList: List<Int?>? = null, duration:Int = 0) {
        //??????Y???????????????????????????????????????
        setYAxisMaxMinimum(values, restrictMax)
        if (duration != 0 && mXAxis is TimeXAxis){
            (mXAxis as TimeXAxis).updateAxisMaximum(duration.toFloat())
        }
        val set1: CustomLineDataSet<*>
        val data = data
        Collections.sort(values, EntryXComparator())
        if (data != null && data.dataSetCount > 0) {
            set1 = data.getDataSetByIndex(0) as CustomLineDataSet<*>
            set1.values = values
            set1.notifyDataSetChanged()
            data.notifyDataChanged()
            notifyDataSetChanged()
        } else {
            set1 = CustomLineDataSet<Entry>(values, "DataSet 1")
            set1.cubicIntensity = 0.2f //???????????????????????????????????????
            set1.setDrawIcons(false) //??????????????????????????????
            if (colorList != null) {
                set1.setColors(colorList)
            } else {
                set1.setColor(mLineChartAttr.lineColor) //????????????????????????
            }
            set1.fillFormatter = CustomFillFormatter()
            //??????????????????????????????
            setFill(set1, mLineChartAttr.lineFillRes, mLineChartAttr.lineFillColor)
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.lineWidth = mLineChartAttr.lineStrokeWidth //????????????????????????
            set1.setDrawCircles(mLineChartAttr.enableDrawCircle) //??????????????????????????????
            set1.setCircleColor(mLineChartAttr.lineColor) //????????????????????????????????????
            set1.setDrawCircleHole(false)
            set1.circleRadius = mLineChartAttr.pointRadius //??????????????????????????????????????????DP
            set1.setDrawValues(false) //?????????????????????

            //???????????????????????????????????????
            set1.disableDashedLine()
            set1.disableDashedHighlightLine()
            set1.isHighlightEnabled = false //???????????????????????????????????????

//            set1.setFillFormatter(fillFormatter);
            set1.setDrawFilled(mLineChartAttr.enableLineFill)
            set1.mode = model
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets
            val l = LineData(dataSets)
            setData(l)
        }
        invalidate()
    }

    private fun setFill(set1: LineDataSet, drawableResource: Int, fillColorResource: Int) {
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(mContext, drawableResource)
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = ContextCompat.getColor(mContext, fillColorResource) //???????????????????????????????????????
        }
    }

    /**
     * draws the grid background
     */
    override fun drawGridBackground(c: Canvas) {
        if (mDrawGridBackground) {
            // draw the grid background
            c.drawRect(mViewPortHandler.contentRect, mGridBackgroundPaint)
        }
        if (mDrawBorders) {
            c.drawRect(mViewPortHandler.contentRect, mBorderPaint)
            //            c.drawLine(mViewPortHandler.contentRight(), mViewPortHandler.contentBottom(),
//                    DisplayUtil.getScreenWidth(), mViewPortHandler.contentBottom(), mBorderPaint);
//            c.drawLine(mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), DisplayUtil.getScreenWidth(),
//                    mViewPortHandler.contentTop(), mBorderPaint);
        }
    }

    companion object {
        private const val TAG = "CustomLineChart"
    }
}